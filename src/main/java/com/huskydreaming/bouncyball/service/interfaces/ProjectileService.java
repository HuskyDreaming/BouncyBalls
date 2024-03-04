package com.huskydreaming.bouncyball.service.interfaces;

import com.huskydreaming.bouncyball.data.ProjectileData;
import com.huskydreaming.bouncyball.service.base.ServiceInterface;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface ProjectileService extends ServiceInterface {

    void removeProjectile(Projectile projectile);

    void addProjectile(String key, Projectile projectile);

    void launchProjectile(Plugin plugin, Player player, ItemStack itemStack, String key);

    boolean hasProjectileData(Entity entity);

    String getKeyFromProjectile(Projectile projectile);

    ProjectileData getDataFromProjectile(Projectile projectile);

    String getKeyFromItemStack(ItemStack itemStack);

    ItemStack getItemStackFromKey(String key);

    ItemStack getItemStackFromProjectile(Projectile projectile);

    void dropProjectile(Projectile projectile);

    void onProjectileEnd(Projectile projectile);

    Projectile updateProjectile(Plugin plugin, Projectile projectile);

    Map<Projectile, String> getProjectileMap();

    Map<String, ProjectileData> getProjectileDataMap();
}