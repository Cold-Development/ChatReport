package dev.padrewin.chatreport.commands;

import dev.padrewin.colddev.utils.StringPlaceholders;
import dev.padrewin.chatreport.ChatReport;
import dev.padrewin.chatreport.manager.CommandManager;
import dev.padrewin.chatreport.manager.LocaleManager;
import dev.padrewin.chatreport.setting.SettingKey;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the commands for the root command.
 *
 * @author padrewin
 */
public class Commander extends CommandHandler {

    public Commander(ChatReport plugin) {
        super(plugin, "chatreport", CommandManager.CommandAliases.ROOT);

        // Register commands.
        this.registerCommand(new HelpCommand(this));
        this.registerCommand(new ReloadCommand());
        this.registerCommand(new VersionCommand());
        this.registerCommand(new ReportCommand());

    }

    @Override
    public void noArgs(CommandSender sender) {
        try {
            String redirect = SettingKey.BASE_COMMAND_REDIRECT.get().trim().toLowerCase();
            BaseCommand command = this.registeredCommands.get(redirect);
            CommandHandler handler = this.registeredHandlers.get(redirect);
            if (command != null) {
                if (!command.hasPermission(sender)) {
                    this.plugin.getManager(LocaleManager.class).sendMessage(sender, "no-permission");
                    return;
                }
                command.execute(this.plugin, sender, new String[0]);
            } else if (handler != null) {
                if (!handler.hasPermission(sender)) {
                    this.plugin.getManager(LocaleManager.class).sendMessage(sender, "no-permission");
                    return;
                }
                handler.noArgs(sender);
            } else {
                VersionCommand.sendInfo(this.plugin, sender);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unknownCommand(CommandSender sender, String[] args) {
        BaseCommand reportCommand = this.registeredCommands.get("report");
        if (reportCommand != null && reportCommand.hasPermission(sender)) {
            reportCommand.execute(this.plugin, sender, args);
            return;
        }

        this.plugin.getManager(LocaleManager.class).sendMessage(sender, "unknown-command", StringPlaceholders.of("input", args[0]));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>(super.onTabComplete(sender, command, alias, args));
            for (String playerName : this.visiblePlayerNames(sender)) {
                if (!completions.contains(playerName)) {
                    completions.add(playerName);
                }
            }
            return completions;
        }

        return super.onTabComplete(sender, command, alias, args);
    }

    private List<String> visiblePlayerNames(CommandSender sender) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> !(sender instanceof Player viewer) || viewer.canSee(player))
                .map(Player::getName)
                .toList();
    }
}
