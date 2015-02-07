package ak.EnchantChanger.eventhandler;

import ak.EnchantChanger.ExtendedPlayerData;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.entity.player.EntityPlayer;

public class CommonTickHandler {
    @SubscribeEvent
    public void playerTick(PlayerTickEvent event) {
    	EntityPlayer player = event.player;
    	if(player != null) {
    		this.doLimitBreak(player);
    	}
    }

    private void doLimitBreak(EntityPlayer player) {
        int limitCount = ExtendedPlayerData.get(player).getLimitBreakCount();
        if (limitCount > 0) {
            ExtendedPlayerData.get(player).decreaseLimitBreakCount();
        }
    }
}