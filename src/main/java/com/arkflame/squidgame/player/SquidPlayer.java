package com.arkflame.squidgame.player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.hooks.PlaceholderAPIFormatter;
import com.arkflame.squidgame.jelly.player.PluginPlayer;
import com.arkflame.squidgame.utils.ChatColors;

public class SquidPlayer extends PluginPlayer {

    private Arena arena = null;
    private PlayerWand wand = null;
    private boolean spectator = false;

    private SquidGame plugin;

    public SquidPlayer(SquidGame plugin, Player player) {
        super(player);
        this.plugin = plugin;
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
            this.getBukkitPlayer().setGameMode(GameMode.SPECTATOR);
        } else {
            this.getBukkitPlayer().setGameMode(GameMode.SURVIVAL);
        }
    }

    public void teleportToLobby() {
        this.teleport(this.plugin.getMainConfig().getLocation("lobby"));
    }

    public String getI18n(String key) {
        return this.plugin.getMessagesConfig().getString(key);
    }

    private String formatMessage(String message) {
        String translatedMessage = this.getI18n(message);
        String formatColor = ChatColors.color(
                translatedMessage == null
                        ? "§6§lWARNING: §eMissing translation key §7" + message + " §ein message.yml file"
                        : translatedMessage);
        String replacedVariables = PlaceholderAPIFormatter.formatString(formatColor, this.getBukkitPlayer());
        return replacedVariables;
    }

    public void sendRawMessage(String message) {
        this.getBukkitPlayer().sendMessage(ChatColors.color(PlaceholderAPIFormatter.formatString(message, this.getBukkitPlayer())));
    }

    public void sendMessage(String message) {
        this.getBukkitPlayer().sendMessage(this.formatMessage(message));
    }

    public void sendTitle(String title, String subtitle, int duration) {
        super.sendTitle(this.formatMessage(title), this.formatMessage(subtitle), duration);
    }

    public void sendScoreboard(String scoreboardKey) {
        this.plugin.getScoreboardHook().request(this.getBukkitPlayer(),
                this.plugin.getScoreboardConfig().getStringList(scoreboardKey));
    }
}
