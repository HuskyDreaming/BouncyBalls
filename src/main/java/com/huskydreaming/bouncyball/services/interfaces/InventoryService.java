package com.huskydreaming.bouncyball.services.interfaces;

import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.interfaces.Service;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.World;

public interface InventoryService extends Service {

    SmartInventory getBouncyBallsInventory(HuskyPlugin plugin);

    SmartInventory getEditInventory(HuskyPlugin plugin, String key);

    SmartInventory getMaterialInventory(World world, HuskyPlugin plugin, String key);

    SmartInventory getParticleInventory(HuskyPlugin plugin, String key);

    SmartInventory getColorInventory(HuskyPlugin plugin, String key);
}
