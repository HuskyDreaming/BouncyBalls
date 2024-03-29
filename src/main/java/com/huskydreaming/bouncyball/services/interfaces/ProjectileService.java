package com.huskydreaming.bouncyball.services.interfaces;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.huskycore.interfaces.Service;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface ProjectileService extends Service {

    BlockFace getInverseFace(Projectile projectile);

    void removeProjectile(Projectile projectile);

    void addProjectile(String key, Projectile projectile);

    void addProjectile(String key, ProjectileData projectileData);

    void removeProjectile(String key);

    boolean containKey(String key);

    void launchProjectile(Plugin plugin, Player player, ItemStack itemStack, String key);

    boolean hasProjectileData(Entity entity);

    String getKeyFromProjectile(Projectile projectile);

    ProjectileData getDataFromProjectile(Projectile projectile);

    ProjectileData getDataFromKey(String key);

    String getKeyFromItemStack(ItemStack itemStack);

    ItemStack getItemStackFromKey(String key);

    ItemStack getItemStackFromProjectile(Projectile projectile);

    void dropProjectile(Projectile projectile);

    void onProjectileEnd(Projectile projectile);

    Projectile updateProjectile(Plugin plugin, Projectile projectile);

    Map<Projectile, String> getProjectileMap();

    Map<String, ProjectileData> getProjectileDataMap();
}