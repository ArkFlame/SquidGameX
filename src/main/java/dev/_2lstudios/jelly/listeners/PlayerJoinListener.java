package dev._2lstudios.jelly.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import dev._2lstudios.jelly.JellyPlugin;

public class PlayerJoinListener implements Listener {
    private JellyPlugin plugin;

    public PlayerJoinListener(JellyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (this.plugin.getPluginPlayerManager() != null) {
            this.plugin.getPluginPlayerManager().addPlayer(e.getPlayer());
        }
    }
}
