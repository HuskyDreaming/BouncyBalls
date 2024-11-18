package com.huskydreaming.bouncyball.listeners;

import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.huskycore.HuskyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final HuskyPlugin plugin;
    private final ProjectileHandler projectileHandler;

    public PlayerListener(HuskyPlugin plugin) {
        this.plugin = plugin;
        projectileHandler = plugin.provide(ProjectileHandler.class);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack itemStack = event.getItem();
            if (itemStack == null) return;

            String key = projectileHandler.getKeyFromItemStack(itemStack);
            if (key != null) {
                projectileHandler.launchProjectile(plugin, event.getPlayer(), itemStack, key);
                event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();
            if (itemStack == null) return;

            String key = projectileHandler.getKeyFromItemStack(itemStack);
            if (key != null) event.setCancelled(true);
        }
    }
}