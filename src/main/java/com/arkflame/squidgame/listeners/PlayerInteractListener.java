package com.arkflame.squidgame.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.player.PlayerWand;
import com.arkflame.squidgame.player.SquidPlayer;
import com.arkflame.squidgame.utils.Materials;

public class PlayerInteractListener implements Listener {

    private final SquidGame plugin;

    public PlayerInteractListener(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final SquidPlayer player = (SquidPlayer) this.plugin.getPlayerManager().getPlayer(e.getPlayer());

        if (player.getWand() != null && e.getItem() != null && e.getItem().getType().equals(Materials.get("BLAZE_ROD"))) {
            final PlayerWand wand = player.getWand();

            if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
                wand.setFirstPoint(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage("§aSet §dfirst §apoint §7(§e" + wand.getFirstPoint().toString() + "§7)");
            } else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                wand.setSecondPoint(e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage("§aSet §bsecond §apoint §7(§e" + wand.getSecondPoint().toString() + "§7)");
            }

            e.setCancelled(true);
        }
    }
}
