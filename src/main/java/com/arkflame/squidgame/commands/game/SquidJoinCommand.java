package com.arkflame.squidgame.commands.game;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.errors.ArenaMisconfiguredException;
import com.arkflame.squidgame.errors.NoAvailableArenaException;
import com.arkflame.squidgame.player.SquidPlayer;

import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandExecutionTarget;
import dev._2lstudios.jelly.commands.CommandListener;

@Command(name = "join", usage = "/squid join", description = "Join to a random game", permission = "squidgame.join", target = CommandExecutionTarget.ONLY_PLAYER)
public class SquidJoinCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws NoAvailableArenaException, ArenaMisconfiguredException {
        final SquidGame plugin = (SquidGame) context.getPlugin();
        final SquidPlayer player = (SquidPlayer) context.getPluginPlayer();

        if (player.getArena() != null) {
            player.sendMessage("arena.already-in-game");
        } else {
            final Arena arena = plugin.getArenaManager().getFirstAvailableArena();
            arena.addPlayer(player);
        }
    }
}
