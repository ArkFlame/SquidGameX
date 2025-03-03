package com.arkflame.squidgame.jelly.player;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.arkflame.squidgame.hooks.PlaceholderAPIFormatter;
import com.arkflame.squidgame.jelly.JellyPlugin;
import com.arkflame.squidgame.jelly.utils.ServerUtils;
import com.arkflame.squidgame.utils.ChatColors;
import com.arkflame.squidgame.utils.EntityTypes;
import com.arkflame.squidgame.utils.Sounds;

public class PluginPlayer {
    private JellyPlugin plugin;
    private Player player;

    public PluginPlayer(JellyPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public Player getBukkitPlayer() {
        return this.player;
    }

    public void playSound(Sound sound) {
        if (sound != null) {
            this.player.playSound(this.player.getLocation(), sound, 1, 1);
        }
    }

    public void teleport(Location loc) {
        this.player.teleport(loc);
    }

    public String formatMessage(String message) {
        return ChatColors.color(PlaceholderAPIFormatter.formatString(message, this.player));
    }

    public void sendMessage(String message) {
        this.player.sendMessage(formatMessage(message));
    }

    public void sendMessage(String message, long delay) {
        this.plugin.runTaskLater(() -> {
            sendMessage(message);
            this.player.playSound(this.player.getLocation(), Sounds.get("ITEM_PICKUP", "ENTITY_ITEM_PICKUP"), 1.0F, 1.0F);
        }, delay);
    }

    public void sendTitle(String title, String subtitle, int duration) {
        this.sendTitle(title, subtitle, 2, duration * 20, 2);
    }

    @SuppressWarnings("deprecation")
    public void sendTitle(String title, String subtitle, int fadeInTime, int showTime,
            int fadeOutTime) {
        title = this.formatMessage(title);
        subtitle = this.formatMessage(subtitle);
        if (ServerUtils.isLegacy()) {
            this.player.resetTitle();
            this.player.sendTitle(title, subtitle);
        } else {
            this.player.sendTitle(title, subtitle, fadeInTime, showTime, fadeOutTime);
        }
    }

    public void spawnFirework(int amount, int power, Color color, boolean flicker) {
        Location loc = this.player.getLocation().add(0, 1, 0);
        World world = loc.getWorld();
        Firework firework = (Firework) world.spawnEntity(loc, EntityTypes.get("FIREWORK", "FIREWORK_ROCKET"));
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(power);
        meta.addEffect(FireworkEffect.builder().withColor(color).flicker(flicker).withTrail().build());
        firework.setFireworkMeta(meta);

        for (int i = 1; i < amount; i++) {
            Firework fireworkCopy = (Firework) world.spawnEntity(loc, EntityTypes.get("FIREWORK", "FIREWORK_ROCKET"));
            fireworkCopy.setFireworkMeta(meta);
        }
    }
}
