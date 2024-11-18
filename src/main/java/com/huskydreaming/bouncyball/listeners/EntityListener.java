package com.huskydreaming.bouncyball.listeners;

import com.huskydreaming.bouncyball.handlers.interfaces.ProjectileHandler;
import com.huskydreaming.huskycore.HuskyPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class EntityListener implements Listener {

    private final ProjectileHandler projectileHandler;

    public EntityListener(HuskyPlugin plugin) {
        projectileHandler = plugin.provide(ProjectileHandler.class);
    }

    @EventHandler
    public void onRemove(EntityRemoveEvent event) {
        if (event.getCause() == EntityRemoveEvent.Cause.PLUGIN) {
            Entity entity = event.getEntity();
            if (event.getEntity() instanceof Snowball snowball) {
                PersistentDataContainer dataContainer = entity.getPersistentDataContainer();
                NamespacedKey namespacedKey = projectileHandler.getProjectileNameSpacedKey();
                if (dataContainer.has(namespacedKey, PersistentDataType.STRING)) {
                    String key = dataContainer.get(namespacedKey, PersistentDataType.STRING);
                    if (key != null) projectileHandler.removeProjectile(snowball);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (event.isCancelled() && event.getEntity() instanceof Snowball snowball) {
            projectileHandler.setCancelled(snowball.getWorld());
        }
    }
}