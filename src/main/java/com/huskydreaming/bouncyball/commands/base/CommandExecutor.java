package com.huskydreaming.bouncyball.commands.base;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.registries.CommandRegistry;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.enumeration.Locale;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class CommandExecutor extends org.bukkit.command.Command {

    private final BouncyBallPlugin plugin;
    private final CommandRegistry commandRegistry;
    private final InventoryService inventoryService;
    private final ProjectileService projectileService;

    public CommandExecutor(BouncyBallPlugin plugin) {
        super(CommandLabel.BOUNCYBALLS.name().toLowerCase());

        this.plugin = plugin;

        setAliases(Arrays.asList("bb", "bouncyb", "bball"));

        commandRegistry = plugin.getCommandRegistry();
        this.inventoryService = plugin.provide(InventoryService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (strings.length > 0) {
                CommandInterface commandInterface = commandRegistry.getCommands().stream().filter(c -> {
                    Set<String> aliases = Arrays.stream(c.getAliases()).map(String::toLowerCase).collect(Collectors.toSet());
                    return c.getLabel().name().equalsIgnoreCase(strings[0]) || aliases.contains(strings[0]);
                }).findFirst().orElse(null);

                if(commandInterface != null) {
                    if (!player.hasPermission("bouncyballs." + commandInterface.getLabel().name().toLowerCase())) {
                        player.sendMessage(Locale.NO_PERMISSIONS.parameterize(commandInterface.getLabel().name()));
                        return true;
                    }
                    commandInterface.run(player, strings);
                }
            } else {
                if(projectileService.getProjectileDataMap().isEmpty()) {
                    player.sendMessage(Locale.NO_BOUNCY_BALLS.prefix());
                } else {
                    inventoryService.getBouncyBallsInventory(plugin).open(player);
                }
            }
        } else {
            commandSender.sendMessage("You must be a player to execute this command.");
        }
        return false;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) return commandRegistry.getCommands().stream().filter(c -> !c.isDebug()).map(c -> c.getLabel().name().toLowerCase()).collect(Collectors.toList());

        if(args.length > 1) {
            CommandInterface commandInterface = commandRegistry.getCommands().stream().filter(c -> {
                Set<String> aliases = Arrays.stream(c.getAliases()).map(String::toLowerCase).collect(Collectors.toSet());
                return c.getLabel().name().equalsIgnoreCase(args[0]) || aliases.contains(args[0]);
            }).findFirst().orElse(null);
            if(commandInterface != null) return commandInterface.onTabComplete(args);
        }
        return new ArrayList<>();
    }
}