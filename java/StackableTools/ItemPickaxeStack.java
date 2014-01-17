package StackableTools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPickaxeStack extends ItemPickaxe
{
	public ItemPickaxeStack(ToolMaterial par2EnumToolMaterial)
	{
		super(par2EnumToolMaterial);
		this.setMaxStackSize(StackableTools.PickaxeMax);
		this.setHasSubtypes(true);
		setUnlocalizedName("pickaxe" + ItemSwordStack.toolMaterialNames[par2EnumToolMaterial.ordinal()]);
		this.setTextureName(ItemSwordStack.toolMaterialNames[par2EnumToolMaterial.ordinal()].toLowerCase() + "_pickaxe");
	}
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    	par3List.add("Stackable");
    }
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLiving, EntityLivingBase par3EntityLiving)
	{
		if(par2EntityLiving.getMaxHealth()>1)
		{
			par1ItemStack.damageItem(2, par3EntityLiving);
		}
		return true;
	}
	@Override
    public boolean func_150894_a(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLiving)
    {
        if ((double)par3.func_149712_f(par2World, par4, par5, par6) > 0.0D)
		{
			par1ItemStack.damageItem(1, par7EntityLiving);
		}
		return true;
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
