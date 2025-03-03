package com.arkflame.squidgame.commands.game;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.errors.ArenaMisconfiguredException;
import com.arkflame.squidgame.errors.NoAvailableArenaException;
import com.arkflame.squidgame.jelly.annotations.Command;
import com.arkflame.squidgame.jelly.commands.CommandContext;
import com.arkflame.squidgame.jelly.commands.CommandExecutionTarget;
import com.arkflame.squidgame.jelly.commands.CommandListener;
import com.arkflame.squidgame.player.SquidPlayer;

@Command(name = "join", usage = "/squid join", description = "Join to a random game", permission = "squidgame.join", target = CommandExecutionTarget.ONLY_PLAYER)
public class SquidJoinCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws NoAvailableArenaException, ArenaMisconfiguredException {
        SquidGame plugin = (SquidGame) context.getPlugin();
        SquidPlayer player = (SquidPlayer) context.getPluginPlayer();

        if (player.getArena() != null) {
            player.sendMessageI18n("arena.already-in-game");
        } else {
            Arena arena = plugin.getArenaManager().getFirstAvailableArena();
            arena.addPlayer(player);
        }
    }
}
