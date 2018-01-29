package ak.enchantchanger.utils;

import ak.enchantchanger.api.MasterMateriaUtils;
import net.minecraft.enchantment.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.*;

import static ak.enchantchanger.api.Constants.NBT_ENCHANTMENT;
import static ak.enchantchanger.api.Constants.NBT_KEY_ENCHANT_CHANGER_MAGIC;

/**
 * Enchantment関係のユーティリティクラス
 * Created by A.K. on 14/10/12.
 */
public class EnchantmentUtils {

    public static final Map<ResourceLocation, Integer> AP_LIMIT = new HashMap<>();
    public static final Map<ResourceLocation, Integer> LEVEL_LIMIT_MAP = new HashMap<>();
    public static final Map<ResourceLocation, Integer> COEFFICIENT_MAP = new HashMap<>();
    public static final List<EnchantmentData> ENCHANTMENT_DATA_LIST = new ArrayList<>();

    public static boolean isApLimit(ResourceLocation registerName, int lv, int ap) {
        return getApLimit(registerName, lv) < ap;
    }

    public static int getApLimit(ResourceLocation registerName, int lv) {
        return AP_LIMIT.computeIfAbsent(registerName, (id) -> 150) * (lv / 5 + 1);
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

        NBTTagList var3 = item.getTagCompound().getTagList(tagName, 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short) Enchantment.getEnchantmentID(enchantment));
        var4.setShort("lvl", (short) (Lv));
        var3.appendTag(var4);
    }

    public static ResourceLocation getEnchantmentRegisterName(ItemStack item) {
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            if (EnchantmentHelper.getEnchantmentLevel(enchantment, item) > 0) {
                return enchantment.getRegistryName();
            }
        }
        return new ResourceLocation("");
    }

    public static byte[] getMagic(@Nonnull ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        return itemStack.getTagCompound().getByteArray(NBT_KEY_ENCHANT_CHANGER_MAGIC);
    }

    public static void setMagic(@Nonnull ItemStack itemStack, byte[] magic) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        itemStack.getTagCompound().setByteArray(NBT_KEY_ENCHANT_CHANGER_MAGIC, magic);
    }

    public static boolean hasMagic(@Nonnull ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(NBT_KEY_ENCHANT_CHANGER_MAGIC, Constants.NBT.TAG_BYTE_ARRAY);
    }

    public static int getDecreasedLevel(ItemStack itemStack, int originalLevel) {
        if (ConfigurationUtils.enableDecMateriaLv) {
            float dmgRatio = (itemStack.getMaxDamage() == 0) ? 1 : (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage();
            int decLv = (dmgRatio > 0.5F) ? 0 : (dmgRatio > 0.25F) ? 1 : 2;
            return (originalLevel - decLv < 0) ? 0 : originalLevel - decLv;
        } else return originalLevel;
    }

    public static Enchantment getEnchantmentFromItemStack(ItemStack item) {
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            if (EnchantmentHelper.getEnchantmentLevel(enchantment, item) > 0) {
                return enchantment;
            }
        }
        return Enchantment.getEnchantmentByID(0);
    }

    public static int getEnchantmentLv(ItemStack item) {
        int lv = 0;
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            if (EnchantmentHelper.getEnchantmentLevel(enchantment, item) > 0) {
                lv = EnchantmentHelper.getEnchantmentLevel(enchantment, item);
                break;
            }
        }
        return lv;
    }

    public static boolean checkLvCap(ItemStack materia) {
        if (ConfigurationUtils.enableLevelCap) {
            Enchantment enchantment = getEnchantmentFromItemStack(materia);
            int lv = getEnchantmentLv(materia);
            return !(enchantment.getMaxLevel() < lv);
        }
        return true;
    }

    public static boolean isEnchantmentValid(Enchantment enchantment, ItemStack itemStack) {
        return enchantment != null && (itemStack.getItem() instanceof ItemBook || enchantment.canApply(itemStack));
    }

    public static boolean isEnchanted(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemEnchantedBook) ? itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey(ak.enchantchanger.api.Constants.NBT_STORED_ENCHANTMENTS, 9) : itemStack.isItemEnchanted();
    }

    public static String getTagName(ItemStack itemStack) {
        return (itemStack.getItem() instanceof ItemEnchantedBook || itemStack.getItem() instanceof ItemBook) ? ak.enchantchanger.api.Constants.NBT_STORED_ENCHANTMENTS : NBT_ENCHANTMENT;
    }

    public static void initMaps() {
        makeMapFromArray(COEFFICIENT_MAP, ConfigurationUtils.enchantmentAPCoefficients);
        for (ResourceLocation registerName : COEFFICIENT_MAP.keySet()) {
            AP_LIMIT.put(registerName, COEFFICIENT_MAP.get(registerName) * ConfigurationUtils.pointAPBase);
        }
        makeMapFromArray(LEVEL_LIMIT_MAP, ConfigurationUtils.enchantmentLevelLimits);
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            for (int lv = enchantment.getMinLevel(); lv <= enchantment.getMaxLevel(); lv++) {
                ENCHANTMENT_DATA_LIST.add(new EnchantmentData(enchantment, lv));
            }
        }
        initHugeMateriasMap();
    }

    private static void initHugeMateriasMap() {
        ItemStack materiaStack = new ItemStack(ak.enchantchanger.utils.Items.itemMateria);
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Blocks.DRAGON_EGG),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 1));
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Items.GOLDEN_APPLE, 1, 1),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 2));
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Items.ENDER_PEARL),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 3));
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Items.ENDER_EYE), new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 4));
        MasterMateriaUtils.registerHugeMateria(0, new ItemStack(Blocks.GOLD_BLOCK),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 5));
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Items.MILK_BUCKET),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 6));
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Items.GOLDEN_BOOTS),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 7));
        MasterMateriaUtils.registerHugeMateria(0,
                new ItemStack(Items.NETHER_WART),
                new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 8));
        MasterMateriaUtils.registerHugeMateria(1,
                new ItemStack(Items.IRON_INGOT),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.PROTECTION, 1));
        MasterMateriaUtils.registerHugeMateria(1,
                new ItemStack(Items.BLAZE_POWDER),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.FIRE_PROTECTION, 1));
        MasterMateriaUtils.registerHugeMateria(1,
                new ItemStack(Items.FEATHER),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.FEATHER_FALLING, 1));
        MasterMateriaUtils.registerHugeMateria(1,
                new ItemStack(Items.GUNPOWDER),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.BLAST_PROTECTION, 1));
        MasterMateriaUtils.registerHugeMateria(1,
                new ItemStack(Items.ARROW),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.PROJECTILE_PROTECTION, 1));
        MasterMateriaUtils.registerHugeMateria(1,
                new ItemStack(Blocks.CACTUS),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.THORNS, 1));
        MasterMateriaUtils.registerHugeMateria(2,
                new ItemStack(Items.REEDS),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.RESPIRATION, 1));
        MasterMateriaUtils.registerHugeMateria(2,
                new ItemStack(Items.GOLDEN_PICKAXE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.AQUA_AFFINITY, 1));
        MasterMateriaUtils.registerHugeMateria(2,
                new ItemStack(Items.FISHING_ROD),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.LUCK_OF_THE_SEA, 1));
        MasterMateriaUtils.registerHugeMateria(2,
                new ItemStack(Items.CARROT_ON_A_STICK),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.LURE, 1));
        MasterMateriaUtils.registerHugeMateria(2,
                new ItemStack(Items.RABBIT_FOOT),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.DEPTH_STRIDER, 1));
        MasterMateriaUtils.registerHugeMateria(3,
                new ItemStack(Items.FIRE_CHARGE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.SHARPNESS, 1));
        MasterMateriaUtils.registerHugeMateria(3,
                new ItemStack(Items.FLINT_AND_STEEL),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.SMITE, 1));
        MasterMateriaUtils.registerHugeMateria(3,
                new ItemStack(Items.SPIDER_EYE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.BANE_OF_ARTHROPODS, 1));
        MasterMateriaUtils.registerHugeMateria(3,
                new ItemStack(Items.SLIME_BALL),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.KNOCKBACK, 1));
        MasterMateriaUtils.registerHugeMateria(3,
                new ItemStack(Items.BLAZE_ROD),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.FIRE_ASPECT, 1));
        MasterMateriaUtils.registerHugeMateria(3,
                new ItemStack(Items.GOLDEN_APPLE, 1, OreDictionary.WILDCARD_VALUE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.LOOTING, 1));
        MasterMateriaUtils.registerHugeMateria(4,
                new ItemStack(Items.GOLDEN_PICKAXE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.EFFICIENCY, 1));
        MasterMateriaUtils.registerHugeMateria(4,
                new ItemStack(Items.STRING),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.SILK_TOUCH, 1));
        MasterMateriaUtils.registerHugeMateria(4,
                new ItemStack(Items.IRON_INGOT),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.UNBREAKING, 1));
        MasterMateriaUtils.registerHugeMateria(4,
                new ItemStack(Items.GOLDEN_APPLE, 1, OreDictionary.WILDCARD_VALUE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.FORTUNE, 1));
        MasterMateriaUtils.registerHugeMateria(5,
                new ItemStack(Items.FIRE_CHARGE),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.POWER, 1));
        MasterMateriaUtils.registerHugeMateria(5,
                new ItemStack(Items.SLIME_BALL),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.PUNCH, 1));
        MasterMateriaUtils.registerHugeMateria(5,
                new ItemStack(Items.BLAZE_ROD),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.FLAME, 1));
        MasterMateriaUtils.registerHugeMateria(5,
                new ItemStack(Items.BOW),
                getEnchantedItemStack(materiaStack.copy(), Enchantments.INFINITY, 1));
    }

    private static ItemStack getEnchantedItemStack(ItemStack base, Enchantment enchantment, int lv) {
        addEnchantmentToItem(base, enchantment, lv);
        return base;
    }

    public static ItemStack getBookResult(ItemStack baseItem, Collection enchantmentCollection) {
        if (baseItem.getItem() instanceof ItemEnchantedBook && enchantmentCollection.isEmpty()) {
            ItemStack book = new ItemStack(Items.BOOK);
            book.setTagCompound(baseItem.getTagCompound());
            return book;
        }

        if (baseItem.getItem() instanceof ItemBook && !enchantmentCollection.isEmpty()) {
            ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
            book.setTagCompound(baseItem.getTagCompound());
            return book;
        }
        return baseItem;
    }

    public static EnchantmentData getEnchantmentData(Random rand) {
        return WeightedRandom.getRandomItem(rand, ENCHANTMENT_DATA_LIST);
    }

    public static ItemStack getLvUpItem(Enchantment enchantment, int lv) {
        if (lv < enchantment.getMaxLevel()) {
            return new ItemStack(Items.EXPERIENCE_BOTTLE, lv, 0);
        }
        return new ItemStack(ak.enchantchanger.utils.Items.itemExExpBottle, lv / enchantment.getMaxLevel(), 0);
    }

    private static void makeMapFromArray(Map<ResourceLocation, Integer> map, String[] array) {
        for (String str : array) {
            String[] split = str.split(" ");
            map.put(new ResourceLocation(split[0]), Integer.valueOf(split[1]));
        }
    }
}
