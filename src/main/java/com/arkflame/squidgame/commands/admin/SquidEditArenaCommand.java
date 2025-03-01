package com.arkflame.squidgame.commands.admin;

import java.io.IOException;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;
import com.arkflame.squidgame.gui.EditArenaGUI;
import com.arkflame.squidgame.player.SquidPlayer;

import dev._2lstudios.jelly.annotations.Command;
import dev._2lstudios.jelly.commands.CommandContext;
import dev._2lstudios.jelly.commands.CommandExecutionTarget;
import dev._2lstudios.jelly.commands.CommandListener;
import dev._2lstudios.jelly.errors.CommandException;

@Command(name = "editarena", usage = "/squid editarena [arena]", description = "Edit an arena", permission = "squidgame.admin", target = CommandExecutionTarget.ONLY_PLAYER, arguments = {
        String.class })
public class SquidEditArenaCommand extends CommandListener {
    @Override
    public void handle(CommandContext context) throws IOException, CommandException {
        String arenaName = context.getArguments().getString(0);
        Arena arena = ((SquidGame) context.getPlugin()).getArenaManager().getArena(arenaName);

        if (arena == null) {
            SquidPlayer player = (SquidPlayer) context.getPluginPlayer();
            player.sendMessage("setup.arena-not-exist");
        } else {
            EditArenaGUI gui = new EditArenaGUI(arena);
            gui.open(context.getPlayer());
        }
    }
}
