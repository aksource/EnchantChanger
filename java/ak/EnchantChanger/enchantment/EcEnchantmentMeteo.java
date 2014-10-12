package ak.EnchantChanger.enchantment;

import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EcEnchantmentMeteo extends Enchantment
{
	public EcEnchantmentMeteo(int var1, int var2)
    {
        super(var1, var2, EnumEnchantmentType.weapon);
    }
	@Override
    public boolean canApplyTogether(Enchantment par1Enchantment)
    {
    	return this != par1Enchantment && par1Enchantment.effectId != ConfigurationUtils.idEnchantmentHoly &&par1Enchantment.effectId != ConfigurationUtils.idEnchantmentTelepo &&par1Enchantment.effectId != ConfigurationUtils.idEnchantmentThunder;
    }
}