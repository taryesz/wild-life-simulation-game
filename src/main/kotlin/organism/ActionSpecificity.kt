package organism

import game.Coordinates

interface ActionSpecificity {
    fun useActionSpecificity(newCoordinates: Coordinates? = null)
}