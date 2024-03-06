package com.huskydreaming.bouncyball.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.ParticleData;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.storage.Json;
import org.bukkit.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleServiceImpl implements ParticleService {

    private Map<String, ParticleData> particleDataMap = new HashMap<>();
    private final ProjectileService projectileService;

    public ParticleServiceImpl(BouncyBallPlugin plugin) {
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void update(String name, ParticleData particleData) {
        particleDataMap.put(name, particleData);
    }

    @Override
    public ParticleData getParticle(String key) {
        return particleDataMap.get(key);
    }

    @Override
    public void addParticle(String key, ParticleData particleData) {
        particleDataMap.put(key, particleData);
    }

    @Override
    public void deserialize(BouncyBallPlugin plugin) {
        Type type = new TypeToken<Map<String, ParticleData>>(){}.getType();
        particleDataMap = Json.read(plugin, "data/particles", type);
        if(particleDataMap == null) {
            particleDataMap = new ConcurrentHashMap<>();
            addExamples();
        }

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> projectileService.getProjectileMap().forEach((projectile, s) -> {
            Location location = projectile.getLocation();
            World world = location.getWorld();
            if (world != null) {
                ParticleData particleData = particleDataMap.get(s);
                if (particleData != null) {
                    if(particleData.particle() == Particle.REDSTONE) {
                        // TODO: Add customisation for particle colors
                        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.RED, 1);
                        world.spawnParticle(particleData.particle(), location, particleData.count(), dustOptions);
                    } else {
                        world.spawnParticle(particleData.particle(), location, particleData.count());
                    }
                }
            }
        }), 0L, 1L);
    }

    @Override
    public void serialize(BouncyBallPlugin plugin) {
        Json.write(plugin, "data/particles", particleDataMap);
    }

    private void addExamples() {
        particleDataMap.put("Snow_Ball", new ParticleData(Particle.SNOW_SHOVEL, 2));
        particleDataMap.put("Turtle_Egg", new ParticleData(Particle.GLOW, 2));
        particleDataMap.put("Hot_Potato", new ParticleData(Particle.LAVA, 2));
        particleDataMap.put("Space_Egg", new ParticleData(Particle.WAX_OFF, 2));
        particleDataMap.put("Newtons_Apple", new ParticleData(Particle.FALLING_LAVA, 2));
        particleDataMap.put("Groovy_Jukebox", new ParticleData(Particle.NOTE, 2));
    }
}