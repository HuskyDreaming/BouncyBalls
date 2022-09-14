package com.huskydreaming.bouncysnowballs.service;

import com.huskydreaming.bouncysnowballs.data.ParticleData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class ParticleServiceImpl implements ParticleService {

    private ParticleData particleData;

    @Override
    public void deserialize(Plugin plugin) {
        FileConfiguration configuration = plugin.getConfig();

        particleData = new ParticleData(
                Particle.valueOf(configuration.getString("Particle.type")),
                configuration.getInt("Particle.count"),
                configuration.getLong("Particle.ticks")
        );
    }

    @Override
    public void run(Plugin plugin, List<Projectile> projectiles) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            for(Projectile projectile : projectiles) {
                Location location = projectile.getLocation();
                World world = location.getWorld();
                if(world != null) {
                    world.spawnParticle(particleData.particle(), location, particleData.count());
                }
            }
        }, 0L, particleData.ticks());
    }
}
