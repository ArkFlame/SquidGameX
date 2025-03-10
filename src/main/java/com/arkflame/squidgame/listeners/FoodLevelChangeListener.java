package com.arkflame.squidgame.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.player.SquidPlayer;

public class FoodLevelChangeListener implements Listener {

    private SquidGame plugin;

    public FoodLevelChangeListener(SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        HumanEntity entity = e.getEntity();
        if (entity instanceof Player) {
            SquidPlayer player = this.plugin.getPlayerManager().getPlayer((Player) entity);
            if (player != null && player.getArena() != null) {
                e.setCancelled(true);
            }
        }
    }
}
