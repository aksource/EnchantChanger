package ak.EnchantChanger.enchantment;

import ak.EnchantChanger.EnchantChanger;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EcEnchantmentThunder extends Enchantment
{
	public EcEnchantmentThunder(int var1, int var2)
    {
        super(var1, var2, EnumEnchantmentType.weapon);
    }
	@Override
    public boolean canApplyTogether(Enchantment par1Enchantment)
    {
    	return this != par1Enchantment && par1Enchantment.effectId != EnchantChanger.EnchantmentMeteorId &&par1Enchantment.effectId != EnchantChanger.EnchantmentHolyId &&par1Enchantment.effectId != EnchantChanger.EnchantmentTelepoId;
    }
}