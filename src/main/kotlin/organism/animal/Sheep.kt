package organism.animal

import game.Game

class Sheep(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
) : Animal() {

    init {
        this.power = 4
        this.initiative = 4
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "s"
    }

    override fun reproduce(): Sheep {
        return Sheep(this.game!!, this.coordinates.x, this.coordinates.y)
    }

}