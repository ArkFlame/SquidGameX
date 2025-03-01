package com.arkflame.squidgame.errors;

import com.arkflame.squidgame.jelly.errors.I18nCommandException;

public class ArenaMisconfiguredException extends I18nCommandException {
    public ArenaMisconfiguredException() {
        super("arena.misconfigured-arena", "Arena is misconfigured, contact server administrator.");
    }
}
