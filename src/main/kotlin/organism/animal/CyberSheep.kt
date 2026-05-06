package organism.animal

import game.Coordinates
import game.Game
import organism.ActionSpecificity
import organism.plant.Heracleum

class CyberSheep(
    game: Game,
    baseX: Int? = null,
    baseY: Int? = null
) : Animal(), ActionSpecificity {

    init {
        this.power = 11
        this.initiative = 4
        this.game = game
        this.spawn(baseX, baseY)
        this.icon = "c"
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {
        val newCoordinates: Coordinates = this.move()
        this.useActionSpecificity(newCoordinates)
    }

    override fun move(baseX: Int, baseY: Int): Coordinates {

        // Get all Hogweeds present in the game.
        val hogweeds = this.game!!.population.getAllOrganisms(Heracleum::class)

        // If there are no hogweeds, act like a normal sheep (default movement).
        if (hogweeds.isEmpty()) return super.move(baseX, baseY)

        // Find the closest hogweed using Manhattan distance.
        val closestHogweed = hogweeds.minByOrNull {
            kotlin.math.abs(it.coordinates.x - baseX) + kotlin.math.abs(it.coordinates.y - baseY)
        }!!

        // Calculate the step direction towards the closest hogweed.
        var dx = 0
        var dy = 0
        val diffX = closestHogweed.coordinates.x - baseX
        val diffY = closestHogweed.coordinates.y - baseY

        // Move along the axis where the target is furthest away.
        if (kotlin.math.abs(diffX) > kotlin.math.abs(diffY)) {
            dx = if (diffX > 0) 1 else -1
        } else if (kotlin.math.abs(diffY) > kotlin.math.abs(diffX)) {
            dy = if (diffY > 0) 1 else -1
        } else {
            // If distance is equal on both axes, pick one randomly to move diagonally.
            if (kotlin.random.Random.nextBoolean()) {
                dx = if (diffX > 0) 1 else -1
            } else {
                dy = if (diffY > 0) 1 else -1
            }
        }

        return Coordinates(baseX + dx, baseY + dy)

    }

    override val organismFactory = ::CyberSheep

}