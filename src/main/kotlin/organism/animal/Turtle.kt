package organism.animal

import game.Game
import organism.ActionSpecificity
import organism.Coordinates
import kotlin.random.Random

class Turtle(game: Game) : Animal(), ActionSpecificity {

    init {
        this.power = 2
        this.initiative = 1
        this.game = game
        this.spawn()
        this.icon = "t"
    }

    override fun reproduce(): Turtle {
        return Turtle(this.game!!)
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {

        // W 75% przypadków nie
        // zmienia swojego położenia.
        if (Random.nextDouble() < 0.75) super.act()

    }

    override fun act() {
        this.useActionSpecificity()
    }

}