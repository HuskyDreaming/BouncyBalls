package com.huskydreaming.bouncyball.repositories.implementations;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileDefault;
import com.huskydreaming.bouncyball.data.projectiles.ProjectilePhysics;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Yaml;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectileRepositoryImpl implements ProjectileRepository {

    private Map<String, ProjectileData> projectileDataMap;
    private Yaml yaml;

    @Override
    public void deserialize(HuskyPlugin plugin) {
        projectileDataMap = new ConcurrentHashMap<>();
        yaml = new Yaml("projectiles");
        yaml.load(plugin);

        FileConfiguration configuration = yaml.getConfiguration();
        ConfigurationSection configurationSection = configuration.getConfigurationSection("");
        if(configurationSection == null) return;

        Set<String> keys = configurationSection.getKeys(false);
        if(keys.isEmpty()) {
            for (ProjectileDefault projectileDefault : ProjectileDefault.values()) {
                if (projectileDefault == ProjectileDefault.DEFAULT) continue;
                projectileDataMap.put(projectileDefault.toString(), projectileDefault.getProjectileData());
            }
            yaml.save();
            return;
        }

        for(String key : keys) {
            ProjectileData projectileData = new ProjectileData();

            String materialName = configuration.getString(key + ".material");
            if(materialName != null) projectileData.setMaterial(Material.valueOf(materialName));

            double damping = configuration.getDouble(key + ".physics.damping");
            projectileData.setPhysics(ProjectilePhysics.DAMPING, damping);

            double launchVelocity = configuration.getDouble(key + ".physics.launch-velocity");
            projectileData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, launchVelocity);

            double threshold = configuration.getDouble(key + ".physics.threshold");
            projectileData.setPhysics(ProjectilePhysics.THRESHOLD, threshold);

            for(String setting : configuration.getStringList(key + ".settings")) {
                projectileData.addSetting(ProjectileSetting.valueOf(setting));
            }

            for(String blockMaterial : configuration.getStringList(key + ".blocks")) {
                projectileData.addBlock(Material.valueOf(blockMaterial));
            }

            projectileDataMap.put(key, projectileData);
        }
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        FileConfiguration configuration = yaml.getConfiguration();

        for(Map.Entry<String, ProjectileData> projectile : projectileDataMap.entrySet()) {
            ProjectileData projectileData = projectile.getValue();
            String key = projectile.getKey();

            List<String> settings = new ArrayList<>();
            for(ProjectileSetting setting : projectileData.getSettings()) {
                settings.add(setting.name());
            }

            List<String> blockMaterials = new ArrayList<>();
            for(Material blockMaterial : projectileData.getBlocks()) {
                blockMaterials.add(blockMaterial.name());
            }

            configuration.set(key + ".settings", settings);
            configuration.set(key + ".blocks", blockMaterials);
            configuration.set(key + ".material", projectileData.getMaterial().name());
            configuration.set(key + ".physics.damping", projectileData.getPhysics(ProjectilePhysics.DAMPING));
            configuration.set(key + ".physics.launch-velocity", projectileData.getPhysics(ProjectilePhysics.LAUNCH_VELOCITY));
            configuration.set(key + ".physics.threshold", projectileData.getPhysics(ProjectilePhysics.THRESHOLD));
        }

        yaml.save();
        projectileDataMap.clear();
    }

    @Override
    public void addProjectileData(String name, ProjectileData projectileData) {
        projectileDataMap.put(name, projectileData);
    }

    @Override
    public void removeProjectileData(String name) {
        projectileDataMap.remove(name);
    }

    @Override
    public boolean hasProjectileData(String name) {
        return projectileDataMap.containsKey(name);
    }

    @Override
    public ProjectileData getProjectileData(String name) {
        return projectileDataMap.get(name);
    }

    @Override
    public Map<String, ProjectileData> getProjectileDataMap() {
        return Collections.unmodifiableMap(projectileDataMap);
    }
}
