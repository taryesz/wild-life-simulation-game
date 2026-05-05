package organism.plant

import game.Game
import game.Logger
import organism.CollisionSpecificity
import organism.Organism

class Belladonna(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
): Plant(), CollisionSpecificity {

    init {
        this.power = 99
        this.initiative = 0
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "B"
    }

    override fun reproduce(): Belladonna {
        return Belladonna(this.game!!, this.coordinates.x, this.coordinates.y)
    }

    override fun useCollisionSpecificity(collidingOrganism: Organism) {
        Logger.log("has no mercy and kills ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
        this.game!!.organismsToRemove.add(collidingOrganism)
    }

}