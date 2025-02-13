package com.arkflame.squidgame;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import com.arkflame.squidgame.arena.ArenaManager;
import com.arkflame.squidgame.commands.SquidGameCommand;
import com.arkflame.squidgame.hooks.PlaceholderAPIHook;
import com.arkflame.squidgame.hooks.ScoreboardHook;
import com.arkflame.squidgame.listeners.AsyncPlayerChatListener;
import com.arkflame.squidgame.listeners.BlockBreakListener;
import com.arkflame.squidgame.listeners.BlockPlaceListener;
import com.arkflame.squidgame.listeners.EntityDamageListener;
import com.arkflame.squidgame.listeners.FoodLevelChangeListener;
import com.arkflame.squidgame.listeners.PlayerDeathListener;
import com.arkflame.squidgame.listeners.PlayerInteractListener;
import com.arkflame.squidgame.listeners.PlayerJoinListener;
import com.arkflame.squidgame.listeners.PlayerMoveListener;
import com.arkflame.squidgame.listeners.PlayerQuitListener;
import com.arkflame.squidgame.player.PlayerManager;
import com.arkflame.squidgame.tasks.ArenaTickTask;

import dev._2lstudios.jelly.JellyPlugin;
import dev._2lstudios.jelly.config.Configuration;

public class SquidGame extends JellyPlugin {

    private ScoreboardHook scoreboardHook;
    private ArenaManager arenaManger;
    private PlayerManager playerManager;

    private boolean usePAPI;

    @Override
    public void onEnable() {
        final PluginManager pluginManager = getServer().getPluginManager();

        // Save current plugin instance as static instance
        SquidGame.instance = this;

        // Instantiate hooks
        this.scoreboardHook = new ScoreboardHook(pluginManager);

        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(this).register();
            this.usePAPI = true;
        }

        // Instantiate managers
        this.arenaManger = new ArenaManager(this);
        this.playerManager = new PlayerManager(this);

        final ScoreboardHook scoreboardHook = new ScoreboardHook(pluginManager);

        // Register commands
        this.addCommand(new SquidGameCommand());

        // Register listeners
        this.addEventListener(new AsyncPlayerChatListener(this));
        this.addEventListener(new BlockBreakListener(this));
        this.addEventListener(new BlockPlaceListener(this));
        this.addEventListener(new EntityDamageListener(this));
        this.addEventListener(new FoodLevelChangeListener(this));
        this.addEventListener(new PlayerDeathListener(this));
        this.addEventListener(new PlayerInteractListener(this));
        this.addEventListener(new PlayerJoinListener(this, scoreboardHook));
        this.addEventListener(new PlayerMoveListener(this));
        this.addEventListener(new PlayerQuitListener(this));

        // Register player manager
        this.setPluginPlayerManager(this.playerManager);

        // Register tasks
        Bukkit.getScheduler().runTaskTimer(this, new ArenaTickTask(this), 20L, 20L);

        // Enable inventory API
        this.useInventoryAPI();

        // Generate config files
        this.getMainConfig();
        this.getMessagesConfig();
        this.getScoreboardConfig();

        // Banner
        this.getLogger().log(Level.INFO, "§7§m==========================================================");
        this.getLogger().log(Level.INFO,
                "                §d§lSquid§f§lGame§r §a(v" + this.getDescription().getVersion() + ")");
        this.getLogger().log(Level.INFO, "§r");
        this.getLogger().log(Level.INFO, "§7- §dArena loaded: §7" + this.arenaManger.getArenas().size());
        this.getLogger().log(Level.INFO, "§7- §dPlaceholderAPI Hook: "
                + (this.usePAPI ? "§aYes" : "§cNo §7(Placeholders option will be disabled)"));
        this.getLogger().log(Level.INFO, "§7- §dScoreboard Hook: "
                + (this.scoreboardHook.canHook() ? "§aYes" : "§cNo §7(The scoreboards option will be disabled)"));
        this.getLogger().log(Level.INFO, "§r");
        this.getLogger().log(Level.INFO, "§7§m==========================================================");

    }

    /* Configuration */
    public Configuration getMainConfig() {
        return this.getConfig("config.yml");
    }

    public Configuration getMessagesConfig() {
        return this.getConfig("messages.yml");
    }

    public Configuration getScoreboardConfig() {
        return this.getConfig("scoreboard.yml");
    }

    /* Managers */
    public ArenaManager getArenaManager() {
        return this.arenaManger;
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public ScoreboardHook getScoreboardHook() {
        return scoreboardHook;
    }

    /* Static instance */
    private static SquidGame instance;

    public static SquidGame getInstance() {
        return instance;
    }
}