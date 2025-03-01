package com.arkflame.squidgame.commands.admin;

import java.io.IOException;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.commands.CommandContext;
import com.arkflame.squidgame.jelly.commands.CommandExecutionTarget;
import com.arkflame.squidgame.jelly.commands.CommandListener;
import com.arkflame.squidgame.jelly.config.Configuration;
import com.arkflame.squidgame.jelly.errors.CommandException;
import com.arkflame.squidgame.player.SquidPlayer;

@Command(name = "setlobby", usage = "/squid setlobby", description = "Set lobby at your current location", permission = "squidgame.admin.setlobby", target = CommandExecutionTarget.ONLY_PLAYER)
public class SquidSetLobbyCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws IOException, CommandException {
        SquidGame plugin = (SquidGame) context.getPlugin();
        SquidPlayer player = (SquidPlayer) context.getPluginPlayer();
        Configuration config = plugin.getMainConfig();

        config.setLocation("lobby", player.getBukkitPlayer().getLocation());
        player.sendMessage("setup.set-lobby");
        config.save();
    }
}
