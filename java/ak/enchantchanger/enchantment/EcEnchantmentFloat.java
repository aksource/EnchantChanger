package ak.EnchantChanger.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EcEnchantmentFloat extends Enchantment {
    public EcEnchantmentFloat(int var1, int var2) {
        super(var1, var2, EnumEnchantmentType.weapon);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }
}