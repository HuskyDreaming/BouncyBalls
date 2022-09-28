package com.huskydreaming.bouncyball.service;

import com.huskydreaming.bouncyball.data.ParticleData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ParticleServiceImpl implements ParticleService {

    private final Map<String, ParticleData> particleDataMap = new HashMap<>();

    @Override
    public void deserialize(Plugin plugin) {
        FileConfiguration configuration = plugin.getConfig();

        ConfigurationSection section = configuration.getConfigurationSection("balls");
        if(section == null) return;

        for(String key : section.getKeys(false)) {
            String path = "balls." + key + ".particle";

            ParticleData particleData = new ParticleData(
                    Particle.valueOf(configuration.getString(path + ".type")),
                    configuration.getInt(path + ".count")
            );

            particleDataMap.put(key, particleData);
        }
    }

    @Override
    public void run(Plugin plugin, Map<Projectile, String> projectiles) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {

            projectiles.forEach((projectile, s) -> {
                Location location = projectile.getLocation();
                World world = location.getWorld();
                if(world != null) {
                    ParticleData particleData = particleDataMap.get(s);
                    if(particleData != null) {
                        world.spawnParticle(particleData.particle(), location, particleData.count());
                    }
                }
            });
        }, 0L, 1L);
    }
}
