package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.commands.base.Command;
import com.huskydreaming.bouncyball.commands.base.CommandInterface;
import com.huskydreaming.bouncyball.commands.base.CommandLabel;
import com.huskydreaming.bouncyball.services.interfaces.LocaleService;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.enumeration.Locale;
import org.bukkit.entity.Player;

import java.util.List;

@Command(label = CommandLabel.RELOAD)
public class ReloadCommand implements CommandInterface {

    private final BouncyBallPlugin plugin;
    private final LocaleService localeService;
    private final ParticleService particleService;
    private final ProjectileService projectileService;

    public ReloadCommand(BouncyBallPlugin plugin) {
        this.plugin = plugin;

        this.localeService = plugin.provide(LocaleService.class);
        this.particleService = plugin.provide(ParticleService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        localeService.getLocale().reload(plugin);
        localeService.getMenu().reload(plugin);

        particleService.serialize(plugin);
        projectileService.serialize(plugin);

        player.sendMessage(Locale.RELOAD.prefix());
    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return List.of();
    }
}