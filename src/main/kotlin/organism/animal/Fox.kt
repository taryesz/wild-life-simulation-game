package organism.animal

import game.Game
import game.Logger
import organism.ActionSpecificity
import game.Coordinates

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

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.useActionSpecificity(newCoordinates)
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {

        // Dobry węch: lis nigdy nie ruszy
        // się na pole zajmowane przez
        // organizm silniejszy niż on
        newCoordinates?.let {
            var danger = false

            val potentiallyDangerousOrganism = this.game!!.population.find(newCoordinates.x, newCoordinates.y)
            potentiallyDangerousOrganism?.let {
                if (it.power!! > this.power!! && it !== this) {
                    danger = true
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

    override val organismFactory = ::Fox

}