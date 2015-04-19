package ak.EnchantChanger.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;

/**
 * Created by A.K. on 14/08/09.
 */
public class StatCheckUtils {

    public static int getTotalMobKillCount(EntityPlayer entityPlayer) {
        if (entityPlayer instanceof EntityPlayerMP) {
            return getStaticData((EntityPlayerMP)entityPlayer, StatList.mobKillsStat);
        }
        return 0;
    }

    public static int getStaticData(EntityPlayerMP entityPlayerMP, StatBase statBase) {
        StatisticsFile statisticsFile = entityPlayerMP.func_147099_x();
        return statisticsFile.writeStat(statBase);
    }
}
