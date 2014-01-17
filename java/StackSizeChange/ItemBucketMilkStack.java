package StackSizeChange;

import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ItemBucketMilkStack extends ItemBucketMilk
{
	public ItemBucketMilkStack()
	{
		super();
	}
	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		if (!par2World.isRemote)
		{
			par3EntityPlayer.clearActivePotions();
		}

		return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, new ItemStack(StackSizeChange.bucketEmpty));
	}
	@Override
    public IIcon getIconFromDamage(int par1)
    {
		if(!StackSizeChange.BucketReplace && StackSizeChange.addStackableBucket)
		{
			return Items.milk_bucket.getIconFromDamage(par1);
		}
		else
		{
	        return super.getIconFromDamage(par1);
		}
    }

	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event)
	{
		if(!(event.target instanceof EntityCow))
		{
			return;
		}
		
		ItemStack itemstack = event.entityPlayer.getCurrentEquippedItem();
		if(itemstack != null && itemstack.getItem() == StackSizeChange.bucketEmpty)
		{
			ItemStack result = StackSizeChange.addropItems(itemstack, event.entityPlayer, new ItemStack(StackSizeChange.bucketMilk), true, true);
			event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, result);
			event.setResult(Result.ALLOW);
			return;
		}
		return;
	}

}
