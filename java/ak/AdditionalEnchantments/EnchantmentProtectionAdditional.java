package ak.AdditionalEnchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.enchantment.EnumEnchantmentType;

public class EnchantmentProtectionAdditional extends Enchantment
{
	public EnchantmentProtectionAdditional(int id, int weight)
	{
		super(id,weight,EnumEnchantmentType.armor);
	}
	public int getMaxLevel()
	{
		return 5;
	}
	public int getMinEnchantability(int par1)
	{
		return 5 + (par1 - 1) * 8;
	}
	public int getMaxEnchantability(int par1)
	{
		return this.getMinEnchantability(par1) + 15;
	}
	public boolean canApplyTogether(Enchantment enchantment)
	{
		return !(enchantment instanceof EnchantmentProtection) || !(enchantment instanceof EnchantmentProtectionAdditional);
	}
}