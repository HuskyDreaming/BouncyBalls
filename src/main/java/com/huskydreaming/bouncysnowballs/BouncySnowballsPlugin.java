package com.huskydreaming.bouncysnowballs;

import com.huskydreaming.bouncysnowballs.data.ParticleData;
import com.huskydreaming.bouncysnowballs.data.ProjectileData;
import com.huskydreaming.bouncysnowballs.listeners.ProjectileListener;
import org.bukkit.Particle;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncySnowballsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ParticleData particleData = new ParticleData(
                Particle.valueOf(getConfig().getString("Particle.type")),
                getConfig().getInt("Particle.count")
        );

        ProjectileData projectileData = new ProjectileData(
                getConfig().getDouble("Projectile.launch-velocity"),
                getConfig().getDouble("Projectile.damping"),
                getConfig().getDouble("Projectile.threshold")
        );

        PluginManager pluginManager = getServer().getPluginManager();

        ProjectileListener projectileListener = new ProjectileListener(particleData, projectileData);
        projectileListener.scheduleParticles(this);

        pluginManager.registerEvents(projectileListener, this);
    }
}
