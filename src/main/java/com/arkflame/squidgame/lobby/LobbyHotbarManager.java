package com.arkflame.squidgame.lobby;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

public class LobbyHotbarManager {
    private Map<String, LobbyHotbar> hotbarNames = new HashMap<>();

    public LobbyHotbar getHotbar(String name) {
        return hotbarNames.get(name);
    }

    public void createHotbar(String name, LobbyHotbar hotbar) {
        hotbarNames.put(name, hotbar);
    }

    public void removeHotbar(String name) {
        hotbarNames.remove(name);
    }

    public void loadHotbars(Configuration config) {
        if (config != null) {
            ConfigurationSection hotbarsSection = config.getConfigurationSection("hotbars");
            if (hotbarsSection != null) {
                for (String hotbarName : hotbarsSection.getKeys(false)) {
                    System.out.println("Loading hotbar: " + hotbarName);
                    ConfigurationSection hotbarSection = hotbarsSection.getConfigurationSection(hotbarName);
                    if (hotbarSection != null) {
                        LobbyHotbar hotbar = new LobbyHotbar();
                        ConfigurationSection itemsSection = hotbarSection.getConfigurationSection("items");
                        if (itemsSection != null) {
                            hotbar.loadItems(itemsSection);
                        }
                        createHotbar(hotbarName, hotbar);
                    }
                }
            }
        }
    }
}
