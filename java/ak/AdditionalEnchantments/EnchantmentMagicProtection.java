package ak.AdditionalEnchantments;

import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class EnchantmentMagicProtection extends EnchantmentProtectionAdditional
{
	public EnchantmentMagicProtection(int id, int weight)
	{
		super(id, weight);
	}
	@Override
	public int calcModifierDamage(int par1, DamageSource dmgSource)
	{
		if(dmgSource.isMagicDamage()){
			float f = (float)(6 + par1 * par1) / 3.0F;
			return MathHelper.floor_float(f * 1.0F);
		}else return 0;
	}
}