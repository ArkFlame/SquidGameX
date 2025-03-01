package com.arkflame.squidgame.jelly.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arkflame.squidgame.jelly.JellyPlugin;
import com.arkflame.squidgame.jelly.player.PluginPlayer;

public class CommandContext {

    private CommandArguments arguments;
    private JellyPlugin plugin;
    private CommandSender sender;

    private boolean isPlayer;

    private PluginPlayer pluginPlayer;

    public CommandContext(JellyPlugin plugin, CommandSender sender, CommandArguments arguments) {
        this.arguments = arguments;
        this.plugin = plugin;
        this.sender = sender;

        this.isPlayer = sender instanceof Player;

        if (plugin.getPluginPlayerManager() != null && this.isExecutedByPlayer()) {
            this.pluginPlayer = plugin.getPluginPlayerManager().getPlayer(this.getPlayer());
        }
    }

    public CommandArguments getArguments() {
        return this.arguments;
    }

    public boolean isExecutedByPlayer() {
        return this.isPlayer;
    }

    public Player getPlayer() {
        if (this.isExecutedByPlayer()) {
            return (Player) this.sender;
        } else {
            return null;
        }
    }

    public PluginPlayer getPluginPlayer() {
        return this.pluginPlayer;
    }

    public JellyPlugin getPlugin() {
        return this.plugin;
    }

    public CommandSender getSender() {
        return this.sender;
    }
}
