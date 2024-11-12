package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAnnotation(label = "edit")
public class EditCommand implements PlayerCommandProvider {

    private final HuskyPlugin plugin;
    private final ProjectileRepository projectileRepository;
    private final InventoryHandler inventoryHandler;

    public EditCommand(HuskyPlugin plugin) {
        this.plugin = plugin;
        projectileRepository = plugin.provide(ProjectileRepository.class);
        inventoryHandler = plugin.provide(InventoryHandler.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length == 2) {
            String projectileName = strings[1].toLowerCase();
            if (!projectileRepository.hasProjectileData(projectileName)) {
                player.sendMessage(Locale.BOUNCY_BALL_NULL.prefix(projectileName));
                return;
            }

            inventoryHandler.getEditInventory(plugin, projectileName).open(player);
        }
    }

    @Override
    public List<String> onTabComplete(Player player, String[] strings) {
        if (strings.length == 2) return projectileRepository.getProjectileDataMap().keySet().stream().toList();
        return List.of();
    }
}