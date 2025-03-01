package com.arkflame.squidgame.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public class PlaceholderAPIFormatter {
    private static boolean enabled = false;

    public static void setEnabled(boolean enabled) {
        PlaceholderAPIFormatter.enabled = enabled;
    }

    public static String formatString(String text, Player player) {
        if (enabled) {
            return PlaceholderAPI.setPlaceholders(player, text);
        } else {
            return text;
        }
    }
}
