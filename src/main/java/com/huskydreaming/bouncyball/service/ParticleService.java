package com.huskydreaming.bouncyball.service;

import org.bukkit.plugin.Plugin;

public interface ParticleService {

    void deserialize(Plugin plugin);

    void run(Plugin plugin);
}
