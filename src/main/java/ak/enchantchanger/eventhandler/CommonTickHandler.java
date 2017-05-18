package ak.enchantchanger.eventhandler;

import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.capability.IPlayerStatusHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import javax.annotation.Nonnull;

public class CommonTickHandler {
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void playerTick(PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player != null) {
            this.doLimitBreak(player);
        }
    }

    private void doLimitBreak(@Nonnull EntityPlayer player) {
        IPlayerStatusHandler playerStatusHandler = CapabilityPlayerStatusHandler.getPlayerStatusHandler(player);
        int limitCount = playerStatusHandler.getLimitBreakCount();
        if (limitCount > 0) {
            playerStatusHandler.decreaseLimitBreakCount();
        }
    }
}