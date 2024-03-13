package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.commands.base.Command;
import com.huskydreaming.bouncyball.commands.base.CommandInterface;
import com.huskydreaming.bouncyball.commands.base.CommandLabel;
import com.huskydreaming.bouncyball.data.*;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.Locale;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

@Command(label = CommandLabel.CREATE)
public class CreateCommand implements CommandInterface {

    private final ParticleService particleService;
    private final ProjectileService projectileService;

    public CreateCommand(BouncyBallPlugin plugin) {
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
    public List<String> onTabComplete(String[] strings) {
        if (strings.length == 2) return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        if (strings.length == 3) return projectileService.getProjectileDataMap().keySet().stream().toList();
        return null;
    }
}