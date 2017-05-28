package ovh.not.kotlinselfbot

import net.dv8tion.jda.core.entities.Channel
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import java.io.File
import java.util.concurrent.Future
import javax.script.Invocable
import javax.script.ScriptEngineManager

class Command(val file: File, val name: String, val content: String, val invokable: Invocable)

class CommandManager {
    val commands = HashMap<String, Command>()

    init {
        load()
    }

    fun load(): Boolean {
        val dir = File(cmdsPath)
        fun getFiles(dir: File): List<File> {
            val files = ArrayList<File>()
            for (file in dir.listFiles()) {
                if (file.isDirectory) {
                    val dirFiles = getFiles(file)
                    dirFiles.forEach {
                        files.add(it)
                    }
                } else {
                    files.add(file)
                }
            }
            return files
        }
        val files = getFiles(dir)
        val engineManager = ScriptEngineManager()
        files.forEach {
            val content = it.readText()
            val engine = engineManager.getEngineByName("nashorn")
            engine.eval(content)
            val invokable = engine as Invocable
            val command = Command(it, it.nameWithoutExtension, content, invokable)
            commands[command.name.toLowerCase()] = command
        }
        return true
    }
}

class Context(val event: MessageReceivedEvent, val args: List<String>, val command: Command, val commandManager: CommandManager) {
    val jda = event.jda
    val isGuild = event.isFromType(ChannelType.TEXT)
    val isGroup = event.isFromType(ChannelType.GROUP)
    val isPrivate = event.isFromType(ChannelType.PRIVATE)

    fun msg(content: String): Message {
        val nContent = content.replace(jda.token, "<lol nice token>")
        return event.channel.sendMessage(nContent).complete()
    }
}