package com.huskydreaming.bouncyball.inventories.providers;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.ProjectileData;
import com.huskydreaming.bouncyball.inventories.base.InventoryPageProvider;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.Locale;
import com.huskydreaming.bouncyball.storage.Menu;
import com.huskydreaming.bouncyball.utilities.ItemBuilder;
import fr.minuskube.inv.content.InventoryContents;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MainInventory extends InventoryPageProvider<String> {
    private final BouncyBallPlugin plugin;
    private final InventoryService inventoryService;
    private final ProjectileService projectileService;

    public MainInventory(BouncyBallPlugin plugin, int rows, String[] keys) {
        super(rows, keys);
        this.plugin = plugin;

        this.projectileService = plugin.provide(ProjectileService.class);
        this.inventoryService = plugin.provide(InventoryService.class);
    }

    @Override
    public ItemStack construct(Player player, int index, String key) {
        ProjectileData projectileData = projectileService.getDataFromKey(key);
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
        ItemStack itemStack = projectileService.getItemStackFromKey(key);
        if (itemStack != null && event.getWhoClicked() instanceof Player player) {
            if (event.isLeftClick()) {
                player.getInventory().addItem(itemStack);
                player.sendMessage(Locale.BOUNCY_BALL_GIVE.prefix(key));
                player.closeInventory();
            } else if (event.isRightClick() && (player.hasPermission("bouncyballs.edit") || player.isOp())) {
                inventoryService.getEditInventory(plugin, key).open(player);
            }
        }
    }
}