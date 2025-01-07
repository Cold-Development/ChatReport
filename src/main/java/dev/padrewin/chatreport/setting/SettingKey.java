package dev.padrewin.chatreport.setting;

import dev.padrewin.colddev.config.ColdSetting;
import dev.padrewin.colddev.config.ColdSettingSerializer;
import dev.padrewin.chatreport.ChatReport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.padrewin.colddev.config.ColdSettingSerializers.*;

public class SettingKey {

    private static final List<ColdSetting<?>> KEYS = new ArrayList<>();

    public static final ColdSetting<String> BASE_COMMAND_REDIRECT = create("base-command-redirect", STRING, "",
            "Which command should we redirect to when using '/chatreport' with no subcommand specified?",
            "You can use a value here such as 'version' to show the output of '/chatreport version'",
            "If you have any aliases defined, do not use them here",
            "If left as blank, the default behavior of showing '/chatreport version' with bypassed permissions will be used",
            "");

    public static final ColdSetting<String> WEBHOOK_URL = create("webhook-url", STRING, "",
            "The URL for the Discord webhook where reports will be sent.",
            "If this is not configured, the plugin will not send any reports.");

    public static final ColdSetting<Integer> REPORT_TIMEOUT = create("report-timeout", INTEGER, 300,
            "The timeout in seconds before a player can be reported again.",
            "This prevents duplicate reports for the same player.");

    public static final ColdSetting<String> SERVER_NAME = create("server-name", STRING, "",
            "The name of the server to be included in reports.",
            "This name will appear in the log messages sent to the Discord webhook.");

    public static final ColdSetting<Boolean> ENABLE_ROLE_MENTION = create("mention-role.enable", BOOLEAN, false,
            "Enable or disable mentioning a role in Discord reports.",
            "When enabled, the role specified by 'mention-role.role-id' will be tagged in reports.");

    public static final ColdSetting<String> ROLE_ID = create("mention-role.role-id", STRING, "",
            "The ID of the role to mention in Discord reports.",
            "If this is not configured, no role will be mentioned.");

    public static final ColdSetting<Integer> DEFAULT_MESSAGE_COUNT = create("default-message-count", INTEGER, 25,
            "The default number of messages to include in reports.");

    public static final ColdSetting<Integer> MAX_LOG_MESSAGES = create("max-log-messages", INTEGER, 30,
            "The maximum number of messages to store for each player.",
            "This limits the memory usage and ensures efficient storage.");

    public static final ColdSetting<Boolean> ENABLE_DEBUG = create("enable-debug", BOOLEAN, false,
            "Enable or disable debug mode for the ChatReport plugin.",
            "When enabled, additional debug information will be logged.");

    // Utility methods
    private static <T> ColdSetting<T> create(String key, ColdSettingSerializer<T> serializer, T defaultValue, String... comments) {
        ColdSetting<T> setting = ColdSetting.backed(ChatReport.getInstance(), key, serializer, defaultValue, comments);
        KEYS.add(setting);
        return setting;
    }

    public static List<ColdSetting<?>> getKeys() {
        return Collections.unmodifiableList(KEYS);
    }

    private SettingKey() {}
}
