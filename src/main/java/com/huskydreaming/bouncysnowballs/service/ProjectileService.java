package com.huskydreaming.bouncysnowballs.service;

import com.huskydreaming.bouncysnowballs.data.ProjectileData;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface ProjectileService {

    void deserialize(Plugin plugin);

    void addProjectile (Projectile projectile);

    void removeProjectile(Projectile projectile);

    void dropProjectile(Projectile projectile);

    void updateVelocity(Projectile projectile, Player player);

    Projectile updateProjectile(Projectile projectile);

    List<Projectile> getProjectiles();

    ProjectileData getData();
}
