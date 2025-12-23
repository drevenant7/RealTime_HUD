package com.drevenant7.realtime_hud.config;

import com.drevenant7.realtime_hud.RealTimeHudClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(parent, RealTimeHudClient.getConfig());
    }
}