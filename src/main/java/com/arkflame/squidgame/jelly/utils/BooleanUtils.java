package com.arkflame.squidgame.jelly.utils;

public class BooleanUtils {
    public static boolean randomBoolean() {
        return NumberUtils.randomNumber(0, 1) == 0;
    }
}
