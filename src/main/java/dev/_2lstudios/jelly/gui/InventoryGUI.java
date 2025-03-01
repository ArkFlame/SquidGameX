package dev._2lstudios.jelly.gui;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class InventoryGUI {

    private Map<Integer, Integer> items;
    private Inventory inventory;
    private InventoryGUI prevGui;

    public InventoryGUI(String name, int size, InventoryGUI prevGui) {
        this.items = new HashMap<>();
        this.inventory = Bukkit.createInventory(null, size, name);
        this.prevGui = prevGui;
        this.init();
    }

    public InventoryGUI(String name, int size) {
        this(name, size, null);
    }

    public InventoryGUI(String name) {
        this(name, 9, null);
    }

    public abstract void init();

    public abstract void handle(int id, Player player);

    public int calcPos(int x, int y) {
        return (x - 1) + (9 * (y - 1));
    }

    public int getItemID(int slot) {
        return items.get(slot);
    }

    public void open(Player player) {
        player.openInventory(this.inventory);
        InventoryManager.openInventory(player, this);
    }

    public void addItem(int id, ItemStack item, int slot) {
        this.inventory.setItem(slot, item);
        this.items.put(slot, id);
    }

    public void addItem(int id, ItemStack item, int x, int y) {
        this.addItem(id, item, this.calcPos(x, y));
    }

    public void addItem(int id, ItemStack item) {
        this.addItem(id, item, items.size());
    }

    public ItemStack createItem(String name, Material material, String lore, int amount) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore.split("\n")));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createItem(String name, Material material, String lore) {
        return this.createItem(name, material, lore, 1);
    }

    public ItemStack createItem(String name, Material material) {
        return this.createItem(name, material, "");
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void close(Player player) {
        player.closeInventory();
        InventoryManager.closeInventory(player);
    }

    public void back(Player player) {
        if (this.prevGui != null) {
            this.prevGui.open(player);
        } else {
            this.close(player);
        }
    }
}