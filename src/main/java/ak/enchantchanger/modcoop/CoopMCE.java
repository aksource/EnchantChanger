package ak.enchantchanger.modcoop;

import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.capability.IPlayerStatusHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by A.K. on 14/08/09.
 */
public class CoopMCE {

    private static final long TERM_MONTH = 24000 * 30;
    private static final long TERM_EVENT = 1200L;

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer
                && ((EntityPlayer) event.getEntityLiving()).worldObj.getTotalWorldTime() % TERM_EVENT == 0L) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            IPlayerStatusHandler data = CapabilityPlayerStatusHandler.getPlayerStatusHandler(player);
            long startTime = data.getSoldierWorkStartTime();
            long nowTime = player.worldObj.getWorldTime();
            if (startTime == 0) {
                data.setSoldierWorkStartTime(nowTime);
            }

            if ((nowTime - startTime) / TERM_MONTH > 0) {
                data.setSoldierWorkStartTime(nowTime);
//                addSalaryToPlayer(player);
            }
        }
    }

//    public static void addSalaryToPlayer(EntityPlayer entityPlayer) {
//        IPlayerStatusHandler playerStatusHandler = CapabilityPlayerStatusHandler.getPlayerStatusHandler(entityPlayer);
//        int prevKill = playerStatusHandler.getMobKillCount();
//        int nowKill = StatCheckUtils.getTotalMobKillCount(entityPlayer);
//        int tempKill = nowKill - prevKill;
//        if (tempKill > 0) {
//            MCEconomyAPI.addPlayerMP(entityPlayer, tempKill * ConfigurationUtils.soldierSalary, false);
//            int playerX = MathHelper.ceiling_double_int(entityPlayer.posX);
//            int playerY = MathHelper.ceiling_double_int(entityPlayer.posY);
//            int playerZ = MathHelper.ceiling_double_int(entityPlayer.posZ);
//            MCEconomyAPI.playCoinSoundEffect(entityPlayer.worldObj, playerX, playerY, playerZ);
//        }
//        playerStatusHandler.setMobKillCount(nowKill);
//    }
}
