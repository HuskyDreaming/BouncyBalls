package com.huskydreaming.bouncyball.data;

import org.bukkit.Material;

public enum ProjectilePhysics {
    DAMPING("Velocity damped when it hits a surface", Material.FEATHER, 0.15D),
    LAUNCH_VELOCITY("Initial velocity of the snowball when thrown", Material.BLAZE_POWDER, 0.1D),
    THRESHOLD("The speed at which the projectile disintegrate", Material.GUNPOWDER, 0.1D);

    private final String description;
    private final Material material;
    private final double increment;

    ProjectilePhysics(String description, Material material, double increment) {
        this.description = description;
        this.material = material;
        this.increment = increment;
    }

    public Material getMaterial() {
        return material;
    }

    public String getDescription() {
        return description;
    }

    public double getIncrement() {
        return increment;
    }
}