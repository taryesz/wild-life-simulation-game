package organism.animal

import game.Logger
import organism.Coordinates
import organism.Organism

abstract class Animal : Organism() {

     fun updateCoordinates(newCoordinates: Coordinates) {

         if (newCoordinates.x in 0 until game!!.boardWidth && newCoordinates.y in 0 until game!!.boardHeight) {
             this.previousCoordinates.x = this.coordinates.x
             this.previousCoordinates.y = this.coordinates.y
             this.coordinates.x = newCoordinates.x
             this.coordinates.y = newCoordinates.y
         }
         else {
             // Make an organism move again if the previously generated coordinates are invalid
             this.updateCoordinates(this.move())
             return
         }

         Logger.log("moved to (${this.coordinates.x},${this.coordinates.y}).", this::class)

    }

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.updateCoordinates(newCoordinates)
    }

}