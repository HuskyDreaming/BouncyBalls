package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.registries.CommandRegistry;
import com.huskydreaming.bouncyball.registries.ListenerRegistry;
import com.huskydreaming.bouncyball.registries.ServiceRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BouncyBallPlugin extends JavaPlugin {

    private ServiceRegistry serviceRegistry;
    private CommandRegistry commandRegistry;

    @Override
    public void onEnable() {

        serviceRegistry = new ServiceRegistry();
        serviceRegistry.register(this);

        commandRegistry = new CommandRegistry();
        commandRegistry.register(this);

        ListenerRegistry listenerRegistry = new ListenerRegistry();
        listenerRegistry.register(this);
    }

    @Override
    public void onDisable() {
        serviceRegistry.unregister(this);
    }

    @NotNull
    public <T> T provide(Class<T> tClass) {
        return tClass.cast(serviceRegistry.getServices().get(tClass));
    }

    public CommandRegistry getCommandRegistry() {
        return commandRegistry;
    }
}