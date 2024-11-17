package com.huskydreaming.bouncyball.handlers.implementations;

import com.huskydreaming.bouncyball.data.particles.ParticleColor;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileData;
import com.huskydreaming.bouncyball.data.projectiles.ProjectilePhysics;
import com.huskydreaming.bouncyball.data.projectiles.ProjectileSetting;
import com.huskydreaming.bouncyball.handlers.interfaces.InventoryHandler;
import com.huskydreaming.bouncyball.inventories.*;
import com.huskydreaming.bouncyball.repositories.interfaces.ProjectileRepository;
import com.huskydreaming.huskycore.HuskyPlugin;
import com.huskydreaming.huskycore.utilities.Util;
import fr.minuskube.inv.InventoryManager;
import fr.minuskube.inv.SmartInventory;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Map;

public class InventoryHandlerImpl implements InventoryHandler {

    private InventoryManager inventoryManager;
    private ProjectileRepository projectileRepository;

    @Override
    public void postInitialize(HuskyPlugin plugin) {
        projectileRepository = plugin.provide(ProjectileRepository.class);

        inventoryManager = new InventoryManager(plugin);
        inventoryManager.init();
    }

    @Override
    public SmartInventory getBouncyBallsInventory(HuskyPlugin plugin) {
        Map<String, ProjectileData> projectileDataMap = projectileRepository.getProjectileDataMap();
        String[] keys = projectileDataMap.keySet().toArray(new String[0]);
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
        String name = Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("editInventory")
                .size(3, 9)
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
        MaterialInventory materialInventory = new MaterialInventory(plugin, key, rows, supportedMaterials, false);
        String name = Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("materialInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(materialInventory)
                .title("Materials: " + name)
                .build();
    }

    @Override
    public SmartInventory getBlockInventory(World world, HuskyPlugin plugin, String key) {
        Material[] materials = Material.values();
        Material[] materialsWithoutAir = Arrays.copyOfRange(materials, 1, materials.length);
        Material[] supportedMaterials = Arrays.stream(materialsWithoutAir)
                .filter(material -> isEnabled(world, material) && material.isItem() && material.isBlock())
                .toList()
                .toArray(new Material[0]);

        int rows = (int) Math.ceil((double) supportedMaterials.length / 9);
        MaterialInventory materialInventory = new MaterialInventory(plugin, key, rows, supportedMaterials, true);
        String name = Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("blockInventory")
                .size(Math.min(rows + 2, 5), 9)
                .provider(materialInventory)
                .title("Blocks: " + name)
                .build();
    }

    @Override
    public SmartInventory getParticleInventory(HuskyPlugin plugin, String key) {
        Particle[] particles = Particle.values();
        int rows = (int) Math.ceil((double) particles.length / 9);

        ParticleInventory particleInventory = new ParticleInventory(plugin, key, rows, particles);
        String name = Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("particleInventory")
                .size(Math.min(rows + 2, 5), 9)
                .title("Particles: " + name)
                .provider(particleInventory)
                .build();
    }

    @Override
    public SmartInventory getSettingsInventory(HuskyPlugin plugin, String key) {
        ProjectileSetting[] settings = ProjectileSetting.values();
        int rows = (int) Math.ceil((double) settings.length / 9);

        SettingsInventory settingsInventory = new SettingsInventory(plugin, key, rows, settings);
        String name = Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("settingsInventory")
                .title("Settings: " + name)
                .size(Math.min(rows + 2, 5), 9)
                .provider(settingsInventory)
                .build();
    }

    @Override
    public SmartInventory getPhysicsInventory(HuskyPlugin plugin, String key) {
        ProjectilePhysics[] physics = ProjectilePhysics.values();
        int rows = (int) Math.ceil((double) physics.length / 9);

        PhysicsInventory physicsInventory = new PhysicsInventory(plugin, key, rows, physics);
        String name = Util.capitalize(key.replace("_", " "));
        return SmartInventory.builder()
                .manager(inventoryManager)
                .id("physicsInventory")
                .title("Physics: " + name)
                .size(Math.min(rows + 2, 5), 9)
                .provider(physicsInventory)
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
                .title("Select Color")
                .size(Math.min(rows + 2, 5), 9)
                .provider(colorInventory)
                .build();
    }

    private boolean isEnabled(World world, Material material) {
        if (Util.getVersion().get(1) < 20) return true;
        return material.isEnabledByFeature(world);
    }
}