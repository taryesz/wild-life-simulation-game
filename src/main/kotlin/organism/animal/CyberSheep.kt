package organism.animal

import game.Game
import organism.ActionSpecificity
import organism.CollisionSpecificity
import organism.Coordinates
import organism.Organism

class CyberSheep(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
) : Animal(), ActionSpecificity, CollisionSpecificity {

    init {
        this.power = 11
        this.initiative = 4
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "c"
    }

    override fun reproduce(): CyberSheep {
        return CyberSheep(this.game!!, this.coordinates.x, this.coordinates.y)
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {
        TODO("Not yet implemented")
    }

    override fun useCollisionSpecificity(collidingOrganism: Organism) {
        TODO("Not yet implemented")
    }

}