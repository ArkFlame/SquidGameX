package com.arkflame.squidgame.hooks;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    private SquidGame plugin;
    private static boolean enabled = false;

    public PlaceholderAPIHook(SquidGame plugin) {
        this.plugin = plugin;
        PlaceholderAPIHook.enabled = plugin.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public String getPlugin() {
        return plugin.getDescription().getName();
    }

    public String getIdentifier() {
        return this.getPlugin().toLowerCase();
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    /* Static Formatter */
    public static String formatString(String text, Player player) {
        if (enabled) {
            return PlaceholderAPI.setPlaceholders(player, text);
        } else {
            return text;
        }
    }

    /* Formatters */
    private String requestPlayerPlaceholder(SquidPlayer player, String identifier) {
        switch (identifier) {
            case "wins":
                return "0";
            case "deaths":
                return "0";
            default:
                return null;
        }
    }

    private String requestArenaPlaceholder(Arena arena, String identifier) {
        switch (identifier) {
            case "death":
                return arena.getDeathPlayer() != null ? arena.getDeathPlayer() : "None";
            case "joined":
                return arena.getJoinedPlayer() != null ? arena.getJoinedPlayer() : "None";
            case "leaved":
                return arena.getLeavedPlayer() != null ? arena.getLeavedPlayer() : "None";
            case "players":
                return arena.getPlayers().size() + "";
            case "winner":
                SquidPlayer winner = arena.calculateWinner();
                String name = winner != null ? winner.getBukkitPlayer().getName() : "None";
                return name;
            case "maxplayers":
                return arena.getMaxPlayers() + "";
            case "required":
                return arena.getMinPlayers() + "";
            case "time":
                return arena.getInternalTime() + "";
            case "spectators":
                return arena.getSpectators().size() + "";
            case "game":
                return arena.getCurrentGame() == null ? "None" : arena.getCurrentGame().getName();
            case "name":
                return arena.getName();
            default:
                return null;
        }
    }

    /* Handler */
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null && (identifier.startsWith("player_") || identifier.startsWith("arena_"))) {
            return "";
        }

        SquidPlayer squidPlayer = this.plugin.getPlayerManager().getPlayer(player);

        if (identifier.startsWith("player_")) {
            return this.requestPlayerPlaceholder(squidPlayer, identifier.split("_")[1]);
        }

        else if (identifier.startsWith("arena_")) {
            Arena arena = squidPlayer != null ? squidPlayer.getArena() : null;
            if (arena == null) {
                return "";
            }
            return this.requestArenaPlaceholder(arena, identifier.split("_")[1]);
        }

        return null;
    }
}