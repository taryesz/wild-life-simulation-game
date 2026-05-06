package organism.animal

import game.Coordinates
import game.Logger
import organism.Organism

abstract class Animal : Organism() {

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.updateCoordinates(newCoordinates)
    }

    protected fun updateCoordinates(newCoordinates: Coordinates) {

        if (!this.checkCoordinatesInvalidity(newCoordinates)) {
            // The new coordinates are valid -> use them.
            this.previousCoordinates.x = this.coordinates.x
            this.previousCoordinates.y = this.coordinates.y
            this.coordinates.x = newCoordinates.x
            this.coordinates.y = newCoordinates.y
        } else {
            // Make an organism move again if the previously generated coordinates are invalid.
            this.updateCoordinates(this.move())
            return
        }

        Logger.log("moved to (${this.coordinates.x},${this.coordinates.y}).", this::class)

    }

}