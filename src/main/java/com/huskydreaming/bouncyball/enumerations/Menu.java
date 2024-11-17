package com.huskydreaming.bouncyball.enumerations;

import com.google.common.base.Functions;
import com.huskydreaming.huskycore.utilities.general.Parseable;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public enum Menu implements Parseable {

    // Projectile Menu
    PROJECTILE_TITLE("&f<0>"),
    PROJECTILE_LORE(List.of("&7Left-Click to retrieve bouncy ball.")),
    PROJECTILE_EDIT("&7Right-Click to edit"),

    // Edit Menu
    EDIT_AMOUNT_TITLE("&fAmount: &b<0>"),
    EDIT_AMOUNT_LORE(List.of("", "&7Left-Click to increase", "&7Right-Click to decrease")),
    EDIT_BLOCK_TITLE("&eEdit Blocks"),
    EDIT_BLOCK_LORE(List.of("&7Click to edit allowed blocks.")),
    EDIT_BLOCK_DISABLED_TITLE("&cEdit Blocks"),
    EDIT_BLOCK_DISABLED_LORE(List.of("&7Can already bounce on all blocks", "&7Available to change in settings")),
    EDIT_COLOR_TITLE("<0><1>"),
    EDIT_COLOR_LORE(List.of("&7Click to select color")),
    EDIT_DELETE_TITLE("&cDelete"),
    EDIT_DELETE_LORE(List.of("&7Click to delete bouncy ball")),
    EDIT_MATERIAL_TITLE("&eEdit Materials"),
    EDIT_MATERIAL_LORE(List.of("&7Click to edit material.")),
    EDIT_PHYSIC_TITLE("&b<0>"),
    EDIT_PHYSIC_LORE(List.of(
            "&f<0>",
            "",
            "&7Amount: &f<1>",
            "",
            "&7Left-Click to increment",
            "&7Right-Click to decrement")),
    EDIT_PHYSICS_TITLE("&eEdit Physics"),
    EDIT_PHYSICS_LORE(List.of("Click to edit material.")),
    EDIT_PARTICLE_TITLE("&eEdit Particles"),
    EDIT_PARTICLE_LORE(List.of("&7Click to edit particles.")),
    EDIT_SET_BLOCK_TITLE("&e<0>"),
    EDIT_SET_MATERIAL_TITLE("&e<0>"),
    EDIT_SET_PARTICLE_TITLE("&e<0>"),
    EDIT_SET_BLOCK_LORE(List.of("&7Click to set block as bounce-able.")),
    EDIT_SET_MATERIAL_LORE(List.of("&7Click to set material.")),
    EDIT_SET_PARTICLE_LORE(List.of("&7Click to set particle.")),
    EDIT_SETTINGS_TITLE("&eEdit Settings"),
    EDIT_SETTINGS_LORE(List.of("&7Click to edit settings.")),
    EDIT_CURRENT_BLOCK_TITLE("&b<0>"),
    EDIT_CURRENT_MATERIAL_TITLE("&b<0>"),
    EDIT_CURRENT_PARTICLE_TITLE("&b<0>"),
    EDIT_CURRENT_BLOCK_LORE(List.of("&7You can bounce on this block.")),
    EDIT_CURRENT_MATERIAL_LORE(List.of("&7This is the current material.")),
    EDIT_CURRENT_PARTICLE_LORE(List.of("&7This is the current particle."));

    private final String def;
    private final List<String> list;
    private static FileConfiguration menuConfiguration;

    Menu(String def) {
        this.def = def;
        this.list = null;
    }

    Menu(List<String> list) {
        this.list = list;
        this.def = null;
    }

    @Override
    public String prefix(Object... objects) {
        return null;
    }

    public String parse() {
        return menuConfiguration.getString(toString(), def);
    }

    @Nullable
    public List<String> parseList() {
        List<?> objects = menuConfiguration.getList(toString(), list);
        if (objects == null) return null;
        return objects.stream().map(Functions.toStringFunction()).collect(Collectors.toList());
    }

    @NotNull
    public String toString() {
        return name().toLowerCase().replace("_", ".");
    }

    public static void setConfiguration(FileConfiguration configuration) {
        Menu.menuConfiguration = configuration;
    }
}
