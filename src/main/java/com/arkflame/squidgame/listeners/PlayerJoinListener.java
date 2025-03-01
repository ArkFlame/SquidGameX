package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.hooks.ScoreboardHook;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.player.SquidPlayer;
import com.arkflame.squidgame.utils.Sounds;

public class PlayerJoinListener implements Listener {
    private SquidGame plugin;
    private ScoreboardHook scoreboardHook;

    public PlayerJoinListener(SquidGame plugin, ScoreboardHook scoreboardHook) {
        this.plugin = plugin;
        this.scoreboardHook = scoreboardHook;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        SquidPlayer squidPlayer = this.plugin.getPlayerManager().getPlayer(player);
        Configuration scoreboardConfig = this.plugin.getScoreboardConfig();

        scoreboardHook.request(squidPlayer, scoreboardConfig.getStringList("lobby"));

        Configuration config = this.plugin.getMainConfig();
        if (config.getBoolean("game-settings.send-player-to-lobby-on-join", true)) {
            if (config.getString("lobby.world", "").isEmpty()) {
                squidPlayer.sendMessage(
                        "&6&lWarning: &eWe have tried to send you to the lobby but it is not defined. Use &c/squid setlobby &eto do this or disable the \"send-player-to-lobby-on-join\" option in the config.yml file.");

            } else {
                squidPlayer.teleportToLobby();
            }
        }

        if (player.hasPermission("squidgame.admin")) {
            this.plugin.runTaskLater(() -> {
                squidPlayer.sendRawMessage(
                        "&6&lSquidGame &7&l>> &eYou are running version &6" + this.plugin.getDescription().getVersion()
                                + "&e. &7&o(https://www.spigotmc.org/resources/squidgame.100000/)");
                player.playSound(player.getLocation(), Sounds.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0F, 1.0F);
            }, 20L);
            this.plugin.runTaskLater(() -> {
                squidPlayer.sendRawMessage(
                        "&6&lSquidGame &7&l>> &eThis plugin is still in development. Please report any bugs to the developer.");
                player.playSound(player.getLocation(), Sounds.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0F, 1.0F);
            }, 40L);
            boolean isLobbySetup = config.get("lobby") != null;
            boolean hasArenas = !this.plugin.getArenaManager().getArenas().isEmpty();
            this.plugin.runTaskLater(() -> {
                squidPlayer.sendRawMessage(
                        "&6&lSquidGame &7&l>> &eThe lobby is " + (isLobbySetup ? "set" : "not set")
                                + ". &7&o(Use &c/squid setlobby &7&oto set the lobby)");
                player.playSound(player.getLocation(), Sounds.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0F, 1.0F);
            }, 60L);
            this.plugin.runTaskLater(() -> {
                squidPlayer.sendRawMessage(
                        "&6&lSquidGame &7&l>> &eThere are " + (hasArenas ? "arenas" : "no arenas")
                                + " configured. &7&o(Use &c/squid createarena [name] &7&oto create an arena)");
                player.playSound(player.getLocation(), Sounds.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0F, 1.0F);
            }, 70L);
            this.plugin.runTaskLater(() -> {
                squidPlayer.sendRawMessage(
                        "&6&lSquidGame &7&l>> &eYou can also use &c/squid help &7&oto get help with the plugin.");
                player.playSound(player.getLocation(), Sounds.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0F, 1.0F);
            }, 80L);
        }
    }
}