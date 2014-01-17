package StackableTools;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemBowStack extends ItemBow
{
	public ItemBowStack()
	{
		super();
		this.setMaxStackSize(StackableTools.BowMax);
		this.setHasSubtypes(true);
		this.setTextureName("bow");
	}
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    	par3List.add("Stackable");
    }
	@Override
	public boolean isItemTool(ItemStack par1ItemStack)
	{
		return par1ItemStack.stackSize == 1;
	}
	@Override
	public boolean isDamageable()
	{
		return true;
	}

}
