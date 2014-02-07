package PokeLoli;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class plItemMobegg extends ItemMonsterPlacer
{
	public plItemMobegg()
	{
		super();
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack)
	{
		if (par1ItemStack.getItemDamage() == 0) {
			return ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
		}
		return super.getUnlocalizedNameInefficiently(par1ItemStack);
	}

	//Entityを右クリックした時に呼ばれるメソッド．なぜか呼ばれない
	@Override
	public boolean itemInteractionForEntity(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
			EntityLivingBase par3EntityLivingBase)
	{
		if (par3EntityLivingBase.worldObj.isRemote) {
			return false;
		}
		if (par1ItemStack != null && par1ItemStack.getItemDamage() == 0 && par3EntityLivingBase != null) {
			int entityID = EntityList.getEntityID(par3EntityLivingBase);
			if (!EntityList.entityEggs.containsKey(Integer.valueOf(entityID))) {
				return false;
			}
			par3EntityLivingBase.setDead();
			ItemStack newEgg = new ItemStack(Items.spawn_egg, 1, entityID);
			if (--par1ItemStack.stackSize <= 0) {
				par1ItemStack = null;
			}

			if (par3EntityLivingBase.entityDropItem(newEgg, 1.0F) != null)
				return true;
		}
		return false;
	}

	//ItemMonsterPlacerでメソッドが使用されているので，オーバーライド．意味はなかった．
	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (par1ItemStack.getItemDamage() != 0)
			return super.onItemRightClick(par1ItemStack, par2World, par3EntityPlayer);
		else {
			if (par2World.isRemote) {
				return par1ItemStack;
			} else {
				MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(par2World,
						par3EntityPlayer, true);

				if (movingobjectposition == null) {
					return par1ItemStack;
				} else {
					if (movingobjectposition.typeOfHit == MovingObjectType.ENTITY) {
						Entity entity = movingobjectposition.entityHit;
						if (entity instanceof EntityLivingBase) {
							if (this.itemInteractionForEntity(par1ItemStack, par3EntityPlayer,
									(EntityLivingBase) entity))
								return null;
							else
								return par1ItemStack;
						}
					}
					return par1ItemStack;
				}
			}
		}
	}

	//左クリックでも動作するように．こちらは動く．
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase,
			EntityLivingBase par3EntityLivingBase)
	{
		this.itemInteractionForEntity(par1ItemStack, (EntityPlayer) par3EntityLivingBase, par2EntityLivingBase);
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		par3List.add(new ItemStack(par1, 1, 0));
	}
}
