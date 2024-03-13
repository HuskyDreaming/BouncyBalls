package com.huskydreaming.bouncyball.data;

import com.huskydreaming.bouncyball.registries.ProjectileRegistry;
import org.bukkit.Color;
import org.bukkit.Particle;

public enum ProjectileDefault {
    DEFAULT(ProjectileRegistry.defaultData(), ParticleData.create(Particle.VILLAGER_HAPPY)),
    SNOW_BALL(ProjectileRegistry.snowBallData(), ParticleData.create(Particle.SNOW_SHOVEL)),
    TURTLE_EGG(ProjectileRegistry.turtleEggData(), ParticleData.create(Particle.GLOW)),
    HOT_POTATO(ProjectileRegistry.hotPotatoData(), ParticleData.create(Particle.LAVA)),
    NEWTONS_APPLE(ProjectileRegistry.newtonsAppleData(), ParticleData.create(Particle.REDSTONE, Color.RED)),
    GROOVY_JUKEBOX(ProjectileRegistry.groovyJukeBoxData(), ParticleData.create(Particle.NOTE));

    private final ProjectileData projectileData;
    private final ParticleData particleData;

    ProjectileDefault(ProjectileData projectileData, ParticleData particleData) {
        this.projectileData = projectileData;
        this.particleData = particleData;
    }

    public ParticleData getParticleData() {
        return particleData;
    }

    public ProjectileData getProjectileData() {
        return projectileData;
    }
}