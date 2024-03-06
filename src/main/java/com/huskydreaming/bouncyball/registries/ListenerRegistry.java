package com.huskydreaming.bouncyball.registries;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.listeners.ProjectileListener;
import org.bukkit.plugin.PluginManager;

public class ListenerRegistry implements Registry {

    @Override
    public void register(BouncyBallPlugin plugin) {
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(new ProjectileListener(plugin), plugin);
    }
}
