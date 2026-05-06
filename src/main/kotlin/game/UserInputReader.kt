package game

object UserInputReader {

    fun get(comparator: (String) -> Boolean): String {
        while(true) {
            val userInput: String = readln()
            if (comparator(userInput)) return userInput
        }
    }

}