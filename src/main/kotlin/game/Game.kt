package game

import organism.animal.Wolf
import organism.Organism
import organism.Organisms
import organism.plant.Belladonna
import kotlin.system.exitProcess

class Game {

    var boardWidth: Int = 0
        private set

    var boardHeight: Int = 0
        private set

    var organisms: MutableList<Organism> = mutableListOf()
        private set

    var organismsToAdd: MutableList<Organism> = mutableListOf()
        private set

    var organismsToRemove: MutableList<Organism> = mutableListOf()
        private set

    var collidingOrganisms: MutableMap<Organism, Boolean> = mutableMapOf()
        private set

    private val specificOrganismKindLimit = 4

    fun start() {
        // get user input - board size
        this.getTotalBoardSize().also { println("Utworzono plansze.") }

        // generate the organisms
        this.spawnOrganisms().also { println("Wygenerowano organizmy.") }

        // show the board and all the organisms
        this.draw()

        // set a key that "makes a move"
    }

    private fun getSingularBoardSize(): Int {

        val size = UserInput.get { userInput ->
            var isSatisfactoryValue = false
            when {
                userInput == "" -> println("Please enter your input.")
                userInput.all { it.isDigit() } -> isSatisfactoryValue = true
                else -> println("Invalid input. Please enter a valid number.")
            }
            isSatisfactoryValue
        }

        return size.toInt()

    }

    private fun getTotalBoardSize() {
        println(">>> Type the board size <<<")

        println("A - the width of the board")
        this.boardWidth = getSingularBoardSize()

        println("B - the height of the board")
        this.boardHeight = getSingularBoardSize()
    }

    private fun drawBoard() {

        val separator = "+---".repeat(boardWidth) + "+"

        for (y in 0 until boardHeight) {

            println(separator)

            for (x in 0 until boardWidth) {
                print("|")

                val organism = organisms.find { it.coordinates.x == x && it.coordinates.y == y }

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

    private fun spawnOrganisms() {

        for (organismType in Organisms.entries) {
            // repeat(Random.nextInt(2, this.specificOrganismKindLimit)) {

            repeat(1) {
                when (organismType) {

                    // Animals
                    Organisms.WOLF -> organisms.add(Wolf(this))
                    Organisms.SHEEP -> null//organisms.add(Sheep(this))
                    Organisms.FOX -> null//organisms.add(Fox(this))
                    Organisms.TURTLE -> null//organisms.add(Turtle(this))
                    Organisms.ANTELOPE -> null//organisms.add(Antelope(this))
                    Organisms.CYBER_SHEEP -> null

                    // Plants
                    Organisms.GRASS -> null//organisms.add(Grass(this))
                    Organisms.SOW_THISTLES -> null//organisms.add(SowThistles(this))
                    Organisms.GUARANA -> null//organisms.add(Guarana(this))
                    Organisms.BELLADONNA -> organisms.add(Belladonna(this))

                }
            }
        }

    }

    private fun updateOrganisms() {
        this.organisms.addAll(organismsToAdd)
        this.organismsToAdd.clear()
        this.organisms.removeAll(organismsToRemove)
        this.organismsToRemove.clear()
        this.collidingOrganisms.clear()
    }

    private fun performNextAction(): String {

        println("Type 'q' to quit the game, 'n' to generate a new round.")

        return UserInput.get { userInput ->
            var isUserInputGameQuit = false
            when (userInput) {
                "" -> println("Please enter your input.")
                "q" -> {
                    println("Quitting the game...")
                    isUserInputGameQuit = true
                }
                "n" -> {
                    this.organisms.sortByDescending { it.initiative } /* Organisms with the highest initiative property
                                                                         make their move firstly */
                    this.organisms.forEach { it.act() }
                    this.organisms.forEach { it.collide() }
                    this.updateOrganisms()
                    this.draw()
                }
                else -> println("Invalid input. Please enter a valid number.")
            }
            isUserInputGameQuit
        }

    }

    private fun draw() {
        while (true) {

            this.drawBoard()

            val actionResult = this.performNextAction()
            if (actionResult == "q") exitProcess(0)  // the game finished

        }
    }

}