package com.arkflame.squidgame.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.hooks.ScoreboardHook;
import com.arkflame.squidgame.player.SquidPlayer;

import dev._2lstudios.jelly.config.Configuration;

public class PlayerJoinListener implements Listener {
    private final SquidGame plugin;
    private final ScoreboardHook scoreboardHook;

    public PlayerJoinListener(final SquidGame plugin, final ScoreboardHook scoreboardHook) {
        this.plugin = plugin;
        this.scoreboardHook = scoreboardHook;
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final SquidPlayer squidPlayer = (SquidPlayer) this.plugin.getPlayerManager().getPlayer(e.getPlayer());
        final Configuration scoreboardConfig = this.plugin.getScoreboardConfig();

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