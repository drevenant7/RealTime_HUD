package com.drevenant7.realtime_hud.config;

import com.drevenant7.realtime_hud.RealTimeHud;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("config/realtimehud.json");

    private boolean enabled = true;
    private Position position = Position.TOP_LEFT;
    private int paddingX = 10;
    private int paddingY = 10;
    private boolean use24HourFormat = true;

    public Config() {
    }

    public enum Position {
        TOP_LEFT("Top Left"),
        TOP_RIGHT("Top Right"),
        BOTTOM_LEFT("Bottom Left"),
        BOTTOM_RIGHT("Bottom Right");

        private final String displayName;

        Position(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        save();
    }

    public void toggleEnabled() {
        this.enabled = !this.enabled;
        save();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        save();
    }

    public int getPaddingX() {
        return paddingX;
    }

    public void setPaddingX(int paddingX) {
        this.paddingX = Math.max(0, Math.min(500, paddingX));
        save();
    }

    public int getPaddingY() {
        return paddingY;
    }

    public void setPaddingY(int paddingY) {
        this.paddingY = Math.max(0, Math.min(500, paddingY));
        save();
    }

    public boolean is24HourFormat() {
        return use24HourFormat;
    }

    public void setUse24HourFormat(boolean use24HourFormat) {
        this.use24HourFormat = use24HourFormat;
        save();
    }

    public void toggle24HourFormat() {
        this.use24HourFormat = !this.use24HourFormat;
        save();
    }

    public String getTimeFormat() {
        return use24HourFormat ? "HH:mm:ss" : "hh:mm:ss a";
    }

    public int calculateX(MinecraftClient client, String text) {
        int screenWidth = client.getWindow().getScaledWidth();
        int textWidth = client.textRenderer.getWidth(text);

        return switch (position) {
            case TOP_LEFT, BOTTOM_LEFT -> paddingX;
            case TOP_RIGHT, BOTTOM_RIGHT -> screenWidth - textWidth - paddingX;
        };
    }

    public int calculateY(MinecraftClient client) {
        int screenHeight = client.getWindow().getScaledHeight();
        int textHeight = client.textRenderer.fontHeight;

        return switch (position) {
            case TOP_LEFT, TOP_RIGHT -> paddingY;
            case BOTTOM_LEFT, BOTTOM_RIGHT -> screenHeight - textHeight - paddingY;
        };
    }

    public void save() {
        try {
            File configDir = CONFIG_FILE.getParentFile();
            if (!configDir.exists()) {
                configDir.mkdirs();
            }

            try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
                GSON.toJson(this, writer);
            }
            RealTimeHud.LOGGER.info("Config saved successfully");
        } catch (IOException e) {
            RealTimeHud.LOGGER.error("Failed to save config", e);
        }
    }

    public void load() {
        if (!CONFIG_FILE.exists()) {
            RealTimeHud.LOGGER.info("Config file not found, creating default config");
            save();
            return;
        }

        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            Config loadedConfig = GSON.fromJson(reader, Config.class);
            if (loadedConfig != null) {
                this.enabled = loadedConfig.enabled;
                this.position = loadedConfig.position != null ? loadedConfig.position : Position.TOP_LEFT;
                this.paddingX = loadedConfig.paddingX;
                this.paddingY = loadedConfig.paddingY;
                this.use24HourFormat = loadedConfig.use24HourFormat;
                RealTimeHud.LOGGER.info("Config loaded successfully");
            }
        } catch (Exception e) {
            RealTimeHud.LOGGER.error("Failed to load config, using defaults", e);
        }
    }
}