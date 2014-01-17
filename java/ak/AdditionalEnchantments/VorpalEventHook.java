package ak.AdditionalEnchantments;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class VorpalEventHook
{
	private boolean vorpaled = false;
	private Random rand = new Random();
	@SubscribeEvent
	public void vorpalAttackEvent(AttackEntityEvent event)
	{	
		ItemStack equipItem = event.entityPlayer.getCurrentEquippedItem();
		int vorpalLv;
		vorpaled = false;
		if(AdditionalEnchantments.addVorpal && event.target instanceof EntityLivingBase &&equipItem != null && EnchantmentHelper.getEnchantmentLevel(AdditionalEnchantments.idVorpal, equipItem) > 0){
			vorpalLv = EnchantmentHelper.getEnchantmentLevel(AdditionalEnchantments.idVorpal, equipItem);
			EntityLivingBase entityliving = (EntityLivingBase) event.target;
			if(vorpalLv * 10 > rand.nextInt(100)){
				vorpaled = true;
				entityliving.attackEntityFrom(DamageSource.causePlayerDamage(event.entityPlayer), 9999999F);
			}
		}
	}
	@SubscribeEvent
	public void entityDropEvent(LivingDropsEvent event)
	{
		if(AdditionalEnchantments.addVorpal && event.source.getEntity() != null && event.source.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.source.getEntity();
			ItemStack equipItem = player.getCurrentEquippedItem();
			int vorpalLv = EnchantmentHelper.getEnchantmentLevel(AdditionalEnchantments.idVorpal, equipItem);
			if((vorpaled|| vorpalLv * 20 > rand.nextInt(100)) && !skullInDrops(event.drops)){
				int skullmeta = skullKind(event.entityLiving);
				if(skullmeta >= 0){
					ItemStack skull = new ItemStack(Items.skull, 1, skullmeta);
					EntityItem entityitem = new EntityItem(event.entityLiving.worldObj, event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ);
					entityitem.setEntityItemStack(skull);
					event.drops.add(entityitem);
					vorpaled = false;
				}
			}
		}
	}
	private int skullKind(EntityLivingBase living)
	{
		if(living instanceof EntitySkeleton){
			if(((EntitySkeleton)living).getSkeletonType() == 0)
				return 0;
			else
				return 1;
		}else if(living instanceof EntityPlayer){
			return 3;
		}else if(living instanceof EntityZombie){
			return 2;
		}else if(living instanceof EntityCreeper){
			return 4;
		}else{
			return -1;
		}
	}
	private boolean skullInDrops(ArrayList<EntityItem> droplist)
	{
		for(int i=0;i<droplist.size();i++)
		{
			if(droplist.get(i).getEntityItem().getItem() instanceof ItemSkull)
				return true;
		}
		return false;
	}
}