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
import java.util.UUID;

public class ReportCommand extends BaseCommand {

    public ReportCommand() {
        super("report", CommandManager.CommandAliases.REPORT);
    }

    @Override
    public void execute(ChatReport plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("chatreport.report")) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "no-permission");
            return;
        }

        if (args.length < 1) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-usage");
            return;
        }

        String targetName = args[0];
        if (!ChatLogger.hasMessages(targetName)) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-no-messages");
            return;
        }

        int reportTimeout = SettingKey.REPORT_TIMEOUT.get(); // Timeout in seconds

        // Verifică dacă jucătorul este deja raportat
        if (ReportTracker.isReported(targetName)) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "user-already-reported");
            return;
        }

        List<String> messages = ChatLogger.getRecentMessages(targetName, SettingKey.DEFAULT_MESSAGE_COUNT.get());

        if (messages.isEmpty()) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-no-messages");
            return;
        }

        // Adaugă raportul în tracker
        ReportTracker.addReport(targetName, reportTimeout);

        WebhookClient webhookClient = plugin.getWebhookClient();
        String reporterName = sender instanceof Player ? sender.getName() : "Console";

        webhookClient.sendReport(reporterName, targetName, messages);

        plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-success");
    }



    @Override
    public List<String> tabComplete(ChatReport plugin, CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .toList();
        }
        return Collections.emptyList();
    }
}
