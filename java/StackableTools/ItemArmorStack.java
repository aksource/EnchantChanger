package StackableTools;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemArmorStack extends ItemArmor
{
	private static final String[] partsNames = {"helmet", "chestplate", "leggings", "boots"};
	private static final String[] materialNames = {"Cloth", "Chain", "Iron", "Diamond", "Gold"};
	private static final String[] materialfileNames = {"leather_", "chainmail_", "iron_", "diamond_", "gold_"};
	public ItemArmorStack(ArmorMaterial enumArmorMaterial, int material, int parts)
	{
		super(enumArmorMaterial, material, parts);
		this.setHasSubtypes(true);
		this.setMaxStackSize(StackableTools.ArmorMax);
		setUnlocalizedName(partsNames[parts] + materialNames[material]);
		this.setTextureName(materialfileNames[material] + partsNames[parts]);
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
