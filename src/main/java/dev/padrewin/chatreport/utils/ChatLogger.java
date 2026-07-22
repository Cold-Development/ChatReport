package dev.padrewin.chatreport.utils;

import org.bukkit.entity.Player;
import dev.padrewin.chatreport.setting.SettingKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatLogger {

    private static final Map<String, Deque<String>> playerMessages = new ConcurrentHashMap<>();


    /**
     * Logs a message for a specific player.
     *
     * @param player  The player sending the message.
     * @param message The message to log.
     */
    public static void logMessage(Player player, String message) {
        String playerName = player.getName();
        Deque<String> messages = playerMessages.computeIfAbsent(playerName, key -> new ArrayDeque<>());
        int maxMessages = SettingKey.MAX_LOG_MESSAGES.get();

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedMessage = "[" + timestamp + "] " + message;

        synchronized (messages) {
            if (messages.size() >= maxMessages) {
                messages.pollFirst(); // Elimină cel mai vechi mesaj
            }

            messages.addLast(formattedMessage);
        }
    }


    /**
     * Retrieves the most recent messages for a player.
     *
     * @param playerName     The player whose messages are being retrieved.
     * @param messageCount The number of messages to retrieve.
     * @return A list of the most recent messages, or an empty list if no messages exist.
     */
    public static List<String> getRecentMessages(String playerName, int messageCount) {
        Deque<String> messages = playerMessages.get(playerName);
        if (messages == null) {
            return List.of();
        }

        synchronized (messages) {
            List<String> snapshot = new ArrayList<>(messages);
            int fromIndex = Math.max(0, snapshot.size() - messageCount);
            return new ArrayList<>(snapshot.subList(fromIndex, snapshot.size()));
        }
    }

    public static boolean hasMessages(String playerName) {
        return playerMessages.containsKey(playerName);
    }


}
