package ak.EnchantChanger.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.*;

public class EcPortableEnchantmentHelper {
    /**
     * Returns the level of enchantment on the ItemStack passed.
     */
//    public static int getEnchantmentLevel(int par0, ItemStack par1ItemStack)
//    {
//        if (par1ItemStack == null)
//        {
//            return 0;
//        }
//        else
//        {
//            NBTTagList var2 = par1ItemStack.getEnchantmentTagList();
//
//            if (var2 == null)
//            {
//                return 0;
//            }
//            else
//            {
//                for (int var3 = 0; var3 < var2.tagCount(); ++var3)
//                {
//                    short var4 = var2.getCompoundTagAt(var3).getShort("id");
//                    short var5 = var2.getCompoundTagAt(var3).getShort("lvl");
//
//                    if (var4 == par0)
//                    {
//                        return var5;
//                    }
//                }
//
//                return 0;
//            }
//        }
//    }

    /**
     * Returns the biggest level of the enchantment on the array of ItemStack passed.
     */
//    private static int getMaxEnchantmentLevel(int par0, ItemStack[] par1ArrayOfItemStack)
//    {
//        int var2 = 0;
//
//        for (ItemStack itemStack : par1ArrayOfItemStack)
//        {
//            int var7 = getEnchantmentLevel(par0, itemStack);
//
//            if (var7 > var2)
//            {
//                var2 = var7;
//            }
//        }
//
//        return var2;
//    }


    /**
     * Returns the enchantability of itemstack, it's uses a singular formula for each index (2nd parameter: 0, 1 and 2),
     * cutting to the max enchantability power of the table (3rd parameter)
     */
    public static int calcItemStackEnchantability(Random par0Random, int par1, int par2, ItemStack par3ItemStack) {
        Item var4 = par3ItemStack.getItem();
        int var5 = var4.getItemEnchantability();

        if (var5 <= 0) {
            return 0;
        } else {
            if (par2 > 15) {
                par2 = 15;
            }

            int var6 = par0Random.nextInt(8) + 1 + (par2 >> 1) + par0Random.nextInt(par2 + 1);
            return par1 == 0 ? Math.max(var6 / 3, 1) : (par1 == 1 ? var6 * 2 / 3 + 1 : Math.max(var6, par2 * 2));
        }
    }

    /**
     * Adds a random enchantment to the specified item. Args: random, itemStack, enchantabilityLevel
     */
//    public static void addRandomEnchantment(Random par0Random, ItemStack par1ItemStack, int par2)
//    {
//        List var3 = buildEnchantmentList(par0Random, par1ItemStack, par2);
//
//        if (var3 != null)
//        {
//            Iterator var4 = var3.iterator();
//
//            while (var4.hasNext())
//            {
//                EnchantmentData var5 = (EnchantmentData)var4.next();
//                par1ItemStack.addEnchantment(var5.enchantmentobj, var5.enchantmentLevel);
//            }
//        }
//    }

    /**
     * Create a list of random EnchantmentData (enchantments) that can be added together to the ItemStack, the 3rd
     * parameter is the total enchantability level.
     */
    public static List buildEnchantmentList(Random par0Random, ItemStack par1ItemStack, int par2) {
        Item var3 = par1ItemStack.getItem();
        int var4 = var3.getItemEnchantability();

        if (var4 <= 0) {
            return null;
        } else {
            var4 /= 2;
            var4 = 1 + par0Random.nextInt((var4 >> 1) + 1) + par0Random.nextInt((var4 >> 1) + 1);
            int var5 = var4 + par2;
            float var6 = (par0Random.nextFloat() + par0Random.nextFloat() - 1.0F) * 0.15F;
            int var7 = (int) ((float) var5 * (1.0F + var6) + 0.5F);

            if (var7 < 1) {
                var7 = 1;
            }

            ArrayList<EnchantmentData> var8 = null;
            Map var9 = mapEnchantmentData(var7, par1ItemStack);

            if (var9 != null && !var9.isEmpty()) {
                EnchantmentData var10 = (EnchantmentData) WeightedRandom.getRandomItem(par0Random, var9.values());

                if (var10 != null) {
                    var8 = new ArrayList<>();
                    var8.add(var10);

                    for (int var11 = var7; par0Random.nextInt(50) <= var11; var11 >>= 1) {
                        Iterator var12 = var9.keySet().iterator();

                        while (var12.hasNext()) {
                            Integer var13 = (Integer) var12.next();
                            boolean var14 = true;
                            Iterator var15 = var8.iterator();

                            while (true) {
                                if (var15.hasNext()) {
                                    EnchantmentData var16 = (EnchantmentData) var15.next();

                                    if (var16.enchantmentobj.canApplyTogether(Enchantment.getEnchantmentById(var13))) {
                                        continue;
                                    }

                                    var14 = false;
                                }

                                if (!var14) {
                                    var12.remove();
                                }

                                break;
                            }
                        }

                        if (!var9.isEmpty()) {
                            EnchantmentData var17 = (EnchantmentData) WeightedRandom.getRandomItem(par0Random, var9.values());
                            var8.add(var17);
                        }
                    }
                }
            }

            return var8;
        }
    }

    /**
     * Creates a 'Map' of EnchantmentData (enchantments) possible to add on the ItemStack and the enchantability level
     * passed.
     */
    public static Map mapEnchantmentData(int par0, ItemStack par1ItemStack) {
        HashMap<Integer, EnchantmentData> var3 = new HashMap<>();
        Enchantment[] var4 = Enchantment.enchantmentsBookList;

        for (Enchantment enchantment : var4) {
            if (enchantment != null && enchantment.canApplyAtEnchantingTable(par1ItemStack)) {
                for (int var8 = enchantment.getMinLevel(); var8 <= enchantment.getMaxLevel(); ++var8) {
                    if (par0 >= enchantment.getMinEnchantability(var8) && par0 <= enchantment.getMaxEnchantability(var8)) {
                        var3.put(enchantment.effectId, new EnchantmentData(enchantment, var8));
                    }
                }
            }
        }

        return var3;
    }
}
