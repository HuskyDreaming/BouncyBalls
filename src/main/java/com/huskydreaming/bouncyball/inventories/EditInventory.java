package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectilePhysics;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EditInventory implements InventoryProvider {

    private final String key;
    private final HuskyPlugin plugin;
    private final InventoryHandler inventoryHandler;
    private final ProjectileRepository projectileRepository;

    public EditInventory(HuskyPlugin plugin, String key) {
        this.key = key;
        this.plugin = plugin;

        this.inventoryHandler = plugin.provide(InventoryHandler.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        contents.set(0, 0, InventoryItem.back(player, inventoryHandler.getBouncyBallsInventory(plugin)));

        contents.set(1, 2, editBlocks());
        contents.set(1, 3, editMaterials());
        contents.set(1, 4, editParticles());
        contents.set(1, 5, deleteItem());

        int index = 2;
        for (ProjectilePhysics projectilePhysics : ProjectilePhysics.values()) {
            contents.set(2, index++, physicsItem(projectilePhysics, contents));
        }

        index = 2;
        for (ProjectileSetting projectileSetting : ProjectileSetting.values()) {
            contents.set(3, index++, activeItem(projectileSetting, contents));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem editBlocks() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_BLOCK_TITLE.parse())
                .setLore(Menu.EDIT_BLOCK_LORE.parseList())
                .setMaterial(Material.GRASS_BLOCK)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                inventoryHandler.getBlockInventory(player.getWorld(), plugin, key).open(player);
            }
        });
    }

    private ClickableItem editMaterials() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_MATERIAL_TITLE.parse())
                .setLore(Menu.EDIT_MATERIAL_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                inventoryHandler.getMaterialInventory(player.getWorld(), plugin, key).open(player);
            }
        });
    }

    private ClickableItem deleteItem() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_DELETE_TITLE.parse())
                .setLore(Menu.EDIT_DELETE_LORE.parseList())
                .setMaterial(Material.TNT_MINECART)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                projectileRepository.removeProjectileData(key);

                if (projectileRepository.getProjectileDataMap().isEmpty()) {
                    player.closeInventory();
                } else {
                    inventoryHandler.getBouncyBallsInventory(plugin).open(player);
                }

            }
        });
    }

    private ClickableItem editParticles() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_PARTICLE_TITLE.parse())
                .setLore(Menu.EDIT_PARTICLE_LORE.parseList())
                .setMaterial(Material.NETHER_STAR)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                inventoryHandler.getParticleInventory(plugin, key).open(player);
            }
        });
    }

    private ClickableItem activeItem(ProjectileSetting projectileSetting, InventoryContents contents) {
        String materialEnabled = DefaultMenu.ENABLE_MATERIAL.parse();
        String materialDisabled = DefaultMenu.DISABLED_MATERIAL.parse();

        String displayNameEnabled = DefaultMenu.ENABLE_TITLE.parameterize(projectileSetting.name());
        String displayNameDisabled = DefaultMenu.DISABLED_TITLE.parameterize(projectileSetting.name());

        ProjectileData projectileData = projectileRepository.getProjectileData(key);
        Set<ProjectileSetting> settings = projectileData.getSettings();

        boolean enabled = settings.contains(projectileSetting);

        String displayName = enabled ? displayNameEnabled : displayNameDisabled;
        Material material = Material.valueOf(enabled ? materialEnabled : materialDisabled);
        String description = enabled ? DefaultMenu.DESCRIPTION_ENABLE.parse() : DefaultMenu.DESCRIPTION_DISABLE.parse();

        List<String> strings = new ArrayList<>();
        strings.add(DefaultMenu.DESCRIPTION_DEFAULT.parameterize(projectileSetting.getDescription()));
        strings.add("");
        strings.add(description);

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(displayName)
                .setLore(strings)
                .setMaterial(material)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                if (settings.contains(projectileSetting)) {
                    projectileData.removeSetting(projectileSetting);
                } else {
                    projectileData.addSetting(projectileSetting);
                }
                contents.inventory().open(player);
            }
        });
    }

    private ClickableItem physicsItem(ProjectilePhysics projectilePhysics, InventoryContents contents) {
        ProjectileData projectileData = projectileRepository.getProjectileData(key);
        double amount = projectileData.getPhysics(projectilePhysics);

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_PHYSICS_TITLE.parameterize(projectilePhysics.name()))
                .setLore(Menu.EDIT_PHYSICS_LORE.parameterizeList(projectilePhysics.getDescription(), amount))
                .setMaterial(projectilePhysics.getMaterial())
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                double increment = projectilePhysics.getIncrement();
                DecimalFormat df = new DecimalFormat("0.00");
                if (e.isLeftClick()) {
                    projectileData.setPhysics(projectilePhysics, Double.parseDouble(df.format(amount + increment)));
                    contents.inventory().open(player);
                    return;
                }
                if (e.isRightClick()) {
                    if (amount <= increment) {
                        // This is a safeguard in case the amount is offset
                        projectileData.setPhysics(projectilePhysics, increment);
                        contents.inventory().open(player);
                        return;
                    }
                    projectileData.setPhysics(projectilePhysics, Double.parseDouble(df.format(amount - increment)));
                    contents.inventory().open(player);
                }
            }
        });
    }
}