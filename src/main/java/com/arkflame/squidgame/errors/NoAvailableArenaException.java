package com.arkflame.squidgame.errors;

import com.arkflame.squidgame.jelly.errors.I18nCommandException;

public class NoAvailableArenaException extends I18nCommandException {
    public NoAvailableArenaException() {
        super("arena.no-available-arena", "No available arena.");
    }
}
