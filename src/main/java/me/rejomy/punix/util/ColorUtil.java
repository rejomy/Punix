package me.rejomy.punix.util;

import org.bukkit.ChatColor;

public class ColorUtil {

    public static String toColor(String message) {

        return ChatColor.translateAlternateColorCodes('&', message);

    }

}
