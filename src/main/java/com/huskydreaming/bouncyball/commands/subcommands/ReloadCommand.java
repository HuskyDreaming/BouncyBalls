package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.services.interfaces.LocaleService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.pareseables.Locale;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(label = "reload")
public class ReloadCommand implements SubCommand {

    private final HuskyPlugin plugin;
    private final LocaleService localeService;
    private final ParticleService particleService;
    private final ProjectileService projectileService;

    public ReloadCommand(HuskyPlugin plugin) {
        this.plugin = plugin;

        this.localeService = plugin.provide(LocaleService.class);
        this.particleService = plugin.provide(ParticleService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void run(CommandSender commandSender, String[] strings) {
        localeService.getLocale().reload(plugin);
        localeService.getMenu().reload(plugin);

        particleService.serialize(plugin);
        projectileService.serialize(plugin);

        commandSender.sendMessage(Locale.RELOAD.prefix());
    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return null;
    }
}