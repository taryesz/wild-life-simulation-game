package game

import kotlin.reflect.KClass

object Logger {

    fun <T : Any> log(message: String, senderClass: KClass<T>) {
        println("[${senderClass.simpleName.toString().uppercase()}] $message")
    }

}