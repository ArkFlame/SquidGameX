package com.arkflame.squidgame.jelly.errors;

import com.arkflame.squidgame.jelly.utils.NumberUtils;

public class ArgumentParserException extends Exception {
    public ArgumentParserException(int index, String required) {
        super(NumberUtils.formatNumber(index) + " argument must be a " + required);
    }
}
