package ak.AdditionalEnchantments;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;

public class EnchantmentDisjunction extends EnchantmentDamagable
{
	public EnchantmentDisjunction(int id, int weight)
	{
		super(id,weight);
	}
	public float calcModifierLiving(int lv, EntityLivingBase living)
	{
		return (living instanceof EntityEnderman)?2.5F:0.0F;
	}
}