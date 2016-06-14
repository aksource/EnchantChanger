package ak.EnchantChanger.api;

import ak.EnchantChanger.CreativeTabEC;
import net.minecraft.creativetab.CreativeTabs;

/**
 * Created by A.K. on 14/10/12.
 */
public class Constants {
    public static final String MOD_ID = "EnchantChanger";
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
    public static final String EcTextureDomain = "enchantchanger:";
    public static final String EcAssetsDomain = "enchantchanger";
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

    public static final String NBT_ATTRIBUTE_MODIFIERS_KEY = "AttributeModifiers";
    public static final String NBT_ATTRIBUTE_MODIFIERS_NAME = "Name";
    public static final String NBT_ATTRIBUTE_MODIFIERS_NAME_KEY = "AttributeName";
    public static final String NBT_ATTRIBUTE_MODIFIERS_AMOUNT = "Amount";
    public static final String NBT_ATTRIBUTE_MODIFIERS_OPERATION = "Operation";
    public static final String NBT_ATTRIBUTE_MODIFIERS_NAME_WEAPON = "Weapon modifier";
    public static final String NBT_ATTRIBUTE_MODIFIERS_UUID_MOST = "UUIDMost";
    public static final String NBT_ATTRIBUTE_MODIFIERS_UUID_LEAST = "UUIDLeast";
}
