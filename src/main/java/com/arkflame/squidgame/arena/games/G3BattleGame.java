package com.arkflame.squidgame.arena.games;

import org.bukkit.Location;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.player.SquidPlayer;

public class G3BattleGame extends ArenaGameBase {

    private int durationTime;

    public G3BattleGame(Arena arena, int durationTime) {
        super("§cBattle", "third", durationTime, arena);
        this.durationTime = durationTime;
    }

    @Override
    public Location getSpawnPosition() {
        Configuration config = this.getArena().getConfig();
        Location location = config.getLocation("arena.waiting_room", false);
        location.setWorld(this.getArena().getWorld());
        return location;
    }

    @Override
    public void onStart() {
        this.getArena().setPvPAllowed(true);

        if (this.getArena().getMainConfig().getBoolean("game-settings.give-blindness-in-game-3", true)) {
            this.getArena()
                    .broadcastPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.durationTime * 20, 1));
        }
    }

    @Override
    public void onStop() {
        if (this.getArena().getMainConfig().getBoolean("game-settings.give-blindness-in-game-3", true)) {
            this.getArena().broadcastRemovePotionEffect(PotionEffectType.BLINDNESS);
        }
    }

    @Override
    public void onTimeUp() {
        this.getArena().setPvPAllowed(false);

        for (SquidPlayer alivePlayer : this.getArena().getPlayers()) {
            alivePlayer.sendTitleI18n("events.game-pass.title", "events.game-pass.subtitle", 2);
        }
    }
}