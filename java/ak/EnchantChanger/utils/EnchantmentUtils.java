package ak.EnchantChanger.utils;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.enchantment.*;
import ak.EnchantChanger.item.EcItemSword;
import net.minecraft.enchantment.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.WeightedRandom;

import java.util.*;

/**
 * Created by A.K. on 14/10/12.
 */
public class EnchantmentUtils {

    public static final HashMap<Integer, Integer> AP_LIMIT = new HashMap<>();
    public static final HashSet<Integer> MAGIC_ENCHANTMENT = new HashSet<>();
    public static final HashMap<Integer, Integer> LEVEL_LIMIT_MAP = new HashMap<>();
    public static final HashMap<Integer, Integer> COEFFICIENT_MAP = new HashMap<>();
    public static final Set<EnchantmentData> ENCHANTMENT_DATA_SET = new HashSet<>();

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
        if (item.stackTagCompound == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        if (!item.stackTagCompound.hasKey("ench", 9)) {
            item.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 =item.stackTagCompound.getTagList("ench", 10);
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

    public static boolean isEnchantmentValid(Enchantment ench, ItemStack par1ItemStack) {
        if (ench == null) {
            return false;
        }
        if (ench instanceof EnchantmentDurability) {
            return par1ItemStack.isItemStackDamageable() || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentDigging) {
            return par1ItemStack.getItem() == Items.shears || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentDamage || ench instanceof EnchantmentLootBonus || ench instanceof EnchantmentFireAspect) {
            return par1ItemStack.getItem() instanceof ItemTool || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentThorns) {
            return par1ItemStack.getItem() instanceof ItemArmor || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentUntouching) {
            return par1ItemStack.getItem() == Items.shears || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EcEnchantmentMeteo || ench instanceof EcEnchantmentHoly || ench instanceof EcEnchantmentTeleport || ench instanceof EcEnchantmentFloat || ench instanceof EcEnchantmentThunder) {
            return par1ItemStack.getItem() instanceof EcItemSword;
        }
        return ench.type.canEnchantItem(par1ItemStack.getItem());
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
