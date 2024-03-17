package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileDefault;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.pareseables.Locale;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

@Command(label = "create")
public class CreateCommand implements SubCommand {

    private final ParticleService particleService;
    private final ProjectileService projectileService;

    public CreateCommand(HuskyPlugin plugin) {
        this.particleService = plugin.provide(ParticleService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void run(CommandSender commandSender, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1].toLowerCase();
            if (projectileService.containKey(string.toLowerCase())) {
                commandSender.sendMessage(Locale.BOUNCY_BALL_EXISTS.prefix(string));
                return;
            }

            ProjectileDefault projectileDefault = ProjectileDefault.DEFAULT;
            particleService.addParticle(string, projectileDefault.getParticleData());
            projectileService.addProjectile(string, projectileDefault.getProjectileData());

            commandSender.sendMessage(Locale.BOUNCY_BALL_CREATE.prefix(string));
        }
    }

    @Override
    public List<String> onTabComplete(String[] strings) {
        return null;
    }
}