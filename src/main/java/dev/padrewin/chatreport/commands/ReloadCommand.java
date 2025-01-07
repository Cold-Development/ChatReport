package dev.padrewin.chatreport.commands;

import java.util.Collections;
import java.util.List;
import dev.padrewin.chatreport.ChatReport;
import dev.padrewin.chatreport.manager.CommandManager;
import dev.padrewin.chatreport.manager.LocaleManager;
import dev.padrewin.chatreport.utils.ReportTracker;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends BaseCommand {

    public ReloadCommand() {
        super("reload", CommandManager.CommandAliases.RELOAD);
    }

    @Override
    public void execute(ChatReport plugin, CommandSender sender, String[] args) {
        if (!sender.hasPermission("chatreport.reload")) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "no-permission");
            return;
        }

        if (args.length > 0) {
            plugin.getManager(LocaleManager.class).sendMessage(sender, "command-reload-usage");
            return;
        }

        ReportTracker.clearReports();
        plugin.reloadConfig();
        plugin.reload();
        plugin.getManager(LocaleManager.class).sendMessage(sender, "command-reload-success");
    }

    @Override
    public List<String> tabComplete(ChatReport plugin, CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
