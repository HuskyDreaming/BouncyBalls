package com.huskydreaming.bouncysnowballs;

import com.huskydreaming.bouncysnowballs.data.ParticleData;
import com.huskydreaming.bouncysnowballs.data.ProjectileData;
import com.huskydreaming.bouncysnowballs.listeners.ProjectileListener;
import org.bukkit.Particle;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncySnowballsPlugin extends JavaPlugin {

    private ParticleData particleData;
    private ProjectileData projectileData;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        particleData = new ParticleData(
                Particle.valueOf(getConfig().getString("Particle.type")),
                getConfig().getInt("Particle.count")
        );

        projectileData = new ProjectileData(
                getConfig().getDouble("Projectile.launch-velocity"),
                getConfig().getDouble("Projectile.damping"),
                getConfig().getDouble("Projectile.threshold")
        );

        PluginManager pluginManager = getServer().getPluginManager();
        ProjectileListener projectileListener = new ProjectileListener(particleData, projectileData);
        projectileListener.scheduleParticles(this);
        pluginManager.registerEvents(projectileListener, this);
    }

    public ParticleData getParticleData() {
        return particleData;
    }

    public ProjectileData getProjectileData() {
        return projectileData;
    }
}
