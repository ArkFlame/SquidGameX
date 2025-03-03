package com.arkflame.squidgame.jelly;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.arkflame.squidgame.jelly.commands.CommandHandler;
import com.arkflame.squidgame.jelly.commands.CommandListener;
import com.arkflame.squidgame.jelly.config.ConfigManager;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.jelly.listeners.InventoryClickListener;
import com.arkflame.squidgame.jelly.listeners.InventoryCloseListener;
import com.arkflame.squidgame.jelly.listeners.PlayerJoinListener;
import com.arkflame.squidgame.jelly.listeners.PlayerQuitListener;
import com.arkflame.squidgame.jelly.player.IPluginPlayerManager;

public class JellyPlugin extends JavaPlugin {

    private CommandHandler commandHandler = new CommandHandler(this);
    private ConfigManager configManager = new ConfigManager(this);
    private IPluginPlayerManager pluginPlayerManager;

    public void useInventoryAPI() {
        this.addEventListener(new InventoryClickListener());
        this.addEventListener(new InventoryCloseListener());
    }

    public void addCommand(CommandListener cmd) {
        this.commandHandler.addCommand(cmd);
    }

    public void addEventListener(Listener listener) {
        this.getServer().getPluginManager().registerEvents(listener, this);
    }

    public Configuration getConfig(String name) {
        return this.configManager.getConfig(name);
    }

    public IPluginPlayerManager getPluginPlayerManager() {
        return this.pluginPlayerManager;
    }

    public void setPluginPlayerManager(IPluginPlayerManager manager) {
        if (this.pluginPlayerManager != null) {
            this.pluginPlayerManager.clear();
        }

        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        this.pluginPlayerManager = manager;

        for (Player player : this.getServer().getOnlinePlayers()) {
            this.pluginPlayerManager.addPlayer(player);
        }
    }

    public void runTaskLater(Runnable runnable, long l) {
        getServer().getScheduler().runTaskLater(this, runnable, l);
    }

    public void runTaskLaterAsynchronously(Runnable runnable, long l) {
        getServer().getScheduler().runTaskLaterAsynchronously(this, runnable, l);
    }

    public void runTask(Runnable runnable) {
        getServer().getScheduler().runTask(this, runnable);
    }

    public void runTaskAsync(Runnable runnable) {
        getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }

    public void runTaskTimer(Runnable runnable, long l, long l1) {
        getServer().getScheduler().runTaskTimer(this, runnable, l, l1);
    }

    public void runTaskTimerAsynchronously(Runnable runnable, long l, long l1) {
        getServer().getScheduler().runTaskTimerAsynchronously(this, runnable, l, l1);
    }

    public void log(Level level, String message) {
        getLogger().log(level, message);
    }
}
