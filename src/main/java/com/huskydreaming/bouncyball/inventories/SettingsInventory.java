package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.InventoryItem;
import com.huskydreaming.huskycore.inventories.providers.InventoryPageProvider;
import com.huskydreaming.huskycore.storage.parseables.DefaultMenu;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingsInventory extends InventoryPageProvider<ProjectileSetting> {

    private final InventoryHandler inventoryHandler;
    private final ProjectileRepository projectileRepository;

    private final HuskyPlugin plugin;
    private final String key;


    public SettingsInventory(HuskyPlugin plugin, String key, int rows, ProjectileSetting[] settings) {
        super("Settings", rows, settings);
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
    public ItemStack construct(Player player, int i, ProjectileSetting setting) {
        String materialEnabled = DefaultMenu.ENABLE_MATERIAL.parse();
        String materialDisabled = DefaultMenu.DISABLED_MATERIAL.parse();

        String displayNameEnabled = DefaultMenu.ENABLE_TITLE.parameterize(setting.name());
        String displayNameDisabled = DefaultMenu.DISABLED_TITLE.parameterize(setting.name());

        ProjectileData projectileData = projectileRepository.getProjectileData(key);
        Set<ProjectileSetting> settings = projectileData.getSettings();

        boolean enabled = settings.contains(setting);

        String displayName = enabled ? displayNameEnabled : displayNameDisabled;
        Material material = Material.valueOf(enabled ? materialEnabled : materialDisabled);
        String description = enabled ? DefaultMenu.DESCRIPTION_ENABLE.parse() : DefaultMenu.DESCRIPTION_DISABLE.parse();

        List<String> strings = new ArrayList<>();
        strings.add(DefaultMenu.DESCRIPTION_DEFAULT.parameterize(setting.getDescription()));
        strings.add("");
        strings.add(description);

        return ItemBuilder.create()
                .setDisplayName(displayName)
                .setLore(strings)
                .setMaterial(material)
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, ProjectileSetting setting, InventoryContents contents) {
        if (event.getWhoClicked() instanceof Player player) {
            ProjectileData projectileData = projectileRepository.getProjectileData(key);
            Set<ProjectileSetting> settings = projectileData.getSettings();

            if (settings.contains(setting)) {
                projectileData.removeSetting(setting);
            } else {
                projectileData.addSetting(setting);
            }
            contents.inventory().open(player);
        }
    }
}