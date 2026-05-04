package game

import organism.Organism
import kotlin.reflect.KClass

object Logger {

    fun <T : Organism> log(message: String, senderClass: KClass<T>) {
        println("[${senderClass.simpleName.toString().uppercase()}] $message")
    }

}