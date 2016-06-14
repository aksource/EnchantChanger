package ak.EnchantChanger.modcoop;

import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.utils.ConfigurationUtils;
import ak.EnchantChanger.utils.StatCheckUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.event.entity.living.LivingEvent;
import shift.mceconomy2.api.MCEconomyAPI;

/**
 * Created by A.K. on 14/08/09.
 */
public class CoopMCE {

    private static final long TERM_MONTH = 24000 * 30;
    private static final long TERM_EVENT = 1200L;

    public static void addSalaryToPlayer(EntityPlayer entityPlayer) {
        int prevKill = ExtendedPlayerData.get(entityPlayer).getMobKillCount();
        int nowKill = StatCheckUtils.getTotalMobKillCount(entityPlayer);
        int tempKill = nowKill - prevKill;
        if (tempKill > 0) {
            MCEconomyAPI.addPlayerMP(entityPlayer, tempKill * ConfigurationUtils.soldierSalary, false);
            int playerX = MathHelper.ceiling_double_int(entityPlayer.posX);
            int playerY = MathHelper.ceiling_double_int(entityPlayer.posY);
            int playerZ = MathHelper.ceiling_double_int(entityPlayer.posZ);
            MCEconomyAPI.playCoinSoundEffect(entityPlayer.worldObj, playerX, playerY, playerZ);
        }
        ExtendedPlayerData.get(entityPlayer).setMobKillCount(nowKill);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer
                && ((EntityPlayer) event.entityLiving).worldObj.getTotalWorldTime() % TERM_EVENT == 0L) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            ExtendedPlayerData data = ExtendedPlayerData.get(player);
            long startTime = data.getSoldierWorkStartTime();
            long nowTime = player.worldObj.getWorldTime();
            if (startTime == 0) {
                data.setSoldierWorkStartTime(nowTime);
            }

            if ((nowTime - startTime) / TERM_MONTH > 0) {
                data.setSoldierWorkStartTime(nowTime);
                addSalaryToPlayer(player);
            }
        }
    }
}
