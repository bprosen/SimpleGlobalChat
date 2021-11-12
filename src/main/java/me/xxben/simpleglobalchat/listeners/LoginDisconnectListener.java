package me.xxben.simpleglobalchat.listeners;

import me.xxben.simpleglobalchat.SimpleGlobalChat;
import me.xxben.simpleglobalchat.managers.FileManager;
import me.xxben.simpleglobalchat.managers.GlobalChatManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class LoginDisconnectListener implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event) {
        Plugin plugin = SimpleGlobalChat.getInstance();

        // run in async to not overload main thread
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            ProxiedPlayer player = event.getPlayer();
            GlobalChatManager chatManager = SimpleGlobalChat.getGlobalChatManager();
            FileManager fileManager = SimpleGlobalChat.getFileManager();

            Configuration config = fileManager.get("config");

            // get list of toggled and muted players
            List<String> toggled = config.getStringList("toggled-players");
            List<String> muted = config.getStringList("muted-players");

            if (toggled.contains(player.getName())) {
                chatManager.addToggled(player.getName());

                // remove toggled from list and then save file
                toggled.remove(player.getName());
                config.set("toggled-players", toggled);
                fileManager.save(config, "config");
            }

            if (muted.contains(player.getName())) {
                chatManager.addMuted(player.getName());

                // remove muted from list and then save file
                muted.remove(player.getName());
                config.set("muted-players", muted);
                fileManager.save(config, "config");
            }
        });
    }

    @EventHandler
    public void onDisconnect(ServerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        GlobalChatManager chatManager = SimpleGlobalChat.getGlobalChatManager();
        FileManager fileManager = SimpleGlobalChat.getFileManager();
        Configuration config = fileManager.get("config");

        // remove from toggled
        if (chatManager.isGlobalChatDisabled(player.getName())) {
            chatManager.removeToggled(player.getName());

            // get list of toggled
            List<String> toggled = config.getStringList("toggled-players");
            // add to list and save
            toggled.add(player.getName());
            config.set("toggled-players", toggled);
            fileManager.save(config, "config");
        }
        // remove from muted
        if (chatManager.isGlobalChatMuted(player.getName())) {
            chatManager.removeMuted(player.getName());

            // get list of muted
            List<String> muted = config.getStringList("muted-players");
            // add to list and save
            muted.add(player.getName());
            config.set("muted-players", muted);
            fileManager.save(config, "config");
        }
    }
}
