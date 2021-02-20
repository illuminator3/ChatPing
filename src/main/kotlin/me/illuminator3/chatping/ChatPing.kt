package me.illuminator3.chatping

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.plugin.java.JavaPlugin

class ChatPing : JavaPlugin()
{
    override fun onEnable() =
        server.pluginManager.registerEvents(Handler(), this)
}

class Handler : Listener
{
    @EventHandler
    fun onChat(e: AsyncPlayerChatEvent)
    {
        val message = e.message

        Bukkit.getOnlinePlayers().forEach {
            if (message.contains("@${it.name}"))
            {
                val lastColor = ChatColor.getLastColors(message)

                e.message = message.replace(Regex("@${it.name}"), "${ChatColor.GREEN}@${it.name}${lastColor.ifEmpty { ChatColor.RESET }}")

                val sound = getSound()

                sound?.also { s ->
                    it.playSound(it.eyeLocation, s, 1f, 1f)
                }
            }
            else if (message.contains("@everyone")) // everyone ping
            {
                val lastColor = ChatColor.getLastColors(message)

                e.message = message.replace(Regex("@everyone"), "${ChatColor.GREEN}@everyone${lastColor.ifEmpty { ChatColor.RESET }}")

                val sound = getSound()

                sound?.also { s ->
                    it.playSound(it.eyeLocation, s, 1f, 1f)
                }
            }
        }
    }

    private fun getSound(): Sound?
    {
        return try {
            Sound.ENTITY_PLAYER_LEVELUP
        } catch (e1: Throwable /* in case we are in <= 1.16 */)
        {
            try
            {
                Sound.valueOf("LEVEL_UP")
            } catch (e2: Throwable /* wtf?! */)
            {
                null
            }
        }
    }
}