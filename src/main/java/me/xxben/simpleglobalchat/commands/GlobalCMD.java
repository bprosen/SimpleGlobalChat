package me.xxben.simpleglobalchat.commands;

import me.xxben.simpleglobalchat.SimpleGlobalChat;
import me.xxben.simpleglobalchat.managers.GlobalChatManager;
import me.xxben.simpleglobalchat.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.Arrays;

public class GlobalCMD extends Command {

    public GlobalCMD(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender sender, String[] a) {
        if (sender instanceof ProxiedPlayer) {

            ProxiedPlayer player = (ProxiedPlayer) sender;
            GlobalChatManager chatManager = SimpleGlobalChat.getGlobalChatManager();
            Configuration config = SimpleGlobalChat.getFileManager().get("lang"); // get lang config

            if (a.length == 1 && a[0].equalsIgnoreCase("toggle")) {
                // if toggled off, remove from cache and file
                if (chatManager.isGlobalChatDisabled(player.getName())) {
                    chatManager.removeToggled(player.getName());
                    player.sendMessage(new TextComponent(Utils.translate(config.getString("toggled-on-chat"))));
                } else {
                    chatManager.addToggled(player.getName());
                    player.sendMessage(new TextComponent(Utils.translate(config.getString("toggled-off-chat"))));
                }
            } else if (a.length == 1 && a[0].equalsIgnoreCase("mute")) {
                // if muted, remove from cache and file
                if (chatManager.isGlobalChatMuted(player.getName())) {
                    chatManager.removeMuted(player.getName());
                    player.sendMessage(new TextComponent(Utils.translate(config.getString("unmuted-chat"))));
                } else {
                    chatManager.addMuted(player.getName());
                    player.sendMessage(new TextComponent(Utils.translate(config.getString("muted-chat"))));
                }
            // /g (message)
            } else if (a.length > 0) {
                String message = String.join(" ", Arrays.copyOfRange(a, 1, a.length)); // join into one string
                SimpleGlobalChat.getGlobalChatManager().sendMessage(player, message);
            }
        }
    }
}
