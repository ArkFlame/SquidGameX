package com.arkflame.squidgame.jelly.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.arkflame.squidgame.jelly.JellyPlugin;

public class PlayerQuitListener implements Listener {
    private JellyPlugin plugin;

    public PlayerQuitListener(JellyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        if (this.plugin.getPluginPlayerManager() != null) {
            this.plugin.getPluginPlayerManager().removePlayer(e.getPlayer());
        }
    }
}
