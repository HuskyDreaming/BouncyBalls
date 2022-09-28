package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.commands.BouncyBallCommand;
import com.huskydreaming.bouncyball.listeners.ProjectileListener;
import com.huskydreaming.bouncyball.service.ParticleService;
import com.huskydreaming.bouncyball.service.ParticleServiceImpl;
import com.huskydreaming.bouncyball.service.ProjectileService;
import com.huskydreaming.bouncyball.service.ProjectileServiceImpl;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncyBallPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        ProjectileService projectileService = new ProjectileServiceImpl();
        projectileService.deserialize(this);

        ParticleService particleService = new ParticleServiceImpl();
        particleService.deserialize(this);
        particleService.run(this, projectileService.getProjectileMap());


        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ProjectileListener(this, projectileService), this);

        PluginCommand pluginCommand = getCommand("bouncyball");
        if(pluginCommand != null) pluginCommand.setExecutor(new BouncyBallCommand(projectileService));
    }
}
