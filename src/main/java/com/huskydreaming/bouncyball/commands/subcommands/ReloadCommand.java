package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.handlers.interfaces.LocalizationHandler;
import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.CommandProvider;
import org.bukkit.command.CommandSender;

@CommandAnnotation(label = "reload")
public class ReloadCommand implements CommandProvider {

    private final HuskyPlugin plugin;
    private final LocalizationHandler localizationHandler;
    private final ParticleRepository particleRepository;
    private final ProjectileRepository projectileRepository;

    public ReloadCommand(HuskyPlugin plugin) {
        this.plugin = plugin;

        this.localizationHandler = plugin.provide(LocalizationHandler.class);
        this.particleRepository = plugin.provide(ParticleRepository.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void onCommand(CommandSender commandSender, String[] strings) {
        localizationHandler.reload(plugin);

        particleRepository.postDeserialize(plugin);
        projectileRepository.postDeserialize(plugin);

        commandSender.sendMessage(Locale.RELOAD.prefix());
    }
}