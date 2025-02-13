package com.arkflame.squidgame.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.arena.ArenaState;
import com.arkflame.squidgame.player.SquidPlayer;

public class EntityDamageListener implements Listener {

    private final SquidGame plugin;

    public EntityDamageListener(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(final EntityDamageEvent e) {
        final Entity entity = e.getEntity();
        if (entity instanceof Player) {
            final SquidPlayer player = (SquidPlayer) this.plugin.getPlayerManager().getPlayer((Player) entity);
            if (player != null && player.getArena() != null) {
                final Arena arena = player.getArena();

                if (e.getCause() == DamageCause.FALL && arena.getState() != ArenaState.IN_GAME) {
                    e.setCancelled(true);
                }

                if (e.getCause() == DamageCause.ENTITY_ATTACK && !arena.isPvPAllowed()) {
                    e.setCancelled(true);
                }

                if (e.getCause() == DamageCause.ENTITY_EXPLOSION) {
                    e.setCancelled(true);
                }

                if (!e.isCancelled() && player.getBukkitPlayer().getHealth() - e.getDamage() <= 0
                        && !player.isSpectator()) {
                    arena.killPlayer(player);
                    e.setCancelled(true);
                }
            }
        }
    }
}
