package com.huskydreaming.bouncyball.commands.subcommands;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.commands.base.Command;
import com.huskydreaming.bouncyball.commands.base.CommandInterface;
import com.huskydreaming.bouncyball.commands.base.CommandLabel;
import com.huskydreaming.bouncyball.data.ParticleData;
import com.huskydreaming.bouncyball.data.ProjectileData;
import com.huskydreaming.bouncyball.data.ProjectilePhysics;
import com.huskydreaming.bouncyball.data.ProjectileSetting;
import com.huskydreaming.bouncyball.services.interfaces.ParticleService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.bouncyball.storage.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
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
            String string = strings[1];
            if(projectileService.containKey(string)) {
                player.sendMessage(Locale.BOUNCY_BALL_EXISTS.prefix(string));
                return;
            }

            ProjectileData projectileData = new ProjectileData();
            projectileData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 1.0D);
            projectileData.setPhysics(ProjectilePhysics.DAMPING, 0.75D);
            projectileData.setPhysics(ProjectilePhysics.THRESHOLD, 0.8D);
            projectileData.addSetting(ProjectileSetting.DROPS);
            projectileData.addSetting(ProjectileSetting.REMOVES);
            projectileData.addSetting(ProjectileSetting.RETURNS);
            projectileData.setMaterial(Material.SNOWBALL);

            particleService.addParticle(string, new ParticleData(Particle.VILLAGER_HAPPY, 2));
            projectileService.addProjectile(string, projectileData);

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