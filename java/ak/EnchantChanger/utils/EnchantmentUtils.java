package ak.EnchantChanger.utils;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.MaterialResultPair;
import ak.EnchantChanger.enchantment.*;
import ak.EnchantChanger.item.EcItemSword;
import net.minecraft.enchantment.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * Created by A.K. on 14/10/12.
 */
public class EnchantmentUtils {

    public static final Map<Integer, Integer> AP_LIMIT = new HashMap<>();
    public static final Set<Integer> MAGIC_ENCHANTMENT = new HashSet<>();
    public static final Map<Integer, Integer> LEVEL_LIMIT_MAP = new HashMap<>();
    public static final Map<Integer, Integer> COEFFICIENT_MAP = new HashMap<>();
    public static final Set<EnchantmentData> ENCHANTMENT_DATA_SET = new HashSet<>();
    //MasterMateria number, materials
    private static final Map<Integer, Set<MaterialResultPair>> MATERIAL_MAP = new HashMap<>();

    public static boolean isApLimit(int Id, int Lv, int ap) {
        return getApLimit(Id, Lv) < ap;
    }

    public static int getApLimit(int Id, int Lv) {
        if (AP_LIMIT.containsKey(Id)) {
            return AP_LIMIT.get(Id) * (Lv / 5 + 1);
        } else
            return 150 * (Lv / 5 + 1);
    }

    public static void addEnchantmentToItem(ItemStack item,
                                            Enchantment enchantment, int Lv) {
        if (item == null || enchantment == null || Lv <= 0) {
            return;
        }
        if (item.getTagCompound() == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        String tagName = getTagName(item);
        if (!item.getTagCompound().hasKey(tagName, Constants.NBT.TAG_LIST)) {
            item.getTagCompound().setTag(tagName, new NBTTagList());
        }

        NBTTagList var3 =item.getTagCompound().getTagList(tagName, 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short) enchantment.effectId);
        var4.setShort("lvl", (short) (Lv));
        var3.appendTag(var4);
    }

    public static int getMateriaEnchKind(ItemStack item) {
        int EnchantmentKind = 256;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                EnchantmentKind = i;
                break;
            }
        }
        return EnchantmentKind;
    }

    public static int getMateriaEnchLv(ItemStack item) {
        int Lv = 0;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                Lv = EnchantmentHelper.getEnchantmentLevel(i, item);
                break;
            }
        }
        return Lv;
    }

    public static byte[] getMagic(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound().getByteArray("EnchantChanger|Magic");
    }

    public static void setMagic(ItemStack itemStack, byte[] magic) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        itemStack.getTagCompound().setByteArray("EnchantChanger|Magic", magic);
    }

    public static boolean hasMagic(ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("EnchantChanger|Magic", Constants.NBT.TAG_BYTE_ARRAY);
    }

    public static int getDecreasedLevel(ItemStack itemStack, int originalLevel) {
        if (ConfigurationUtils.enableDecMateriaLv) {
            float dmgratio = (itemStack.getMaxDamage() == 0) ? 1 : (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage();
            int declv = (dmgratio > 0.5F) ? 0 : (dmgratio > 0.25F) ? 1 : 2;
            return (originalLevel - declv < 0) ? 0 : originalLevel - declv;
        } else return originalLevel;
    }

    public static Enchantment enchKind(ItemStack item) {
        int EnchantmentKind = -1;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                EnchantmentKind = i;
                break;
            }
        }
        return EnchantmentKind != -1 ? Enchantment.enchantmentsList[EnchantmentKind] : Enchantment.enchantmentsList[0];
    }

    public static int enchLv(ItemStack item) {
        int Lv = 0;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                Lv = EnchantmentHelper.getEnchantmentLevel(i, item);
                break;
            }
        }
        return Lv;
    }

    public static boolean checkLvCap(ItemStack materia) {
        if (ConfigurationUtils.enableLevelCap) {
            int ench = getMateriaEnchKind(materia);
            int lv = getMateriaEnchLv(materia);
            return !(Enchantment.enchantmentsList[ench].getMaxLevel() < lv);
        }
        return true;
    }

    public static boolean isEnchantmentValid(Enchantment ench, ItemStack itemStack) {
        if (ench == null) {
            return false;
        }
        if (itemStack.getItem() instanceof ItemBook) {
            return true;
        }
        if (ench instanceof EnchantmentDurability) {
            return itemStack.isItemStackDamageable() || ench.type.canEnchantItem(itemStack.getItem());
        }
        if (ench instanceof EnchantmentDigging) {
            return itemStack.getItem() instanceof ItemShears || ench.type.canEnchantItem(itemStack.getItem()) || isExtraTools(itemStack);
        }
        if (ench instanceof EnchantmentDamage || ench instanceof EnchantmentLootBonus || ench instanceof EnchantmentFireAspect) {
            return itemStack.getItem() instanceof ItemTool || ench.type.canEnchantItem(itemStack.getItem()) || isExtraSwords(itemStack);
        }
        if (ench instanceof EnchantmentThorns) {
            return itemStack.getItem() instanceof ItemArmor || ench.type.canEnchantItem(itemStack.getItem()) || isExtraArmors(itemStack);
        }
        if (ench instanceof EnchantmentUntouching) {
            return itemStack.getItem() instanceof ItemShears || ench.type.canEnchantItem(itemStack.getItem()) || isExtraTools(itemStack);
        }
        if (ench instanceof EcEnchantmentMeteo || ench instanceof EcEnchantmentHoly || ench instanceof EcEnchantmentTeleport || ench instanceof EcEnchantmentFloat || ench instanceof EcEnchantmentThunder) {
            return itemStack.getItem() instanceof EcItemSword;
        }
        return ench.type.canEnchantItem(itemStack.getItem());
    }

    private static boolean isExtraTools(ItemStack itemStack) {
        String uName = EnchantChanger.getUniqueStrings(itemStack);
        return Arrays.asList(ConfigurationUtils.extraToolIDs).contains(uName);
    }

    private static boolean isExtraSwords(ItemStack itemStack) {
        String uName = EnchantChanger.getUniqueStrings(itemStack);
        return Arrays.asList(ConfigurationUtils.extraSwordIDs).contains(uName);
    }

    private static boolean isExtraArmors(ItemStack itemStack) {
        String uName = EnchantChanger.getUniqueStrings(itemStack);
        return Arrays.asList(ConfigurationUtils.extraArmorIDs).contains(uName);
    }

    private static boolean isExtraBows(ItemStack itemStack) {
        String uName = EnchantChanger.getUniqueStrings(itemStack);
        return Arrays.asList(ConfigurationUtils.extraBowIDs).contains(uName);
    }

    public static boolean isEnchanted(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemEnchantedBook) ? itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("StoredEnchantments", 9): itemStack.isItemEnchanted();
    }

    public static String getTagName(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemEnchantedBook || itemStack.getItem() instanceof ItemBook) ? "StoredEnchantments" : "ench";
    }

    public static void initMaps() {
        makeMapFromArray(COEFFICIENT_MAP, ConfigurationUtils.enchantmentAPCoefficients);
        for (Integer integer : COEFFICIENT_MAP.keySet()) {
            AP_LIMIT.put(integer, COEFFICIENT_MAP.get(integer) * ConfigurationUtils.pointAPBase);
        }
        MAGIC_ENCHANTMENT.add(ConfigurationUtils.idEnchantmentMeteor);
        MAGIC_ENCHANTMENT.add(ConfigurationUtils.idEnchantmentFloat);
        MAGIC_ENCHANTMENT.add(ConfigurationUtils.idEnchantmentHoly);
        MAGIC_ENCHANTMENT.add(ConfigurationUtils.idEnchantmentTelepo);
        MAGIC_ENCHANTMENT.add(ConfigurationUtils.idEnchantmentThunder);
        makeMapFromArray(LEVEL_LIMIT_MAP, ConfigurationUtils.enchantmentLevelLimits);
        for (Enchantment enchantment : Enchantment.enchantmentsList) {
            if (enchantment != null) {
                for (int lv = enchantment.getMinLevel(); lv <= enchantment.getMaxLevel(); lv++) {
                    ENCHANTMENT_DATA_SET.add(new EnchantmentData(enchantment, lv));
                }
            }
        }
        initHugeMateriasMap();
    }

    private static void initHugeMateriasMap() {
        ItemStack materiaStack = new ItemStack(EnchantChanger.itemMateria);
        registerHugeMateria(0, new ItemStack(Blocks.dragon_egg), new ItemStack(EnchantChanger.itemMateria, 1, 1));
        registerHugeMateria(0, new ItemStack(Items.golden_apple, 1, 1), new ItemStack(EnchantChanger.itemMateria, 1, 2));
        registerHugeMateria(0, new ItemStack(Items.ender_pearl), new ItemStack(EnchantChanger.itemMateria, 1, 3));
        registerHugeMateria(0, new ItemStack(Items.ender_eye), new ItemStack(EnchantChanger.itemMateria, 1, 4));
        registerHugeMateria(0, new ItemStack(Blocks.gold_block), new ItemStack(EnchantChanger.itemMateria, 1, 5));
        registerHugeMateria(0, new ItemStack(Items.milk_bucket), new ItemStack(EnchantChanger.itemMateria, 1, 6));
        registerHugeMateria(0, new ItemStack(Items.golden_boots), new ItemStack(EnchantChanger.itemMateria, 1, 7));
        registerHugeMateria(0, new ItemStack(Items.nether_wart), new ItemStack(EnchantChanger.itemMateria, 1, 8));
        registerHugeMateria(1, new ItemStack(Items.iron_ingot), getEnchantedItemStack(materiaStack.copy(), Enchantment.protection, 1));
        registerHugeMateria(1, new ItemStack(Items.blaze_powder), getEnchantedItemStack(materiaStack.copy(), Enchantment.fireProtection, 1));
        registerHugeMateria(1, new ItemStack(Items.feather), getEnchantedItemStack(materiaStack.copy(), Enchantment.featherFalling, 1));
        registerHugeMateria(1, new ItemStack(Items.gunpowder), getEnchantedItemStack(materiaStack.copy(), Enchantment.blastProtection, 1));
        registerHugeMateria(1, new ItemStack(Items.arrow), getEnchantedItemStack(materiaStack.copy(), Enchantment.projectileProtection, 1));
        registerHugeMateria(1, new ItemStack(Blocks.cactus), getEnchantedItemStack(materiaStack.copy(), Enchantment.thorns, 1));
        registerHugeMateria(2, new ItemStack(Items.reeds), getEnchantedItemStack(materiaStack.copy(), Enchantment.respiration, 1));
        registerHugeMateria(2, new ItemStack(Items.golden_pickaxe), getEnchantedItemStack(materiaStack.copy(), Enchantment.aquaAffinity, 1));
        registerHugeMateria(2, new ItemStack(Items.fishing_rod), getEnchantedItemStack(materiaStack.copy(), Enchantment.field_151370_z, 1));
        registerHugeMateria(2, new ItemStack(Items.carrot_on_a_stick), getEnchantedItemStack(materiaStack.copy(), Enchantment.field_151369_A, 1));
        registerHugeMateria(3, new ItemStack(Items.fire_charge), getEnchantedItemStack(materiaStack.copy(), Enchantment.sharpness, 1));
        registerHugeMateria(3, new ItemStack(Items.flint_and_steel), getEnchantedItemStack(materiaStack.copy(), Enchantment.smite, 1));
        registerHugeMateria(3, new ItemStack(Items.spider_eye), getEnchantedItemStack(materiaStack.copy(), Enchantment.baneOfArthropods, 1));
        registerHugeMateria(3, new ItemStack(Items.slime_ball), getEnchantedItemStack(materiaStack.copy(), Enchantment.knockback, 1));
        registerHugeMateria(3, new ItemStack(Items.blaze_rod), getEnchantedItemStack(materiaStack.copy(), Enchantment.fireAspect, 1));
        registerHugeMateria(3, new ItemStack(Items.golden_apple, 1, OreDictionary.WILDCARD_VALUE), getEnchantedItemStack(materiaStack.copy(), Enchantment.looting, 1));
        registerHugeMateria(4, new ItemStack(Items.golden_pickaxe), getEnchantedItemStack(materiaStack.copy(), Enchantment.efficiency, 1));
        registerHugeMateria(4, new ItemStack(Items.string), getEnchantedItemStack(materiaStack.copy(), Enchantment.silkTouch, 1));
        registerHugeMateria(4, new ItemStack(Items.iron_ingot), getEnchantedItemStack(materiaStack.copy(), Enchantment.unbreaking, 1));
        registerHugeMateria(4, new ItemStack(Items.golden_apple, 1, OreDictionary.WILDCARD_VALUE), getEnchantedItemStack(materiaStack.copy(), Enchantment.fortune, 1));
        registerHugeMateria(5, new ItemStack(Items.fire_charge), getEnchantedItemStack(materiaStack.copy(), Enchantment.power, 1));
        registerHugeMateria(5, new ItemStack(Items.slime_ball), getEnchantedItemStack(materiaStack.copy(), Enchantment.punch, 1));
        registerHugeMateria(5, new ItemStack(Items.blaze_rod), getEnchantedItemStack(materiaStack.copy(), Enchantment.flame, 1));
        registerHugeMateria(5, new ItemStack(Items.bow), getEnchantedItemStack(materiaStack.copy(), Enchantment.infinity, 1));
    }

    public static void registerHugeMateria(int master, ItemStack material, ItemStack result) {
        Set<MaterialResultPair> set;
        if (!MATERIAL_MAP.containsKey(master)) {
            set = new HashSet<>();
            set.add(MaterialResultPair.getMaterialResultPair(material, result));
            MATERIAL_MAP.put(master, set);
        }
        set = MATERIAL_MAP.get(master);
        for (MaterialResultPair mrp : set) {
            if (mrp.getMaterial().getContainItemStack().isItemEqual(material)) {
                return;
            }
        }
        set.add(MaterialResultPair.getMaterialResultPair(material, result));
    }

    public static boolean isMaterialValid(int master, ItemStack material) {
        if (!MATERIAL_MAP.containsKey(master)) return false;
        Set<MaterialResultPair> set = MATERIAL_MAP.get(master);
        for (MaterialResultPair mrp : set) {
            ItemStack containItemStack = mrp.getMaterial().getContainItemStack();
            if (areItemsEqualsWildCard(containItemStack, material)) {
                return true;
            }
        }
        return false;
    }

    public static ItemStack getResult(int master, ItemStack material) {
        Set<MaterialResultPair> set = MATERIAL_MAP.get(master);
        for (MaterialResultPair mrp : set) {
            ItemStack containItemStack = mrp.getMaterial().getContainItemStack();
            if (areItemsEqualsWildCard(containItemStack, material)) {
                return mrp.getResultCopy();
            }
        }
        return new ItemStack(Blocks.air);
    }

    private static boolean areItemsEqualsWildCard(ItemStack master, ItemStack checkStack) {
        return (master.getItemDamage() == OreDictionary.WILDCARD_VALUE && master.getItem() == checkStack.getItem())
                || master.isItemEqual(checkStack);
    }

    private static ItemStack getEnchantedItemStack(ItemStack base, Enchantment enchantment, int lv) {
        addEnchantmentToItem(base, enchantment, lv);
        return base;
    }

    public static ItemStack getBookResult(ItemStack baseItem, Collection enchantmentCollection) {
        if (baseItem.getItem() instanceof ItemEnchantedBook && enchantmentCollection.isEmpty()) {
            ItemStack book = new ItemStack(Items.book);
            book.setTagCompound(baseItem.getTagCompound());
            return book;
        }

        if (baseItem.getItem() instanceof ItemBook && !enchantmentCollection.isEmpty()) {
            ItemStack book = new ItemStack(Items.enchanted_book);
            book.setTagCompound(baseItem.getTagCompound());
            return book;
        }
        return baseItem;
    }

    public static EnchantmentData getEnchantmentData(Random rand) {
        return (EnchantmentData) WeightedRandom.getRandomItem(rand, ENCHANTMENT_DATA_SET);
    }

    public static ItemStack getLvUpitem(Enchantment enchantment, int lv) {
        if (lv < enchantment.getMaxLevel()) {
            return new ItemStack(Items.experience_bottle, lv, 0);
        }
        return new ItemStack(EnchantChanger.itemExExpBottle, lv / enchantment.getMaxLevel(), 0);
    }

    private static void makeMapFromArray(Map<Integer, Integer> map, String[] array) {
        String[] split;
        for (String enchLimit : array) {
            split = enchLimit.split(":");
            map.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        }
    }
}
