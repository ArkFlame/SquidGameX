package com.arkflame.squidgame.commands;

import com.arkflame.squidgame.commands.admin.SquidCreateArenaCommand;
import com.arkflame.squidgame.commands.admin.SquidEditArenaCommand;
import com.arkflame.squidgame.commands.admin.SquidSetLobbyCommand;
import com.arkflame.squidgame.commands.admin.SquidWandCommand;
import com.arkflame.squidgame.commands.game.SquidJoinCommand;
import com.arkflame.squidgame.commands.game.SquidLeaveCommand;
import com.arkflame.squidgame.commands.game.SquidStartCommand;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.commands.CommandContext;
import com.arkflame.squidgame.jelly.commands.CommandListener;

@Command(name = "squidgame")
public class SquidGameCommand extends CommandListener {
    public SquidGameCommand() {
        // Admin commands
        this.addSubcommand(new SquidCreateArenaCommand());
        this.addSubcommand(new SquidSetLobbyCommand());
        this.addSubcommand(new SquidEditArenaCommand());
        this.addSubcommand(new SquidWandCommand());

        // Game commands
        this.addSubcommand(new SquidLeaveCommand());
        this.addSubcommand(new SquidJoinCommand());
        this.addSubcommand(new SquidStartCommand());
    }

    @Override
    public void handle(CommandContext context) {
        context.getSender().sendMessage("\n§d§lSquid§f§lGame§r");
        context.getSender().sendMessage(this.mapCommandListToString("§d/squid {name} §8- §f{description}", "\n"));
    }
}
