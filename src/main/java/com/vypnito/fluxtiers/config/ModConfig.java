package com.vypnito.fluxtiers.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/fluxtiers.json");

    private String apiUrl = "http://104.194.9.117:25319/api/v1/tiers";
    private int cacheExpiry = 300;
    private boolean showUnverifiedMessage = true;
    private String discordLink = "fluxsmp.fun";

    public static ModConfig load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, ModConfig.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ModConfig config = new ModConfig();
        config.save();
        return config;
    }

    public void save() {
        try {
            CONFIG_FILE.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public int getCacheExpiry() {
        return cacheExpiry;
    }

    public boolean isShowUnverifiedMessage() {
        return showUnverifiedMessage;
    }

    public String getDiscordLink() {
        return discordLink;
    }
}
