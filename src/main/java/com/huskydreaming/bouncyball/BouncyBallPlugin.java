package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.listeners.ProjectileListener;
import com.huskydreaming.bouncyball.service.interfaces.ParticleService;
import com.huskydreaming.bouncyball.service.implementations.ParticleServiceImpl;
import com.huskydreaming.bouncyball.service.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.service.implementations.ProjectileServiceImpl;
import com.huskydreaming.bouncyball.storage.Locale;
import com.huskydreaming.bouncyball.storage.Yaml;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BouncyBallPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        Yaml locale = new Yaml("locale");
        locale.load(this);
        Locale.setConfiguration(locale.getConfiguration());

        for (Locale message : Locale.values()) {
            locale.getConfiguration().set(message.toString(), message.parseList() != null ? message.parseList() : message.parse());
        }
        locale.save();


        ProjectileService projectileService = new ProjectileServiceImpl();
        projectileService.deserialize(this);

        ParticleService particleService = new ParticleServiceImpl(projectileService);
        particleService.deserialize(this);
        particleService.run(this);

        PluginManager pluginManager = getServer().getPluginManager();
        ProjectileListener projectileListener = new ProjectileListener(this, projectileService);
        pluginManager.registerEvents(projectileListener, this);

        PluginCommand pluginCommand = getCommand("bouncyball");
        if (pluginCommand != null) pluginCommand.setExecutor(new BouncyBallCommand(projectileService));
    }
}