package com.huskydreaming.bouncysnowballs.listeners;

import com.huskydreaming.bouncysnowballs.data.ParticleData;
import com.huskydreaming.bouncysnowballs.data.ProjectileData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ProjectileListener implements Listener {

    private final ParticleData particleData;
    private final ProjectileData projectileData;
    private final List<Projectile> projectiles = new ArrayList<>();

    public ProjectileListener(ParticleData particleData, ProjectileData projectileData) {
        this.particleData = particleData;
        this.projectileData = projectileData;
    }


    public void scheduleParticles(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(Projectile projectile : projectiles) {
                particleData.spawn(projectile.getLocation());
            }
        }, 0, 1L);
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player player) {
            if(event.getEntity() instanceof Snowball snowball) {
                projectileData.adjustVelocity(snowball, player.getLocation());
                projectiles.add(snowball);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Snowball snowball) {
            Projectile projectile = projectileData.updateProjectile(snowball);
            if(projectile != null) projectiles.add(projectile);

            projectiles.remove(snowball);
            snowball.remove();
        }
    }
}
