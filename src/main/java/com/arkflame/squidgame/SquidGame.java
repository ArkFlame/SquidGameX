package com.arkflame.squidgame;

import java.util.logging.Level;

import org.bukkit.plugin.PluginManager;

import com.arkflame.squidgame.arena.ArenaManager;
import com.arkflame.squidgame.commands.SquidGameCommand;
import com.arkflame.squidgame.hooks.PlaceholderAPIFormatter;
import com.arkflame.squidgame.hooks.PlaceholderAPIHook;
import com.arkflame.squidgame.hooks.ScoreboardHook;
import com.arkflame.squidgame.jelly.JellyPlugin;
import com.arkflame.squidgame.jelly.config.Configuration;
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
import com.arkflame.squidgame.lobby.LobbyHotbarManager;
import com.arkflame.squidgame.lobby.PlayerHotbarManager;
import com.arkflame.squidgame.player.PlayerManager;
import com.arkflame.squidgame.tasks.ArenaTickTask;

public class SquidGame extends JellyPlugin {

    private ScoreboardHook scoreboardHook;
    private ArenaManager arenaManger;
    private PlayerManager playerManager;

    private LobbyHotbarManager lobbyHotbarManager;
    private PlayerHotbarManager playerHotbarManager;

    private boolean usePAPI;

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();

        // Save current plugin instance as static instance
        SquidGame.instance = this;

        // Instantiate hooks
        scoreboardHook = new ScoreboardHook(pluginManager);

        if (pluginManager.isPluginEnabled("PlaceholderAPI")) {
            new PlaceholderAPIHook(this).register();
            PlaceholderAPIFormatter.setEnabled(true);
            usePAPI = true;
        }

        // Instantiate managers
        arenaManger = new ArenaManager(this);
        playerManager = new PlayerManager(this);

        // Instantiate hotbars
        lobbyHotbarManager = new LobbyHotbarManager();
        playerHotbarManager = new PlayerHotbarManager(lobbyHotbarManager);

        // Load hotbars
        lobbyHotbarManager.loadHotbars(getConfig());

        // Instantiate hooks
        ScoreboardHook scoreboardHook = new ScoreboardHook(pluginManager);

        // Register commands
        addCommand(new SquidGameCommand());

        // Register listeners
        addEventListener(new AsyncPlayerChatListener(this));
        addEventListener(new BlockBreakListener(this));
        addEventListener(new BlockPlaceListener(this));
        addEventListener(new EntityDamageListener(this));
        addEventListener(new FoodLevelChangeListener(this));
        addEventListener(new PlayerDeathListener(this));
        addEventListener(new PlayerInteractListener(this));
        addEventListener(new PlayerJoinListener(this, scoreboardHook));
        addEventListener(new PlayerMoveListener(this));
        addEventListener(new PlayerQuitListener(this));

        // Register listeners for hotbars
        addEventListener(playerHotbarManager);

        // Register player manager
        setPluginPlayerManager(playerManager);

        // Register tasks
        runTaskTimer(new ArenaTickTask(this), 20L, 20L);

        // Enable inventory API
        useInventoryAPI();

        // Generate config files
        getMainConfig();
        getMessagesConfig();
        getScoreboardConfig();

        // Banner
        log(Level.INFO, "§7§m==========================================================");
        log(Level.INFO,
                "                §d§lSquid§f§lGame§r §a(v" + getDescription().getVersion() + ")");
        log(Level.INFO, "§r");
        log(Level.INFO, "§7- §dArena loaded: §7" + arenaManger.getArenas().size());
        log(Level.INFO, "§7- §dPlaceholderAPI Hook: "
                + (usePAPI ? "§aYes" : "§cNo §7(Placeholders option will be disabled)"));
        log(Level.INFO, "§7- §dScoreboard Hook: "
                + (scoreboardHook.canHook() ? "§aYes" : "§cNo §7(The scoreboards option will be disabled)"));
        log(Level.INFO, "§r");
        log(Level.INFO, "§7§m==========================================================");
    }

    /* Configuration */
    public Configuration getMainConfig() {
        return getConfig("config.yml");
    }

    public Configuration getMessagesConfig() {
        return getConfig("messages.yml");
    }

    public Configuration getScoreboardConfig() {
        return getConfig("scoreboard.yml");
    }

    /* Managers */
    public ArenaManager getArenaManager() {
        return arenaManger;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
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