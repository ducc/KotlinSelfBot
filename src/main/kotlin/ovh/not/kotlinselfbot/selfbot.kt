package ovh.not.kotlinselfbot

import net.dv8tion.jda.core.AccountType
import net.dv8tion.jda.core.JDA
import net.dv8tion.jda.core.JDABuilder
import java.io.File

val cmdsPath = "cmd"
val cmdPrefix = "self."
var jda: JDA? = null
var commandManager = CommandManager()

fun main(args: Array<String>) {
    val token = args[0]
    jda = JDABuilder(AccountType.CLIENT).setToken(token).addEventListener(Listener()).setAudioEnabled(true).buildBlocking()
}