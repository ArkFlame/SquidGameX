package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlayerDeathListener implements Listener {

    private final SquidGame plugin;

    public PlayerDeathListener(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        final Player bukkitPlayer = e.getEntity();
        final SquidPlayer player = (SquidPlayer) this.plugin.getPlayerManager().getPlayer(bukkitPlayer);

        if (player != null && player.getArena() != null && !player.isSpectator()) {
            final Arena arena = player.getArena();
            arena.killPlayer(player);
        }
    }
}
