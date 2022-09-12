package com.huskydreaming.bouncysnowballs;

import com.huskydreaming.bouncysnowballs.listeners.ProjectileListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncySnowballsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        double launchVelocity = getConfig().getDouble("launch-velocity");
        double damping = getConfig().getDouble("damping");
        double threshold = getConfig().getDouble("threshold");

        PluginManager pluginManager = getServer().getPluginManager();
        ProjectileListener projectileListener = new ProjectileListener(launchVelocity, damping, threshold);
        pluginManager.registerEvents(projectileListener, this);
    }
}
