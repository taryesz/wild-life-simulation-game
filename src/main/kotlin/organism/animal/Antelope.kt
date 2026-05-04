package organism.animal

import game.Game
import game.Logger
import organism.ActionSpecificity
import organism.CollisionSpecificity
import organism.Coordinates
import organism.Organism
import kotlin.random.Random

class Antelope(game: Game) : Animal(), ActionSpecificity, CollisionSpecificity {

    init {
        this.power = 4
        this.initiative = 4
        this.game = game
        this.spawn()
        this.icon = "a"
    }

    override fun reproduce(): Antelope {
        return Antelope(this.game!!)
    }

    override fun useActionSpecificity(newCoordinates: Coordinates?) {

        // Zasięg ruchu wynosi 2 pola.
        newCoordinates?.let {
            this.updateCoordinates(this.move(newCoordinates.x, newCoordinates.y))
        }

    }

    override fun act() {
        val newCoordinates: Coordinates = this.move()
        this.useActionSpecificity(newCoordinates)
    }

    override fun useCollisionSpecificity(collidingOrganism: Organism) {

        // 50% szans na ucieczkę
        // przed walką. Wówczas
        // przesuwa się na niezajęte
        // sąsiednie pole.
        if (Random.nextFloat() <= 0.5f) {
            Logger.log("managed to escape a fight with ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
            this.act()
            return
        }

        this.game!!.organismsToRemove.add(this)

    }

}