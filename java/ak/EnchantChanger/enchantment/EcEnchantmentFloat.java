package ak.EnchantChanger.enchantment;

import ak.EnchantChanger.api.Constants;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

@Deprecated
public class EcEnchantmentFloat extends Enchantment {
    public EcEnchantmentFloat(int var1, int var2) {
        super(var1, new ResourceLocation(Constants.EcTextureDomain + "floating"), var2, EnumEnchantmentType.WEAPON);
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }
}