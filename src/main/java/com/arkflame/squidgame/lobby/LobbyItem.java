package com.arkflame.squidgame.lobby;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyItem {
    private ItemStack item;
    private String[] commands;
    private int slot;

    public LobbyItem(int slot, Material material, short damage, String name, boolean enchanted, String... lore) {
        this.item = new ItemStack(material, 1, damage);
        ItemMeta meta = this.item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (enchanted) {
            meta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
        }
        this.item.setItemMeta(meta);
        this.slot = slot;
    }

    public LobbyItem setCommands(String... commands) {
        this.commands = commands;
        return this;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public void onClick(Player player) {
        if (this.commands != null) {
            for (String command : this.commands) {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command.replace("{player}", player.getName()));
            }
        }
    }

    public String toString() {
        return this.item.toString();
    }

    public int getSlot() {
        return slot;
    }
}
