package com.arkflame.squidgame.hooks;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.arkflame.squidgame.player.SquidPlayer;

import dev._2lstudios.swiftboard.SwiftBoard;

public class ScoreboardHook {
    private PluginManager pluginManager;

    public ScoreboardHook(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void request(Player player, List<String> lines) {
        if (pluginManager.isPluginEnabled("SwiftBoard")) {
            SwiftBoard.getSwiftSidebar().setLines(player, lines);
        }
    }

    public void request(SquidPlayer squidPlayer, List<String> lines) {
        request(squidPlayer.getBukkitPlayer(), lines);
    }

    public boolean canHook() {
        return pluginManager.isPluginEnabled("SwiftBoard");
    }
}
