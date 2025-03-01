package dev._2lstudios.jelly.player;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.arkflame.squidgame.utils.EntityTypes;

import dev._2lstudios.jelly.utils.ServerUtils;

public class PluginPlayer {
    private Player player;

    public PluginPlayer(Player player) {
        this.player = player;
    }

    public Player getBukkitPlayer() {
        return this.player;
    }

    public void playSound(Sound sound) {
        if (sound != null) {
            this.getBukkitPlayer().playSound(this.getBukkitPlayer().getLocation(), sound, 1, 1);
        }
    }

    public void teleport(Location loc) {
        this.getBukkitPlayer().teleport(loc);
    }

    public void sendTitle(String title, String subtitle, int duration) {
        this.sendTitle(title, subtitle, 2, duration * 20, 2);
    }

    public void spawnFirework(int amount, int power, Color color, boolean flicker) {
        Location loc = this.getBukkitPlayer().getLocation().clone().add(0, 1, 0);

        Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityTypes.get("FIREWORK", "FIREWORK_ROCKET"));
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(power);
        meta.addEffect(FireworkEffect.builder().withColor(color).flicker(flicker).withTrail().build());
        firework.setFireworkMeta(meta);

        for (int i = 1; i < amount; i++) {
            Firework fireworkCopy = (Firework) loc.getWorld().spawnEntity(loc, EntityTypes.get("FIREWORK", "FIREWORK_ROCKET"));
            fireworkCopy.setFireworkMeta(meta);
        }
    }

    @SuppressWarnings("deprecation")
    public void sendTitle(String title, String subtitle, int fadeInTime, int showTime,
            int fadeOutTime) {
        if (ServerUtils.isLegacy()) {
            this.getBukkitPlayer().resetTitle();
            this.getBukkitPlayer().sendTitle(title, subtitle);
        } else {
            this.getBukkitPlayer().sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
        }
    }

}
