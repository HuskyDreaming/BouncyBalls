package com.huskydreaming.bouncyball.services.implementations;

import com.google.common.reflect.TypeToken;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileDefault;
import com.huskydreaming.bouncyball.data.projectiles.ProjectilePhysics;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.storage.Json;
import com.huskydreaming.huskycore.utilities.Util;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectileServiceImpl implements ProjectileService {

    private NamespacedKey projectileNameSpacedKey;

    private Map<String, ProjectileData> projectileDataMap;
    private final Map<Projectile, String> projectileMap = new ConcurrentHashMap<>();

    @Override
    public void deserialize(HuskyPlugin plugin) {
        Type type = new TypeToken<Map<String, ProjectileData>>() {
        }.getType();
        projectileDataMap = Json.read(plugin, "data/projectiles", type);
        projectileNameSpacedKey = new NamespacedKey(plugin, "BOUNCY_BALL");

        if (projectileDataMap != null) return;
        projectileDataMap = new ConcurrentHashMap<>();

        for (ProjectileDefault projectileDefault : ProjectileDefault.values()) {
            if (projectileDefault == ProjectileDefault.DEFAULT) continue;
            String[] strings = projectileDefault.name().toLowerCase().split("_");
            String string = Util.capitalize(String.join("_", strings));

            projectileDataMap.put(string, projectileDefault.getProjectileData());
        }
    }

    @Override
    public void serialize(HuskyPlugin plugin) {
        Json.write(plugin, "data/projectiles", projectileDataMap);
    }

    @Override
    public void launchProjectile(Plugin plugin, Player player, ItemStack itemStack, String key) {
        ProjectileData projectileData = projectileDataMap.get(key);

        if (projectileData.getSettings().contains(ProjectileSetting.REMOVES)) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }

        Snowball projectile = player.launchProjectile(Snowball.class);
        PersistentDataContainer persistentDataContainer = projectile.getPersistentDataContainer();

        persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);
        double launchVelocity = projectileData.getPhysics(ProjectilePhysics.LAUNCH_VELOCITY);
        projectile.setVelocity(player.getLocation().getDirection().multiply(launchVelocity));
        projectile.setGlowing(projectileData.getSettings().contains(ProjectileSetting.GLOWS));
        projectile.setItem(new ItemStack(projectileData.getMaterial()));
        projectile.setInvulnerable(true);
        projectile.setShooter(player);

        projectileMap.put(projectile, key);
    }

    @Override
    public void dropProjectile(Projectile projectile) {
        ProjectileData projectileData = getDataFromProjectile(projectile);
        if (projectileData == null) return;

        if (!projectileData.getSettings().contains(ProjectileSetting.DROPS)) return;
        Location location = projectile.getLocation();

        World world = location.getWorld();
        if (world == null) return;

        ItemStack itemStack = getItemStackFromProjectile(projectile);
        if (itemStack == null) return;
        Item item = world.dropItem(location, itemStack);
        item.setGlowing(projectileData.getSettings().contains(ProjectileSetting.GLOWS));

        if (projectileData.getSettings().contains(ProjectileSetting.ITEM_NAME)) {
            item.setCustomNameVisible(true);
            item.setCustomName(getKeyFromProjectile(projectile));
        }
    }

    @Override
    public void onProjectileEnd(Projectile projectile) {
        // TODO: Projectile end effect
    }

    @Override
    public Projectile updateProjectile(Plugin plugin, Projectile projectile) {
        ProjectileData projectileData = getDataFromProjectile(projectile);
        if (projectileData == null) return null;

        BlockFace blockFace = getInverseFace(projectile);

        if (blockFace == null) return null;
        Vector velocity = projectile.getVelocity();
        double speed = velocity.length();

        speed *= projectileData.getPhysics(ProjectilePhysics.DAMPING);
        if (speed > projectileData.getPhysics(ProjectilePhysics.THRESHOLD)) {

            Vector direction = new Vector(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());
            direction = direction.multiply(velocity.dot(direction)).multiply(2.0D);

            String key = getKeyFromProjectile(projectile);

            Snowball newProjectile = (Snowball) projectile.getWorld().spawnEntity(projectile.getLocation(), projectile.getType());
            PersistentDataContainer persistentDataContainer = newProjectile.getPersistentDataContainer();
            persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);
            newProjectile.setGlowing(projectileData.getSettings().contains(ProjectileSetting.GLOWS));
            newProjectile.setItem(getItemStackFromKey(key));
            newProjectile.setVelocity(velocity.subtract(direction).normalize().multiply(speed));
            newProjectile.setShooter(projectile.getShooter());
            newProjectile.setInvulnerable(true);
            return newProjectile;
        }

        onProjectileEnd(projectile);
        dropProjectile(projectile);
        removeProjectile(projectile);
        return null;
    }

    @Override
    public String getKeyFromItemStack(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.get(projectileNameSpacedKey, PersistentDataType.STRING);
    }

    @Override
    public ItemStack getItemStackFromKey(String key) {
        ProjectileData projectileData = projectileDataMap.get(key);
        if (projectileData == null) return null;

        ItemStack itemStack = new ItemStack(projectileData.getMaterial());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;

        itemMeta.setDisplayName(ChatColor.RESET + Util.capitalize(key.replace("_", " ")));
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public ItemStack getItemStackFromProjectile(Projectile projectile) {
        ProjectileData projectileData = getDataFromProjectile(projectile);
        if (projectileData == null) return null;

        String key = getKeyFromProjectile(projectile);
        if (key == null) return null;

        ItemStack itemStack = getItemStackFromKey(key);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return null;

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(projectileNameSpacedKey, PersistentDataType.STRING, key);

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public String getKeyFromProjectile(Projectile projectile) {
        PersistentDataContainer persistentDataContainer = projectile.getPersistentDataContainer();
        return persistentDataContainer.get(projectileNameSpacedKey, PersistentDataType.STRING);
    }

    @Override
    public ProjectileData getDataFromProjectile(Projectile projectile) {
        String key = getKeyFromProjectile(projectile);
        if (key == null) return null;

        return projectileDataMap.get(key);
    }

    @Override
    public BlockFace getInverseFace(Projectile projectile) {
        Location location = projectile.getLocation();
        World world = location.getWorld();
        if (world == null) return null;

        Block block = location.getBlock();
        BlockIterator blockIterator = new BlockIterator(world, location.toVector(), projectile.getVelocity(), 0.0D, 3);

        Block previousBlock = block;
        Block nextBlock = blockIterator.next();

        while (blockIterator.hasNext() && (nextBlock.getType() == Material.AIR || nextBlock.isLiquid() || nextBlock.equals(block))) {
            previousBlock = nextBlock;
            nextBlock = blockIterator.next();
        }

        BlockFace blockFace = nextBlock.getFace(previousBlock);
        return blockFace == BlockFace.SELF ? BlockFace.UP : blockFace;
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
    public void addProjectile(String key, ProjectileData projectileData) {
        projectileDataMap.put(key, projectileData);
    }

    @Override
    public void removeProjectile(String key) {
        projectileDataMap.remove(key);
    }

    @Override
    public boolean containKey(String key) {
        return projectileDataMap.containsKey(key);
    }

    @Override
    public boolean hasProjectileData(Entity entity) {
        return entity.getPersistentDataContainer().has(projectileNameSpacedKey, PersistentDataType.STRING);
    }

    @Override
    public ProjectileData getDataFromKey(String key) {
        return projectileDataMap.get(key);
    }

    @Override
    public Map<Projectile, String> getProjectileMap() {
        return Collections.unmodifiableMap(projectileMap);
    }

    @Override
    public Map<String, ProjectileData> getProjectileDataMap() {
        return projectileDataMap;
    }
}