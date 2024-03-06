package com.huskydreaming.bouncyball.utilities;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Util {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final String regex = "\\s+";

    public static boolean isNumeric(String string) {
        return pattern.matcher(string).matches();
    }

    public static String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }

        return Arrays.stream(input.split(regex))
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}