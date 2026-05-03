import kotlin.random.Random

abstract class Animal : Organism() {

    override fun act() {

        val moveHorizontal = Random.nextBoolean()

        var dx = 0
        var dy = 0

        if (moveHorizontal) {
            dx = Random.nextInt(-1, 2)
        } else {
            dy = Random.nextInt(-1, 2)
        }

        val nextX = this.coordinates.x + dx
        val nextY = this.coordinates.y + dy

        if (nextX in 0 until game!!.boardWidth && nextY in 0 until game!!.boardHeight) {
            this.previousCoordinates.x = this.coordinates.x
            this.previousCoordinates.y = this.coordinates.x
            this.coordinates.x = nextX
            this.coordinates.y = nextY
        }

    }

    override fun collide() {

        val collidingOrganism: Organism? = this.checkIfCollision()

        collidingOrganism?.let {
            if (collidingOrganism::class == this::class) {
                println("LOVE")
            }
            else {
                // fight
                // wygrywa ten kto atakowal (czyli czyje wspolrzedne sie zmienialy)
                if (this.power == collidingOrganism.power) {
                    if (this.coordinates.x == this.previousCoordinates.x && this.coordinates.y == this.previousCoordinates.y) {
                        // usunac this
                        this.game!!.organismsToRemove.add(this)
                    }
                    else if (collidingOrganism.coordinates.x == this.previousCoordinates.x && collidingOrganism.coordinates.y == this.previousCoordinates.x) {
                        // usunac organism
                        this.game!!.organismsToRemove.add(collidingOrganism)
                    }
                }
            }
        }

    }

}