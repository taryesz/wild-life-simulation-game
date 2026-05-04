package organism.animal

import game.Logger
import organism.CollisionSpecificity
import organism.Coordinates
import organism.Organism
import kotlin.random.Random

abstract class Animal : Organism() {

    protected fun move(baseX: Int = this.coordinates.x, baseY: Int = this.coordinates.y): Coordinates {

        val moveHorizontal = Random.nextBoolean()

        var dx = 0
        var dy = 0

        if (moveHorizontal) {
            dx = if (Random.nextBoolean()) 1 else -1
        } else {
            dy = if (Random.nextBoolean()) 1 else -1
        }

        val nx = baseX + dx
        val ny = baseY + dy

        return Coordinates(nx, ny)

    }

     fun updateCoordinates(newCoordinates: Coordinates) {

         if (newCoordinates.x in 0 until game!!.boardWidth && newCoordinates.y in 0 until game!!.boardHeight) {
             this.previousCoordinates.x = this.coordinates.x
             this.previousCoordinates.y = this.coordinates.y
             this.coordinates.x = newCoordinates.x
             this.coordinates.y = newCoordinates.y
         }
         else {
             // Make an organism move again if the previously generated coordinates are invalid
             this.updateCoordinates(this.move())
             return
         }

         Logger.log("moved to (${this.coordinates.x},${this.coordinates.y}).", this::class)

    }

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.updateCoordinates(newCoordinates)
    }

    override fun collide() {

        val collidingOrganism: Organism? = this.checkIfCollision()

        // If a collision is detected...
        collidingOrganism?.let {

            // Don't perform the same logic twice for each of the 2 organisms that collide
            if (this.game!!.collidingOrganisms.contains(collidingOrganism)) return
            this.game!!.collidingOrganisms[this] = true

            // If the organisms are of the same type, they create a baby
            if (collidingOrganism::class == this::class) {
                Logger.log("making a baby!", this::class)
                val babyOrganism: Organism = this.reproduce()
                this.game!!.organismsToAdd.add(babyOrganism)
            }
            else {

                // The organisms of different types start fighting.

                when {

                    // What if the power property is the same?
                    // The fight is then won by the attacking organism
                    // (which we can detect based on its previous coordinates:
                    // - if they stayed the same, the organism IS BEING attacked
                    // - if they changed, the organism IS ATTACKING another organism).
                    this.power!! == collidingOrganism.power!! -> {
                        if (this.coordinates.x == this.previousCoordinates.x && this.coordinates.y == this.previousCoordinates.y) {
                            Logger.log("is being attacked by ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
                            this.game!!.organismsToRemove.add(this)
                        }
                        else if (collidingOrganism.coordinates.x == this.previousCoordinates.x && collidingOrganism.coordinates.y == this.previousCoordinates.x) {
                            Logger.log("attacks a ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
                            this.game!!.organismsToRemove.add(collidingOrganism)
                        }
                    }

                    // The fight is won by the stronger organism.
                    this.power!! > collidingOrganism.power!! -> {
                        Logger.log("attacks a ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
                        if (collidingOrganism is CollisionSpecificity) collidingOrganism.useCollisionSpecificity(this)
                        else this.game!!.organismsToRemove.add(collidingOrganism)
                    }
                    else -> {
                        Logger.log("is being attacked by ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
                        if (this is CollisionSpecificity) this.useCollisionSpecificity(collidingOrganism)
                        else this.game!!.organismsToRemove.add(this)
                    }

                }

            }
        }

    }

}