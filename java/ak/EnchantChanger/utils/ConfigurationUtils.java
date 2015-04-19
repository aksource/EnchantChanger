package ak.EnchantChanger.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.config.Configuration;

/**
 * Created by A.K. on 14/10/12.
 */
public class ConfigurationUtils {
    public static boolean enableLevelCap = true;
    public static boolean debug = false;
    public static float powerMeteor;
    public static float sizeMeteor;
    public static String[] extraSwordIDs = new String[]{};
    public static String[] extraToolIDs = new String[]{"gregtech:gt.metatool.01"};
    public static String[] extraBowIDs = new String[]{};
    public static String[] extraArmorIDs = new String[]{};
    public static String[] enchantmentLevelLimits = new String[]{
            "0:10",
            "1:10",
            "2:10",
            "3:10",
            "4:10",
            "32:0",
            "34:30",
            "303:0",
            "307:0",
            "310:0"
    };
    public static String[] enchantmentAPCoefficients = new String[]{
            "0:2",
            "1:1",
            "2:1",
            "3:1",
            "4:1",
            "5:1",
            "6:1",
            "7:1",
            "16:2",
            "17:1",
            "18:1",
            "19:1",
            "20:1",
            "21:3",
            "32:1",
            "33:1",
            "34:1",
            "35:2",
            "48:2",
            "49:1",
            "50:1",
            "51:1",
            "61:1",
            "62:1"
    };
    public static boolean enableDecMateriaLv = false;
    public static boolean flagYOUARETERRA = false;
    public static int minutesMateriaEffects = 10;
    public static int difficulty = 2;
    public static int enchantChangerCost = 5;
    public static double sizeAbsorbBox = 5D;
    //    public static int MaxLv = 127;
    public static boolean enableAPSystem = true;
    public static boolean enableDungeonLoot = true;
    public static int pointAPBase = 200;
    public static int idMakoPoison = 100;
    public static boolean enableCloudSwordDisplay = true;
    public static int cloudInvXCoord = 0;
    public static int cloudInvYCoord = 0;
    public static int soldierSalary = 10;
    public static boolean enableBackSword = true;
    public static int materiaGeneratingRatio = 256;
    public static int lifeStreamLakeRatio = 256;
    @Deprecated
    public static int idEnchantmentMeteor = 240;
    @Deprecated
    public static Enchantment enchantmentMeteor;
    @Deprecated
    public static int idEnchantmentHoly = 241;
    @Deprecated
    public static Enchantment enchantmentHoly;
    @Deprecated
    public static int idEnchantmentTelepo = 242;
    @Deprecated
    public static Enchantment enchantmentTelepo;
    @Deprecated
    public static int idEnchantmentFloat = 243;
    @Deprecated
    public static Enchantment enchantmentFloat;
    @Deprecated
    public static int idEnchantmentThunder = 244;
    @Deprecated
    public static Enchantment enchantmentThunder;

    public static void initConfig(Configuration config) {
        config.load();
        enableLevelCap = config
                .get(Configuration.CATEGORY_GENERAL,
                        "enableLevelCap",
                        enableLevelCap,
                        "TRUE:You cannot change a Materia to a enchantment over max level of the enchantment.")
                .getBoolean(enableLevelCap);
        debug = config.get(Configuration.CATEGORY_GENERAL, "debug mode", debug,
                "デバッグ用").getBoolean(debug);
        enableAPSystem = config.get(Configuration.CATEGORY_GENERAL,
                "enableAPSystem", enableAPSystem).getBoolean(enableAPSystem);
        enableDungeonLoot = config.get(Configuration.CATEGORY_GENERAL,
                "enableDungeonLoot", enableDungeonLoot).getBoolean(enableDungeonLoot);
        pointAPBase = config.get(Configuration.CATEGORY_GENERAL, "APBasePoint",
                pointAPBase).getInt();
        idMakoPoison = config.get(Configuration.CATEGORY_GENERAL, "idMakoPoison", idMakoPoison, "Mako Poison Effect Id").getInt();
        extraSwordIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra SwordIds", extraSwordIDs,
                "Put Ids which you want to operate as  swords.").getStringList();
        extraToolIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra ToolIds", extraToolIDs,
                "Put Ids which you want to operate as  tools.").getStringList();
        extraBowIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra BowIds", extraBowIDs,
                "Put Ids which you want to operate as  bows.").getStringList();
        extraArmorIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra ArmorIds", extraArmorIDs,
                "Put Ids which you want to operate as  armors.").getStringList();

        enableDecMateriaLv = config
                .get(Configuration.CATEGORY_GENERAL, "enableDecMateriaLv", enableDecMateriaLv,
                        "TRUE:The level of extracted Materia is decreased by the item damage")
                .getBoolean(enableDecMateriaLv);
        flagYOUARETERRA = config
                .get(Configuration.CATEGORY_GENERAL,
                        "flagYOUARETERRA",
                        flagYOUARETERRA,
                        "TRUE:You become Tera in FF4. It means that you can use Magic Materia when your MP is exhausted")
                .getBoolean(flagYOUARETERRA);
        powerMeteor = (float) config.get(Configuration.CATEGORY_GENERAL,
                "METEOR POWER", 10, "This is a power of Meteor").getInt();
        sizeMeteor = (float) config.get(Configuration.CATEGORY_GENERAL,
                "Meteor Size", 10, "This is a Size of Meteor").getInt();
        minutesMateriaEffects = config.get(Configuration.CATEGORY_GENERAL,
                "Materia Potion Minutes", minutesMateriaEffects,
                "How long minutes Materia put potion effect to MOB or ANIMAL")
                .getInt();
        difficulty = config.get(Configuration.CATEGORY_GENERAL, "Difficulty",
                difficulty, "Difficulty of this MOD. 0 = Easy, 1 = Normal, 2 = Hard")
                .getInt();
        idEnchantmentMeteor = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentMeteorId", idEnchantmentMeteor).getInt();
        idEnchantmentHoly = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentHolyId", idEnchantmentHoly).getInt();
        idEnchantmentTelepo = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentTelepoId", idEnchantmentTelepo).getInt();
        idEnchantmentFloat = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentFloatId", idEnchantmentFloat).getInt();
        idEnchantmentThunder = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentThunderId", idEnchantmentThunder).getInt();
        enchantmentLevelLimits = config
                .get(Configuration.CATEGORY_GENERAL, "ApSystemLevelLimit", enchantmentLevelLimits,
                        "Set Enchantmets Level Limit for AP System Format EnchantmentID:LimitLv(LimitLv = 0 > DefaultMaxLevel)")
                .getStringList();
        enchantmentAPCoefficients = config.get(Configuration.CATEGORY_GENERAL, "ApSystemCoefficientList", enchantmentAPCoefficients,
                "Set Coefficients of AP System. Format EnchantmentsID:Coefficient").getStringList();
        enableCloudSwordDisplay = config.get(Configuration.CATEGORY_GENERAL, "EnableCloudSwordDisplay", enableCloudSwordDisplay).getBoolean();
        cloudInvXCoord = config.get(Configuration.CATEGORY_GENERAL, "CloudSwordHUDxCoordinate", cloudInvXCoord).getInt();
        cloudInvYCoord = config.get(Configuration.CATEGORY_GENERAL, "CloudSwordHUDyCoordinate", cloudInvYCoord).getInt();
        enableBackSword = config.get(Configuration.CATEGORY_GENERAL, "EnableBackSword", enableBackSword, "TRUE:Render Sword on player's back. ").getBoolean();
        enchantChangerCost = config.get(Configuration.CATEGORY_GENERAL, "EnchantChangerOpenCost", enchantChangerCost, "Cost to open EnchantChanger or Materia Window when mods difficulty is hard").getInt();
        soldierSalary = config.get(Configuration.CATEGORY_GENERAL, "SoldiersSalary" , soldierSalary, "Monthly Salary of soldier.").getInt();
        materiaGeneratingRatio = config.get(Configuration.CATEGORY_GENERAL, "MateriaGeneratingRatio", materiaGeneratingRatio, "Materia generating ratio in Mako reactor").getInt();
        config.save();
    }
}
