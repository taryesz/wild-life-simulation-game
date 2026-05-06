package organism.plant

import game.Coordinates
import game.Game
import game.Logger
import organism.ActionSpecificity
import organism.CollisionSpecificity
import organism.Organism
import organism.animal.Animal
import organism.animal.CyberSheep

class Heracleum(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
): Plant(), ActionSpecificity, CollisionSpecificity {

    init {
        this.power = 10
        this.initiative = 0
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "H"
    }

    override fun act() {
        this.useActionSpecificity()
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {

        val directions = listOf(
            Pair(1, 0),  // right
            Pair(-1, 0), // left
            Pair(0, -1), // up
            Pair(0, 1)   // down
        )

        for ((dx, dy) in directions) {
            val targetX = this.coordinates.x + dx
            val targetY = this.coordinates.y + dy

            val target = this.checkIfCollision(x = targetX, y = targetY)

            if (target is Animal && target !is CyberSheep) {
                Logger.log("kills everyone who approaches it...including ${target::class.simpleName.toString().uppercase()}.", this::class)
                this.game!!.population.remove(target)
            }
        }

    }

    override fun useCollisionSpecificity(collidingOrganism: Organism) {
        if (collidingOrganism !is CyberSheep) {
            Logger.log("has no mercy and kills ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
            this.game!!.population.remove(collidingOrganism)
            return
        }
        // a cyber sheep kills the heracleum
        this.game!!.population.remove(this)
    }

    override val organismFactory = ::Heracleum

}