package organism

import game.Game
import kotlin.random.Random

abstract class Organism {
    /*
    podstawowe pola:
 siła
 inicjatywa
 położenie (x,y).
 świat - referencja do świata w którym znajduje się organizm
podstawowe metody:
 akcja() → określa zachowanie organizmu w trakcie tury,
 kolizja() → określa zachowanie organizmu w trakcie kontaktu/zderzenia z innym
organizmem,
 rysowanie() → powoduje narysowanie symbolicznej reprezentacji organizmu
     */

    var power: Int? = null
    var initiative: Int? = null
    var coordinates = Coordinates(null, null)
    var previousCoordinates = Coordinates(null, null)
    var game: Game? = null
    protected var icon: String? = null

    abstract fun act()
    abstract fun collide()

    abstract fun reproduce(): Organism

    protected fun spawn() {
        do {
            val x = Random.nextInt(0, game!!.boardWidth)
            val y = Random.nextInt(0, game!!.boardHeight)
            this.coordinates.set(x, y)
            this.previousCoordinates.set(x, y)
            // println("Wygenerowano nowy organizm na $x, $y.")
        } while (checkIfCollision() is Organism)
    }

    fun draw() {
        print(" ${this.icon ?: "?"} ")
    }

    protected fun checkIfCollision(): Organism? {
        for (otherOrganism in this.game!!.organisms) {
            if (this !== otherOrganism &&
                this.coordinates.x == otherOrganism.coordinates.x &&
                this.coordinates.y == otherOrganism.coordinates.y) {
                // println("Znaleziono kolizje ${this.coordinates.x}, ${this.coordinates.y}.")
                return otherOrganism
            }
        }
        // println("Nie znaleziono kolizji.")
        return null
    }
}

class Coordinates(initX: Int? = null, initY: Int? = null) {
    var x = initX ?: -1
    var y = initY ?: -1

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

}