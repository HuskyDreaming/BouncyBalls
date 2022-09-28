package com.huskydreaming.bouncyball.service;

import com.huskydreaming.bouncyball.data.ProjectileData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface ProjectileService {

    void deserialize(Plugin plugin);

    void removeProjectile(Projectile projectile);

    void addProjectile(String key, Projectile projectile);

    void launchProjectile(Plugin plugin, Player player, String key);

    boolean hasProjectileData(Entity entity);

    String getKeyFromProjectile(Projectile projectile);

    ProjectileData getDataFromProjectile(Projectile projectile);

    String getKeyFromItemStack(ItemStack itemStack);

    ItemStack getItemStackFromKey(String key);

    ItemStack getItemStackFromProjectile(Projectile projectile);

    void dropProjectile(Projectile projectile);

    Projectile updateProjectile(Plugin plugin, Projectile projectile);

    Map<Projectile, String> getProjectileMap();
}
