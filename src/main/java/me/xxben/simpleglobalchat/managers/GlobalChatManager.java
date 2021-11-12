package me.xxben.simpleglobalchat.managers;

import me.xxben.simpleglobalchat.SimpleGlobalChat;
import me.xxben.simpleglobalchat.utils.Utils;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.HashSet;
import java.util.List;

public class GlobalChatManager {

    // store the toggled players and muted players in cache
    private HashSet<String> toggledPlayers = new HashSet<>();
    private HashSet<String> mutedPlayers = new HashSet<>();

    // add muted
    public void addMuted(String name) { mutedPlayers.add(name); }

    // add toggled
    public void addToggled(String name) {
        toggledPlayers.add(name);
    }

    // remove toggled
    public void removeToggled(String name) {
        toggledPlayers.remove(name);
    }

    // remove muted
    public void removeMuted(String name) {
        mutedPlayers.remove(name);
    }

    // if global chat is disabled
    public boolean isGlobalChatDisabled(String name) {
        return toggledPlayers.contains(name);
    }

    // if global chat is muted
    public boolean isGlobalChatMuted(String name) {
        return mutedPlayers.contains(name);
    }

    // broadcast message to all
    public void sendMessage(ProxiedPlayer sender, String message) {
        // loop through each
        SimpleGlobalChat.getInstance().getProxy().getPlayers().forEach(online -> {
            if (!mutedPlayers.contains(online.getName())) { // if not muted, send

                String msg = Utils.translate("&6&lGLOBAL &d" + sender.getDisplayName() + " " + message);
                online.sendMessage(new TextComponent(msg));
            }
        });
    }

    public void saveData() {
        FileManager fileManager = SimpleGlobalChat.getFileManager();
        Configuration config = fileManager.get("config");

        // get list of toggled and muted players
        List<String> toggled = config.getStringList("toggled-players");
        List<String> muted = config.getStringList("muted-players");

        // add all to lists
        toggled.addAll(toggledPlayers);
        muted.addAll(mutedPlayers);

        // clear lists
        toggledPlayers.clear();
        mutedPlayers.clear();

        // set config data
        config.set("toggled-players", toggled);
        config.set("muted-players", muted);

        fileManager.save(config, "config");
    }
}
