package com.arkflame.squidgame.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.player.PluginPlayer;

public class SquidPlayer extends PluginPlayer {

    private Arena arena = null;
    private PlayerWand wand = null;
    private boolean spectator = false;

    private SquidGame plugin;
    private Player player;

    public SquidPlayer(SquidGame plugin, Player player) {
        super(plugin, player);
        this.plugin = plugin;
        this.player = player;
    }

    public PlayerWand getWand() {
        return this.wand;
    }

    public PlayerWand createWand(PlayerWand wand) {
        this.wand = wand;
        return this.wand;
    }

    public Arena getArena() {
        return this.arena;
    }

    public void setArena(Arena arena) {
        if (arena == null && this.arena != null) {
            this.arena.removePlayer(this);
            this.arena = null;
        } else if (arena != null && this.arena == null) {
            this.arena = arena;
            arena.addPlayer(this);
        }
    }

    public boolean isSpectator() {
        return this.spectator;
    }

    public void setSpectator(boolean result) {
        this.spectator = result;
        if (result) {
            this.player.setGameMode(GameMode.SPECTATOR);
        } else {
            this.player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public void teleportToLobby() {
        this.teleport(this.plugin.getMainConfig().getLocation("lobby"));
    }

    public String getI18n(String key) {
        return this.plugin.getMessagesConfig().getString(key);
    }

    public void sendMessageI18n(String key) {
        sendMessage(getI18n(key));
    }

    public void sendTitleI18n(String titleKey, String subtitleKey, int duration) {
        sendTitle(getI18n(titleKey), getI18n(subtitleKey), duration);
    }

    public void sendScoreboardI18n(String scoreboardKey) {
        this.plugin.getScoreboardHook().request(this.player,
                this.plugin.getScoreboardConfig().getStringList(scoreboardKey));
    }
}
