package dev._2lstudios.jelly.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class InventoryManager {
    private static Map<Player, InventoryGUI> inventories = new HashMap<>();

    public static void openInventory(Player player, InventoryGUI gui) {
        inventories.put(player, gui);
    }

    public static void closeInventory(Player player) {
        inventories.remove(player);
    }

    public static InventoryGUI getOpenInventory(Player player) {
        return inventories.get(player);
    }

}
