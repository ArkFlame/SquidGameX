package com.arkflame.squidgame.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlayerQuitListener implements Listener {
    private final SquidGame plugin;

    public PlayerQuitListener(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final SquidPlayer squidPlayer = (SquidPlayer) this.plugin.getPlayerManager().getPlayer(e.getPlayer());
        if (squidPlayer != null && squidPlayer.getArena() != null) {
            squidPlayer.getArena().removePlayer(squidPlayer);
        }
    }
}