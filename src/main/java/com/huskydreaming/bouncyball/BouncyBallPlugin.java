package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.commands.BaseCommand;
import com.huskydreaming.bouncyball.commands.subcommands.CreateCommand;
import com.huskydreaming.bouncyball.commands.subcommands.GiveCommand;
import com.huskydreaming.bouncyball.commands.subcommands.ReloadCommand;
import com.huskydreaming.bouncyball.listeners.ProjectileListener;
import com.huskydreaming.bouncyball.services.implementations.InventoryServiceImpl;
import com.huskydreaming.bouncyball.services.implementations.LocaleServiceImpl;
import com.huskydreaming.bouncyball.services.implementations.ParticleServiceImpl;
import com.huskydreaming.bouncyball.services.implementations.ProjectileServiceImpl;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.LocaleService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.huskycore.HuskyPlugin;

public class BouncyBallPlugin extends HuskyPlugin {

    @Override
    public void onEnable() {
        super.onEnable();

        // Register Services
        serviceRegistry.register(LocaleService.class, new LocaleServiceImpl());
        serviceRegistry.register(InventoryService.class, new InventoryServiceImpl());
        serviceRegistry.register(ProjectileService.class, new ProjectileServiceImpl());
        serviceRegistry.register(ParticleService.class, new ParticleServiceImpl(this));
        serviceRegistry.deserialize(this);
        serviceRegistry.provide(ParticleService.class).run(this);

        // Register Commands
        commandRegistry.setCommandExecutor(new BaseCommand(this));
        commandRegistry.add(new CreateCommand(this));
        commandRegistry.add(new GiveCommand(this));
        commandRegistry.add(new ReloadCommand(this));
        commandRegistry.deserialize(this);

        registerListeners(new ProjectileListener(this));
    }

    @Override
    public void onDisable() {
        serviceRegistry.serialize(this);
    }
}