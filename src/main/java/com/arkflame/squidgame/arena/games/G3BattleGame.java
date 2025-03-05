package com.arkflame.squidgame.arena.games;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.player.SquidPlayer;
import com.arkflame.squidgame.utils.Materials;

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
        Arena arena = this.getArena();
        arena.setPvPAllowed(true);

        if (arena.getMainConfig().getBoolean("game-settings.give-blindness-in-game-3", true)) {
            arena.broadcastPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, this.durationTime * 20, 1));
        }

        ConfigurationSection section = arena.getMainConfig().getConfigurationSection("game-settings.game-3-items");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                arena.giveItem(new ItemStack(Materials.get(key + ".material"), section.getInt(key + ".amount")));
            }
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