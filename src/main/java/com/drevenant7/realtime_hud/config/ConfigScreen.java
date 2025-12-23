package com.drevenant7.realtime_hud.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final Config config;

    public ConfigScreen(Screen parent, Config config) {
        super(Text.literal("RealTime HUD Configuration"));
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = 50;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 25;

        // Enable/Disable toggle
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.isEnabled())
                        .build(centerX - buttonWidth / 2, startY, buttonWidth, buttonHeight,
                                Text.literal("HUD Enabled"),
                                (button, value) -> config.setEnabled(value))
        );

        // Position cycle button
        // Position cycle button
        this.addDrawableChild(
                CyclingButtonWidget.builder((Config.Position position) -> Text.literal(position.getDisplayName()))
                        .values(Config.Position.values())
                        .initially(config.getPosition())
                        .build(centerX - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight,
                                Text.literal("Position"),
                                (button, value) -> config.setPosition(value))
        );

        // 24-hour format toggle
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.is24HourFormat())
                        .build(centerX - buttonWidth / 2, startY + spacing * 2, buttonWidth, buttonHeight,
                                Text.literal("24-Hour Format"),
                                (button, value) -> config.setUse24HourFormat(value))
        );

        // Padding X slider
        this.addDrawableChild(new SliderWidget(
                centerX - buttonWidth / 2, startY + spacing * 3, buttonWidth, buttonHeight,
                Text.literal("Horizontal Padding: " + config.getPaddingX()),
                config.getPaddingX() / 500.0
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Horizontal Padding: " + (int) (this.value * 500)));
            }

            @Override
            protected void applyValue() {
                config.setPaddingX((int) (this.value * 500));
            }
        });

        // Padding Y slider
        this.addDrawableChild(new SliderWidget(
                centerX - buttonWidth / 2, startY + spacing * 4, buttonWidth, buttonHeight,
                Text.literal("Vertical Padding: " + config.getPaddingY()),
                config.getPaddingY() / 500.0
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal("Vertical Padding: " + (int) (this.value * 500)));
            }

            @Override
            protected void applyValue() {
                config.setPaddingY((int) (this.value * 500));
            }
        });

        // Done button
        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, button -> this.close())
                        .dimensions(centerX - buttonWidth / 2, startY + spacing * 5 + 20, buttonWidth, buttonHeight)
                        .build()
        );
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}
