package com.huskydreaming.bouncyball.commands;

import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.pareseables.Locale;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.AbstractCommand;
import com.huskydreaming.huskycore.interfaces.Parseable;
import org.bukkit.entity.Player;

public class BaseCommand extends AbstractCommand {

    private final HuskyPlugin plugin;
    private final InventoryService inventoryService;
    private final ProjectileService projectileService;

    public BaseCommand(HuskyPlugin plugin) {
        super("bouncyballs", plugin);

        this.plugin = plugin;
        this.inventoryService = plugin.provide(InventoryService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (projectileService.getProjectileDataMap().isEmpty()) {
            player.sendMessage(Locale.NO_BOUNCY_BALLS.prefix());
        } else {
            inventoryService.getBouncyBallsInventory(plugin).open(player);
        }
    }

    @Override
    public Parseable getPermissionsLocale() {
        return Locale.NO_PERMISSIONS;
    }
}