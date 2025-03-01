package com.arkflame.squidgame.errors;

import dev._2lstudios.jelly.errors.I18nCommandException;

public class ArenaAlreadyExistException extends I18nCommandException {
    public ArenaAlreadyExistException(String name) {
        super("setup.arena-already-exist", "Arena " + name + " already exist.");
    }
}
