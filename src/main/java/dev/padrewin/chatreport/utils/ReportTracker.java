package dev.padrewin.chatreport.utils;

import java.util.WeakHashMap;

public class ReportTracker {

    private static final WeakHashMap<Object, Long> activeReports = new WeakHashMap<>();

    /**
     * Checks if a player is already reported (by name or UUID).
     *
     * @param key The key (playerName or UUID) to check.
     * @return True if the player is reported and the report is still active, otherwise false.
     */
    public static boolean isReported(Object key) {
        Long expiryTime = activeReports.get(key);
        if (expiryTime == null || System.currentTimeMillis() > expiryTime) {
            activeReports.remove(key); // Cleanup expired entry
            return false;
        }
        return true;
    }

    /**
     * Adds a player to the report tracker (by name or UUID).
     *
     * @param key The key (playerName or UUID) of the reported player.
     * @param timeoutSeconds The timeout in seconds after which the report expires.
     */
    public static void addReport(Object key, int timeoutSeconds) {
        long timeoutMillis = timeoutSeconds * 1000L; // Convert to milliseconds
        activeReports.put(key, System.currentTimeMillis() + timeoutMillis);
    }

    /**
     * Removes a specific report (by name or UUID).
     *
     * @param key The key (playerName or UUID) to remove.
     */
    public static void removeReport(Object key) {
        activeReports.remove(key);
    }

    /**
     * Clears all active reports from the tracker.
     */
    public static void clearReports() {
        activeReports.clear();
    }
}
