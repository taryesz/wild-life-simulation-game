package organism.plant

import game.Game
import game.Logger
import organism.CollisionSpecificity
import organism.Organism

class Guarana(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
): Plant(), CollisionSpecificity {

    init {
        this.power = 0
        this.initiative = 0
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "U"
    }

    override fun reproduce(): Guarana {
        return Guarana(this.game!!, this.coordinates.x, this.coordinates.y)
    }

    override fun useCollisionSpecificity(collidingOrganism: Organism) {
        collidingOrganism.power = collidingOrganism.power?.times(3) ?: throw Exception("Colliding organism's power property is null and cannot be multiplied.")
        Logger.log("increased its power to ${collidingOrganism.power}!", collidingOrganism::class)
        this.game!!.organismsToRemove.add(this)
    }

}