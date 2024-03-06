package com.huskydreaming.bouncyball.services.interfaces;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.data.ProjectileData;
import com.huskydreaming.bouncyball.services.base.ServiceInterface;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.World;

public interface InventoryService extends ServiceInterface {

    SmartInventory getBouncyBallsInventory(BouncyBallPlugin plugin);

    SmartInventory getEditInventory(BouncyBallPlugin plugin, String key);

    SmartInventory getMaterialInventory(World world, BouncyBallPlugin plugin, String key);

    SmartInventory getParticleInventory(BouncyBallPlugin plugin, String key);
}
