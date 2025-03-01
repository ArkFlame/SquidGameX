package com.arkflame.squidgame.arena.games;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.config.Configuration;

public abstract class ArenaGameBase {

    private String name;
    private String configKey;
    private Arena arena;
    private int gameTime;

    public ArenaGameBase(String name, String configKey, int gameTime, Arena arena) {
        this.name = name;
        this.configKey = configKey;
        this.arena = arena;
        this.gameTime = gameTime;
    }

    public void onExplainStart() {
        String key = "games." + this.configKey + ".tutorial";
        this.broadcastTitleAfterSeconds(3, key + ".1.title", key + ".1.subtitle");
        this.broadcastTitleAfterSeconds(6, key + ".2.title", key + ".2.subtitle");
        this.broadcastTitleAfterSeconds(9, key + ".3.title", key + ".3.subtitle");
        this.broadcastTitleAfterSeconds(12, key + ".4.title", key + ".4.subtitle");
        this.broadcastTitleAfterSeconds(15, "events.game-start.title", "events.game-start.subtitle");
    }

    public void onStart() {
    }

    public void onTimeUp() {
    }

    public void onStop() {

    }

    public Location getSpawnPosition() {
        Configuration config = this.arena.getConfig();
        Location location = config.getLocation("games." + this.configKey + ".spawn", false);
        location.setWorld(this.arena.getWorld());
        return location;
    }

    public void broadcastTitleAfterSeconds(int seconds, String title, String subtitle) {
        Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
            this.arena.broadcastTitle(title, subtitle);
        }, seconds * 20L);
    }

    public void broadcastMessageAfterSeconds(int seconds, String message) {
        Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
            this.arena.broadcastMessage(message);
        }, seconds * 20L);
    }

    public int getGameTime() {
        return this.gameTime;
    }

    public Arena getArena() {
        return this.arena;
    }

    public String getName() {
        return this.name;
    }
}
