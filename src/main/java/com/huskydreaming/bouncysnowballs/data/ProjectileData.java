package com.huskydreaming.bouncysnowballs.data;

public class ProjectileData {

    private final double launchVelocity;
    private final double damping;
    private final double threshold;

    public ProjectileData(double launchVelocity, double damping, double threshold) {
        this.launchVelocity = launchVelocity;
        this.damping = damping;
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }

    public double getDamping() {
        return damping;
    }

    public double getLaunchVelocity() {
        return launchVelocity;
    }
}
