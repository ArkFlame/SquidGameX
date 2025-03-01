package com.arkflame.squidgame.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.player.SquidPlayer;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    private final SquidGame plugin;

    public PlaceholderAPIHook(SquidGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        return plugin.getDescription().getName().toLowerCase();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (player == null && (identifier.startsWith("player_") || identifier.startsWith("arena_"))) {
            return "";
        }

        SquidPlayer squidPlayer = this.plugin.getPlayerManager().getPlayer(player);

        if (identifier.startsWith("player_")) {
            return requestPlayerPlaceholder(squidPlayer, identifier.split("_")[1]);
        } else if (identifier.startsWith("arena_")) {
            Arena arena = squidPlayer != null ? squidPlayer.getArena() : null;
            if (arena == null) {
                return "";
            }
            return requestArenaPlaceholder(arena, identifier.split("_")[1]);
        }

        return null;
    }

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
                return String.valueOf(arena.getPlayers().size());
            case "winner":
                SquidPlayer winner = arena.calculateWinner();
                return winner != null ? winner.getBukkitPlayer().getName() : "None";
            case "maxplayers":
                return String.valueOf(arena.getMaxPlayers());
            case "required":
                return String.valueOf(arena.getMinPlayers());
            case "time":
                return String.valueOf(arena.getInternalTime());
            case "spectators":
                return String.valueOf(arena.getSpectators().size());
            case "game":
                return arena.getCurrentGame() == null ? "None" : arena.getCurrentGame().getName();
            case "name":
                return arena.getName();
            default:
                return null;
        }
    }
}
