package ovh.not.kotlinselfbot

import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.eclipse.golo.compiler.GoloClassLoader
import java.io.File
import java.io.FileInputStream
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Method
import javax.script.Invocable
import javax.script.ScriptEngineManager

abstract class Command(val file: File, val content: String) {
    abstract fun invoke(ctx: Context)
}

class NashornCommand(file: File, content: String, val invocable: Invocable): Command(file, content) {
    override fun invoke(ctx: Context) {
        invocable.invokeFunction("execute", ctx)
    }
}

class GoloCommand(file: File, content: String, val method: Method): Command(file, content) {
    override fun invoke(ctx: Context) {
        try {
            method.invoke(null, ctx)
        } catch (e: Exception) {
            ctx.msg(e.message!!)
            e.printStackTrace()
        }
    }
}

class CommandManager {
    val lookup = MethodHandles.lookup()
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
        val goloClassLoader = GoloClassLoader()
        files.forEach {
            val content = it.readText()
            if (it.extension == "js") {
                val engine = engineManager.getEngineByName("nashorn")
                engine.eval(content)
                val invokable = engine as Invocable
                val command = NashornCommand(it, content, invokable)
                commands[it.nameWithoutExtension.toLowerCase()] = command
            } else if (it.extension == "golo") {
                val clazz = goloClassLoader.load(cmdsPath + "/" + it.name, FileInputStream(it))
                var method: Method? = null
                clazz.declaredMethods.forEach methods@ {
                    if (it.name == "execute") {
                        method = it
                        return@methods
                    }
                }
                if (method == null) {
                    throw RuntimeException("error loading ${it.name}")
                }
                val command = GoloCommand(it, content, method!!)
                commands[it.nameWithoutExtension.toLowerCase()] = command
            }
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
