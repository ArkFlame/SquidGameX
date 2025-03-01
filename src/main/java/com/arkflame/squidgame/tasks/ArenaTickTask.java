package com.arkflame.squidgame.tasks;

import com.arkflame.squidgame.SquidGame;
import com.arkflame.squidgame.arena.Arena;

public class ArenaTickTask implements Runnable {

    private SquidGame plugin;

    public ArenaTickTask(SquidGame plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Arena arena : this.plugin.getArenaManager().getArenas()) {
            arena.doArenaTick();
        }
    }
}
