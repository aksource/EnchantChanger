package StackSizeChange;

import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ItemSoupStack extends ItemFood
{
	public ItemSoupStack(int par2)
	{
		super(par2, false);
		setTextureName("mushroom_stew");
	}
	@Override
	public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		super.onEaten(par1ItemStack, par2World, par3EntityPlayer);
		return StackSizeChange.addropItems(par1ItemStack, par3EntityPlayer, new ItemStack(Item.bowlEmpty), false);
	}

	@SubscribeEvent
	public void onEntityInteractEvent(EntityInteractEvent event)
	{
		if(!(event.target instanceof EntityMooshroom))
		{
			return;
		}
		
		ItemStack itemstack = event.entityPlayer.getCurrentEquippedItem();
		if(itemstack != null && itemstack.itemID == Item.bowlEmpty.itemID)
		{
			ItemStack result = StackSizeChange.addropItems(itemstack, event.entityPlayer, new ItemStack(Item.bowlSoup), true, true);
			event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, result);
			event.setResult(Result.ALLOW);
			event.setCanceled(true);
			return;
		}
		return;
	}
}
