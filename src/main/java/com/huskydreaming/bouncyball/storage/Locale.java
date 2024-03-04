package com.huskydreaming.bouncyball.storage;
import com.google.common.base.Functions;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Locale implements Parseable {
    PREFIX("&aBouncyballs: &7"),
    BOUNCY_BALL_NULL("The &b{0} bouncy ball does not seem to exist."),
    BOUNCY_BALL_GIVE("You have been given the &b{0}&7 bouncy ball."),
    BOUNCY_BALL_GIVE_AMOUNT("You have been given &ex{0} &b{1}&7 bouncy ball(s)."),
    BOUNCY_BALL_SEND("You have given &a{0} &7a &b{1}&7 bouncy ball."),
    BOUNCY_BALL_SEND_AMOUNT("You have given &a{0} &ex{1} &b{2}&7 bouncy ball(s)."),
    INVALID_NUMBER("You must provide a valid number"),
    PLAYER_NULL("That player does not seem to exist."),
    NO_PERMISSIONS("You do not have permissions to run that command.");

    private final String def;

    private final List<String> list;
    private static FileConfiguration localeConfiguration;

    Locale(String def) {
        this.def = def;
        this.list = null;
    }

    public String parse() {
        String message = localeConfiguration.getString(toString(), def);
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    @Nullable
    public List<String> parseList() {
        List<?> objects = localeConfiguration.getList(toString(), list);
        if (objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Locale.localeConfiguration = configuration;
    }
}