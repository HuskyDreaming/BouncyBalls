package com.huskydreaming.bouncyball.commands.base;

import org.bukkit.entity.Player;

import java.util.List;

public interface CommandInterface {

    default CommandLabel getLabel() {
        return getClass().getAnnotation(Command.class).label();
    }

    default String[] getAliases() {
        return getClass().getAnnotation(Command.class).aliases();
    }

    default String getArguments() {
        return getClass().getAnnotation(Command.class).arguments();
    }

    default boolean isDebug() {
        return getClass().getAnnotation(Command.class).debug();
    }

    void run(Player player, String[] strings);

    List<String> onTabComplete(String[] strings);
}