package dev.padrewin.chatreport.commands;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import dev.padrewin.chatreport.ChatReport;
import dev.padrewin.chatreport.manager.CommandManager;
import dev.padrewin.chatreport.manager.LocaleManager;
import dev.padrewin.chatreport.setting.SettingKey;
import dev.padrewin.chatreport.utils.ChatLogger;
import dev.padrewin.chatreport.utils.ReportTracker;
import dev.padrewin.chatreport.utils.WebhookClient;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);

        if (!target.hasPlayedBefore()) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-player-never-joined");
            return;
        }

        //if (target == null || !target.isOnline()) {
        //    plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-player-not-online");
        //    return;
        //}

        UUID targetId = target.getUniqueId();
        int reportTimeout = SettingKey.REPORT_TIMEOUT.get(); // Timeout in seconds

        // Verifică dacă jucătorul este deja raportat
        if (ReportTracker.isReported(targetId)) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "user-already-reported");
            return;
        }

        List<String> messages = ChatLogger.getRecentMessages(targetId, SettingKey.DEFAULT_MESSAGE_COUNT.get());

        if (messages.isEmpty()) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-report-no-messages");
            return;
        }

        // Adaugă raportul în tracker
        ReportTracker.addReport(targetId, reportTimeout);

        WebhookClient webhookClient = plugin.getWebhookClient();
        String reporterName = sender instanceof Player ? sender.getName() : "Console";

        webhookClient.sendReport(reporterName, target, messages);

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
