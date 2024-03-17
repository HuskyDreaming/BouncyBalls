package com.huskydreaming.bouncyball.data.projectiles;

import com.huskydreaming.bouncyball.data.particles.ParticleData;
import org.bukkit.Color;
import org.bukkit.Particle;

public enum ProjectileDefault {
    DEFAULT(ProjectileDefaults.defaultData(), ParticleData.create(Particle.VILLAGER_HAPPY)),
    SNOW_BALL(ProjectileDefaults.snowBallData(), ParticleData.create(Particle.SNOW_SHOVEL)),
    TURTLE_EGG(ProjectileDefaults.turtleEggData(), ParticleData.create(Particle.GLOW)),
    HOT_POTATO(ProjectileDefaults.hotPotatoData(), ParticleData.create(Particle.LAVA)),
    NEWTONS_APPLE(ProjectileDefaults.newtonsAppleData(), ParticleData.create(Particle.REDSTONE, Color.RED)),
    GROOVY_JUKEBOX(ProjectileDefaults.groovyJukeBoxData(), ParticleData.create(Particle.NOTE));

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