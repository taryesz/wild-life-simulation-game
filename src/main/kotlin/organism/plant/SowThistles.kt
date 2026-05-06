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

    override fun act() {
        this.useActionSpecificity()
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {
        val probability = 0.005

        if (Random.nextDouble() < probability) {
            Logger.log("multiplying...", this::class)
            val babyOrganism = this.reproduce()
            this.game!!.population.add(babyOrganism)
            return
        }

    }

    override val organismFactory = ::SowThistles

}