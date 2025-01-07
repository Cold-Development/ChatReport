package dev.padrewin.chatreport.commands;

import java.util.Collections;
import java.util.List;
import dev.padrewin.chatreport.ChatReport;
import dev.padrewin.chatreport.manager.CommandManager;
import dev.padrewin.chatreport.manager.LocaleManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class VersionCommand extends BaseCommand {

    public VersionCommand() {
        super("version", CommandManager.CommandAliases.VERSION);
    }

    @Override
    public void execute(ChatReport plugin, CommandSender sender, String[] args) {
        sendInfo(plugin, sender);
    }

    @Override
    public List<String> tabComplete(ChatReport plugin, CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    public static void sendInfo(ChatReport plugin, CommandSender sender) {
        LocaleManager localeManager = plugin.getManager(LocaleManager.class);

        String baseColor = localeManager.getLocaleMessage("base-command-color");
        localeManager.sendCustomMessage(sender, baseColor + "");
        localeManager.sendCustomMessage(sender, baseColor + "Running <g:#635AA7:#E6D4F8:#9E48F6>ChatReport" + baseColor + " v" + plugin.getDescription().getVersion());

        List<String> authors = plugin.getDescription().getAuthors();
        String authorsFormatted = String.join(", ", authors);

        localeManager.sendCustomMessage(sender, baseColor + "&7Developers: <g:#FF0000:#793434>" + authorsFormatted);

        if (sender instanceof Player) {
            Player player = (Player) sender;

            TextComponent baseMessage = new TextComponent(baseColor + "GitHub: ");
            TextComponent clickableText = new TextComponent(ChatColor.RED + "" + ChatColor.UNDERLINE + "click here");
            clickableText.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Cold-Development/ChatReport"));
            baseMessage.addExtra(clickableText);

            player.spigot().sendMessage(baseMessage);

        } else if (sender instanceof ConsoleCommandSender) {
            String ansiRed = "\u001B[31m";
            String ansiReset = "\u001B[0m";
            String ansiAqua = "\u001B[36m";

            sender.sendMessage(ansiAqua + "GitHub: " + ansiRed + "https://github.com/Cold-Development/ChatReport" + ansiReset);
        }

        localeManager.sendSimpleMessage(sender, "base-command-help");
        localeManager.sendCustomMessage(sender, baseColor + "");
    }


}