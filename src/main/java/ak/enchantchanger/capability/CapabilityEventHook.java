package ak.enchantchanger.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * キャパビリティーアタッチイベント
 * Created by A.K. on 2017/04/18.
 */
public class CapabilityEventHook {
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onAttach(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(CapabilityPlayerStatusHandler.PLAYER_STATUS, new PlayerStatusHandlerImpl());
        }
    }
}
