package com.huskydreaming.bouncyball.repositories.implementations;

import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileDefault;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Yaml;
import com.huskydreaming.huskycore.utilities.Util;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParticleRepositoryImpl implements ParticleRepository {

    private Map<String, ParticleData> particleDataMap;
    private Yaml yaml;

    @Override
    public void deserialize(HuskyPlugin plugin) {
        particleDataMap = new HashMap<>();
        yaml = new Yaml("particles");
        yaml.load(plugin);

        FileConfiguration configuration = yaml.getConfiguration();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("");
        if(configurationSection == null) return;

        Set<String> keys = configurationSection.getKeys(false);
        if (keys.isEmpty()) {
            for (ProjectileDefault projectileDefault : ProjectileDefault.values()) {
                if (projectileDefault == ProjectileDefault.DEFAULT) continue;
                String[] strings = projectileDefault.name().toLowerCase().split("_");
                String string = Util.capitalize(String.join("_", strings));

                particleDataMap.put(string, projectileDefault.getParticleData());
            }
            yaml.save();
            return;
        }

        for (String key : configurationSection.getKeys(false)) {
            ParticleData particleData = new ParticleData();

            int count = configuration.getInt(key + ".count");
            particleData.setCount(count);

            int r = configuration.getInt(key + ".color.r");
            int g = configuration.getInt(key + ".color.g");
            int b = configuration.getInt(key + ".color.b");

            particleData.setColor(Color.fromRGB(r, g, b));

            String particle = configuration.getString(key + ".type");
            if (particle != null) particleData.setParticle(Particle.valueOf(particle));

            particleDataMap.put(key, particleData);
        }
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        FileConfiguration configuration = yaml.getConfiguration();

        for(Map.Entry<String, ParticleData> particle : particleDataMap.entrySet()) {
            ParticleData particleData = particle.getValue();
            String key = particle.getKey();

            configuration.set(key + ".count", particleData.getCount());
            configuration.set(key + ".type", particleData.getParticle().name());
            configuration.set(key + ".color.r", particleData.getColor().getRed());
            configuration.set(key + ".color.g", particleData.getColor().getGreen());
            configuration.set(key + ".color.b", particleData.getColor().getBlue());
        }

        yaml.save();
        particleDataMap.clear();
    }

    @Override
    public void addParticleData(String key, ParticleData particleData) {
        particleDataMap.put(key, particleData);
    }

    @Override
    public ParticleData getParticleData(String name) {
        return particleDataMap.get(name);
    }
}
