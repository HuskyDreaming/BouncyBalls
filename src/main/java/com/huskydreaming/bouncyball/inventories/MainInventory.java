package com.huskydreaming.bouncyball.inventories;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.enumerations.Menu;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.inventories.providers.InventoryPageProvider;
import com.huskydreaming.huskycore.utilities.builders.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainInventory extends InventoryPageProvider<String> {

    private final HuskyPlugin plugin;
    private final InventoryHandler inventoryHandler;
    private final ProjectileHandler projectileHandler;
    private final ProjectileRepository projectileRepository;

    public MainInventory(HuskyPlugin plugin, int rows, String[] keys) {
        super("Bouncy Balls", rows, keys);
        this.plugin = plugin;

        this.inventoryHandler = plugin.provide(InventoryHandler.class);
        this.projectileHandler = plugin.provide(ProjectileHandler.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public ItemStack construct(Player player, int index, String key) {
        ProjectileData projectileData = projectileRepository.getProjectileData(key);
        List<String> lore = Menu.PROJECTILE_LORE.parseList();
        if (lore != null && (player.hasPermission("bouncyballs.edit") || player.isOp())) {
            lore.add(Menu.PROJECTILE_EDIT.parse());
        }

        return ItemBuilder.create()
                .setDisplayName(Menu.PROJECTILE_TITLE.parameterize(key))
                .setLore(lore)
                .setMaterial(projectileData.getMaterial())
                .build();
    }

    @Override
    public void run(InventoryClickEvent event, String key, InventoryContents contents) {
        ItemStack itemStack = projectileHandler.getItemStackFromKey(key);
        if (itemStack != null && event.getWhoClicked() instanceof Player player) {
            if (event.isLeftClick()) {
                player.getInventory().addItem(itemStack);
                player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(key));
                player.closeInventory();
            } else if (event.isRightClick() && (player.hasPermission("bouncyballs.edit") || player.isOp())) {
                inventoryHandler.getEditInventory(plugin, key).open(player);
            }
        }
    }
}