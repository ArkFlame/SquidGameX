package com.arkflame.squidgame.arena.games;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.math.Cuboid;
import com.arkflame.squidgame.jelly.math.Vector3;
import com.arkflame.squidgame.jelly.utils.NumberUtils;
import com.arkflame.squidgame.player.SquidPlayer;

public class G1RedGreenLightGame extends ArenaGameBase {

    private Cuboid barrier;
    private Cuboid killZone;
    private Cuboid goalZone;

    private boolean canWalk = true;
    private boolean playing = false;

    public G1RedGreenLightGame(Arena arena, int durationTime) {
        super("§aGreen Light §7| §cRed Light", "first", durationTime, arena);
    }

    public Cuboid getBarrier() {
        if (this.barrier == null) {
            this.barrier = this.getArena().getConfig().getCuboid("games.first.barrier");
        }

        return this.barrier;
    }

    public Cuboid getKillZone() {
        if (this.killZone == null) {
            this.killZone = this.getArena().getConfig().getCuboid("games.first.killzone");
        }

        return this.killZone;
    }

    public Cuboid getGoalZone() {
        if (this.goalZone == null) {
            this.goalZone = this.getArena().getConfig().getCuboid("games.first.goal");
        }

        return this.goalZone;
    }

    private void singDoll() {
        if (!this.playing) {
            return;
        }

        int time = NumberUtils.randomNumber(2, 5);
        this.getArena().broadcastTitle("games.first.green-light.title", "games.first.green-light.subtitle");
        this.getArena().broadcastSound(
                this.getArena().getMainConfig().getSound("game-settings.sounds.green-light", "GHAST_MOAN"));
        this.canWalk = true;

        Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
            this.getArena().broadcastTitle("games.first.red-light.title", "games.first.red-light.subtitle");
            this.getArena().broadcastSound(
                    this.getArena().getMainConfig().getSound("game-settings.sounds.red-light", "BLAZE_HIT"));
            Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
                this.canWalk = false;
                int waitTime = NumberUtils.randomNumber(2, 5);
                Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
                    singDoll();
                }, waitTime * 20);
            }, 20);
        }, time * 20);
    }

    @Override
    public void onStart() {
        this.playing = true;
        this.singDoll();
        this.getArena().setPvPAllowed(true);
    }

    @Override
    public void onStop() {
        this.playing = false;
    }

    @Override
    public void onTimeUp() {
        this.getArena().setPvPAllowed(false);
        this.canWalk = false;
        this.playing = false;

        this.getArena().broadcastTitle("events.game-timeout.title", "events.game-timeout.subtitle");

        List<SquidPlayer> death = new ArrayList<>();
        List<SquidPlayer> alive = new ArrayList<>();

        for (SquidPlayer squidPlayer : this.getArena().getPlayers()) {
            Player player = squidPlayer.getBukkitPlayer();
            Location location = player.getLocation();
            Vector3 position = new Vector3(location.getX(), location.getY(), location.getZ());

            if (this.getGoalZone().isBetween(position)) {
                alive.add(squidPlayer);
            } else {
                death.add(squidPlayer);
            }
        }

        Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
            for (SquidPlayer player : death) {
                player.sendTitleI18n("events.game-timeout-died.title", "events.game-timeout-died.subtitle", 3);
                player.playSound(
                        this.getArena().getMainConfig().getSound("game-settings.sounds.player-loss-game", "CAT_HIT"));
            }

            for (SquidPlayer player : alive) {
                player.sendTitleI18n("events.game-pass.title", "events.game-pass.subtitle", 3);
                player.playSound(
                        this.getArena().getMainConfig().getSound("game-settings.sounds.player-pass-game", "LEVELUP"));
            }
        }, 40L);

        Bukkit.getScheduler().runTaskLater(SquidGame.getInstance(), () -> {
            for (SquidPlayer squidPlayer : death) {
                this.getArena().killPlayer(squidPlayer);
            }
        }, 80L);
    }

    public boolean isCanWalk() {
        return this.canWalk;
    }
}
