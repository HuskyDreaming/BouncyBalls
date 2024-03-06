package com.huskydreaming.bouncyball.services.implementations;

import com.huskydreaming.bouncyball.BouncyBallPlugin;
import com.huskydreaming.bouncyball.inventories.providers.EditInventory;
import com.huskydreaming.bouncyball.inventories.providers.MainInventory;
import com.huskydreaming.bouncyball.inventories.providers.MaterialInventory;
import com.huskydreaming.bouncyball.inventories.providers.ParticleInventory;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Arrays;

public class InventoryServiceImpl implements InventoryService {

    private InventoryManager inventoryManager;

    @Override
    public SmartInventory getBouncyBallsInventory(BouncyBallPlugin plugin) {
        ProjectileService projectileService = plugin.provide(ProjectileService.class);
        String[] keys = projectileService.getProjectileDataMap().keySet().toArray(new String[0]);
        int rows = (int) Math.ceil((double) keys.length / 9);
        MainInventory mainInventory = new MainInventory(plugin, rows, keys);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("mainInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(mainInventory)
                .title("Bouncy Balls")
                .build();
    }

    @Override
    public SmartInventory getEditInventory(BouncyBallPlugin plugin, String key) {
        EditInventory mainInventory = new EditInventory(plugin, key);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("editInventory")
                .size(4, 9)
                .provider(mainInventory)
                .title("Editing: " + key)
                .build();
    }

    @Override
    public SmartInventory getMaterialInventory(World world, BouncyBallPlugin plugin, String key) {
        Material[] materials = Material.values();
        Material[] materialsWithoutAir = Arrays.copyOfRange(materials, 1, materials.length);
        Material[] supportedMaterials = Arrays.stream(materialsWithoutAir)
                .filter(material -> material.isEnabledByFeature(world) && material.isItem())
                .toList()
                .toArray(new Material[0]);

        int rows = (int) Math.ceil((double) supportedMaterials.length / 9);
        MaterialInventory mainInventory = new MaterialInventory(plugin, key, rows, supportedMaterials);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("materialInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(mainInventory)
                .title("Editing Materials: " + key)
                .build();
    }

    @Override
    public SmartInventory getParticleInventory(BouncyBallPlugin plugin, String key) {
        Particle[] particles = Particle.values();
        int rows = (int) Math.ceil((double) particles.length / 9);

        ParticleInventory particleInventory = new ParticleInventory(plugin, key, rows, particles);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("particleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(particleInventory)
                .title("Editing Particles: " + key)
                .build();
    }

    @Override
    public void deserialize(BouncyBallPlugin plugin) {
        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }
}