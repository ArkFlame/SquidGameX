package com.arkflame.squidgame.commands.game;

import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.errors.ArenaMisconfiguredException;
import com.arkflame.squidgame.errors.NoAvailableArenaException;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.commands.CommandContext;
import com.arkflame.squidgame.jelly.commands.CommandExecutionTarget;
import com.arkflame.squidgame.jelly.commands.CommandListener;
import com.arkflame.squidgame.player.SquidPlayer;

@Command(name = "leave", usage = "/squid leave", description = "Leave your current game", permission = "squidgame.leave", target = CommandExecutionTarget.ONLY_PLAYER)
public class SquidLeaveCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws NoAvailableArenaException, ArenaMisconfiguredException {
        SquidPlayer player = (SquidPlayer) context.getPluginPlayer();

        Arena arena = player.getArena();
        if (arena != null) {
            arena.removePlayer(player);
        } else {
            player.sendMessage("arena.not-in-game");
        }
    }
}
