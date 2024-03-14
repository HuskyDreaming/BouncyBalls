package com.huskydreaming.bouncyball.storage.base;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.huskydreaming.bouncyball.storage.enumeration.Extension;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Json {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static void write(Plugin plugin, String fileName, Object object) {
        Path path = check(plugin, fileName);

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            GSON.toJson(object, bufferedWriter);
            plugin.getLogger().info("Serialized " + path.getFileName() + " successfully.");
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
    }

    public static <T> T read(Plugin plugin, String fileName, Type type) {
        Path path = check(plugin, fileName);
        try {
            BufferedReader bufferedReader = Files.newBufferedReader(check(plugin, fileName), StandardCharsets.UTF_8);
            JsonReader jsonReader = new JsonReader(bufferedReader);
            T t = GSON.fromJson(jsonReader, type);
            plugin.getLogger().info("Deserialized " + path.getFileName() + " successfully.");
            return t;
        } catch (IOException e) {
            return null;
        }
    }

    public static Path check(Plugin plugin, String fileName) {
        String[] splitPath = fileName.split("/");
        String directoryPath = null;

        if (splitPath.length > 1) {
            directoryPath = splitPath[0] + File.separator;
        }

        String dataFolder = plugin.getDataFolder() + File.separator;
        Path path = Paths.get(dataFolder + fileName + Extension.JSON);
        try {
            if (!Files.exists(plugin.getDataFolder().toPath())) {
                Files.createDirectories(plugin.getDataFolder().toPath());
                plugin.getLogger().info("Creating new directory: " + plugin.getDataFolder());
            }

            if (!Files.exists(path)) {
                if(directoryPath != null) {
                    Files.createDirectories(Paths.get(dataFolder  + directoryPath));
                }
                Files.createFile(path);
                plugin.getLogger().info("Creating new file: " + path.getFileName());
            }
        } catch (IOException e) {
            plugin.getLogger().severe(e.getMessage());
        }
        return path;
    }
}