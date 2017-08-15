package ak.enchantchanger.utils;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.item.*;
import net.minecraft.item.Item;

import static ak.enchantchanger.utils.Blocks.blockMakoReactor;

/**
 * アイテムインスタンス格納クラス
 * Created by A.K. on 2017/05/07.
 */
public class Items {
    public static Item itemExExpBottle;
    public static Item itemMateria;
    public static Item itemZackSword;
    public static Item itemCloudSword;
    public static Item itemCloudSwordCore;
    public static Item itemSephirothSword;
    public static Item itemUltimateWeapon;
    public static Item itemPortableEnchantChanger;
    public static Item itemPortableEnchantmentTable;
    public static Item itemMasterMateria;
    public static Item itemImitateSephirothSword;
    public static Item itemHugeMateria;
    public static Item itemBlockMakoReactor;
    public static void init() {


        itemMateria = (new EcItemMateria("Materia"))
                .setRegistryName("materia")
                .setHasSubtypes(true)
                .setMaxDamage(0);
        itemExExpBottle = new EcItemExExpBottle("ExExpBottle")
                .setRegistryName("exexpbottle");
        itemZackSword = (new EcItemZackSword("ZackSword"))
                .setRegistryName("zacksword")
                .setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemCloudSwordCore = (new EcItemCloudSwordCore("CloudSwordCore"))
                .setRegistryName("cloudswordcore")
                .setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemCloudSword = (new EcItemCloudSword("CloudSword"))
                .setRegistryName("cloudsword")
                .setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemSephirothSword = (new EcItemSephirothSword("MasamuneBlade"))
                .setRegistryName("masamuneblade")
                .setMaxDamage(Item.ToolMaterial.DIAMOND.getMaxUses() * 2);
        itemUltimateWeapon = (new EcItemUltimateWeapon("UltimateWeapon"))
                .setRegistryName("ultimateweapon")
                .setMaxDamage(Item.ToolMaterial.DIAMOND.getMaxUses() * 14);
        itemPortableEnchantChanger = (new EcItemMaterializer("PortableEnchantChanger"))
                .setRegistryName("portableenchantchanger");
        itemPortableEnchantmentTable = (new EcItemEnchantmentTable("PortableEnchantmentTable"))
                .setRegistryName("portableenchantmenttable");
        itemMasterMateria = new EcItemMasterMateria("itemMasterMateria")
                .setRegistryName("mastermateria")
                .setHasSubtypes(true)
                .setMaxDamage(0)
                .setMaxStackSize(1);
        itemImitateSephirothSword = (new EcItemSephirothSwordImit("ImitateMasamuneBlade"))
                .setRegistryName("imitationmasamuneblade");

        itemHugeMateria = new EcItemHugeMateria("HugeMateria")
                .setRegistryName("itemhugemateria");

        itemBlockMakoReactor = new EcItemBlockMakoReactor(blockMakoReactor).setRegistryName(Constants.REG_BLOCK_MAKO_REACTOR);
    }
}
