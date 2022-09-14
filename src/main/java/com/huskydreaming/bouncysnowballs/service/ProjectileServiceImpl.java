package com.huskydreaming.bouncysnowballs.service;

import com.huskydreaming.bouncysnowballs.data.ProjectileData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProjectileServiceImpl implements ProjectileService {

    private final List<Projectile> projectiles = new ArrayList<>();
    private ProjectileData projectileData;

    @Override
    public void deserialize(Plugin plugin) {
        FileConfiguration configuration = plugin.getConfig();

        projectileData = new ProjectileData(
                configuration.getStringList("Projectile.blocks"),
                configuration.getBoolean("Projectile.returns"),
                configuration.getBoolean("Projectile.drops"),
                configuration.getDouble("Projectile.launch-velocity"),
                configuration.getDouble("Projectile.damping"),
                configuration.getDouble("Projectile.threshold")
        );
    }

    @Override
    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    @Override
    public void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
    }

    @Override
    public void dropProjectile(Projectile projectile) {
        if (projectileData.drops()) {
            Location location = projectile.getLocation();
            World world = location.getWorld();
            if (world != null) {
                world.dropItem(location, new ItemStack(Material.SNOWBALL));
            }
        }
    }

    @Override
    public void updateVelocity(Projectile projectile, Player player) {
        projectile.setVelocity(player.getLocation().getDirection().multiply(projectileData.launchVelocity()));
    }


    @Override
    public Projectile updateProjectile(Projectile projectile) {
        BlockFace blockFace = getInverseFace(projectile);

        if (blockFace != null) {
            Vector velocity = projectile.getVelocity();
            double speed = velocity.length();

            speed *= projectileData.damping();
            if (speed > projectileData.threshold()) {

                Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
                direction = direction.multiply(velocity.dot(direction)).multiply(2.0D);

                Projectile newProjectile = (Projectile) projectile.getWorld().spawnEntity(projectile.getLocation(), projectile.getType());
                newProjectile.setVelocity(velocity.subtract(direction).normalize().multiply(speed));
                newProjectile.setShooter(projectile.getShooter());

                return newProjectile;
            }
            dropProjectile(projectile);
        }
        return null;
    }

    @Override
    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    @Override
    public ProjectileData getData() {
        return projectileData;
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
