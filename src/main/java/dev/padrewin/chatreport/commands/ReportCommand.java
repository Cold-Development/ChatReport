package dev.padrewin.chatreport.commands;

import dev.padrewin.chatreport.ChatReport;
import dev.padrewin.chatreport.manager.CommandManager;
import dev.padrewin.chatreport.manager.LocaleManager;
import dev.padrewin.chatreport.setting.SettingKey;
import dev.padrewin.chatreport.utils.ChatLogger;
import dev.padrewin.chatreport.utils.ReportTracker;
import dev.padrewin.chatreport.utils.WebhookClient;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ReportCommand extends BaseCommand {

    public ReportCommand() {
        super("report", CommandManager.CommandAliases.REPORT);
    }

    @Override
    public void execute(ChatReport plugin, CommandSender sender, String[] args) {

        // Permisiune de baza
        if (!sender.hasPermission("chatreport.report")) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "no-permission");
            return;
        }

        boolean isStaff = sender.hasPermission("chatreport.staff");

        // Argumente insuficiente
        if (args.length < 1) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-usage");
            return;
        }

        String targetName = args[0];

        boolean staffOnline = isStaffOnline();

        // DACA ESTE STAFF ONLINE SI SENDER NU ARE BYPASS
        if (staffOnline && !isStaff) {
            if (args.length < 2 || !args[1].equalsIgnoreCase("confirm")) {
                plugin.getManager(LocaleManager.class)
                        .sendMessage(sender, "staff-online-confirm-required");
                return;
            }
        }

        // Verificare mesaje
        if (!ChatLogger.hasMessages(targetName)) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-no-messages");
            return;
        }

        int reportTimeout = SettingKey.REPORT_TIMEOUT.get();

        if (ReportTracker.isReported(targetName)) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "user-already-reported");
            return;
        }

        List<String> messages = ChatLogger.getRecentMessages(targetName, SettingKey.DEFAULT_MESSAGE_COUNT.get());

        ReportTracker.addReport(targetName, reportTimeout);

        WebhookClient webhookClient = plugin.getWebhookClient();
        String reporterName = sender instanceof Player ? sender.getName() : "Console";
        String serverName = plugin.getConfig().getString("server-name", "server");

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->
                webhookClient.sendReport(reporterName, targetName, serverName, messages));

        plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-success");
    }


    private boolean isStaffOnline() {
        return Bukkit.getOnlinePlayers().stream()
                .anyMatch(p -> p.hasPermission("chatreport.staff"));
    }


    @Override
    public List<String> tabComplete(ChatReport plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return this.visiblePlayerNames(sender);
        }
        return Collections.emptyList();
    }

    private List<String> visiblePlayerNames(CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !(sender instanceof Player viewer) || viewer.canSee(player))
                .map(Player::getName)
                .toList();
    }
}
