package com.arkflame.squidgame.jelly.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import com.arkflame.squidgame.jelly.gui.InventoryManager;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        InventoryManager.closeInventory((Player) e.getPlayer());
    }
}