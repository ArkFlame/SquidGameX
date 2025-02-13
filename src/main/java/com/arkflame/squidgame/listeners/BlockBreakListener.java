package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class BlockBreakListener implements Listener {
    private final SquidGame plugin;

    public BlockBreakListener(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent e) {
        final Player bukkitPlayer = e.getPlayer();
        final SquidPlayer squidPlayer = (SquidPlayer) this.plugin.getPlayerManager().getPlayer(bukkitPlayer);
        final Arena arena = squidPlayer.getArena();

        if (arena != null) {
            e.setCancelled(true);
        }
    }
}