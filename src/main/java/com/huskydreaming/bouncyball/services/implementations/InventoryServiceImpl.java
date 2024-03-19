package com.huskydreaming.bouncyball.services.implementations;

import com.huskydreaming.bouncyball.data.particles.ParticleColor;
import com.huskydreaming.bouncyball.inventories.*;
import com.huskydreaming.bouncyball.services.interfaces.InventoryService;
import com.huskydreaming.bouncyball.services.interfaces.ProjectileService;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.utilities.Util;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Arrays;

public class InventoryServiceImpl implements InventoryService {

    private InventoryManager inventoryManager;

    @Override
    public SmartInventory getBouncyBallsInventory(HuskyPlugin plugin) {
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
    public SmartInventory getEditInventory(HuskyPlugin plugin, String key) {
        EditInventory mainInventory = new EditInventory(plugin, key);
        String name =  Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("editInventory")
                .size(4, 9)
                .provider(mainInventory)
                .title("Editing: " + name)
                .build();
    }

    @Override
    public SmartInventory getMaterialInventory(World world, HuskyPlugin plugin, String key) {
        Material[] materials = Material.values();
        Material[] materialsWithoutAir = Arrays.copyOfRange(materials, 1, materials.length);
        Material[] supportedMaterials = Arrays.stream(materialsWithoutAir)
                .filter(material -> isEnabled(world, material) && material.isItem())
                .toList()
                .toArray(new Material[0]);

        int rows = (int) Math.ceil((double) supportedMaterials.length / 9);
        MaterialInventory mainInventory = new MaterialInventory(plugin, key, rows, supportedMaterials);
        String name =  Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("materialInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(mainInventory)
                .title("Materials: " + name)
                .build();
    }

    @Override
    public SmartInventory getParticleInventory(HuskyPlugin plugin, String key) {
        Particle[] particles = Particle.values();
        int rows = (int) Math.ceil((double) particles.length / 9);

        ParticleInventory particleInventory = new ParticleInventory(plugin, key, rows, particles);
        String name =  Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("particleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(particleInventory)
                .title("Particles: " + name)
                .build();
    }

    @Override
    public SmartInventory getColorInventory(HuskyPlugin plugin, String key) {
        ParticleColor[] particleColors = ParticleColor.values();
        int rows = (int) Math.ceil((double) particleColors.length / 9);

        ColorInventory colorInventory = new ColorInventory(plugin, key, rows, particleColors);
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("colorInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(colorInventory)
                .title("Particle Color")
                .build();
    }

    @Override
    public void deserialize(HuskyPlugin plugin) {
        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }

    private boolean isEnabled(World world, Material material) {
        if(Util.getVersion().get(1) < 20) return true;
        return material.isEnabledByFeature(world);
    }
}