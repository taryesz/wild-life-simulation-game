package organism.animal

import game.Game

class Wolf(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
) : Animal() {

    init {
        this.power = 9
        this.initiative = 5
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "w"
    }

    override val organismFactory = ::Wolf

}