package com.huskydreaming.bouncyball.storage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Yaml {

    private final String name;
    private Path path;
    private FileConfiguration configuration;

    public Yaml(String name) {
        this.name = name;
    }

    public void load(Plugin plugin) {
        path = create(plugin, name);
        configuration = YamlConfiguration.loadConfiguration(path.toFile());
    }

    public void save() {
        if (configuration == null || path == null || !Files.exists(path)) return;
        try {
            configuration.save(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload(Plugin plugin) {
        configuration = YamlConfiguration.loadConfiguration(path.toFile());
        InputStream inputStream = plugin.getResource(getFileName());

        if (inputStream == null) return;

        Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        YamlConfiguration defaultConfiguration = YamlConfiguration.loadConfiguration(reader);
        configuration.setDefaults(defaultConfiguration);
    }

    public FileConfiguration getConfiguration() {
        return configuration;
    }

    private Path create(Plugin plugin, String fileName) {
        Path path = Paths.get(plugin.getDataFolder() + File.separator + fileName + Extension.YAML);
        Path parentPath = path.getParent();
        try {
            if (!Files.exists(parentPath)) {
                Files.createDirectories(parentPath);
                plugin.getLogger().info("Created new directory: " + parentPath.getFileName());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
                plugin.getLogger().info("Created new file: " + path.getFileName());
            }
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        return path;
    }

    private String getFileName() {
        return name + ".yml";
    }
}