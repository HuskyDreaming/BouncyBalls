package com.huskydreaming.bouncysnowballs;

import com.huskydreaming.bouncysnowballs.listeners.ProjectileListener;
import com.huskydreaming.bouncysnowballs.service.ParticleService;
import com.huskydreaming.bouncysnowballs.service.ParticleServiceImpl;
import com.huskydreaming.bouncysnowballs.service.ProjectileService;
import com.huskydreaming.bouncysnowballs.service.ProjectileServiceImpl;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncySnowballsPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ProjectileService projectileService = new ProjectileServiceImpl();
        projectileService.deserialize(this);

        ParticleService particleService = new ParticleServiceImpl();
        particleService.deserialize(this);
        particleService.run(this, projectileService.getProjectiles());


        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ProjectileListener(projectileService), this);
    }
}
