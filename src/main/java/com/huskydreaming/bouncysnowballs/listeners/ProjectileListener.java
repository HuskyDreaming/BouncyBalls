package com.huskydreaming.bouncysnowballs.listeners;

import com.huskydreaming.bouncysnowballs.service.ProjectileService;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class ProjectileListener implements Listener {

    private final ProjectileService projectileService;

    public ProjectileListener(ProjectileService projectileService) {
        this.projectileService = projectileService;
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player player) {
            if(event.getEntity() instanceof Snowball snowball) {
                projectileService.updateVelocity(snowball, player);
                projectileService.addProjectile(snowball);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Snowball snowball) {

            if (event.getHitEntity() instanceof Player player) {
                if ( projectileService.getData().returns()) {
                    player.getInventory().addItem(new ItemStack(Material.SNOWBALL));
                    projectileService.removeProjectile(snowball);
                    snowball.remove();
                    return;
                }
            }

            if(projectileService.getData().isBouncyBlock(event.getHitBlock())) {
                Projectile projectile = projectileService.updateProjectile(snowball);
                if (projectile != null) projectileService.addProjectile(projectile);
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
