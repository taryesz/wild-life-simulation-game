package organism.plant

import game.Game
import game.Logger
import organism.ActionSpecificity
import game.Coordinates
import kotlin.random.Random

class SowThistles(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
): Plant(), ActionSpecificity {

    init {
        this.power = 0
        this.initiative = 0
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "S"
    }

    override fun reproduce(): SowThistles {
        return SowThistles(this.game!!, this.coordinates.x, this.coordinates.y)
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {
        val probability = 0.005

        if (Random.nextDouble() < probability) {
            Logger.log("multiplying...", this::class)
            val babyOrganism: SowThistles = this.reproduce()
            this.game!!.organismsToAdd.add(babyOrganism)
            return
        }

    }

    override fun act() {
        this.useActionSpecificity()
    }

}