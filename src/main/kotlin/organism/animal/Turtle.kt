package organism.animal

import game.Game
import game.Logger
import organism.ActionSpecificity
import organism.CollisionSpecificity
import organism.Coordinates
import organism.Organism
import kotlin.random.Random

class Turtle(game: Game) : Animal(), ActionSpecificity, CollisionSpecificity {

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
        if (Random.nextDouble() >= 0.75) {
            Logger.log("waited for eternity and decided to move!", this::class)
            super.act()
        }

    }

    override fun act() {
        this.useActionSpecificity()
    }

    override fun useCollisionSpecificity(collidingOrganism: Organism) {

        // Odpiera ataki zwierząt o
        // sile <5. Napastnik musi
        // wrócić na swoje
        // poprzednie pole.
        if (collidingOrganism.power!! < 5) {
            Logger.log("repelled the attack of ${collidingOrganism::class.simpleName.toString().uppercase()}!", this::class)
            if (collidingOrganism is Animal) {
                collidingOrganism.coordinates.x = collidingOrganism.previousCoordinates.x
                collidingOrganism.coordinates.y = collidingOrganism.previousCoordinates.y
            }
            return
        }

        this.game!!.organismsToRemove.add(this)

    }

}