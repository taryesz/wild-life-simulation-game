package organism

import game.Coordinates
import game.Game
import game.Logger
import kotlin.random.Random

abstract class Organism: Comparable<Organism> {

    var power: Int? = null

    protected var initiative: Int? = null

    val coordinates = Coordinates(null, null)
    val previousCoordinates = Coordinates(null, null)

    protected var game: Game? = null

    protected var icon: String? = null

    protected abstract val organismFactory: (Game, Int?, Int?) -> Organism

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
                    val rx = Random.nextInt(0, game!!.world.boardWidth!!)
                    val ry = Random.nextInt(0, game!!.world.boardHeight!!)
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

    protected open fun move(baseX: Int = this.coordinates.x, baseY: Int = this.coordinates.y): Coordinates {

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

    protected fun checkCoordinatesInvalidity(coordinates: Coordinates): Boolean {
        return coordinates.x < 0 || coordinates.x >= this.game!!.world.boardWidth!! ||
                coordinates.y < 0 || coordinates.y >= this.game!!.world.boardHeight!!
    }

    fun draw() {
        print(" ${this.icon ?: "?"} ")
    }

    override fun compareTo(other: Organism): Int {
        val myInitiative = this.initiative ?: 0
        val otherInitiative = other.initiative ?: 0
        return myInitiative.compareTo(otherInitiative)
    }

    abstract fun act()

    fun collide() {

        val collidingOrganism: Organism? = this.checkIfCollision()

        // If a collision is detected...
        collidingOrganism?.let {

            // Don't perform the same logic twice for each of the 2 organisms that collide
            this.game!!.population.containsCollisionRecord(collidingOrganism)?.let { return }
            this.game!!.population.addCollisionRecord(this)

            // If the organisms are of the same type, they create a baby
            if (collidingOrganism::class == this::class) {
                Logger.log("making a baby!", this::class)
                val babyOrganism: Organism = this.reproduce()
                this.game!!.population.add(babyOrganism)
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
                            collidingOrganism.attack(this)
                        }
                        else if (collidingOrganism.coordinates.x == this.previousCoordinates.x && collidingOrganism.coordinates.y == this.previousCoordinates.x) {
                            this.attack(collidingOrganism)
                        }
                    }

                    // The fight is won by the stronger organism.
                    this.power!! > collidingOrganism.power!! -> {
                        this.attack(collidingOrganism)
                    }
                    else -> {
                        collidingOrganism.attack(this)
                    }

                }

            }
        }

    }

    protected fun checkIfCollision(x: Int = this.coordinates.x , y: Int = this.coordinates.y): Organism? {

        val collidingOrganism = this.game!!.population.find(x, y)
        return collidingOrganism?.let {
            return if (collidingOrganism !== this) collidingOrganism else null
        }

//        for (otherOrganism in this.game!!.population) {
//            if (this !== otherOrganism &&
//                x == otherOrganism.coordinates.x &&
//                y == otherOrganism.coordinates.y) {
//                // println("Znaleziono kolizje ${this.coordinates.x}, ${this.coordinates.y}.")
//                return otherOrganism
//            }
//        }
//        // println("Nie znaleziono kolizji.")
//        return null

    }

    private fun attack(toAttack: Organism) {
        Logger.log("is being attacked by ${this::class.simpleName.toString().uppercase()}!", toAttack::class)
        if (toAttack is CollisionSpecificity) toAttack.useCollisionSpecificity(this)
        else if (this is CollisionSpecificity) this.useCollisionSpecificity(toAttack)
        else this.game!!.population.remove(toAttack)
    }

    fun reproduce(): Organism {
        return organismFactory(this.game!!, this.coordinates.x, this.coordinates.y)
    }

}