package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class BlockPlaceListener implements Listener {
    private final SquidGame plugin;

    public BlockPlaceListener(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent e) {
        final Player bukkitPlayer = e.getPlayer();
        final SquidPlayer squidPlayer = (SquidPlayer) this.plugin.getPlayerManager().getPlayer(bukkitPlayer);
        final Arena arena = squidPlayer.getArena();

        if (arena != null) {
            e.setCancelled(true);
        }
    }
}