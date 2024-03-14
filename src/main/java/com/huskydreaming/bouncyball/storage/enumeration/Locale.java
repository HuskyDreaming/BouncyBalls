package com.huskydreaming.bouncyball.storage.enumeration;
import com.google.common.base.Functions;
import com.huskydreaming.bouncyball.storage.base.Parseable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Locale implements Parseable {
    PREFIX("&aBouncyballs: &7"),
    BOUNCY_BALL_CREATE("You have created the &b{0}&7 bouncy ball."),
    BOUNCY_BALL_EXISTS("The bouncy ball &b{0} &7 already exists."),
    BOUNCY_BALL_NULL("The &b{0}&7 bouncy ball does not seem to exist."),
    BOUNCY_BALL_GIVE("You have been given the &b{0}&7 bouncy ball."),
    BOUNCY_BALL_GIVE_AMOUNT("You have been given &ex{0} {1}&7 bouncy ball(s)."),
    BOUNCY_BALL_SEND("You have given &a{0} &7a {1}&7 bouncy ball."),
    BOUNCY_BALL_SEND_AMOUNT("You have given &a{0} &ex{1} {2}&7 bouncy ball(s)."),
    INVALID_NUMBER("You must provide a valid number"),
    PLAYER_NULL("That player does not seem to exist."),
    PLAYER_OFFLINE("You do not have permissions to run that command."),
    NO_PERMISSIONS("You do not have permissions to run that command."),
    NO_BOUNCY_BALLS("No bouncy balls have been created... type &b/bouncyballs create [name]"),
    RELOAD("You have successfully reloaded the configuration and saved data.");

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

    @NotNull
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Locale.localeConfiguration = configuration;
    }
}