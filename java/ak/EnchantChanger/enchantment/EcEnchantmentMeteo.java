package ak.EnchantChanger.enchantment;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.util.ResourceLocation;

public class EcEnchantmentMeteo extends Enchantment {
    public EcEnchantmentMeteo(int var1, int var2) {
        super(var1, new ResourceLocation(Constants.EcTextureDomain + "meteor"), var2, EnumEnchantmentType.WEAPON);
    }

    @Override
    public boolean canApplyTogether(Enchantment par1Enchantment) {
        return this != par1Enchantment && par1Enchantment.effectId != ConfigurationUtils.idEnchantmentHoly && par1Enchantment.effectId != ConfigurationUtils.idEnchantmentTelepo && par1Enchantment.effectId != ConfigurationUtils.idEnchantmentThunder;
    }

    @Override
    public boolean isAllowedOnBooks() {
        return false;
    }
}