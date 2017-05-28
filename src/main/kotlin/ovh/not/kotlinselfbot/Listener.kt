package ovh.not.kotlinselfbot

import club.minnced.kjda.entities.send
import net.dv8tion.jda.client.events.call.CallCreateEvent
import net.dv8tion.jda.client.events.call.CallDeleteEvent
import net.dv8tion.jda.core.events.ReadyEvent
import net.dv8tion.jda.core.events.ShutdownEvent
import net.dv8tion.jda.core.events.guild.GuildBanEvent
import net.dv8tion.jda.core.events.guild.GuildJoinEvent
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveAllEvent
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.core.hooks.ListenerAdapter

class Listener: ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("ready boyos")
    }

    override fun onShutdown(event: ShutdownEvent) {

    }

    override fun onMessageReceived(event: MessageReceivedEvent) {
        var content = event.message.contentDisplay
        println(content)
        if (event.author.idLong != event.jda.selfUser.idLong) {
            return
        }
        println(content)
        if (content == "self.reload") {
            commandManager.commands.clear()
            commandManager.load()
            event.channel.sendMessage("reloaded lol").queue()
            return
        }
        if (content.length < cmdPrefix.length + 1 || !content.startsWith(cmdPrefix)) {
            return
        }
        content = content.substring(cmdPrefix.length)
        var args = content.split(Regex("\\s+"))
        val name = args[0]
        val command = commandManager.commands[name] ?: return
        if (args.size > 1) {
            val nArgs = ArrayList<String>()
            var first = true
            for (arg in args) {
                if (first) {
                    first = false
                    continue
                }
                nArgs.add(arg)
                args = nArgs
            }
        } else {
            args = ArrayList()
        }
        val ctx = Context(event, args, command, commandManager)
        command.invokable.invokeFunction("execute", ctx)
    }

    override fun onCallCreate(event: CallCreateEvent?) {

    }

    override fun onCallDelete(event: CallDeleteEvent?) {

    }

    override fun onGuildJoin(event: GuildJoinEvent?) {

    }

    override fun onGuildLeave(event: GuildLeaveEvent?) {

    }

    override fun onGuildBan(event: GuildBanEvent?) {

    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent?) {

    }

    override fun onMessageReactionRemove(event: MessageReactionRemoveEvent?) {

    }

    override fun onMessageReactionRemoveAll(event: MessageReactionRemoveAllEvent?) {

    }
}