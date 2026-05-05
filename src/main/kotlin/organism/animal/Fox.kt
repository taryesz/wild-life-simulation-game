package organism.animal

import game.Game
import game.Logger
import organism.ActionSpecificity
import organism.Coordinates

class Fox(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
) : Animal(), ActionSpecificity {

    init {
        this.power = 3
        this.initiative = 7
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "f"
    }

    override fun reproduce(): Fox {
        return Fox(this.game!!, this.coordinates.x, this.coordinates.y)
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
                    organism.coordinates.y == newCoordinates.y &&
                    organism.power!! > this.power!!)
                {
                    danger = true
                    break
                }
            }

            if (danger) {
                Logger.log("found danger at a new spot (${newCoordinates.x}, ${newCoordinates.y})... moving somewhere else...", this::class)
                val newCoordinates: Coordinates = this.move()
                useActionSpecificity(newCoordinates)
                return
            }

            this.updateCoordinates(newCoordinates)

        }

    }

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.useActionSpecificity(newCoordinates)
    }

}