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

    private static final long termMonth = 20 * 60 * 20 * 30;
    private static final long termEvent = 1200L;
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.entityLiving instanceof EntityPlayer
                && ((EntityPlayer) event.entityLiving).worldObj.getTotalWorldTime() % termEvent == 0L) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            long startTime = ExtendedPlayerData.get(player).getSoldierWorkStartTime();
            long nowTime = player.worldObj.getWorldTime();
            if (startTime == 0) {
                ExtendedPlayerData.get(player).setSoldierWorkStartTime(nowTime);
            }

            if ((nowTime - startTime) / termMonth > 0) {
                addSalaryToPlayer(player);
            }
        }
    }

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
}
