package com.drevenant7.realtime_hud;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealTimeHud implements ModInitializer {
    public static final String MOD_ID = "realtimehud";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("RealTime HUD initialized!");
    }
}