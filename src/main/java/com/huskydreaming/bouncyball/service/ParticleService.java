package com.huskydreaming.bouncyball.service;

import org.bukkit.entity.Projectile;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public interface ParticleService {

    void deserialize(Plugin plugin);

    void run(Plugin plugin, Map<Projectile, String> projectiles);
}
