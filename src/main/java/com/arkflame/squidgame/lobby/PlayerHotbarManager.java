package com.arkflame.squidgame.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerHotbarManager implements Listener {
    private LobbyHotbarManager lobbyHotbarManager;
    private Map<UUID, LobbyHotbar> playerHotbars = new HashMap<>();
    private Map<UUID, Long> lastInteract = new HashMap<>();

    public PlayerHotbarManager(LobbyHotbarManager lobbyHotbarManager) {
        this.lobbyHotbarManager = lobbyHotbarManager;
    }

    public void removeHotbar(UUID uuid) {
        playerHotbars.remove(uuid);
    }

    public void removeHotbar(Player player) {
        removeHotbar(player.getUniqueId());
    }

    public LobbyHotbar getHotbar(UUID uuid) {
        return playerHotbars.get(uuid);
    }

    public LobbyHotbar getHotbar(Player player) {
        return getHotbar(player.getUniqueId());
    }

    public void setHotbar(UUID uuid, LobbyHotbar hotbar) {
        playerHotbars.put(uuid, hotbar);
    }

    public void setHotbar(Player player, LobbyHotbar hotbar) {
        LobbyHotbar oldHotbar = getHotbar(player);
        if (oldHotbar != null) {
            oldHotbar.takeItems(player);
        }
        setHotbar(player.getUniqueId(), hotbar);
        hotbar.giveItems(player);
    }

    public boolean onClick(Player player, int slot) {
        LobbyHotbar hotbar = getHotbar(player);
        if (hotbar != null) {
            LobbyItem item = hotbar.getItem(slot);
            if (item != null) {
                item.onClick(player);
                return true;
            }
        }
        return false;
    }

    public boolean isItem(Player player, int slot) {
        LobbyHotbar hotbar = getHotbar(player);
        if (hotbar != null) {
            return hotbar.getItem(slot) != null;
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent e) {
        Action action = e.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = e.getPlayer();
        int slot = player.getInventory().getHeldItemSlot();
        if (isItem(player, slot)) {
            e.setCancelled(true);
            if (lastInteract.containsKey(player.getUniqueId())) {
                long last = lastInteract.get(player.getUniqueId());
                if (System.currentTimeMillis() - last < 500) {
                    return;
                }
            }
            lastInteract.put(player.getUniqueId(), System.currentTimeMillis());
            if (onClick(player, slot)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            if (onClick(player, slot)) {
                e.setCancelled(true);
            }
            int hotbarButton = e.getHotbarButton();
            if (hotbarButton != -1) {
                if (isItem(player, hotbarButton)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            for (int slot : e.getRawSlots()) {
                if (onClick(player, slot)) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent e) {
        LobbyHotbar hotbar = lobbyHotbarManager.getHotbar("lobby");
        setHotbar(e.getPlayer(), hotbar);
    }
}
