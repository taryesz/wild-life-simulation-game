import kotlin.random.Random

class Game {

    var boardWidth: Int = 0
        private set

    var boardHeight: Int = 0
        private set

    private var organisms: MutableList<Organism> = mutableListOf()
    private val specificOrganismKindLimit = 4
    private val availableOrganismTypes: List<Organisms> = listOf(
        Organisms.WOLF,
        Organisms.SHEEP,
        Organisms.CYBER_SHEEP,
        Organisms.ANTELOPE,
        Organisms.FOX,
        Organisms.TURTLE
    )

    fun start() {
        // get user input - board size
        this.getTotalBoardSize()

        // generate the organisms
        this.generateOrganisms()

        // show the board and all the organisms
        while (true) {

            for (org in organisms) {
                println("$org: x=${org.coordinates[0]}, y=${org.coordinates[1]}")
            }

            this.showBoard()

            println("Type 'q' to quit the game, 'n' to generate a new round.")
            val userInput = readln()

            when (userInput) {
                "" -> println("Please enter your input.")
                "q" -> {
                    println("Quitting the game...")
                    break
                }
                "n" -> continue // TODO: next round generation
                else -> println("Invalid input. Please enter a valid number.")
            }

        }

        // set a key that "makes a move"
    }
    fun stop() {}
    fun load() {}
    fun save() {}

    private fun getSingularBoardSize(): Int {
        while(true) {

            val userInput = readln()

            when {
                userInput == "" -> println("Please enter your input.")
                userInput.all { it.isDigit() } -> return userInput.toInt()
                else -> println("Invalid input. Please enter a valid number.")
            }

        }
    }

    private fun getTotalBoardSize() {
        println(">>> Type the board size <<<")

        println("A - the width of the board")
        this.boardWidth = getSingularBoardSize()

        println("B - the height of the board")
        this.boardHeight = getSingularBoardSize()
    }

    private fun showBoard() {

        val boardWidthInAsciiSymbols = (this.boardWidth * 3) + (this.boardWidth + 1)
        val boardHeightInAsciiSymbols = (this.boardHeight * 1) + (this.boardHeight + 1)

        var isCrossTurn = true
        var hyphenCounter = 0
        var spaceCounter = 0

        for (verticalSymbol in 1..boardHeightInAsciiSymbols) {

            for (horizontalSymbol in 1..boardWidthInAsciiSymbols) {
                if (isCrossTurn) {
                    print(if (verticalSymbol % 2 != 0) "+" else "|")
                    isCrossTurn = false
                    continue
                }

                if (verticalSymbol % 2 != 0) {
                    print("-")
                }
                else {
                    ++spaceCounter
                    if (spaceCounter == 2) {
                        var foundOrganism = false
                        for (organism in organisms) {
                            foundOrganism = organism.draw(horizontalSymbol.floorDiv(3) , verticalSymbol.floorDiv(2))
                            if (foundOrganism) break
                        }
                        if (!foundOrganism) print(" ")
                    }
                    else {
                        print(" ")
                        if (spaceCounter == 3) spaceCounter = 0
                    }
                }

                hyphenCounter++
                if (hyphenCounter % 3 == 0) isCrossTurn = true
            }

            println()
            isCrossTurn = true

        }

    }

    // +---+---+---+        1 5 9 13
    // |   |   |   |        2
    // +---+---+---+        3
    // |   |   |   |        4
    // +---+---+---+        5

    private fun generateOrganisms() {

        for (organismType in this.availableOrganismTypes) {
            repeat(Random.nextInt(2, this.specificOrganismKindLimit)) {
                when (organismType) {
                    Organisms.WOLF -> organisms.add(Wolf(this))
                    Organisms.SHEEP -> null
                    Organisms.FOX -> null
                    Organisms.TURTLE -> null
                    Organisms.ANTELOPE -> null
                    Organisms.CYBER_SHEEP -> null
                }
            }
        }

    }

}