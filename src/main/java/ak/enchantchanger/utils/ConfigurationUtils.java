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
            "minecraft:protection 10",
            "minecraft:fire_protection 10",
            "minecraft:feather_falling 10",
            "minecraft:blast_protection 10",
            "minecraft:projectile_protection 10",
            "minecraft:efficiency 0",
            "minecraft:unbreaking 30"
    };
    public static String[] enchantmentAPCoefficients = new String[]{
            "minecraft:protection 2",
            "minecraft:fire_protection 1",
            "minecraft:feather_falling 1",
            "minecraft:blast_protection 1",
            "minecraft:projectile_protection 1",
            "minecraft:respiration 1",
            "minecraft:aqua_affinity 1",
            "minecraft:thorns 1",
            "minecraft:sharpness 2",
            "minecraft:smite 1",
            "minecraft:bane_of_arthropods 1",
            "minecraft:knockback 1",
            "minecraft:fire_aspect 1",
            "minecraft:looting 3",
            "minecraft:efficiency 1",
            "minecraft:silk_touch 1",
            "minecraft:unbreaking 1",
            "minecraft:fortune 2",
            "minecraft:power 2",
            "minecraft:punch 1",
            "minecraft:flame 1",
            "minecraft:infinity 1",
            "minecraft:luck_of_the_sea 1",
            "minecraft:lure 1",
            "minecraft:binding_curse 0",
            "minecraft:sweeping 2",
            "minecraft:vanishing_curse 3",
            "minecraft:depth_strider 2",
            "minecraft:frost_walker 2",
            "minecraft:mending 2"
    };
    public static boolean enableDecMateriaLv = false;
    public static boolean flagYOUARETERRA = false;
    public static int minutesMateriaEffects = 10;
    public static int difficulty = 2;
    public static int enchantChangerCost = 5;
    public static double sizeAbsorbBox = 5D;
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
    public static boolean disableFloating = false;
    public static boolean disableMeteor = false;


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
                        "Set Enchantments Level Limit for AP System Format 'ModID:EnchantmentID LimitLv'(LimitLv = 0 > DefaultMaxLevel)")
                .getStringList();
        enchantmentAPCoefficients = config.get(Configuration.CATEGORY_GENERAL, "ApSystemCoefficientList", enchantmentAPCoefficients,
                "Set Coefficients of AP System. Format 'ModID:EnchantmentsID Coefficient'").getStringList();
        enableCloudSwordDisplay = config.get(Configuration.CATEGORY_GENERAL, "EnableCloudSwordDisplay", enableCloudSwordDisplay).getBoolean();
        cloudInvXCoord = config.get(Configuration.CATEGORY_GENERAL, "CloudSwordHUDxCoordinate", cloudInvXCoord).getInt();
        cloudInvYCoord = config.get(Configuration.CATEGORY_GENERAL, "CloudSwordHUDyCoordinate", cloudInvYCoord).getInt();
        enableBackSword = config.get(Configuration.CATEGORY_GENERAL, "EnableBackSword", enableBackSword, "TRUE:Render Sword on player's back. ").getBoolean();
        enchantChangerCost = config.get(Configuration.CATEGORY_GENERAL, "EnchantChangerOpenCost", enchantChangerCost, "Cost to open enchantchanger or Materia Window when mods difficulty is hard").getInt();
        soldierSalary = config.get(Configuration.CATEGORY_GENERAL, "SoldiersSalary", soldierSalary, "Monthly Salary of soldier.").getInt();
        materiaGeneratingRatio = config.get(Configuration.CATEGORY_GENERAL, "MateriaGeneratingRatio", materiaGeneratingRatio, "Materia generating ratio in Mako reactor").getInt();
        disableFloating = config.get(Configuration.CATEGORY_GENERAL, "DisableFloatingMateria", disableFloating, "TRUE:Disable floating materia").getBoolean();
        disableMeteor = config.get(Configuration.CATEGORY_GENERAL, "DisableMeteor", disableFloating, "TRUE:Disable meteor").getBoolean();
        config.save();
    }
}
