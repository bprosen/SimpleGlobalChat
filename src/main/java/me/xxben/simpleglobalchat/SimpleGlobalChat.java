package me.xxben.simpleglobalchat;

import me.xxben.simpleglobalchat.commands.GlobalCMD;
import me.xxben.simpleglobalchat.listeners.ChatListener;
import me.xxben.simpleglobalchat.managers.FileManager;
import me.xxben.simpleglobalchat.managers.GlobalChatManager;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.logging.Logger;

public class SimpleGlobalChat extends Plugin {

    private static Plugin plugin;
    private static Logger logger;
    private static GlobalChatManager globalChatManager;
    private static FileManager fileManager;

    @Override
    public void onEnable() {
        plugin = this;
        logger = getLogger();

        // register listener and cmd
        getProxy().getPluginManager().registerListener(this, new ChatListener());
        getProxy().getPluginManager().registerCommand(this, new GlobalCMD("global"));

        loadManagers();
        logger.info("SimpleGlobalChat Enabled");
    }

    @Override
    public void onDisable() {

        // save data to file before nulling
        globalChatManager.saveData();

        unloadManagers();
        logger.info("SimpleGlobalChat Disabled");

        plugin = null;
        logger = null;
    }

    private void loadManagers() {
        globalChatManager = new GlobalChatManager();
        fileManager = new FileManager();
    }

    private void unloadManagers() {
        globalChatManager = null;
        fileManager = null;
    }

    // instance getter
    public static Plugin getInstance() { return plugin; }

    // manager getters
    public static GlobalChatManager getGlobalChatManager() { return globalChatManager; }
    public static FileManager getFileManager() { return fileManager; }
}
