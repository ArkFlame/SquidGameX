package com.arkflame.squidgame.tasks;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;

public class ArenaTickTask implements Runnable {

    private final SquidGame plugin;

    public ArenaTickTask(final SquidGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (final Arena arena : this.plugin.getArenaManager().getArenas()) {
            arena.doArenaTick();
        }
    }
}
