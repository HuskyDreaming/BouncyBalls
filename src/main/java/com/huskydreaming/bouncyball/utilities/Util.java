package com.huskydreaming.bouncyball.utilities;

import java.util.regex.Pattern;

public class Util {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    public static boolean isNumeric(String string) {
        return pattern.matcher(string).matches();
    }
}