package com.arkflame.squidgame.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.hooks.ScoreboardHook;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlayerJoinListener implements Listener {
    private SquidGame plugin;
    private ScoreboardHook scoreboardHook;

    public PlayerJoinListener(SquidGame plugin, ScoreboardHook scoreboardHook) {
        this.plugin = plugin;
        this.scoreboardHook = scoreboardHook;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SquidPlayer squidPlayer = this.plugin.getPlayerManager().getPlayer(e.getPlayer());
        Configuration scoreboardConfig = this.plugin.getScoreboardConfig();

        scoreboardHook.request(squidPlayer, scoreboardConfig.getStringList("lobby"));

        if (this.plugin.getMainConfig().getBoolean("game-settings.send-player-to-lobby-on-join", true)) {
            if (this.plugin.getMainConfig().getString("lobby.world", "").isEmpty()) {
                squidPlayer.sendMessage(
                        "&6&lWarning: &eWe have tried to send you to the lobby but it is not defined. Use &c/squid setlobby &eto do this or disable the \"send-player-to-lobby-on-join\" option in the config.yml file.");

            } else {
                squidPlayer.teleportToLobby();
            }
        }
    }
}