package com.huskydreaming.bouncyball.service;

import com.huskydreaming.bouncyball.data.ProjectileData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class ProjectileServiceImpl implements ProjectileService {

    private NamespacedKey projectileNameSpacedKey;

    private final Map<String, ProjectileData> projectileDataMap = new HashMap<>();
    private final Map<Projectile, String> projectileMap = new HashMap<>();

    @Override
    public void deserialize(Plugin plugin) {
        projectileNameSpacedKey = new NamespacedKey(plugin, "BOUNCY_BALL");

        FileConfiguration configuration = plugin.getConfig();

        ConfigurationSection section = configuration.getConfigurationSection("");
        if(section != null) {
            for (String key : section.getKeys(false)) {
                String path = key + ".projectile";
                ProjectileData projectileData = new ProjectileData(
                        configuration.getStringList(path + ".blocks"),
                        configuration.getString(path + ".item"),
                        configuration.getBoolean(path + ".returns"),
                        configuration.getBoolean(path + ".drops"),
                        configuration.getBoolean(path + ".removes"),
                        configuration.getDouble(path + ".launch-velocity"),
                        configuration.getDouble(path + ".damping"),
                        configuration.getDouble(path + ".threshold")
                );

                projectileDataMap.put(key, projectileData);
            }
        }
    }

    @Override
    public void removeProjectile(Projectile projectile) {
        projectileMap.remove(projectile);
    }

    @Override
    public void addProjectile(String key, Projectile projectile) {
        projectileMap.put(projectile, key);
    }

    @Override
    public void launchProjectile(Plugin plugin, Player player, ItemStack itemStack, String key) {
        ProjectileData projectileData = projectileDataMap.get(key);

        if(projectileData.removes()) itemStack.setAmount(itemStack.getAmount() - 1);

        Snowball projectile = player.launchProjectile(Snowball.class);
        PersistentDataContainer persistentDataContainer = projectile.getPersistentDataContainer();

        persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);
        projectile.setVelocity(player.getLocation().getDirection().multiply(projectileData.launchVelocity()));
        projectile.setItem(new ItemStack(Material.valueOf(projectileData.item())));

        projectileMap.put(projectile, key);
    }

    @Override
    public boolean hasProjectileData(Entity entity) {
        return entity.getPersistentDataContainer().has(projectileNameSpacedKey, PersistentDataType.STRING);
    }

    @Override
    public String getKeyFromProjectile(Projectile projectile) {
        PersistentDataContainer persistentDataContainer = projectile.getPersistentDataContainer();
        return persistentDataContainer.get(projectileNameSpacedKey, PersistentDataType.STRING);
    }

    @Override
    public ProjectileData getDataFromProjectile(Projectile projectile) {
        String key = getKeyFromProjectile(projectile);
        if(key == null) return null;

        return projectileDataMap.get(key);
    }

    @Override
    public String getKeyFromItemStack(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.get(projectileNameSpacedKey, PersistentDataType.STRING);
    }

    @Override
    public ItemStack getItemStackFromKey(String key) {
        ProjectileData projectileData = projectileDataMap.get(key);
        if(projectileData == null) return null;

        ItemStack itemStack = new ItemStack(Material.valueOf(projectileData.item()));
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public ItemStack getItemStackFromProjectile(Projectile projectile) {
        ProjectileData projectileData = getDataFromProjectile(projectile);
        if(projectileData == null) return null;

        ItemStack itemStack = new ItemStack(Material.valueOf(projectileData.item()));

        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta == null) return null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String key = getKeyFromProjectile(projectile);
        if(key == null) return null;

        persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void dropProjectile(Projectile projectile) {
        ProjectileData projectileData = getDataFromProjectile(projectile);
        if(projectileData == null) return;

        if(projectileData.drops()) {

            Location location = projectile.getLocation();
            World world = location.getWorld();

            if (world != null) {
                ItemStack itemStack = getItemStackFromProjectile(projectile);
                if(itemStack != null) world.dropItem(location, itemStack);
            }
        }
    }

    @Override
    public Projectile updateProjectile(Plugin plugin, Projectile projectile) {
        ProjectileData projectileData = getDataFromProjectile(projectile);
        if(projectileData == null) return null;

        BlockFace blockFace = getInverseFace(projectile);

        if (blockFace != null) {
            Vector velocity = projectile.getVelocity();
            double speed = velocity.length();

            speed *= projectileData.damping();
            if (speed > projectileData.threshold()) {

                Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
                direction = direction.multiply(velocity.dot(direction)).multiply(2.0D);

                String key = getKeyFromProjectile(projectile);

                Snowball newProjectile = (Snowball) projectile.getWorld().spawnEntity(projectile.getLocation(), projectile.getType());
                PersistentDataContainer persistentDataContainer = newProjectile.getPersistentDataContainer();
                persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);
                newProjectile.setItem(new ItemStack(Material.valueOf(projectileData.item())));
                newProjectile.setVelocity(velocity.subtract(direction).normalize().multiply(speed));
                newProjectile.setShooter(projectile.getShooter());
                newProjectile.setInvulnerable(true);
                return newProjectile;
            }

            dropProjectile(projectile);
            removeProjectile(projectile);
        }
        return null;
    }

    @Override
    public Map<Projectile, String> getProjectileMap() {
        return projectileMap;
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
