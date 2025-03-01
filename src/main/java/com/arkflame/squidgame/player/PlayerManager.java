package com.arkflame.squidgame.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;

import dev._2lstudios.jelly.player.IPluginPlayerManager;

public class PlayerManager implements IPluginPlayerManager {

    private Map<Player, SquidPlayer> players;
    private SquidGame plugin;

    public PlayerManager(SquidGame plugin) {
        this.players = new HashMap<>();
        this.plugin = plugin;
    }

    @Override
    public SquidPlayer addPlayer(Player player) {
        SquidPlayer pluginPlayer = new SquidPlayer(this.plugin, player);
        this.players.put(player, pluginPlayer);
        return pluginPlayer;
    }

    @Override
    public SquidPlayer removePlayer(Player player) {
        return this.players.remove(player);
    }

    @Override
    public SquidPlayer getPlayer(Player player) {
        return this.players.get(player);
    }

    @Override
    public void clear() {
        this.players.clear();
    }

}
