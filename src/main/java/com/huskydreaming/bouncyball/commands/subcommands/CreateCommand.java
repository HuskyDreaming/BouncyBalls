package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileDefault;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.pareseables.Locale;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.Command;
import com.huskydreaming.huskycore.commands.SubCommand;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@Command(label = "create")
public class CreateCommand implements SubCommand {

    private final ParticleService particleService;
    private final ProjectileService projectileService;

    public CreateCommand(HuskyPlugin plugin) {
        this.particleService = plugin.provide(ParticleService.class);
        this.projectileService = plugin.provide(ProjectileService.class);
    }

    @Override
    public void run(Player player, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1].toLowerCase();
            if (projectileService.containKey(string.toLowerCase())) {
                player.sendMessage(Locale.BOUNCY_BALL_EXISTS.prefix(string));
                return;
            }

            ProjectileDefault projectileDefault = ProjectileDefault.DEFAULT;
            particleService.addParticle(string, projectileDefault.getParticleData());
            projectileService.addProjectile(string, projectileDefault.getProjectileData());

            player.sendMessage(Locale.BOUNCY_BALL_CREATE.prefix(string));
        }
    }

    @Override
    public void run(ConsoleCommandSender sender, String[] strings) {
        if (strings.length == 2) {
            String string = strings[1].toLowerCase();
            if (projectileService.containKey(string.toLowerCase())) {
                sender.sendMessage(Locale.BOUNCY_BALL_EXISTS.prefix(string));
                return;
            }

            ProjectileDefault projectileDefault = ProjectileDefault.DEFAULT;
            particleService.addParticle(string, projectileDefault.getParticleData());
            projectileService.addProjectile(string, projectileDefault.getProjectileData());

            sender.sendMessage(Locale.BOUNCY_BALL_CREATE.prefix(string));
        }
    }
}