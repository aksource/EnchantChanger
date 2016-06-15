package ak.EnchantChanger.api;

import ak.EnchantChanger.CreativeTabEC;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.b3d.B3DLoader;

/**
 * 定数クラス
 * Created by A.K. on 14/10/12.
 */
public class Constants {
    public static final String MOD_ID = "EnchantChanger";
    public static final String MOD_NAME = "EnchantChanger";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_MC_VERSION = "[1.8,1.8.9]";
    public static final String OBJ_ITEM_DOMAIN = ":item/";
    public static final String EcMeteorPNG = "textures/items/Meteo.png";
    public static final String EcExpBottlePNG = "textures/items/ExExpBottle.png";
    public static final String EcZackSwordPNG = "textures/item/ZackSword.png";
    public static final String EcSephirothSwordPNG = "textures/item/SephirothSword.png";
    public static final String EcCloudSword2PNG = "textures/item/CloudSword-3Dtrue.png";
    public static final String EcCloudSwordCore2PNG = "textures/item/CloudSwordCore-3Dtrue.png";
    public static final String EcUltimateWeaponPNG = "textures/item/UltimaWeapon.png";
    public static final String EcGuiMaterializer = "textures/gui/materializer.png";
    public static final String EcGuiHuge = "textures/gui/HugeMateriaContainer.png";
    public static final String EcGuiMako = "textures/gui/MakoReactorContainer.png";
    public static final String EcGuiMateriaWindow = "textures/gui/MaterializingContainer.png";
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
    public static final ResourceLocation ITEM_BUSTER_SWORD_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/bustersword-united.b3d", "Sword");
    public static final ResourceLocation ITEM_UNION_SWORD_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/unionsword-united.b3d", "centerplate");
    public static final ResourceLocation ITEM_FIRST_SWORD_CLOSED_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/firstsword-closed-united.b3d", "centerplate");
    public static final ResourceLocation ITEM_FIRST_SWORD_OPEN_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/firstsword-open-united.b3d", "centerplate");
    public static final ResourceLocation ITEM_MASAMUNE_BLADE_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/masamune-united.b3d", "sword");
    public static final ResourceLocation ITEM_MASAMUNE_BLADE_SCABBARD_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/masamune-scabbard-united.b3d", "sword");
    public static final ResourceLocation ITEM_ULTIMATE_WEAPON_MODEL_RL = new B3DLoader.B3DMeshLocation(MOD_ID, "models/item/ultimateweapon-united.b3d", "sword");
    public static final ResourceLocation ITEM_MATERIA_MODEL_RL = new ResourceLocation(MOD_ID, "item/spheresmall.b3d");
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
            "EnchantChanger");
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

}
