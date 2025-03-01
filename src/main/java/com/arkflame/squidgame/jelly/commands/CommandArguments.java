package com.arkflame.squidgame.jelly.commands;

import org.bukkit.entity.Player;

public class CommandArguments {

    private Object[] args;

    public CommandArguments(Object[] args) {
        this.args = args;
    }

    /* Generic getters */
    public Object get(int index) {
        if (args.length < index) {
            return null;
        }

        return this.args[index];
    }

    public Object get(int index, Object defaultValue) {
        Object result = this.get(index);
        return result == null ? defaultValue : result;
    }

    /* Primitive java getters */
    public boolean getBoolean(int index, boolean defaultValue) {
        return (boolean) this.get(index, defaultValue);
    }

    public boolean getBoolean(int index) {
        return this.getBoolean(index, false);
    }

    public int getInt(int index, int defaultValue) {
        return (int) this.get(index, defaultValue);
    }

    public int getInt(int index) {
        return this.getInt(index, Integer.MIN_VALUE);
    }

    public String getString(int index) {
        return (String) this.get(index);
    }

    public String getString(int index, String defaultValue) {
        return (String) this.get(index, defaultValue);
    }

    /* Bukkit getters */
    public Player getPlayer(int index) {
        return (Player) this.get(index);
    }

    public Player getPlayer(int index, Player defaultValue) {
        return (Player) this.get(index, defaultValue);
    }
}
