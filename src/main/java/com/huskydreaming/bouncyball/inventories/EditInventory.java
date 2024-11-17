package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

        contents.set(1, 1, editBlocks());
        contents.set(1, 2, editMaterials());
        contents.set(1, 3, editParticles());
        contents.set(1, 4, editSettings());
        contents.set(1, 5, editPhysics());
        contents.set(1, 7, deleteItem());
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

    private ClickableItem editBlocks() {
        ProjectileData projectileData = projectileRepository.getProjectileData(key);
        Set<ProjectileSetting> settings = projectileData.getSettings();

        boolean allBlocks = settings.contains(ProjectileSetting.ALL_BLOCKS);

        String title = allBlocks ? Menu.EDIT_BLOCK_DISABLED_TITLE.parse() : Menu.EDIT_BLOCK_TITLE.parse();
        List<String> lore = allBlocks ? Menu.EDIT_BLOCK_DISABLED_LORE.parseList() : Menu.EDIT_BLOCK_LORE.parseList();
        Material material = allBlocks ? Material.BEDROCK : Material.GRASS_BLOCK;

        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(title)
                .setLore(lore)
                .setMaterial(material)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player && !allBlocks) {
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

    private ClickableItem editSettings() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_SETTINGS_TITLE.parse())
                .setLore(Menu.EDIT_SETTINGS_LORE.parseList())
                .setMaterial(Material.COMPARATOR)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                inventoryHandler.getSettingsInventory(plugin, key).open(player);
            }
        });
    }

    private ClickableItem editPhysics() {
        ItemStack itemStack = ItemBuilder.create()
                .setDisplayName(Menu.EDIT_PHYSICS_TITLE.parse())
                .setLore(Menu.EDIT_SETTINGS_LORE.parseList())
                .setMaterial(Material.FEATHER)
                .build();

        return ClickableItem.of(itemStack, e -> {
            if (e.getWhoClicked() instanceof Player player) {
                inventoryHandler.getPhysicsInventory(plugin, key).open(player);
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
}