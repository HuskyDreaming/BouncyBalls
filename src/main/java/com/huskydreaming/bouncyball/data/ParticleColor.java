package com.huskydreaming.bouncyball.data;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum ParticleColor {
    RED(ChatColor.RED, Color.RED, Material.RED_DYE),
    ORANGE(ChatColor.GOLD, Color.ORANGE, Material.ORANGE_DYE),
    YELLOW(ChatColor.YELLOW, Color.YELLOW, Material.YELLOW_DYE),
    GREEN(ChatColor.GREEN, Color.LIME, Material.LIME_DYE),
    AQUA(ChatColor.AQUA, Color.AQUA, Material.LIGHT_BLUE_DYE),
    BLUE(ChatColor.BLUE, Color.BLUE, Material.BLUE_DYE),
    PURPLE(ChatColor.LIGHT_PURPLE, Color.PURPLE, Material.PURPLE_DYE),
    WHITE(ChatColor.WHITE, Color.WHITE, Material.WHITE_DYE),
    GRAY(ChatColor.GRAY, Color.GRAY, Material.GRAY_DYE),
    BLACK(ChatColor.BLACK, Color.BLACK, Material.BLACK_DYE);

    private final ChatColor chatColor;
    private final Color color;
    private final Material material;

    ParticleColor(ChatColor chatColor, Color color, Material material) {
        this.chatColor = chatColor;
        this.color = color;
        this.material = material;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Color getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }
}