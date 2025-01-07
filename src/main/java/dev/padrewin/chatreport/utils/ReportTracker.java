package dev.padrewin.chatreport.utils;

import java.util.WeakHashMap;
import java.util.UUID;

public class ReportTracker {

    private static final WeakHashMap<UUID, Long> activeReports = new WeakHashMap<>();

    /**
     * Adds a player to the report tracker.
     *
     * @param playerId The UUID of the reported player.
     * @param timeoutSeconds The timeout in seconds after which the report expires.
     */
    public static void addReport(UUID playerId, int timeoutSeconds) {
        long timeoutMillis = timeoutSeconds * 1000L; // Convert to milliseconds
        activeReports.put(playerId, System.currentTimeMillis() + timeoutMillis);
    }

    /**
     * Checks if a player is already reported.
     *
     * @param playerId The UUID of the player to check.
     * @return True if the player is reported and the report is still active, otherwise false.
     */
    public static boolean isReported(UUID playerId) {
        Long expiryTime = activeReports.get(playerId);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            activeReports.remove(playerId); // Cleanup expired entry
            return false;
        }
        return true;
    }

    /**
     * Clears all active reports from the tracker.
     */
    public static void clearReports() {
        activeReports.clear();
    }
}
