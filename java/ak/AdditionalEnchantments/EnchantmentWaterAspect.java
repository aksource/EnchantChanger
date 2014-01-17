package ak.AdditionalEnchantments;

import net.minecraft.entity.EntityLivingBase;

public class EnchantmentWaterAspect extends EnchantmentDamagable
{
	public EnchantmentWaterAspect(int id, int weight)
	{
		super(id, weight);
	}
	public float calcModifierLiving(int lv, EntityLivingBase living)
	{
		return (living.isImmuneToFire())?2.5F:0.0F;
	}
}