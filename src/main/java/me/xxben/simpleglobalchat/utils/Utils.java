package me.xxben.simpleglobalchat.utils;

import net.md_5.bungee.api.ChatColor;

public class Utils {

    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
