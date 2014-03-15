package ak.EnchantChanger.Client;

import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * Created by A.K. on 14/03/11.
 */
public class RenderingOverlayEvent {
    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent.Post event) {
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(EnchantChanger.potionMako)) {

        }
    }
}
