package organism

import game.Game
import kotlin.random.Random
import game.Logger

abstract class Organism {

    var power: Int? = null
    var initiative: Int? = null
    var coordinates = Coordinates(null, null)
    var previousCoordinates = Coordinates(null, null)
    var game: Game? = null
    protected var icon: String? = null

    abstract fun act()

    fun collide() {

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

    abstract fun reproduce(): Organism

    protected fun checkCoordinatesInvalidity(coordinates: Coordinates): Boolean {
        return coordinates.x < 0 || coordinates.x >= this.game!!.boardWidth ||
                coordinates.y < 0 || coordinates.y >= this.game!!.boardHeight
    }

    protected fun spawn(baseX: Int?, baseY: Int?) {

        var x: Int = -1
        var y: Int = -1
        var attempts = 0
        val maxAttempts = 20

        do {

            attempts++
            if (attempts > maxAttempts) {
                Logger.log("could not find space to spawn", this::class)
                return
            }

            val newCoordinates: Coordinates =
                if (baseX != null && baseY != null) {
                    var res: Coordinates
                    var moveAttempts = 0
                    do {
                        moveAttempts++
                        res = this.move(baseX, baseY)
                        if (moveAttempts > 10) break
                    } while (checkCoordinatesInvalidity(res))
                    res
                } else {
                    val rx = Random.nextInt(0, game!!.boardWidth)
                    val ry = Random.nextInt(0, game!!.boardHeight)
                    Coordinates(rx, ry)
                }

            if (checkCoordinatesInvalidity(newCoordinates)) continue

            x = newCoordinates.x
            y = newCoordinates.y
            this.coordinates.set(x, y)
            this.previousCoordinates.set(x, y)

        } while (checkIfCollision() is Organism)

        Logger.log("spawned @($x,$y).", this::class)
    }

    fun draw() {
        print(" ${this.icon ?: "?"} ")
    }

    protected fun checkIfCollision(x: Int = this.coordinates.x , y: Int = this.coordinates.y): Organism? {
        for (otherOrganism in this.game!!.organisms) {
            if (this !== otherOrganism &&
                x == otherOrganism.coordinates.x &&
                y == otherOrganism.coordinates.y) {
                // println("Znaleziono kolizje ${this.coordinates.x}, ${this.coordinates.y}.")
                return otherOrganism
            }
        }
        // println("Nie znaleziono kolizji.")
        return null
    }

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

}

class Coordinates(initX: Int? = null, initY: Int? = null) {
    var x = initX ?: -1
    var y = initY ?: -1

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

}