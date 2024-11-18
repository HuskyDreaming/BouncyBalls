package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectilePhysics;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.providers.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;

public class PhysicsInventory extends InventoryPageProvider<ProjectilePhysics> {

    private final InventoryHandler inventoryHandler;
    private final ProjectileRepository projectileRepository;

    private final HuskyPlugin plugin;
    private final String key;

    public PhysicsInventory(HuskyPlugin plugin, String key, int rows, ProjectilePhysics[] physics) {
        super("Physics", rows, physics);
        this.plugin = plugin;
        this.key = key;

        inventoryHandler = plugin.provide(InventoryHandler.class);
        projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryHandler.getEditInventory(plugin, key)));
    }

    @Override
    public ItemStack construct(Player player, int i, ProjectilePhysics physics) {
        ProjectileData projectileData = projectileRepository.getProjectileData(key);
        double amount = projectileData.getPhysics(physics);

        return ItemBuilder.create()
                .setDisplayName(Menu.EDIT_PHYSIC_TITLE.parameterize(physics.name()))
                .setLore(Menu.EDIT_PHYSIC_LORE.parameterizeList(physics.getDescription(), amount))
                .setMaterial(physics.getMaterial())
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, ProjectilePhysics physics, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            ProjectileData projectileData = projectileRepository.getProjectileData(key);
            double increment = physics.getIncrement();
            double amount = projectileData.getPhysics(physics);
            DecimalFormat df = new DecimalFormat("0.00");

            if (event.isLeftClick()) {
                projectileData.setPhysics(physics, Double.parseDouble(df.format(amount + increment)));
                contents.inventory().open(player);
                return;
            }

            if (event.isRightClick()) {
                if (amount <= increment) {
                    // This is a safeguard in case the amount is offset
                    projectileData.setPhysics(physics, 0.0D);
                    contents.inventory().open(player);
                    return;
                }
                projectileData.setPhysics(physics, Double.parseDouble(df.format(amount - increment)));
                contents.inventory().open(player);
            }
        }
    }
}