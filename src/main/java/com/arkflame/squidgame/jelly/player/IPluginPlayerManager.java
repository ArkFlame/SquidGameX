package com.arkflame.squidgame.jelly.player;

import org.bukkit.entity.Player;

public interface IPluginPlayerManager {
    PluginPlayer addPlayer(Player player);

    PluginPlayer removePlayer(Player player);

    PluginPlayer getPlayer(Player player);

    void clear();
}
