package com.arkflame.squidgame.utils;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for retrieving Bukkit EntityTypes across different server versions.
 */
public class EntityTypes {
    private static Map<String, EntityType> CACHE = new HashMap<>();

    private EntityTypes() {}

    /**
     * Attempts to get the first valid EntityType from a list of names.
     * If no valid EntityType is found, returns EntityType.UNKNOWN.
     *
     * @param names One or more String names of the EntityType to retrieve.
     * @return The first found EntityType, or EntityType.UNKNOWN if none are found.
     */
    public static EntityType get(String... names) {
        for (String name : names) {
            name = name.toUpperCase();
            if (CACHE.containsKey(name)) {
                return CACHE.get(name);
            }
            EntityType entityType = EntityType.fromName(name);
            if (entityType != null) {
                CACHE.put(name, entityType);
                return entityType;
            }
        }
        return EntityType.UNKNOWN;
    }

    /**
     * Attempts to get the first valid EntityType from a list of names.
     * If no valid EntityType is found, returns EntityType.UNKNOWN.
     *
     * @param names One or more String names of the EntityType to retrieve.
     * @return The first found EntityType, or EntityType.UNKNOWN if none are found.
     */
    public static EntityType get(List<String> names) {
        return get(names.toArray(new String[0]));
    }
}
