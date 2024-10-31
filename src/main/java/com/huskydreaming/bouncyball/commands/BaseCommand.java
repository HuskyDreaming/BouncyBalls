package com.huskydreaming.bouncyball.commands;

import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.abstraction.AbstractCommand;
import com.huskydreaming.huskycore.commands.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.utilities.general.Parseable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAnnotation(label = "bouncyballs")
public class BaseCommand extends AbstractCommand {

    private final HuskyPlugin plugin;
    private final InventoryHandler inventoryHandler;
    private final ProjectileRepository projectileRepository;

    public BaseCommand(HuskyPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        this.inventoryHandler = plugin.provide(InventoryHandler.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] strings) {
        if(commandSender instanceof Player player) {
            if (projectileRepository.getProjectileDataMap().isEmpty()) {
                player.sendMessage(Locale.NO_BOUNCY_BALLS.prefix());
            } else {
                inventoryHandler.getBouncyBallsInventory(plugin).open(player);
            }
        } else {
            commandSender.sendMessage("You must be a player to run this command");
        }
    }

    @Override
    public Parseable getUsage() {
        return Locale.USAGE;
    }

    @Override
    public Parseable getPermission() {
        return Locale.NO_PERMISSIONS;
    }
}