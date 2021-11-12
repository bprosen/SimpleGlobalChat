package me.xxben.simpleglobalchat.listeners;

import me.xxben.simpleglobalchat.SimpleGlobalChat;
import me.xxben.simpleglobalchat.managers.GlobalChatManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        GlobalChatManager chatManager = SimpleGlobalChat.getGlobalChatManager();

        // if not disabled, cancel chat and send message
        if (!chatManager.isGlobalChatDisabled(player.getName())) {
            event.setCancelled(true);
            chatManager.sendMessage(player, event.getMessage());
        }
    }
}
