package dev.padrewin.chatreport.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import dev.padrewin.chatreport.ChatReport;

public class WebhookClient {

    private final String webhookUrl;
    private final boolean debug;
    private final boolean mentionEnabled;
    private final String roleId;

    public WebhookClient(FileConfiguration config) {
        this.webhookUrl = config.getString("webhook-url", "");
        this.debug = config.getBoolean("enable-debug", false);
        this.mentionEnabled = config.getBoolean("mention-role.enable", false);
        this.roleId = config.getString("mention-role.role-id", "");
        debug("WebhookClient initialized with:");
        debug("Webhook URL: " + this.webhookUrl);
        debug("Debug enabled: " + this.debug);
        debug("Mention enabled: " + this.mentionEnabled);
        debug("Role ID: " + this.roleId);
    }


    /**
     * Sends a report to the configured webhook.
     *
     * @param reporterName Name of the player reporting.
     * @param targetPlayerName Player being reported.
     * @param messages List of recent messages from the target player.
     */
    public void sendReport(String reporterName, String targetPlayerName, List<String> messages) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            debug("Webhook URL is not configured.");
            return;
        }

        try {
            JsonObject payload = new JsonObject();

            // Add content for mentions if enabled
            if (mentionEnabled && !roleId.isEmpty()) {
                payload.addProperty("content", "<@&" + roleId + ">");
            }

            JsonObject embed = new JsonObject();

            // Title with the reported player's name
            embed.addProperty("title", targetPlayerName + " - Report");

            // Set the color of the embed
            embed.addProperty("color", 0xFF0000); // Red color

            // Build the log-like message
            StringBuilder logBuilder = new StringBuilder("```log\n");
            String serverName = ChatReport.getInstance().getConfig().getString("server-name", "server");

            for (String message : messages) {
                // Split the message to extract timestamp and actual content
                int closingBracketIndex = message.indexOf("]");
                String timestamp = message.substring(0, closingBracketIndex + 1); // Extract timestamp
                String content = message.substring(closingBracketIndex + 1).trim(); // Extract content

                logBuilder.append(timestamp) // Add the timestamp
                        .append(" (")
                        .append(serverName) // Add server name
                        .append(") ")
                        .append(targetPlayerName) // Add player name
                        .append(" » ")
                        .append(content) // Add the message content
                        .append("\n");
            }

            logBuilder.append("```");

            embed.addProperty("description", logBuilder.toString());

            // Add footer with reporter name and timestamp
            JsonObject footer = new JsonObject();
            String reportTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            footer.addProperty("text", "Reported by: " + reporterName + " - " + reportTime);
            embed.add("footer", footer);

            payload.add("embeds", new JsonArray());
            payload.getAsJsonArray("embeds").add(embed);

            sendPayload(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void sendPayload(JsonObject payload) {
        try {
            debug("Opening connection to webhook URL...");
            URL url = new URL(webhookUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream();
                 OutputStreamWriter osw = new OutputStreamWriter(os);
                 BufferedWriter writer = new BufferedWriter(osw)) {
                writer.write(payload.toString());
                writer.flush();
                debug("Payload written to webhook URL.");
            }

            int responseCode = connection.getResponseCode();
            debug("Response code: " + responseCode);

            if (responseCode != 204) {
                debug("Failed to send webhook: Response code " + responseCode);
            }
        } catch (IOException e) {
            debug("Exception occurred during webhook request: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void debug(String message) {
        if (debug) {
            System.out.println("[ChatReport Debug] " + message);
        }
    }
}