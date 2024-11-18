package com.huskydreaming.bouncyball.handlers.implementations;

import com.huskydreaming.bouncyball.data.particles.ParticleData;
import com.huskydreaming.bouncyball.handlers.interfaces.ParticleHandler;
import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.bouncyball.repositories.interfaces.ParticleRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Projectile;

import java.util.Map;

public class ParticleHandlerImpl implements ParticleHandler {

    private ParticleRepository particleRepository;

    @Override
    public void postInitialize(HuskyPlugin plugin) {
        particleRepository = plugin.provide(ParticleRepository.class);
        ProjectileHandler projectileHandler = plugin.provide(ProjectileHandler.class);
        Map<Projectile, String> projectileMap = projectileHandler.getProjectileMap();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> projectileMap.forEach((projectile, s) -> {
            if(s == null) projectileHandler.removeProjectile(projectile);

            Location location = projectile.getLocation();
            World world = location.getWorld();
            if (world == null) return;

            ParticleData particleData = particleRepository.getParticleData(s);
            if (particleData == null) return;

            if (particleData.getParticle() == Particle.REDSTONE) {
                Particle.DustOptions dustOptions = new Particle.DustOptions(particleData.getColor(), 1);
                world.spawnParticle(particleData.getParticle(), location, particleData.getCount(), dustOptions);
            } else {
                world.spawnParticle(particleData.getParticle(), location, particleData.getCount());
            }
            Bukkit.getLogger().info(String.valueOf(projectileMap.size()));
        }), 0L, 1L);
    }
}