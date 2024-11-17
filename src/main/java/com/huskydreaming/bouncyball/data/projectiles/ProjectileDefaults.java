package com.huskydreaming.bouncyball.data.projectiles;

import org.bukkit.Material;

public class ProjectileDefaults {

    public static ProjectileData defaultData() {
        ProjectileData defaultData = new ProjectileData();
        defaultData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 1.0D);
        defaultData.setPhysics(ProjectilePhysics.DAMPING, 0.75D);
        defaultData.setPhysics(ProjectilePhysics.THRESHOLD, 0.8D);
        defaultData.addSetting(ProjectileSetting.DROPS);
        defaultData.addSetting(ProjectileSetting.REMOVES);
        defaultData.addSetting(ProjectileSetting.RETURNS);
        defaultData.addSetting(ProjectileSetting.ALL_BLOCKS);
        defaultData.addBlock(Material.GRASS_BLOCK);
        defaultData.addBlock(Material.SAND);
        defaultData.addBlock(Material.STONE);
        defaultData.setMaterial(Material.SNOWBALL);
        return defaultData;
    }

    public static ProjectileData snowBallData() {
        ProjectileData snowballData = new ProjectileData();
        snowballData.setMaterial(Material.SNOWBALL);
        snowballData.addSetting(ProjectileSetting.ALL_BLOCKS);
        snowballData.addSetting(ProjectileSetting.REMOVES);
        snowballData.addSetting(ProjectileSetting.RETURNS);
        snowballData.addSetting(ProjectileSetting.DROPS);
        snowballData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 1.0D);
        snowballData.setPhysics(ProjectilePhysics.THRESHOLD, 0.25D);
        snowballData.setPhysics(ProjectilePhysics.DAMPING, 0.75D);
        snowballData.addBlock(Material.GRASS_BLOCK);
        snowballData.addBlock(Material.SAND);
        snowballData.addBlock(Material.STONE);
        return snowballData;
    }

    public static ProjectileData turtleEggData() {
        ProjectileData turtleEggData = new ProjectileData();
        turtleEggData.setMaterial(Material.TURTLE_EGG);
        turtleEggData.addSetting(ProjectileSetting.ALL_BLOCKS);
        turtleEggData.addSetting(ProjectileSetting.REMOVES);
        turtleEggData.addSetting(ProjectileSetting.DROPS);
        turtleEggData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 1.0D);
        turtleEggData.setPhysics(ProjectilePhysics.THRESHOLD, 0.35D);
        turtleEggData.setPhysics(ProjectilePhysics.DAMPING, 0.75D);
        turtleEggData.addBlock(Material.GRASS_BLOCK);
        turtleEggData.addBlock(Material.SAND);
        turtleEggData.addBlock(Material.STONE);
        return turtleEggData;
    }

    public static ProjectileData hotPotatoData() {
        ProjectileData hotPotatoData = new ProjectileData();
        hotPotatoData.setMaterial(Material.BAKED_POTATO);
        hotPotatoData.addSetting(ProjectileSetting.ALL_BLOCKS);
        hotPotatoData.addSetting(ProjectileSetting.REMOVES);
        hotPotatoData.addSetting(ProjectileSetting.RETURNS);
        hotPotatoData.addSetting(ProjectileSetting.DROPS);
        hotPotatoData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 0.8D);
        hotPotatoData.setPhysics(ProjectilePhysics.THRESHOLD, 0.20D);
        hotPotatoData.setPhysics(ProjectilePhysics.DAMPING, 0.75D);
        hotPotatoData.addBlock(Material.GRASS_BLOCK);
        hotPotatoData.addBlock(Material.SAND);
        hotPotatoData.addBlock(Material.STONE);
        return hotPotatoData;
    }

    public static ProjectileData newtonsAppleData() {
        ProjectileData newtonsAppleData = new ProjectileData();
        newtonsAppleData.setMaterial(Material.APPLE);
        newtonsAppleData.addSetting(ProjectileSetting.ALL_BLOCKS);
        newtonsAppleData.addSetting(ProjectileSetting.REMOVES);
        newtonsAppleData.addSetting(ProjectileSetting.RETURNS);
        newtonsAppleData.addSetting(ProjectileSetting.DROPS);
        newtonsAppleData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 1.5D);
        newtonsAppleData.setPhysics(ProjectilePhysics.THRESHOLD, 0.15D);
        newtonsAppleData.setPhysics(ProjectilePhysics.DAMPING, 0.75D);
        newtonsAppleData.addBlock(Material.GRASS_BLOCK);
        newtonsAppleData.addBlock(Material.SAND);
        newtonsAppleData.addBlock(Material.STONE);
        return newtonsAppleData;
    }

    public static ProjectileData groovyJukeBoxData() {
        ProjectileData groovyJukeBoxData = new ProjectileData();
        groovyJukeBoxData.setMaterial(Material.JUKEBOX);
        groovyJukeBoxData.addSetting(ProjectileSetting.ALL_BLOCKS);
        groovyJukeBoxData.addSetting(ProjectileSetting.REMOVES);
        groovyJukeBoxData.addSetting(ProjectileSetting.RETURNS);
        groovyJukeBoxData.addSetting(ProjectileSetting.DROPS);
        groovyJukeBoxData.addSetting(ProjectileSetting.GLOWS);
        groovyJukeBoxData.setPhysics(ProjectilePhysics.LAUNCH_VELOCITY, 1.0D);
        groovyJukeBoxData.setPhysics(ProjectilePhysics.THRESHOLD, 0.20D);
        groovyJukeBoxData.setPhysics(ProjectilePhysics.DAMPING, 0.45D);
        groovyJukeBoxData.addBlock(Material.GRASS_BLOCK);
        groovyJukeBoxData.addBlock(Material.SAND);
        groovyJukeBoxData.addBlock(Material.STONE);
        return groovyJukeBoxData;
    }
}
