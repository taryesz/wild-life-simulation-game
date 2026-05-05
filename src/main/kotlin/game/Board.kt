package game

class Board(val game: Game) {

    var boardWidth: Int? = null
        private set

    var boardHeight: Int? = null
        private set

    fun readSize(): Coordinates {

        Logger.log("Type the width of the board: ", this::class)
        this.boardWidth = readSingularSize()

        Logger.log("Type the height of the board: ", this::class)
        this.boardHeight = readSingularSize()

        // at this point, the size properties are ALWAYS going to be initialized
        return Coordinates(this.boardWidth!!, this.boardHeight!!)

    }

    private fun readSingularSize(): Int {

        val size = UserInput.get { userInput ->
            var isSatisfactoryValue = false
            when {
                userInput == "" -> Logger.log("Please enter your input.", this::class)
                userInput.all { it.isDigit() } -> isSatisfactoryValue = true
                else -> Logger.log("Invalid input. Please enter a valid number.", this::class)
            }
            isSatisfactoryValue
        }

        return size.toInt()

    }

    fun draw() {

        val separator = "+---".repeat(boardWidth!!) + "+"

        for (y in 0 until boardHeight!!) {

            println(separator)

            for (x in 0 until boardWidth!!) {
                print("|")

                val organism = this.game.organisms.find { it.coordinates.x == x && it.coordinates.y == y }

                if (organism != null) {
                    organism.draw()
                } else {
                    print("   ")
                }
            }
            println("|")
        }

        println(separator)

    }

}