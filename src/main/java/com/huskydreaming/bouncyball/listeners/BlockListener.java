package com.huskydreaming.bouncyball.listeners;

import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.huskycore.HuskyPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockListener implements Listener {

    private final ProjectileHandler projectileHandler;

    public BlockListener(HuskyPlugin plugin) {
        projectileHandler = plugin.provide(ProjectileHandler.class);
    }

    @EventHandler
    public void onIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.FIREBALL) {
            Entity entity = event.getIgnitingEntity();

            if (entity != null && projectileHandler.hasProjectileData(entity)) {
                event.setCancelled(true);
            }
        }
    }
}