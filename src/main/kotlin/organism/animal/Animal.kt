package organism.animal

import organism.Coordinates
import organism.Organism
import kotlin.random.Random

abstract class Animal : Organism() {

    protected fun move(): Coordinates {

        val moveHorizontal = Random.nextBoolean()

        var dx = 0
        var dy = 0

        if (moveHorizontal) {
            dx = Random.nextInt(-1, 2)
        } else {
            dy = Random.nextInt(-1, 2)
        }

        return Coordinates(dx, dy)

    }

    protected fun updateCoordinates(newCoordinates: Coordinates) {

        val nextX = this.coordinates.x + newCoordinates.x
        val nextY = this.coordinates.y + newCoordinates.y

        if (nextX in 0 until game!!.boardWidth && nextY in 0 until game!!.boardHeight) {
            this.previousCoordinates.x = this.coordinates.x
            this.previousCoordinates.y = this.coordinates.x
            this.coordinates.x = nextX
            this.coordinates.y = nextY
        }

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
                println("BABY!")
                val babyOrganism: Organism = this.reproduce()
                this.game!!.organismsToAdd.add(babyOrganism)
            }
            else {

                // The organisms of different types start fighting.
                println("FIGHT!")

                when {

                    // What if the power property is the same?
                    // The fight is then won by the attacking organism
                    // (which we can detect based on its previous coordinates:
                    // - if they stayed the same, the organism IS BEING attacked
                    // - if they changed, the organism IS ATTACKING another organism).
                    this.power!! == collidingOrganism.power!! -> {
                        if (this.coordinates.x == this.previousCoordinates.x && this.coordinates.y == this.previousCoordinates.y) {
                            this.game!!.organismsToRemove.add(this)
                        }
                        else if (collidingOrganism.coordinates.x == this.previousCoordinates.x && collidingOrganism.coordinates.y == this.previousCoordinates.x) {
                            this.game!!.organismsToRemove.add(collidingOrganism)
                        }
                    }

                    // The fight is won by the stronger organism.
                    this.power!! > collidingOrganism.power!! -> this.game!!.organismsToRemove.add(collidingOrganism)
                    else -> this.game!!.organismsToRemove.add(this)

                }

            }
        }

    }

}