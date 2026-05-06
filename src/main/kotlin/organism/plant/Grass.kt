package organism.plant

import game.Game

class Grass(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
): Plant() {

    init {
        this.power = 0
        this.initiative = 0
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "G"
    }

    override val organismFactory = ::Grass

}