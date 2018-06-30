package ak.enchantchanger.api;

import ak.enchantchanger.CreativeTabEC;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

/**
 * 定数クラス
 * Created by A.K. on 14/10/12.
 */
public class Constants {
    public static final String MOD_ID = "enchantchanger";
    public static final String MOD_NAME = "EnchantChanger";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_MC_VERSION = "[1.11,1.99.99]";
    public static final String OBJ_ITEM_DOMAIN = ":item/";
    public static final String TEXTURES_METEO_PNG = "textures/items/meteo.png";
    public static final String TEXTURES_EX_EXP_BOTTLE_PNG = "textures/items/ex_exp_bottle.png";
    public static final String EcZackSwordPNG = "textures/item/zack_sword.png";
    public static final String EcSephirothSwordPNG = "textures/item/sephiroth_sword.png";
    public static final String EcCloudSword2PNG = "textures/item/cloud_sword_3d_true.png";
    public static final String EcCloudSwordCore2PNG = "textures/item/cloud_sword_core_3d_true.png";
    public static final String EcUltimateWeaponPNG = "textures/item/ultima_weapon.png";
    public static final String TEXTURES_GUI_MATERIALIZER_PNG = "textures/gui/materializer.png";
    public static final String TEXTURES_GUI_HUGE_MATERIA = "textures/gui/huge_materia_container.png";
    public static final String EcGuiMako = "textures/gui/mako_reactor_container.png";
    public static final String TEXTURES_GUI_MATERIALIZING = "textures/gui/materializing_container.png";
    public static final String EcPotionEffect = "textures/gui/potioneffect.png";
    public static final String EcHugetex = "textures/item/hugemateriatex.png";
    public static final String FLUID_LIFESTREAM_STILL = "blocks/lifestream_still";
    public static final ResourceLocation LIFESTREAM_STILL_RL = new ResourceLocation(MOD_ID, FLUID_LIFESTREAM_STILL);
    public static final String FLUID_LIFESTREAM_FLOW = "blocks/lifestream_flow";
    public static final ResourceLocation LIFESTREAM_FLOW_RL = new ResourceLocation(MOD_ID, FLUID_LIFESTREAM_FLOW);
    public static final ResourceLocation MAKO_REACTOR_FRONT_RL = new ResourceLocation(MOD_ID, "blocks/makoreactor-front");
    public static final ResourceLocation MAKO_REACTOR_SIDE_RL = new ResourceLocation(MOD_ID, "blocks/makoreactor-side");
    public static final String EcTextureDomain = "enchantchanger:";
    public static final String EcAssetsDomain = "enchantchanger";
    public static final String MODEL_TYPE_INVENTORY = "inventory";
    public static final String MODEL_TYPE_FLUID = "fluid";
    public static final String MODEL_TYPE_GAS = "gas";
    public static final ResourceLocation ITEM_BUSTER_SWORD_MODEL_RL = new ResourceLocation(MOD_ID, "item/bustersword.obj");
    public static final ResourceLocation ITEM_UNION_SWORD_MODEL_RL = new ResourceLocation(MOD_ID, "item/unionsword.obj");
    public static final ResourceLocation ITEM_FIRST_SWORD_CLOSED_MODEL_RL = new ResourceLocation(MOD_ID, "item/firstsword-closed-united.obj");
    public static final ResourceLocation ITEM_FIRST_SWORD_OPEN_MODEL_RL = new ResourceLocation(MOD_ID, "item/firstsword-open-united.obj");
    public static final ResourceLocation ITEM_MASAMUNE_BLADE_MODEL_RL = new ResourceLocation(MOD_ID, "item/masamune-noscabbard-united.obj");
    public static final ResourceLocation ITEM_MASAMUNE_BLADE_SCABBARD_MODEL_RL = new ResourceLocation(MOD_ID, "item/masamune-scabbard-united.obj");
    public static final ResourceLocation ITEM_ULTIMATE_WEAPON_MODEL_RL = new ResourceLocation(MOD_ID, "item/ultimateweapon.obj");
    public static final ResourceLocation ITEM_MATERIA_MODEL_RL = new ResourceLocation(MOD_ID, "item/spherell.obj");
    public static final String TEXTURE_NAME_ULTIMATE_EMBLEM = "ultimateweapon256-emblem";
    public static final String TEXTURE_NAME_ULTIMATE_GRIP = "ultimateweapon256-grip";
    public static final String TEXTURE_NAME_ULTIMATE_HAND = "ultimateweapon256-handguard";
    public static final String TEXTURE_NAME_ULTIMATE_PIPE_01 = "ultimateweapon256-pipe01";
    public static final String TEXTURE_NAME_ULTIMATE_PIPE_02 = "ultimateweapon256-pipe02";
    public static final String TEXTURE_NAME_ULTIMATE_SWORD = "ultimateweapon256-sword";
    public static final String TEXTURE_NAME_BUSTER_EDGE = "bustersword256-edge";
    public static final String TEXTURE_NAME_BUSTER_BOX = "bustersword256-box";
    public static final String TEXTURE_NAME_BUSTER_CYLINDER = "bustersword256-cylinder";
    public static final String TEXTURE_NAME_BUTTERFLY_EDGE = "butterflyedge256-edge";
    public static final String TEXTURE_NAME_BUTTERFLY_GRIP = "butterflyedge256-grip";
    public static final String TEXTURE_NAME_FIRST_CASE = "firstsword256-case";
    public static final String TEXTURE_NAME_FIRST_CENTER = "firstsword256-center";
    public static final String TEXTURE_NAME_FIRST_EDGE = "firstsword256-edge";
    public static final String TEXTURE_NAME_FIRST_GRIP = "firstsword256-grip";
    public static final String TEXTURE_NAME_MASAMUNE_GRIP = "masamune256-grip";
    public static final String TEXTURE_NAME_MASAMUNE_SWORD = "masamune256-sword";
    public static final String TEXTURE_NAME_MASAMUNE_SCABBARD = "masamune16-scabbard";
    public static final String TEXTURE_NAME_ORGANIX_EDGE = "organix256-edge";
    public static final String TEXTURE_NAME_ORGANIX_GRIP = "organix256-grip";
    public static final String TEXTURE_NAME_RUNE_EDGE = "runeblade256-edge";
    public static final String TEXTURE_NAME_RUNE_GRIP = "runeblade256-grip";
    public static final String TEXTURE_NAME_RUNE_HAND = "runeblade256-hand";


    public static final byte MagicKEY = 0;
    public static final byte MateriaKEY = 1;
    public static final byte CtrlKEY = 2;

    public static final int GUI_ID_MATERIALIZER = 0;
    public static final int GUI_ID_PORTABLE_ENCHANTMENT_TABLE = 1;
    public static final int GUI_ID_HUGE_MATERIA = 2;
    public static final int GUI_ID_MATERIA_WINDOW = 3;
    public static final int GUI_ID_MAKO_REACTOR = 4;
    public static final CreativeTabs TAB_ENCHANT_CHANGER = new CreativeTabEC(
            "enchantchanger");
    public static final int LIMIT_GAUGE_MIN = 0;
    public static final int LIMIT_GAUGE_MAX = 100;
    public static final int[] LIMIT_GAUGE_MAX_ARRAY = {100, 100, 150, 150, 200, 200, 250};
    public static final int LIMIT_BREAK_TIME = 20 * 15;
    public static final byte LIMIT_BREAK_OMNISLASH_FIRST = 0;
    public static final byte LIMIT_BREAK_POWER_UP = 1;

    public static final byte MAGIC_ID_METEOR = 1;
    public static final byte MAGIC_ID_HOLY = 2;
    public static final byte MAGIC_ID_TELEPO = 3;
    public static final byte MAGIC_ID_FLOAT = 4;
    public static final byte MAGIC_ID_THUNDER = 5;
    public static final byte MAGIC_ID_DESPELL = 6;
    public static final byte MAGIC_ID_HASTE = 7;
    public static final byte MAGIC_ID_ABSORPTION = 8;

    public static final String BATTOUKEN_MOD_ID = "battouken";

    public static final int FIELD_INDEX_MINECRAFT_TIMER = 20;
    public static final String NBT_ENCHANTMENT = "ench";
    public static final String NBT_KEY_ENCHANT_ID = "id";
    public static final String NBT_KEY_ENCHANT_LEVEL = "lvl";
    public static final String NBT_KEY_AP = "ap";
    public static final String NBT_KEY_AP_LIST = "ap_list";
    public static final String NBT_IS_LEVITATING = "is_levitating";
    public static final String NBT_SOLDIER_MODE = "soldier_mode";
    public static final String NBT_AP_COOLING_TIME = "ap_cooling_time";
    public static final String NBT_LIMIT_VALUE = "limit_value";
    public static final String NBT_SOLDIER_WORK_START_TIME = "soldier_work_start_time";
    public static final String NBT_MOB_KILL_COUNT = "mob_kill_count";
    public static final String NBT_LIMIT_BREAK_COUNT = "limit_break_count";
    public static final String NBT_LIMIT_BREAK_ID = "limit_break_id";
    public static final String NBT_GG_MODE = "gg_mode";
    public static final String NBT_INITIALIZE = "initialized";
    public static final String NBT_ENABLE_LEVITATION = "enable_levitation";
    public static final String CAP_KEY_PLAYER_STATUS = "player_status";
    public static final String NBT_KEY_ENCHANT_CHANGER_MAGIC = "enchant_changer|magic";
    public static final String NBT_STORED_ENCHANTMENTS = "StoredEnchantments";
    public static final String MOD_ID_MULTITOOLHOLDERS = "multitoolholders";
    public static final String MOD_ID_MCECONOMY_2 = "mceconomy2";
    public static final String MOD_ID_SEXTIARYSECTOR = "sextiarysector";
    public static final String MOD_ID_COFHCORE = "cofhcore";
    public static final String NBT_ENCHANT_CHANGER_ACTIVEMODE = "enchant_changer|activemode";
    public static final String KEY_ENCHANTCHANGER_CLOUDSWORDCORE_MODE_ACTIVE = "enchantchanger.cloudswordcore.mode.active";
    public static final String KEY_ENCHANTCHANGER_CLOUDSWORDCORE_MODE_INACTIVE = "enchantchanger.cloudswordcore.mode.inactive";
    public static final String NBT_REACTOR_FACE = "face";
    public static final String NBT_REACTOR_SMELTING_TIME = "smelting_time";
    public static final String NBT_REACTOR_CREATE_HUGE_MATERIA = "create_huge_materia";
    public static final String NBT_ITEMS = "Items";
    public static final String NBT_SLOT = "Slot";
    public static final String NBT_REACTOR_SMELTING_ITEMS = "SmeltingItems";
    public static final String NBT_REACTOR_MAKO_TANK = "mako_tank";
    public static final String NBT_REACTOR_HMCOORDX = "hmcoordx";
    public static final String NBT_REACTOR_HMCOORDY = "hmcoordy";
    public static final String NBT_REACTOR_HMCOORDZ = "hmcoordz";
    public static final String NBT_REACTOR_OUTPUT_MAX_RF_VALUE = "output_max_rf_value";
    public static final String NBT_REACTOR_STORED_RF_ENERGY = "stored_rf_energy";
    public static final String NBT_REACTOR_GENERATING_RF_TIME = "generating_rf_time";
    public static final String NBT_REACTOR_NOW_RF = "now_rf";
    public static final String REG_BLOCK_ENCHANTCHANGER = "enchant_changer";
    public static final String REG_BLOCK_HUGE_MATERIA = "block_huge_materia";
    public static final String REG_FLUID_LIFE_STREAM = "life_stream";
    public static final String REG_BLOCK_LIFE_STREAM = "life_stream";
    public static final String REG_BLOCK_MAKO_REACTOR = "mako_reactor";

    public static final ResourceLocation runebladeGrip = new ResourceLocation(MOD_ID, "textures/item/runeblade256-grip.png");
    public static final ResourceLocation runebladeHand = new ResourceLocation(MOD_ID, "textures/item/runeblade256-hand.png");
    public static final ResourceLocation runebladeEdge = new ResourceLocation(MOD_ID, "textures/item/runeblade256-edge.png");
    public static final ResourceLocation butterflyedgeGrip = new ResourceLocation(MOD_ID, "textures/item/butterflyedge256-grip.png");
    public static final ResourceLocation butterflyedgeEdge = new ResourceLocation(MOD_ID, "textures/item/butterflyedge256-edge.png");
    public static final ResourceLocation organixGrip = new ResourceLocation(MOD_ID, "textures/item/organix256-grip.png");
    public static final ResourceLocation organixEdge = new ResourceLocation(MOD_ID, "textures/item/organix256-edge.png");
    public static final ResourceLocation unionSwordObj = new ResourceLocation(MOD_ID, "models/item/unionsword.obj");
    public static final ResourceLocation firstSwordGrip = new ResourceLocation(MOD_ID, "textures/item/firstsword256-grip.png");
    public static final ResourceLocation firstSwordCase = new ResourceLocation(MOD_ID, "textures/item/firstsword256-case.png");
    public static final ResourceLocation firstSwordCenter = new ResourceLocation(MOD_ID, "textures/item/firstsword256-center.png");
    public static final ResourceLocation firstSwordEdge = new ResourceLocation(MOD_ID, "textures/item/firstsword256-edge.png");
    public static final ResourceLocation firstSwordObj = new ResourceLocation(MOD_ID, "models/item/firstsword.obj");
    public static final ResourceLocation masamuneGrip = new ResourceLocation(MOD_ID, "textures/item/masamune256-grip.png");
    public static final ResourceLocation masamuneSword = new ResourceLocation(MOD_ID, "textures/item/masamune256-sword.png");
    public static final ResourceLocation masamuneObj = new ResourceLocation(MOD_ID, "models/item/masamune.obj");
    public static final ResourceLocation ultimateWeaponPipe02 = new ResourceLocation(MOD_ID, "textures/item/ultimateweapon256-pipe02.png");
    public static final ResourceLocation ultimateWeaponPipe01 = new ResourceLocation(MOD_ID, "textures/item/ultimateweapon256-pipe01.png");
    public static final ResourceLocation ultimateWeaponGrip = new ResourceLocation(MOD_ID, "textures/item/ultimateweapon256-grip.png");
    public static final ResourceLocation ultimateWeaponHandguard = new ResourceLocation(MOD_ID, "textures/item/ultimateweapon256-handguard.png");
    public static final ResourceLocation ultimateWeaponEmblem = new ResourceLocation(MOD_ID, "textures/item/ultimateweapon256-emblem.png");
    public static final ResourceLocation ultimateWeaponSword = new ResourceLocation(MOD_ID, "textures/item/ultimateweapon256-sword.png");
    public static final ResourceLocation ultimateWeaponObj = new ResourceLocation(MOD_ID, "models/item/ultimateweapon.obj");
    public static final ResourceLocation zackSwordCylinder = new ResourceLocation(MOD_ID, "textures/item/bustersword256-cylinder.png");
    public static final ResourceLocation zackSwordBox = new ResourceLocation(MOD_ID, "textures/item/bustersword256-box.png");
    public static final ResourceLocation zackSwordEdge = new ResourceLocation(MOD_ID, "textures/item/bustersword256-edge.png");
    public static final ResourceLocation zackSwordObj = new ResourceLocation(MOD_ID, "models/item/bustersword.obj");

}
