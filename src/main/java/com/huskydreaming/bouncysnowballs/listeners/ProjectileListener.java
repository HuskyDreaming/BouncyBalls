package com.huskydreaming.bouncysnowballs.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class ProjectileListener implements Listener {

    private final double launchVelocity;
    private final double damping;
    private final double threshold;

    public ProjectileListener(double launchVelocity, double damping, double threshold) {
        this.launchVelocity = launchVelocity;
        this.damping = damping;
        this.threshold = threshold;
    }

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if(event.getEntity().getShooter() instanceof Player player) {
            if(event.getEntity() instanceof Snowball snowball) {
                snowball.setVelocity(player.getLocation().getDirection().multiply(launchVelocity));
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

            speed *= damping;
            if(speed > threshold) {

                Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
                direction = direction.multiply(velocity.dot(direction)).multiply(2.0D);

                Projectile newProjectile = (Projectile) projectile.getWorld().spawnEntity(projectile.getLocation(), projectile.getType());
                newProjectile.setVelocity(velocity.subtract(direction).normalize().multiply(speed));
                newProjectile.setShooter(projectile.getShooter());

                projectile.remove();
            }
        }
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
