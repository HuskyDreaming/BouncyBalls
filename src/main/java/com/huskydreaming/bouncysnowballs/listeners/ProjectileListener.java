package com.huskydreaming.bouncysnowballs.listeners;

import com.huskydreaming.bouncysnowballs.data.ParticleData;
import com.huskydreaming.bouncysnowballs.data.ProjectileData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ProjectileListener implements Listener {

    private final ParticleData particleData;
    private final ProjectileData projectileData;
    private final List<Projectile> projectiles = new ArrayList<>();

    public ProjectileListener(ParticleData particleData, ProjectileData projectileData) {
        this.particleData = particleData;
        this.projectileData = projectileData;
    }


    public void scheduleParticles(Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for(Projectile projectile : projectiles) {
                Location location = projectile.getLocation();
                World world = location.getWorld();
                if(world != null) {
                    world.spawnParticle(particleData.getParticle(), location, particleData.getCount());
                }
            }
        }, 0, 1L);
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player player) {
            if(event.getEntity() instanceof Snowball snowball) {
                snowball.setVelocity(player.getLocation().getDirection().multiply(projectileData.getLaunchVelocity()));
                projectiles.add(snowball);
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if(event.getEntity() instanceof Snowball snowball) {
            updateProjectile(snowball, getInverseFace(snowball));
        }
    }

    private void updateProjectile(Projectile projectile, BlockFace blockFace) {
        if(blockFace != null) {

            Vector velocity = projectile.getVelocity();
            double speed = velocity.length();

            speed *= projectileData.getDamping();
            if(speed > projectileData.getThreshold()) {

                Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
                direction = direction.multiply(velocity.dot(direction)).multiply(2.0D);

                Projectile newProjectile = (Projectile) projectile.getWorld().spawnEntity(projectile.getLocation(), projectile.getType());
                newProjectile.setVelocity(velocity.subtract(direction).normalize().multiply(speed));
                newProjectile.setShooter(projectile.getShooter());
                projectiles.add(newProjectile);
            }
        }

        projectiles.remove(projectile);
        projectile.remove();
    }

    private BlockFace getInverseFace(Projectile projectile) {
        Location location = projectile.getLocation();
        World world = location.getWorld();
        if(world == null) return null;

        Block block = location.getBlock();
        BlockIterator blockIterator = new BlockIterator(world, location.toVector(), projectile.getVelocity(), 0.0D, 3);

        Block previousBlock = block;
        Block nextBlock = blockIterator.next();

        while (blockIterator.hasNext() && (nextBlock.getType() == Material.AIR || nextBlock.isLiquid() || nextBlock.equals(block))) {
            previousBlock = nextBlock;
            nextBlock = blockIterator.next();
        }


        BlockFace blockFace = nextBlock.getFace(previousBlock);
        return blockFace == BlockFace.SELF ?  BlockFace.UP : blockFace;
    }
}
