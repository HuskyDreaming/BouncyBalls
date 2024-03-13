package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.registries.CommandRegistry;
import com.huskydreaming.bouncyball.registries.ListenerRegistry;
import com.huskydreaming.bouncyball.registries.Registry;
import com.huskydreaming.bouncyball.registries.ServiceRegistry;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class BouncyBallPlugin extends JavaPlugin {

    private ServiceRegistry serviceRegistry;
    private CommandRegistry commandRegistry;

    @Override
    public void onEnable() {
        serviceRegistry = new ServiceRegistry();
        commandRegistry = new CommandRegistry();
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        register(serviceRegistry, commandRegistry, listenerRegistry);

        provide(ParticleService.class).run(this);
    }

    @Override
    public void onDisable() {
        serviceRegistry.unregister(this);
    }

    @NotNull
    public <T> T provide(Class<T> tClass) {
        return tClass.cast(serviceRegistry.getServices().get(tClass));
    }

    private void register(Registry... registries){
        Arrays.stream(registries).forEach(registry -> registry.register(this));
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}