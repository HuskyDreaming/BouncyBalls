package com.huskydreaming.bouncyball.data.projectiles;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ProjectileData {

    private final Set<Material> blocks = new HashSet<>();
    private final Set<ProjectileSetting> settings = new HashSet<>();
    private final Map<ProjectilePhysics, Double> physics = new ConcurrentHashMap<>();
    private Material material;

    public boolean isBouncyBlock(Block block) {
        if (!blocks.isEmpty() && block != null) {
            return blocks.contains(block.getType());
        }
        return true;
    }

    public void addBlock(Material material) {
        blocks.add(material);
    }

    public void removeBlock(Material material) {
        blocks.remove(material);
    }

    public Set<Material> getBlocks() {
        return Collections.unmodifiableSet(blocks);
    }

    public void addSetting(ProjectileSetting setting) {
        settings.add(setting);
    }

    public void removeSetting(ProjectileSetting setting) {
        settings.remove(setting);
    }

    public Set<ProjectileSetting> getSettings() {
        return Collections.unmodifiableSet(settings);
    }

    public void setPhysics(ProjectilePhysics projectilePhysics, double amount) {
        physics.put(projectilePhysics, amount);
    }

    public double getPhysics(ProjectilePhysics projectilePhysics) {
        return physics.get(projectilePhysics);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}