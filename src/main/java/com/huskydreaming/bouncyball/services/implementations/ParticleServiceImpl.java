package com.huskydreaming.bouncyball.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.ParticleData;
import com.huskydreaming.bouncyball.data.ProjectileDefault;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.storage.Json;
import com.huskydreaming.bouncyball.utilities.Util;
import org.bukkit.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleServiceImpl implements ParticleService {

    private Map<String, ParticleData> particleDataMap = new HashMap<>();
    private final ProjectileService projectileService;

    @Override
    public void deserialize(BouncyBallPlugin plugin) {
        Type type = new TypeToken<Map<String, ParticleData>>(){}.getType();
        particleDataMap = Json.read(plugin, "data/particles", type);

        if(particleDataMap == null) {
            particleDataMap = new ConcurrentHashMap<>();

            for(ProjectileDefault projectileDefault : ProjectileDefault.values()) {
                if(projectileDefault == ProjectileDefault.DEFAULT) continue;
                String[] strings = projectileDefault.name().toLowerCase().split("_");
                String string = Util.capitalize(String.join("_", strings));

                particleDataMap.put(string, projectileDefault.getParticleData());
            }
        }
    }

    @Override
    public void serialize(BouncyBallPlugin plugin) {
        Json.write(plugin, "data/particles", particleDataMap);
    }

    public ParticleServiceImpl(BouncyBallPlugin plugin) {
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void addParticle(String key, ParticleData particleData) {
        particleDataMap.put(key, particleData);
    }

    @Override
    public ParticleData getParticle(String key) {
        return particleDataMap.get(key);
    }

    @Override
    public void run(BouncyBallPlugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> projectileService.getProjectileMap().forEach((projectile, s) -> {
            Location location = projectile.getLocation();
            World world = location.getWorld();
            if (world != null) {
                ParticleData particleData = particleDataMap.get(s);
                if (particleData != null) {
                    if(particleData.getParticle() == Particle.REDSTONE) {
                        Particle.DustOptions dustOptions = new Particle.DustOptions(particleData.getColor(), 1);
                        world.spawnParticle(particleData.getParticle(), location, particleData.getCount(), dustOptions);
                    } else {
                        world.spawnParticle(particleData.getParticle(), location, particleData.getCount());
                    }
                }
            }
        }), 0L, 1L);
    }
}