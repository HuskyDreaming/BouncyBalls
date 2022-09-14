package com.huskydreaming.bouncysnowballs.service;

import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface ParticleService {

    void deserialize(Plugin plugin);

    void run(Plugin plugin, List<Projectile> projectiles);

}
