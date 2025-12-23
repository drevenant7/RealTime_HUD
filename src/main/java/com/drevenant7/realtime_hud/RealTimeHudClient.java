package com.drevenant7.realtime_hud;

import com.drevenant7.realtime_hud.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class RealTimeHudClient implements ClientModInitializer {
    private static Config config;
    private static KeyBinding toggleKey;
    private static KeyBinding formatToggleKey;

    @Override
    public void onInitializeClient() {
        RealTimeHud.LOGGER.info("RealTime HUD Client initialized!");

        // Initialize config
        config = new Config();
        config.load();

        // Register keybindings
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.realtimehud.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.realtimehud"
        ));

        formatToggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.realtimehud.formatToggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_F,
                "category.realtimehud"
        ));

        // Register HUD rendering
        HudRenderCallback.EVENT.register(this::renderHud);

        // Register key press handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Toggle HUD on/off
            while (toggleKey.wasPressed()) {
                config.toggleEnabled();
                RealTimeHud.LOGGER.info("RealTime HUD toggled: " + config.isEnabled());
            }

            // Toggle time format (24h <-> 12h)
            while (formatToggleKey.wasPressed()) {
                config.toggle24HourFormat();
                String format = config.is24HourFormat() ? "24-hour" : "12-hour";
                RealTimeHud.LOGGER.info("Time format changed to: " + format);
            }
        });
    }

    private void renderHud(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (!config.isEnabled()) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden) {
            return;
        }

        // Get current real time
        LocalTime time = LocalTime.now();
        String timeString = time.format(DateTimeFormatter.ofPattern(config.getTimeFormat()));

        // Calculate position based on corner and padding
        int x = config.calculateX(client, timeString);
        int y = config.calculateY(client);

        // Draw the time text
        drawContext.drawTextWithShadow(client.textRenderer, timeString, x, y, 0xFFFFFF);
    }

    public static Config getConfig() {
        return config;
    }
}