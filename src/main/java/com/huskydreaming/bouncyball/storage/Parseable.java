package com.huskydreaming.bouncyball.storage;

import org.bukkit.ChatColor;

import java.util.List;

public interface Parseable {

    String parse();

    List<String> parseList();

    default String prefix(Object... objects) {
        String string = parse();
        for (int i = 0; i < objects.length; i++) {
            String parameter = (objects[i] instanceof String stringObject) ? stringObject : String.valueOf(objects[i]);
            if (string != null) string = string.replace("{" + i + "}", parameter);
        }
        return ChatColor.translateAlternateColorCodes('&', Locale.PREFIX.parse() + string);
    }

    default String parameterize(Object... objects) {
        String string = parse();
        if(string == null) return null;
        for (int i = 0; i < objects.length; i++) {
            String parameter = (objects[i] instanceof String stringObject) ? stringObject : String.valueOf(objects[i]);
            string = string.replace("{" + i + "}", parameter);
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}