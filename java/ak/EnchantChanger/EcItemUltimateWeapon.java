package ak.EnchantChanger;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
public class EcItemUltimateWeapon extends EcItemSword
{
	private float ultimateWeaponDamage = 0f;
	public EcItemUltimateWeapon()
	{
		super(ToolMaterial.EMERALD);
	}
	@Override
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity)
	{
		if(player.worldObj.isRemote)return false;
		if(entity instanceof EntityLivingBase){
			float mobmaxhealth =((EntityLivingBase) entity).getMaxHealth() / 3 + 1;
			float weaponDmgFromHP = WeaponDamagefromHP(player);
			ultimateWeaponDamage = (mobmaxhealth > weaponDmgFromHP)?mobmaxhealth:weaponDmgFromHP;
		}else if(entity instanceof EntityDragonPart){
			ultimateWeaponDamage = 100;
		}else{
			ultimateWeaponDamage = 10;
		}
		ObfuscationReflectionHelper.setPrivateValue(ItemSword.class, (ItemSword)itemstack.getItem(), ultimateWeaponDamage, 0);
		return false;
	}
	public float WeaponDamagefromHP(EntityPlayer player)
	{
		float nowHP = player.getHealth();
		float maxHP = player.getMaxHealth();
		float hpRatio = nowHP / maxHP;
		float damageratio;
		if(hpRatio >= 0.8){
			damageratio = 1;
		}else if(hpRatio >= 0.5){
			damageratio = 0.7F;
		}else if(hpRatio >= 0.2){
			damageratio = 0.5F;
		}else{
			damageratio = 0.3F;
		}
		int EXPLv = player.experienceLevel;
		return MathHelper.floor_float((10 + EXPLv/5)*damageratio);
	}
}
