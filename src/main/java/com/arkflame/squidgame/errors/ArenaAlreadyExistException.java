package com.arkflame.squidgame.errors;

import com.arkflame.squidgame.jelly.errors.I18nCommandException;

public class ArenaAlreadyExistException extends I18nCommandException {
    public ArenaAlreadyExistException(String name) {
        super("setup.arena-already-exist", "Arena " + name + " already exist.");
    }
}
