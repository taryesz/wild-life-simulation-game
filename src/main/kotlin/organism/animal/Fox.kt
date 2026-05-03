package organism.animal

import game.Game
import organism.ActionSpecificity
import organism.Coordinates

class Fox(game: Game) : Animal(), ActionSpecificity {

    init {
        this.power = 3
        this.initiative = 7
        this.game = game
        this.spawn()
        this.icon = "f"
    }

    override fun reproduce(): Fox {
        return Fox(this.game!!)
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {

        // Dobry węch: lis nigdy nie ruszy
        // się na pole zajmowane przez
        // organizm silniejszy niż on
        newCoordinates?.let {
            var danger = false

            for (organism in this.game!!.organisms) {
                if (this !== organism &&
                    organism.coordinates.x == newCoordinates.x &&
                    organism.coordinates.y == newCoordinates.y)
                {
                    danger = true
                    println("FOX - found danger...")
                    break
                }
            }

            if (danger) {
                println("FOX - generating new move...")
                val newCoordinates: Coordinates = this.move()
                useActionSpecificity(newCoordinates)
            }

            println("FOX - found SAFE place!")
            this.updateCoordinates(newCoordinates)
        }

    }

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.useActionSpecificity(newCoordinates)
    }

}