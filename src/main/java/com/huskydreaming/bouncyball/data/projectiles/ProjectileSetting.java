package com.huskydreaming.bouncyball.data.projectiles;

public enum ProjectileSetting {
    RETURNS("Goes into inventory when player is hit"),
    DROPS("Item drops when projectile stops moving"),
    GLOWS("Makes the projectile glow"),
    ITEM_NAME("Name above item when dropped"),
    ALL_BLOCKS("Bounces on all blocks"),
    REMOVES("Projectile is removed from inventory");

    private final String description;

    ProjectileSetting(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
