package com.arkflame.squidgame.commands.game;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.errors.ArenaMisconfiguredException;
import com.arkflame.squidgame.errors.NoAvailableArenaException;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.commands.CommandContext;
import com.arkflame.squidgame.jelly.commands.CommandExecutionTarget;
import com.arkflame.squidgame.jelly.commands.CommandListener;
import com.arkflame.squidgame.player.SquidPlayer;

@Command(name = "start", usage = "/squid start", description = "Force game to start", permission = "squidgame.start", target = CommandExecutionTarget.ONLY_PLAYER)
public class SquidStartCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws NoAvailableArenaException, ArenaMisconfiguredException {
        SquidPlayer player = (SquidPlayer) context.getPluginPlayer();
        Arena arena = player.getArena();

        if (arena == null) {
            player.sendMessage("arena.not-in-game");
        } else {
            arena.forceStart();
        }
    }
}
