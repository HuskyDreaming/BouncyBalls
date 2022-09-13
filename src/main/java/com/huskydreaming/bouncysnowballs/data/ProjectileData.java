package com.huskydreaming.bouncysnowballs.data;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Projectile;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public record ProjectileData(double launchVelocity, double damping, double threshold) {

    public void adjustVelocity(Projectile projectile, Location location) {
        projectile.setVelocity(location.getDirection().multiply(launchVelocity));
    }

    public Projectile updateProjectile(Projectile projectile) {

        BlockFace blockFace = getInverseFace(projectile);

        if(blockFace != null) {
            Vector velocity = projectile.getVelocity();
            double speed = velocity.length();

            speed *= damping;
            if (speed > threshold) {

                Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
                direction = direction.multiply(velocity.dot(direction)).multiply(2.0D);

                Projectile newProjectile = (Projectile) projectile.getWorld().spawnEntity(projectile.getLocation(), projectile.getType());
                newProjectile.setVelocity(velocity.subtract(direction).normalize().multiply(speed));
                newProjectile.setShooter(projectile.getShooter());

                return newProjectile;
            }
        }
        return null;
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
