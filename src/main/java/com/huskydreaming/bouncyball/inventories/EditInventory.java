package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectilePhysics;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.pareseables.Menu;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.ItemBuilder;
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
    private final InventoryService inventoryService;
    private final ProjectileService projectileService;

    public EditInventory(HuskyPlugin plugin, String key) {
        this.key = key;
        this.plugin = plugin;

        this.inventoryService = plugin.provide(InventoryService.class);
        this.projectileService =  plugin.provide(ProjectileService.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.fillBorders(InventoryItem.border());

        contents.set(0, 0, InventoryItem.back(player, inventoryService.getBouncyBallsInventory(plugin)));
        contents.set(0, 1, deleteItem());

        contents.set(1, 2, editMaterials());
        contents.set(1, 3, editParticles());

        int physicsIndex = 4;
        for (ProjectilePhysics projectilePhysics : ProjectilePhysics.values()) {
            contents.set(1, physicsIndex++, physicsItem(projectilePhysics, contents));
        }

        int settingsIndex = 2;
        for (ProjectileSetting projectileSetting : ProjectileSetting.values()) {
            contents.set(2, settingsIndex++, activeItem(projectileSetting, contents));
        }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem editMaterials() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_MATERIAL_TITLE.parse())
                .setLore(Menu.EDIT_MATERIAL_LORE.parseList())
                .setMaterial(Material.ANVIL)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                inventoryService.getMaterialInventory(player.getWorld(), plugin, key).open(player);
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
                projectileService.removeProjectile(key);

                if(projectileService.getProjectileDataMap().isEmpty()) {
                    player.closeInventory();
                } else {
                    inventoryService.getBouncyBallsInventory(plugin).open(player);
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
                inventoryService.getParticleInventory(plugin, key).open(player);
            }
        });
    }

    private ClickableItem activeItem(ProjectileSetting projectileSetting, InventoryContents contents) {
        String materialEnabled = Menu.GENERAL_ENABLE_MATERIAL.parse();
        String materialDisabled = Menu.GENERAL_DISABLED_MATERIAL.parse();

        String displayNameEnabled = Menu.GENERAL_ENABLE_TITLE.parameterize(projectileSetting.name());
        String displayNameDisabled = Menu.GENERAL_DISABLED_TITLE.parameterize(projectileSetting.name());

        ProjectileData projectileData = projectileService.getDataFromKey(key);
        Set<ProjectileSetting> settings = projectileData.getSettings();

        boolean enabled = settings.contains(projectileSetting);

        String displayName = enabled ? displayNameEnabled : displayNameDisabled;
        Material material = Material.valueOf(enabled ? materialEnabled : materialDisabled);
        String description = enabled ? Menu.GENERAL_ENABLED_DESCRIPTION.parse() : Menu.GENERAL_DISABLED_DESCRIPTION.parse();

        List<String> strings = new ArrayList<>();
        strings.add(Menu.GENERAL_DESCRIPTION.parameterize(projectileSetting.getDescription()));
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
        ProjectileData projectileData = projectileService.getDataFromKey(key);
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