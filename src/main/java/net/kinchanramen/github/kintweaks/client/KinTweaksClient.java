package net.kinchanramen.github.kintweaks.client;

import net.fabricmc.api.ClientModInitializer;
import net.kinchanramen.github.kintweaks.KinTweaksConfig;
import net.kinchanramen.github.kintweaks.handler.KinEventHandlers;

public class KinTweaksClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KinTweaksConfig.getInstance().load();
        KinEventHandlers.registerEventHandlers();
    }
}
