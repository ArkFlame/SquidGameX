package com.arkflame.squidgame.lobby;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class LobbyHotbar {
    private Map<Integer, LobbyItem> items = new HashMap<>();

    public void loadItems(ConfigurationSection itemsSection) {
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    System.out.println("Loading hotbar item: " + key);
                    int slot = itemSection.getInt("slot");
                    String name = itemSection.getString("name");
                    boolean enchanted = itemSection.getBoolean("enchanted");
                    String[] lore = itemSection.getStringList("lore").toArray(new String[0]);
                    Material material = Material.getMaterial(itemSection.getString("material"));
                    short damage = (short) itemSection.getInt("damage");
                    LobbyItem item = new LobbyItem(slot, material, damage, name, enchanted, lore);
                    item.setCommands(itemSection.getStringList("commands").toArray(new String[0]));
                    items.put(slot, item);
                    System.out.println("Loaded hotbar item with contents: " + item);
                }
            }
        }
    }
    
    public LobbyItem getItem(int slot) {
        return items.get(slot);
    }

    public void giveItems(Player player) {
        for (LobbyItem item : items.values()) {
            player.getInventory().setItem(item.getSlot(), item.getItem());
        }
    }

    public void takeItems(Player player) {
        for (LobbyItem item : items.values()) {
            player.getInventory().remove(item.getItem());
        }
    }
}
