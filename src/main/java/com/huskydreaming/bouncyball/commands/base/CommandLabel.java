package com.huskydreaming.bouncyball.commands.base;

public enum CommandLabel {
    BOUNCYBALLS,
    CREATE,
    EDIT,
    GIVE,
    RELOAD;

    public static boolean contains(String string) {
        for (CommandLabel c : CommandLabel.values()) {
            if (c.name().equals(string.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}