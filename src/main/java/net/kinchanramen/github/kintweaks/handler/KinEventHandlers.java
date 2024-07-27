package net.kinchanramen.github.kintweaks.handler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.kinchanramen.github.kintweaks.KinTweaks;
import net.kinchanramen.github.kintweaks.features.KinTweaksFeatures;
import net.kinchanramen.github.kintweaks.features.KinTypeFeatures;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public class KinEventHandlers {
    private static int colorIndex=0;
    private static final int[] colors=new int[]{
            0xFF0000, // 赤
            0x00FF00, // 緑
            0x0000FF, // 青
            0xFFFF00  // 黄
    };
    public static void registerEventHandlers(){
        ClientTickEvents.END_CLIENT_TICK.register(KinEventHandlers::handleClientTick);
    }
    private static void handleClientTick(MinecraftClient client) {
        if (KinTweaksFeatures.TWEAK_TOTEM.getValueFromConfig()) {
            if (client.player != null && client.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                colorIndex = (colorIndex + 1) % colors.length;
                int color = colors[colorIndex];
                Text message = Text.literal("Totem!").styled(style -> style.withColor(color));

                String displayType = KinTypeFeatures.DISPLAY_TYPE.getValueFromConfig();
                if ("ACTION_BAR".equals(displayType)) {
                    client.inGameHud.setOverlayMessage(message, true);
                } else if ("CHAT".equals(displayType)) {
                    client.player.sendMessage(message, false);
                } else if ("TITLE".equals(displayType)) {
                    Text message2=Text.literal("Totem!").styled(style -> style.withColor(color));
                    client.inGameHud.setTitle(message2);
                }
            }
        }
    }
}
