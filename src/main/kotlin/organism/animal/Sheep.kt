package organism.animal

import game.Game

class Sheep(game: Game) : Animal() {

    init {
        this.power = 4
        this.initiative = 4
        this.game = game
        this.spawn()
        this.icon = "s"
    }

    override fun reproduce(): Sheep {
        return Sheep(this.game!!)
    }

}