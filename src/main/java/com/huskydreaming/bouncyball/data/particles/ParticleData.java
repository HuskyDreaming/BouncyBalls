package com.huskydreaming.bouncyball.data.particles;

import org.bukkit.Color;
import org.bukkit.Particle;

public class ParticleData {

    private Particle particle;
    private Color color;
    private int count;

    public static ParticleData create(Particle particle) {
        return new ParticleData(particle);
    }

    public static ParticleData create(Particle particle, Color color) {
        return new ParticleData(particle, color);
    }

    public ParticleData() {

    }

    public ParticleData(Particle particle, Color color) {
        this.particle = particle;
        this.color = color;
        this.count = 2;
    }

    public ParticleData(Particle particle) {
        this.particle = particle;
        this.color = Color.WHITE;
        this.count = 2;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Particle getParticle() {
        return particle;
    }

    public Color getColor() {
        if(color == null) color = Color.WHITE;
        return color;
    }

    public int getCount() {
        return count;
    }
}
