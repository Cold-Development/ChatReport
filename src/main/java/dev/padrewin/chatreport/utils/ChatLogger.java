package dev.padrewin.chatreport.utils;

import org.bukkit.entity.Player;
import dev.padrewin.chatreport.setting.SettingKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ChatLogger {

    private static final Map<String, Deque<String>> playerMessages = new HashMap<>();


    /**
     * Logs a message for a specific player.
     *
     * @param player  The player sending the message.
     * @param message The message to log.
     */
    public static void logMessage(Player player, String message) {
        String playerName = player.getName();
        playerMessages.putIfAbsent(playerName, new LinkedList<>());

        Deque<String> messages = playerMessages.get(playerName);
        int maxMessages = SettingKey.MAX_LOG_MESSAGES.get();

        if (messages.size() >= maxMessages) {
            messages.pollFirst(); // Elimină cel mai vechi mesaj
        }

        // Formatează mesajul
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String formattedMessage = "[" + timestamp + "] " + message;

        messages.addLast(formattedMessage);
    }


    /**
     * Retrieves the most recent messages for a player.
     *
     * @param playerName     The player whose messages are being retrieved.
     * @param messageCount The number of messages to retrieve.
     * @return A list of the most recent messages, or an empty list if no messages exist.
     */
    public static List<String> getRecentMessages(String playerName, int messageCount) {
        Deque<String> messages = playerMessages.getOrDefault(playerName, new LinkedList<>());

        List<String> recentMessages = new ArrayList<>();
        int fromIndex = Math.max(0, messages.size() - messageCount);
        int index = 0;

        for (String msg : messages) {
            if (index++ >= fromIndex) {
                recentMessages.add(msg);
            }
        }

        return recentMessages;
    }

    public static boolean hasMessages(String playerName) {
        return playerMessages.containsKey(playerName);
    }


}
