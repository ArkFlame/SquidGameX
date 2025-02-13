package com.arkflame.squidgame.commands.game;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.errors.ArenaMisconfiguredException;
import com.arkflame.squidgame.errors.NoAvailableArenaException;
import com.arkflame.squidgame.player.SquidPlayer;

import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandExecutionTarget;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "start", usage = "/squid start", description = "Force game to start", permission = "squidgame.start", target = CommandExecutionTarget.ONLY_PLAYER)
public class SquidStartCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws NoAvailableArenaException, ArenaMisconfiguredException {
        final SquidPlayer player = (SquidPlayer) context.getPluginPlayer();
        final Arena arena = player.getArena();

        if (arena == null) {
            player.sendMessage("arena.not-in-game");
        } else {
            arena.forceStart();
        }
    }
}
