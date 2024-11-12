package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.providers.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class MaterialInventory extends InventoryPageProvider<Material> {

    private final String key;
    private final boolean blocksOnly;

    private final HuskyPlugin plugin;
    private final InventoryHandler inventoryHandler;
    private final ProjectileRepository projectileRepository;

    public MaterialInventory(HuskyPlugin plugin, String key, int rows, Material[] array, boolean blocksOnly) {
        super(blocksOnly ? "Blocks" : "Materials", rows, array);
        this.key = key;
        this.blocksOnly = blocksOnly;

        this.plugin = plugin;
        this.inventoryHandler = plugin.provide(InventoryHandler.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        super.init(player, contents);

        contents.set(0, 0, InventoryItem.back(player, inventoryHandler.getEditInventory(plugin, key)));
    }

    @Override
    public ItemStack construct(Player player, int index, Material material) {
        ProjectileData projectileData = projectileRepository.getProjectileData(key);

        boolean isMaterial;
        if(blocksOnly) {
            isMaterial = projectileData.getBlocks().contains(material);
        } else {
            isMaterial = projectileData.getMaterial() == material;
        }

        Menu title;
        Menu lore;

        if(blocksOnly) {
            title = isMaterial ? Menu.EDIT_CURRENT_BLOCK_TITLE : Menu.EDIT_SET_BLOCK_TITLE;
            lore = isMaterial ? Menu.EDIT_CURRENT_BLOCK_LORE : Menu.EDIT_SET_BLOCK_LORE;
        } else {
            title = isMaterial ? Menu.EDIT_CURRENT_MATERIAL_TITLE : Menu.EDIT_SET_MATERIAL_TITLE;
            lore = isMaterial ? Menu.EDIT_CURRENT_MATERIAL_LORE : Menu.EDIT_SET_MATERIAL_LORE;
        }

        return ItemBuilder.create()
                .setDisplayName(title.parameterize(material))
                .setLore(lore.parseList())
                .setMaterial(material)
                .setEnchanted(isMaterial)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, Material material, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            ProjectileData projectileData = projectileRepository.getProjectileData(key);
            if (projectileData.getMaterial() == material) return;

            int page = contents.pagination().getPage();
            if(blocksOnly) {
                if(projectileData.getBlocks().contains(material)) {
                    projectileData.removeBlock(material);
                } else {
                    projectileData.addBlock(material);
                }

                inventoryHandler.getBlockInventory(player.getWorld(), plugin, key).open(player, page);
            } else {
                projectileData.setMaterial(material);
                inventoryHandler.getMaterialInventory(player.getWorld(), plugin, key).open(player, page);
            }
        }
    }
}