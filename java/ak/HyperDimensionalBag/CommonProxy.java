package ak.HyperDimensionalBag;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler
{
	public void registerClientInfo(){}
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == HyperDimensionalBag.guiID)
		{
			ItemStack heldItem = player.getCurrentEquippedItem();
			if( heldItem!= null && heldItem.getItem() instanceof ItemHDBag)
			{
				return new ContainerBag(player.inventory, ItemHDBag.getBagData(heldItem, world), heldItem.getItemDamage());
			}
			else return null;
		}
		else return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == HyperDimensionalBag.guiID)
		{
			ItemStack heldItem = player.getCurrentEquippedItem();
			if( heldItem!= null && heldItem.getItem() instanceof ItemHDBag)
			{
				return new GuiBag(player.inventory, ItemHDBag.getBagData(heldItem, world), heldItem.getItemDamage());
			}
			else return null;
		}
		else return null;
	}
	
}