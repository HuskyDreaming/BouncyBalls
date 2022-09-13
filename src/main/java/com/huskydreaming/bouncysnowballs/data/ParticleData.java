package com.huskydreaming.bouncysnowballs.data;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

public record ParticleData(Particle particle, int count) {

    public void spawn(Location location) {
        World world = location.getWorld();
        if(world != null) {
            world.spawnParticle(particle, location, count);
        }
    }
}
