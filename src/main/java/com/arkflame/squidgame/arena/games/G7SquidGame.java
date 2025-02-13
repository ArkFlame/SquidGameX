package com.arkflame.squidgame.arena.games;

import com.arkflame.squidgame.arena.Arena;

public class G7SquidGame extends ArenaGameBase {
    public G7SquidGame(final Arena arena, final int durationTime) {
        super("§dSquid§fGame", "seventh", durationTime, arena);
    }

    @Override
    public void onStart() {
        this.getArena().setPvPAllowed(true);
    }

    @Override
    public void onTimeUp() {
        this.getArena().killAllPlayers();
    }

    @Override
    public void onStop() {
        this.getArena().setPvPAllowed(false);
    }
}