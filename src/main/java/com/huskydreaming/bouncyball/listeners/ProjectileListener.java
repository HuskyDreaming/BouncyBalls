package com.huskydreaming.bouncyball.listeners;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ProjectileListener implements Listener {

    private final Plugin plugin;
    private final ProjectileService projectileService;

    public ProjectileListener(BouncyBallPlugin plugin) {
        this.plugin = plugin;
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack itemStack = event.getItem();
            if (itemStack == null) return;

            String key = projectileService.getKeyFromItemStack(itemStack);
            if (key != null) {
                projectileService.launchProjectile(plugin, event.getPlayer(), itemStack, key);
                event.setCancelled(true);
            } else {
                event.setCancelled(false);
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack itemStack = event.getItem();
            if (itemStack == null) return;

            String key = projectileService.getKeyFromItemStack(itemStack);
            if (key != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockEvent(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL) {
            Entity entity = event.getIgnitingEntity();

            if (entity != null && projectileService.hasProjectileData(entity)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {

        if (event.getEntity() instanceof Snowball snowball) {

            ProjectileData projectileData = projectileService.getDataFromProjectile(snowball);
            if (projectileData == null) return;

            if (event.getHitEntity() instanceof Player player) {

                if (projectileData.getSettings().contains(ProjectileSetting.RETURNS)) {

                    ItemStack itemStack = projectileService.getItemStackFromProjectile(snowball);
                    if (itemStack == null) return;

                    player.getInventory().addItem(itemStack);
                    projectileService.removeProjectile(snowball);
                    snowball.remove();
                    return;
                }
            }

            if (projectileData.isBouncyBlock(event.getHitBlock())) {
                String key = projectileService.getKeyFromProjectile(snowball);
                Projectile projectile = projectileService.updateProjectile(plugin, snowball);
                if (projectile != null) projectileService.addProjectile(key, projectile);

                projectileService.removeProjectile(snowball);
                snowball.remove();
                return;
            }

            projectileService.dropProjectile(snowball);
            projectileService.removeProjectile(snowball);
            snowball.remove();
        }
    }
}