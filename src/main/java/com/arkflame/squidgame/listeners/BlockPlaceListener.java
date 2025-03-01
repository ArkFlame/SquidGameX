package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class BlockPlaceListener implements Listener {
    private SquidGame plugin;

    public BlockPlaceListener(SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        Player bukkitPlayer = e.getPlayer();
        SquidPlayer squidPlayer = this.plugin.getPlayerManager().getPlayer(bukkitPlayer);
        Arena arena = squidPlayer.getArena();

        if (arena != null) {
            e.setCancelled(true);
        }
    }
}