package com.huskydreaming.bouncysnowballs.data;

import org.bukkit.Particle;

public class ParticleData {

    private final Particle particle;
    private final int count;

    public ParticleData(Particle particle, int count) {
        this.particle = particle;
        this.count = count;
    }

    public Particle getParticle() {
        return particle;
    }

    public int getCount() {
        return count;
    }
}
