package com.arkflame.squidgame.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.player.SquidPlayer;

public class ArenaHandler {
    private Arena arena;
    private Configuration mainConfig;

    public ArenaHandler(Arena arena) {
        this.arena = arena;
        this.mainConfig = SquidGame.getInstance().getMainConfig();
    }

    public void handleArenaSwitchState() {
        ArenaState state = this.arena.getState();
        String scoreboardKey = state.toString().toLowerCase();
        this.arena.broadcastScoreboard(scoreboardKey);
    }

    public void handlePlayerJoin(SquidPlayer player) {
        arena.broadcastMessage("arena.join");
        player.sendScoreboard(this.arena.getState().toString().toLowerCase());

        if (arena.getState() == ArenaState.WAITING) {
            if (arena.getPlayers().size() >= arena.getMinPlayers()) {
                arena.setInternalTime(this.mainConfig.getInt("game-settings.starting-time", 30));
                arena.setState(ArenaState.STARTING);
                arena.broadcastMessage("arena.starting");
            }
        }
    }

    public void handlePlayerLeave(SquidPlayer player) {
        if (this.arena.getState() == ArenaState.FINISHING_ARENA) {
            return;
        }

        else if (this.arena.getState() == ArenaState.WAITING || this.arena.getState() == ArenaState.STARTING) {
            arena.broadcastMessage("arena.leave");

            if (arena.getPlayers().size() < arena.getMinPlayers() && arena.getState() == ArenaState.STARTING) {
                arena.setState(ArenaState.WAITING);
                arena.broadcastMessage("arena.no-enough-players");
                arena.setInternalTime(this.mainConfig.getInt("game-settings.starting-time", 30));
            }
        }
    }

    public void handleArenaStart() {
        arena.broadcastMessage("arena.started");
        arena.nextGame();
        arena.broadcastSound(this.mainConfig.getSound("game-settings.sounds.arena-start", "ORB_PICKUP"));
    }

    public void handleArenaTick() {
        if (arena.getState() == ArenaState.STARTING) {
            if (arena.getInternalTime() == 10) {
                arena.broadcastMessage("arena.starting");
                arena.broadcastSound(this.mainConfig.getSound("game-settings.sounds.arena-starting", "CLICK"));
            }

            else if (arena.getInternalTime() <= 5 && arena.getInternalTime() > 0) {
                arena.broadcastMessage("arena.starting");
                arena.broadcastSound(this.mainConfig.getSound("game-settings.sounds.arena-countdown", "NOTE_PLING"));
            }

            else if (arena.getInternalTime() == 0) {
                this.handleArenaStart();
            }
        }

        else if (arena.getState() == ArenaState.EXPLAIN_GAME) {
            if (arena.getInternalTime() == 0) {
                this.arena.setState(ArenaState.IN_GAME);
                this.arena.setInternalTime(this.arena.getCurrentGame().getGameTime());
                this.arena.getCurrentGame().onStart();
            }
        }

        else if (arena.getState() == ArenaState.IN_GAME) {
            if (arena.getInternalTime() == 0) {
                this.arena.setState(ArenaState.FINISHING_GAME);
                this.arena.setInternalTime(this.mainConfig.getInt("game-settings.finishing-time", 5));
                this.arena.getCurrentGame().onTimeUp();
            }
        }

        else if (arena.getState() == ArenaState.FINISHING_GAME) {
            if (arena.getInternalTime() == 0) {
                this.arena.nextGame();
            }
        }

        else if (arena.getState() == ArenaState.INTERMISSION) {
            if (arena.getInternalTime() == 0) {
                arena.setState(ArenaState.EXPLAIN_GAME);
                arena.teleportAllPlayers(arena.getSpawnPosition());
                arena.setInternalTime(15);
                arena.getCurrentGame().onExplainStart();
            }
        }

        else if (arena.getState() == ArenaState.FINISHING_ARENA) {
            SquidPlayer winner = this.arena.calculateWinner();
            if (winner != null && this.mainConfig.getBoolean("game-settings.spawn-fireworks-on-win", true)) {
                winner.spawnFirework(1, 1, Color.RED, true);
            }

            if (arena.getInternalTime() == 0) {
                arena.resetArena();
            }
        }
    }

    public void handleArenaFinish(ArenaFinishReason reason) {
        this.arena.setInternalTime(this.mainConfig.getInt("game-settings.finishing-time", 5));

        switch (reason) {
        case ALL_PLAYERS_DEATH:
            this.arena.broadcastTitle("events.finish.draw.title", "events.finish.draw.subtitle");
            return;
        case ONE_PLAYER_IN_ARENA:
            this.arena.broadcastTitle("events.finish.winner.title", "events.finish.winner.subtitle");

            // Give rewards
            List<String> rewardCommands = this.mainConfig.getStringList("game-settings.rewards",
                    new ArrayList<>());

            for (String reward : rewardCommands) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                        reward.replace("{winner}", arena.calculateWinner().getBukkitPlayer().getName()));
            }

            break;
        case PLUGIN_STOP:
            break;
        }
    }
}
