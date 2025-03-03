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
                squidPlayer.sendMessageI18n(
                        "&d&lWarning: &fWe have tried to send you to the lobby but it is not defined. Use &a/squid setlobby &fto do this or disable the \"send-player-to-lobby-on-join\" option in the config.yml file.");

            } else {
                squidPlayer.teleportToLobby();
            }
        }

        if (player.hasPermission("squidgame.admin")) {
            squidPlayer.sendMessage(
                    "&fYou are running version &d" + this.plugin.getDescription().getVersion()
                    + "&f. &7(https://www.spigotmc.org/resources/squidgame.100000/)", 20L);

            squidPlayer.sendMessage(
                    "&fThis plugin is still in development. Please report any bugs to the developer.", 40L);

            boolean isLobbySetup = config.get("lobby") != null;
            boolean hasArenas = !this.plugin.getArenaManager().getArenas().isEmpty();

            squidPlayer.sendMessage(
                    "&fThe lobby is " + (isLobbySetup ? "&aset" : "&cnot set")
                    + "&f. &7(Use &a/squid setlobby &7to set the lobby)", 60L);

            squidPlayer.sendMessage(
                    "&fThere are " + (hasArenas ? "&aarenas" : "&cno arenas")
                    + "&f configured. &7(Use &a/squid createarena [name] &7to create an arena)", 70L);

            squidPlayer.sendMessage(
                    "&fYou can also use &a/squid help &fto get help with the plugin.", 80L);
        }
    }
}