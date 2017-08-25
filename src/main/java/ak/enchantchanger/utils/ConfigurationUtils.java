package ak.enchantchanger.utils;

import net.minecraftforge.common.config.Configuration;

/**
 * 設定系のユーティリティクラス
 * Created by A.K. on 14/10/12.
 */
public class ConfigurationUtils {
    public static boolean enableLevelCap = true;
    public static boolean debug = false;
    public static float powerMeteor;
    public static float sizeMeteor;
    public static String[] enchantmentLevelLimits = new String[]{
            "protection:10",
            "fire_protection:10",
            "feather_falling:10",
            "blast_protection:10",
            "projectile_protection:10",
            "efficiency:0",
            "unbreaking:30"
    };
    public static String[] enchantmentAPCoefficients = new String[]{
            "protection:2",
            "fire_protection:1",
            "feather_falling:1",
            "blast_protection:1",
            "projectile_protection:1",
            "respiration:1",
            "aqua_affinity:1",
            "thorns:1",
            "sharpness:2",
            "smite:1",
            "bane_of_arthropods:1",
            "knockback:1",
            "fire_aspect:1",
            "looting:3",
            "efficiency:1",
            "silk_touch:1",
            "unbreaking:1",
            "fortune:2",
            "power:2",
            "punch:1",
            "flame:1",
            "infinity:1",
            "luck_of_the_sea:1",
            "lure:1",
            "mending:1",
            "depth_strider:1",
            "frost_walker:1"
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
    public static boolean enableCloudSwordDisplay = true;
    public static int cloudInvXCoord = 0;
    public static int cloudInvYCoord = 0;
    public static int soldierSalary = 10;
    public static boolean enableBackSword = true;
    public static int materiaGeneratingRatio = 256;
    public static int lifeStreamLakeRatio = 256;


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
        enchantChangerCost = config.get(Configuration.CATEGORY_GENERAL, "EnchantChangerOpenCost", enchantChangerCost, "Cost to open enchantchanger or Materia Window when mods difficulty is hard").getInt();
        soldierSalary = config.get(Configuration.CATEGORY_GENERAL, "SoldiersSalary", soldierSalary, "Monthly Salary of soldier.").getInt();
        materiaGeneratingRatio = config.get(Configuration.CATEGORY_GENERAL, "MateriaGeneratingRatio", materiaGeneratingRatio, "Materia generating ratio in Mako reactor").getInt();
        config.save();
    }
}
