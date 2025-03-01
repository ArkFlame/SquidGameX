package com.arkflame.squidgame.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlayerQuitListener implements Listener {
    private SquidGame plugin;

    public PlayerQuitListener(SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        SquidPlayer squidPlayer = this.plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (squidPlayer != null && squidPlayer.getArena() != null) {
            squidPlayer.getArena().removePlayer(squidPlayer);
        }
    }
}