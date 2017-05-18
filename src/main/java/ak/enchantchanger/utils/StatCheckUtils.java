package ak.enchantchanger.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;

/**
 * 統計処理用ユーティリティクラス
 * Created by A.K. on 14/08/09.
 */
public class StatCheckUtils {

    public static int getTotalMobKillCount(EntityPlayer entityPlayer) {
        if (entityPlayer instanceof EntityPlayerMP) {
            return getStaticData((EntityPlayerMP) entityPlayer, StatList.MOB_KILLS);
        }
        return 0;
    }

    public static int getStaticData(EntityPlayerMP entityPlayerMP, StatBase statBase) {
        StatisticsManagerServer statisticsFile = entityPlayerMP.getStatFile();
        return statisticsFile.readStat(statBase);
    }
}
