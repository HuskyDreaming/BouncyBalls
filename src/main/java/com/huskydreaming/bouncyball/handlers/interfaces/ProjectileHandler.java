package com.huskydreaming.bouncyball.handlers.interfaces;

import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.huskycore.handlers.interfaces.Handler;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface ProjectileHandler extends Handler {

    void launchProjectile(Plugin plugin, Player player, ItemStack itemStack, String key);

    void dropProjectile(Projectile projectile);

    void onProjectileEnd(Projectile projectile);

    void removeProjectile(Projectile projectile);

    Map<Projectile, String> getProjectileMap();

    Projectile updateProjectile(Plugin plugin, Projectile projectile);

    void addProjectile(String key, Projectile projectile);

    boolean hasProjectileData(Entity entity);

    String getKeyFromProjectile(Projectile projectile);

    ProjectileData getDataFromProjectile(Projectile projectile);

    String getKeyFromItemStack(ItemStack itemStack);

    ItemStack getItemStackFromKey(String key);

    ItemStack getItemStackFromProjectile(Projectile projectile);

    BlockFace getInverseFace(Projectile projectile);

    NamespacedKey getProjectileNameSpacedKey();

    void setCancelled(Player player);

    void setCancelled(World world);
}
