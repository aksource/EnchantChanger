package ak.EnchantChanger;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/06/27.
 */
public class Recipes {

    public static void init() {
        ItemStack[] materiaArray = new ItemStack[256];
        for (Enchantment enchantment : Enchantment.enchantmentsList) {
            if (enchantment != null) {
                materiaArray[enchantment.effectId] = new ItemStack(EnchantChanger.itemMateria);
                materiaArray[enchantment.effectId].addEnchantment(enchantment, enchantment.getMaxLevel());
            }
        }
    }
}
