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

    val power: Int? = null
    val initiative: Int? = null
    var coordinates = IntArray(2)
    var game: Game? = null
    var icon: String? = null

    abstract fun act()
    abstract fun collide()

    fun setInitialPosition() {
        val x = Random.nextInt(1, game?.boardWidth ?: 0)
        this.coordinates[0] = x

        val y = Random.nextInt(1, game?.boardHeight ?: 0)
        this.coordinates[1] = y
    }

    fun draw(x: Int, y: Int): Boolean {
        if (coordinates[0] == x && coordinates[1] == y) {
            print(this.icon ?: "?")
            return true
        }
        return false
    }

}