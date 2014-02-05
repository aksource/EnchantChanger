package StackableTools;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemSwordStack extends ItemSword
{
	public static final String[] toolMaterialNames = {"Wood", "Stone", "Iron", "Diamond", "Gold"};
	public float efficiencyOnWeb = 4.0F;
	public ItemSwordStack(ToolMaterial par2EnumToolMaterial)
	{
		super(par2EnumToolMaterial);
		this.setMaxStackSize(StackableTools.SwordMax);
		this.setHasSubtypes(true);
		this.efficiencyOnWeb = par2EnumToolMaterial.getEfficiencyOnProperMaterial()
							 * par2EnumToolMaterial.getEfficiencyOnProperMaterial() / 2 + 10.0F;
		setUnlocalizedName("sword" + toolMaterialNames[par2EnumToolMaterial.ordinal()]);
		this.setTextureName(ItemSwordStack.toolMaterialNames[par2EnumToolMaterial.ordinal()].toLowerCase() + "_sword");
	}
	@Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
    	par3List.add("Stackable");
    }
	@Override
	public float func_150893_a(ItemStack par1ItemStack, Block par2Block)
	{
		return par2Block == Blocks.web ? this.efficiencyOnWeb : 1.5F;
	}
	@Override
	public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase)
	{
		if(par2EntityLivingBase.getMaxHealth()>1)
		{
			par1ItemStack.damageItem(1, par3EntityLivingBase);
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
	@Override
	public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, Block par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase)
	{
		if ((double)par3.getBlockHardness(par2World, par4, par5, par6) > 0.0D)
		{	
			if(par3 == Blocks.web)
			{
				par1ItemStack.damageItem(StackableTools.WebDamage, par7EntityLivingBase);
			}
			else
			{
				par1ItemStack.damageItem(2, par7EntityLivingBase);
			}
		}
		return true;
	}

}
