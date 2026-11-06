package dev.padrewin.chatreport.listeners;

import dev.padrewin.chatreport.utils.ChatLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class ChatListener implements Listener {

    /**
     * Logs only messages that were NOT cancelled by other plugins.
     * Uses HIGH priority to run after anti-spam plugins.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        // Doar mesajele care NU au fost anulate ajung aici
        ChatLogger.logMessage(event.getPlayer(), PlainTextComponentSerializer.plainText().serialize(event.message()));
    }
}