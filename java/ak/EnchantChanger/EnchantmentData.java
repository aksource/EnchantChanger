package ak.EnchantChanger;

import net.minecraft.enchantment.Enchantment;

/**
 * Created by A.K. on 14/02/27.
 */
public class EnchantmentData {
    public Enchantment enchantment;
    public int lv;
    public EnchantmentData(Enchantment enchantmentId, int enchantmentLv) {
        this.enchantment = enchantmentId;
        this.lv = enchantmentLv;
    }
}
