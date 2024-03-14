package com.huskydreaming.bouncyball.storage.base;

import com.huskydreaming.bouncyball.storage.enumeration.Locale;
import com.huskydreaming.bouncyball.utilities.Util;

import java.util.ArrayList;
import java.util.List;

public interface Parseable {

    String parse();

    List<String> parseList();

    default String prefix(Object... objects) {
        return Locale.PREFIX.parse() + parameterize(objects);
    }

    default String parameterize(Object... objects) {
        String string = parse();
        for (int i = 0; i < objects.length; i++) {
            String parameter = (objects[i] instanceof String stringObject) ? stringObject : String.valueOf(objects[i]);
            string = string.replace("{" + i + "}", Util.capitalize(parameter.replace("_", " ")));
        }

        return string;
    }

    default List<String> parameterizeList(Object... objects) {
        List<String> parameterList = new ArrayList<>();
        for (String string : parseList()) {
            for (int i = 0; i < objects.length; i++) {
                string = string.replace("{" + i + "}", String.valueOf(objects[i]));
            }
            parameterList.add(string);
        }
        return parameterList;
    }
}