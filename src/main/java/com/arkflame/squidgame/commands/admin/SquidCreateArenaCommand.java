package com.arkflame.squidgame.commands.admin;

import java.io.IOException;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.commands.CommandContext;
import com.arkflame.squidgame.jelly.commands.CommandExecutionTarget;
import com.arkflame.squidgame.jelly.commands.CommandListener;
import com.arkflame.squidgame.jelly.errors.CommandException;
import com.arkflame.squidgame.jelly.errors.I18nCommandException;
import com.arkflame.squidgame.player.SquidPlayer;

@Command(name = "createarena", usage = "/squid createarena [name]", description = "Create a new arena in your current world", permission = "squidgame.admin", target = CommandExecutionTarget.ONLY_PLAYER, arguments = {
        String.class })
public class SquidCreateArenaCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws IOException, CommandException, I18nCommandException {
        SquidGame plugin = (SquidGame) context.getPlugin();
        SquidPlayer player = (SquidPlayer) context.getPluginPlayer();
        String arenaName = context.getArguments().getString(0);
        plugin.getArenaManager().createArena(arenaName, player.getBukkitPlayer().getWorld());
        player.sendMessageI18n("setup.arena-created");
    }
}
