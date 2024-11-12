package com.huskydreaming.bouncyball;

import com.huskydreaming.bouncyball.commands.BaseCommand;
import com.huskydreaming.bouncyball.commands.subcommands.CreateCommand;
import com.huskydreaming.bouncyball.commands.subcommands.EditCommand;
import com.huskydreaming.bouncyball.commands.subcommands.GiveCommand;
import com.huskydreaming.bouncyball.commands.subcommands.ReloadCommand;
import com.huskydreaming.bouncyball.handlers.implementations.InventoryHandlerImpl;
import com.huskydreaming.bouncyball.handlers.implementations.LocalizationHandlerImpl;
import com.huskydreaming.bouncyball.handlers.implementations.ParticleHandlerImpl;
import com.huskydreaming.bouncyball.handlers.implementations.ProjectileHandlerImpl;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.handlers.interfaces.LocalizationHandler;
import com.huskydreaming.bouncyball.handlers.interfaces.ParticleHandler;
import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.bouncyball.listeners.ProjectileListener;
import com.huskydreaming.bouncyball.repositories.implementations.ParticleRepositoryImpl;
import com.huskydreaming.bouncyball.repositories.implementations.ProjectileRepositoryImpl;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.handlers.implementations.DefaultConfigHandlerImpl;
import com.huskydreaming.huskycore.handlers.interfaces.CommandHandler;
import com.huskydreaming.huskycore.handlers.interfaces.DefaultConfigHandler;

public class BouncyBallPlugin extends HuskyPlugin {

    @Override
    public void onInitialize() {
        handlerRegistry.register(DefaultConfigHandler.class, new DefaultConfigHandlerImpl());
        handlerRegistry.register(LocalizationHandler.class, new LocalizationHandlerImpl());
        handlerRegistry.register(InventoryHandler.class, new InventoryHandlerImpl());
        handlerRegistry.register(ParticleHandler.class, new ParticleHandlerImpl());
        handlerRegistry.register(ProjectileHandler.class, new ProjectileHandlerImpl());

        repositoryRegistry.register(ParticleRepository.class, new ParticleRepositoryImpl());
        repositoryRegistry.register(ProjectileRepository.class, new ProjectileRepositoryImpl());
    }

    @Override
    public void onPostInitialize() {
        CommandHandler commandHandler = handlerRegistry.provide(CommandHandler.class);
        commandHandler.setCommandExecutor(new BaseCommand(this));
        commandHandler.add(new CreateCommand(this));
        commandHandler.add(new EditCommand(this));
        commandHandler.add(new GiveCommand(this));
        commandHandler.add(new ReloadCommand(this));

        registerListeners(new ProjectileListener(this));
    }
}