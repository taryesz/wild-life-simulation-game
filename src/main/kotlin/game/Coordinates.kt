package game

class Coordinates(initX: Int? = null, initY: Int? = null) {
    var x = initX ?: -1
    var y = initY ?: -1

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

}