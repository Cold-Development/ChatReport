package dev.padrewin.chatreport;

import dev.padrewin.chatreport.listeners.ChatListener;
import dev.padrewin.colddev.ColdPlugin;
import dev.padrewin.colddev.config.ColdSetting;
import dev.padrewin.colddev.manager.Manager;
import dev.padrewin.colddev.manager.PluginUpdateManager;
import dev.padrewin.chatreport.manager.CommandManager;
import dev.padrewin.chatreport.manager.LocaleManager;
import dev.padrewin.chatreport.setting.SettingKey;
import dev.padrewin.chatreport.utils.WebhookClient;

import java.io.File;
import java.util.List;

public final class ChatReport extends ColdPlugin {

    /**
     * Console colors
     */
    String ANSI_RESET = "\u001B[0m";
    String ANSI_CHINESE_PURPLE = "\u001B[38;5;93m";
    String ANSI_PURPLE = "\u001B[35m";
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_AQUA = "\u001B[36m";
    String ANSI_PINK = "\u001B[35m";

    private static ChatReport instance;
    private WebhookClient webhookClient;

    public ChatReport() {
        super("Cold-Development", "ChatReport", 24248, null, LocaleManager.class, null);
        instance = this;
    }

    @Override
    public void enable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        getManager(PluginUpdateManager.class);

        // Initialize WebhookClient
        webhookClient = new WebhookClient(getConfig());

        String name = getDescription().getName();
        getLogger().info("");
        getLogger().info(ANSI_CHINESE_PURPLE + "  ____ ___  _     ____  " + ANSI_RESET);
        getLogger().info(ANSI_PINK + " / ___/ _ \\| |   |  _ \\" + ANSI_RESET);
        getLogger().info(ANSI_CHINESE_PURPLE + "| |  | | | | |   | | | |" + ANSI_RESET);
        getLogger().info(ANSI_PINK + "| |__| |_| | |___| |_| |" + ANSI_RESET);
        getLogger().info(ANSI_CHINESE_PURPLE + " \\____\\___/|_____|____/ " + ANSI_RESET);
        getLogger().info("    " + ANSI_GREEN + name + ANSI_RED + " v" + getDescription().getVersion() + ANSI_RESET);
        getLogger().info(ANSI_PURPLE + "    Author(s): " + ANSI_PURPLE + getDescription().getAuthors().get(0) + ANSI_RESET);
        getLogger().info(ANSI_AQUA + "    (c) Cold Development ❄" + ANSI_RESET);
        getLogger().info("");

        File configFile = new File(getDataFolder(), "en_US.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        saveDefaultConfig();
    }

    @Override
    public void disable() {
        getLogger().info("ChatReport unloaded.");
    }

    @Override
    public void reload() {
        super.reload();
        webhookClient = new WebhookClient(getConfig()); // Reinitializează WebhookClient cu configurările noi
    }

    @Override
    protected List<Class<? extends Manager>> getManagerLoadPriority() {
        return List.of(
                CommandManager.class
        );
    }

    @Override
    protected List<ColdSetting<?>> getColdConfigSettings() {
        return SettingKey.getKeys();
    }

    @Override
    protected String[] getColdConfigHeader() {
        return new String[] {
                "  ____  ___   _      ____   ",
                " / ___|/ _ \\ | |    |  _ \\  ",
                "| |   | | | || |    | | | | ",
                "| |___| |_| || |___ | |_| | ",
                " \\____|\\___/ |_____|_____/  ",
                "                           "
        };
    }

    public static ChatReport getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ChatReport instance is not initialized!");
        }
        return instance;
    }

    public WebhookClient getWebhookClient() {
        return webhookClient;
    }
}
