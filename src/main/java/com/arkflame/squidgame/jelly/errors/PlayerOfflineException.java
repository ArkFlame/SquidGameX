package com.arkflame.squidgame.jelly.errors;

public class PlayerOfflineException extends Exception {
    public PlayerOfflineException(String player) {
        super("Player " + player + " isn't online.");
    }
}
