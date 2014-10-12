package ak.EnchantChanger.utils;

import ak.EnchantChanger.enchantment.*;
import ak.EnchantChanger.item.EcItemSword;
import net.minecraft.enchantment.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by A.K. on 14/10/12.
 */
public class EnchantmentUtils {

    public static final HashMap<Integer, Integer> apLimit = new HashMap<>();
    public static final HashSet<Integer> magicEnchantment = new HashSet<>();
    public static HashMap<Integer, Integer> levelLimitMap = new HashMap<>();
    public static HashMap<Integer, Integer> coefficientMap = new HashMap<>();

    public static boolean isApLimit(int Id, int Lv, int ap) {
        return getApLimit(Id, Lv) < ap;
    }

    public static int getApLimit(int Id, int Lv) {
        if (apLimit.containsKey(Id)) {
            return apLimit.get(Id) * (Lv / 5 + 1);
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
        makeMapFromArray(coefficientMap, ConfigurationUtils.enchantmentAPCoefficients);
        for (Integer integer : coefficientMap.keySet()) {
            apLimit.put(integer, coefficientMap.get(integer) * ConfigurationUtils.pointAPBase);
        }
        magicEnchantment.add(ConfigurationUtils.idEnchantmentMeteor);
        magicEnchantment.add(ConfigurationUtils.idEnchantmentFloat);
        magicEnchantment.add(ConfigurationUtils.idEnchantmentHoly);
        magicEnchantment.add(ConfigurationUtils.idEnchantmentTelepo);
        magicEnchantment.add(ConfigurationUtils.idEnchantmentThunder);
//        apLimit.put(0, 2 * pointAPBase);
//        apLimit.put(1, 1 * pointAPBase);
//        apLimit.put(2, 1 * pointAPBase);
//        apLimit.put(3, 1 * pointAPBase);
//        apLimit.put(4, 1 * pointAPBase);
//        apLimit.put(5, 1 * pointAPBase);
//        apLimit.put(6, 1 * pointAPBase);
//        apLimit.put(7, 1 * pointAPBase);
//        apLimit.put(16, 2 * pointAPBase);
//        apLimit.put(17, 1 * pointAPBase);
//        apLimit.put(18, 1 * pointAPBase);
//        apLimit.put(19, 1 * pointAPBase);
//        apLimit.put(20, 1 * pointAPBase);
//        apLimit.put(21, 3 * pointAPBase);
//        apLimit.put(32, 1 * pointAPBase);
//        apLimit.put(33, 1 * pointAPBase);
//        apLimit.put(34, 1 * pointAPBase);
//        apLimit.put(35, 2 * pointAPBase);
//        apLimit.put(48, 2 * pointAPBase);
//        apLimit.put(49, 1 * pointAPBase);
//        apLimit.put(50, 1 * pointAPBase);
//        apLimit.put(51, 1 * pointAPBase);
//        apLimit.put(61, 1 * pointAPBase);
//        apLimit.put(62, 1 * pointAPBase);
        makeMapFromArray(levelLimitMap, ConfigurationUtils.enchantmentLevelLimits);
    }

    private static void makeMapFromArray(Map<Integer, Integer> map, String[] array) {
        String[] split;
        for (String enchLimit : array) {
            split = enchLimit.split(":");
            map.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        }
    }
}
