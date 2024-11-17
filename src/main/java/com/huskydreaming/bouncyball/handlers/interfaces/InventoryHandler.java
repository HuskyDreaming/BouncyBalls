package com.huskydreaming.bouncyball.handlers.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.handlers.interfaces.Handler;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.World;

public interface InventoryHandler extends Handler {
    SmartInventory getBouncyBallsInventory(HuskyPlugin plugin);

    SmartInventory getEditInventory(HuskyPlugin plugin, String key);

    SmartInventory getMaterialInventory(World world, HuskyPlugin plugin, String key);

    SmartInventory getBlockInventory(World world, HuskyPlugin plugin, String key);

    SmartInventory getParticleInventory(HuskyPlugin plugin, String key);

    SmartInventory getSettingsInventory(HuskyPlugin plugin, String key);

    SmartInventory getPhysicsInventory(HuskyPlugin plugin, String key);

    SmartInventory getColorInventory(HuskyPlugin plugin, String key);
}
