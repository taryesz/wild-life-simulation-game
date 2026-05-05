package game

import organism.animal.Wolf
import organism.Organism
import organism.Organisms
import organism.animal.Antelope
import organism.animal.CyberSheep
import organism.animal.Fox
import organism.animal.Sheep
import organism.animal.Turtle
import organism.plant.Belladonna
import organism.plant.Grass
import organism.plant.Guarana
import organism.plant.Heracleum
import organism.plant.SowThistles
import kotlin.random.Random
import kotlin.system.exitProcess

class Game: GameLauncher {

    val world: Board = Board(this)

    var organisms: MutableList<Organism> = mutableListOf()
        private set

    var organismsToAdd: MutableList<Organism> = mutableListOf()
        private set

    var organismsToRemove: MutableList<Organism> = mutableListOf()
        private set

    var collidingOrganisms: MutableMap<Organism, Boolean> = mutableMapOf()
        private set

    private val maxSpecificOrganismCount = 4
    private val minSpecificOrganismCount = 2

    override fun start() {

        Logger.log("Starting the game...", this::class)

        // Get a board size from the user.
        this.world.readSize().also {
            Logger.log("Successfully read the board size.", this::class)
        }

        // Generate all organisms.
        this.spawnOrganisms().also {
            Logger.log("Successfully generated all organisms.", this::class)
        }

        // Play the game:
        // - draw the board,
        // - get a command from the user -> perform an according action.
        this.play()

    }

    private fun spawnOrganisms() {

        for (organismType in Organisms.entries) {

            // Generate a random number of each organism.
            repeat(Random.nextInt(this.minSpecificOrganismCount, this.maxSpecificOrganismCount)) {
                when (organismType) {

                    // Animals
                    Organisms.WOLF -> organisms.add(Wolf(this))
                    Organisms.SHEEP -> organisms.add(Sheep(this))
                    Organisms.FOX -> organisms.add(Fox(this))
                    Organisms.TURTLE -> organisms.add(Turtle(this))
                    Organisms.ANTELOPE -> organisms.add(Antelope(this))
                    Organisms.CYBER_SHEEP -> organisms.add(CyberSheep(this))

                    // Plants
                    Organisms.GRASS -> organisms.add(Grass(this))
                    Organisms.SOW_THISTLES -> organisms.add(SowThistles(this))
                    Organisms.GUARANA -> organisms.add(Guarana(this))
                    Organisms.BELLADONNA -> organisms.add(Belladonna(this))
                    Organisms.HERACLEUM -> organisms.add(Heracleum(this))

                }
            }

        }

    }

    private fun play() {

        this.world.draw()

        while (true) {
            val userCommand = this.readUserCommand()
            if (userCommand == UserCommands.QUIT_GAME.keyboardKey) exitProcess(0)  // The game closes.
        }

    }

    private fun readUserCommand(): String {

        Logger.log(
            "Type '${UserCommands.QUIT_GAME.keyboardKey}' to quit the game, " +
                    "'${UserCommands.NEXT_ITERATION.keyboardKey}' to perform an iteration.", this::class)

        // Read the user's command input and check if there was a "close the game" request.
        return UserInput.get { userInput ->
            var isUserInputGameQuit = false
            when (userInput) {
                UserCommands.EMPTY.keyboardKey -> {
                    Logger.log("Please enter your input.", this::class)
                }
                UserCommands.QUIT_GAME.keyboardKey -> {
                    Logger.log("Quitting the game...", this::class)
                    isUserInputGameQuit = true
                }
                UserCommands.NEXT_ITERATION.keyboardKey -> {
                    Logger.log("Performing next iteration...", this::class)
                    this.performIteration()
                }
                else -> Logger.log("Invalid input. Please try again.", this::class)
            }
            isUserInputGameQuit
        }

    }

    private fun performIteration() {

        // The first organisms to make a move are the ones with the highest "initiative" property - sort accordingly.
        this.organisms.sortByDescending { it.initiative }

        this.organisms.forEach { it.act() }     // Each organism moves (and optionally does something else).
        this.organisms.forEach { it.collide() } // The game checks the collisions among the organisms and solves them.
        this.updateOrganisms()                  // Actually change the organisms' properties after each iteration.
        this.world.draw()

    }

    private fun updateOrganisms() {

        this.organisms.addAll(organismsToAdd)           // Add every new "baby organism".
        this.organismsToAdd.clear()
        this.organisms.removeAll(organismsToRemove)     // Remove every killed organism.
        this.organismsToRemove.clear()

        // This map stores the collision data for each organism - we need to clear it after each iteration.
        this.collidingOrganisms.clear()

    }

}