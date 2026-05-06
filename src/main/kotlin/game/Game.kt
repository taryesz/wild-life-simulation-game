package game

import organism.Population
import kotlin.system.exitProcess

class Game: GameLauncher {

    val world: Board = Board(this)

    val population: Population = Population(this)

    override fun start() {

        Logger.log("Starting the game...", this::class)

        // Get a board size from the user.
        this.world.readSize().also {
            Logger.log("Successfully read the board size.", this::class)
        }

        // Generate all organisms.
        this.population.spawn().also {
            Logger.log("Successfully generated all organisms.", this::class)
        }

        // Play the game:
        // - draw the board,
        // - get a command from the user -> perform an according action.
        this.play()

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
        return UserInputReader.get { userInput ->
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
        this.population.live()
        this.world.draw()
    }

}