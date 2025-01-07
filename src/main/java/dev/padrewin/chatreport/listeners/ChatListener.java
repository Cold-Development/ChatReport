package dev.padrewin.chatreport.listeners;

import dev.padrewin.chatreport.utils.ChatLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    /**
     * Listens to chat messages sent by players and logs them using ChatLogger.
     *
     * @param event The AsyncPlayerChatEvent containing player chat data.
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Log the message if it is sent by a player
        ChatLogger.logMessage(event.getPlayer(), event.getMessage());
    }
}
