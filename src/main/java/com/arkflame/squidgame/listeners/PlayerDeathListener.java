package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlayerDeathListener implements Listener {

    private SquidGame plugin;

    public PlayerDeathListener(SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player bukkitPlayer = e.getEntity();
        SquidPlayer player = this.plugin.getPlayerManager().getPlayer(bukkitPlayer);

        if (player != null && player.getArena() != null && !player.isSpectator()) {
            Arena arena = player.getArena();
            arena.killPlayer(player);
        }
    }
}
