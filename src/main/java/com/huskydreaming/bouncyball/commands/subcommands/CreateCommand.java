package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileDefault;
import com.huskydreaming.bouncyball.enumerations.Locale;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.commands.annotations.CommandAnnotation;
import com.huskydreaming.huskycore.commands.providers.PlayerCommandProvider;
import org.bukkit.entity.Player;

@CommandAnnotation(label = "create")
public class CreateCommand implements PlayerCommandProvider {

    private final ParticleRepository particleRepository;
    private final ProjectileRepository projectileRepository;

    public CreateCommand(HuskyPlugin plugin) {
        this.particleRepository = plugin.provide(ParticleRepository.class);
        this.projectileRepository = plugin.provide(ProjectileRepository.class);
    }

    @Override
    public void onCommand(Player player, String[] strings) {
        if (strings.length == 2) {
            String projectileName = strings[1].toLowerCase();
            if (projectileRepository.hasProjectileData(projectileName)) {
                player.sendMessage(Locale.BOUNCY_BALL_EXISTS.prefix(projectileName));
                return;
            }

            ProjectileDefault projectileDefault = ProjectileDefault.DEFAULT;
            particleRepository.addParticleData(projectileName, projectileDefault.getParticleData());
            projectileRepository.addProjectileData(projectileName, projectileDefault.getProjectileData());

            player.sendMessage(Locale.BOUNCY_BALL_CREATE.prefix(projectileName));
        }
    }
}