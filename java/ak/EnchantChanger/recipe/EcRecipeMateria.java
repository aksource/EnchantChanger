package ak.EnchantChanger.recipe;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.utils.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class EcRecipeMateria implements IRecipe
{
	private ItemStack output = null;
	@Override
	public boolean matches(InventoryCrafting var1, World var2)
	{
		ItemStack materia1 = null;
		ItemStack materia2 = null;
		ItemStack expBottle = null;
		ItemStack exExpBottle = null;
		boolean flag = false;
		ItemStack craftitem;
		for(int i=0; i< var1.getSizeInventory();i++)
		{
			craftitem = var1.getStackInSlot(i);
			if(craftitem == null)
				continue;
			if(craftitem.getItem() instanceof EcItemMateria)
			{
				if(materia1 == null)
					materia1 = craftitem;
				else if(materia2 == null)
					materia2 = craftitem;
				else
					return false;
			}
			else if(craftitem.getItem() == Items.experience_bottle && expBottle == null && exExpBottle == null && materia2 == null)
				expBottle = craftitem;
			else if(craftitem.getItem() == EnchantChanger.itemExExpBottle && expBottle == null && exExpBottle == null && materia2 == null)
				exExpBottle = craftitem;
			else
				return false;
		}
		if(materia2 != null && materia1.isItemEnchanted() && materia2.isItemEnchanted())
		{
			if(ItemStack.areItemStackTagsEqual(materia1, materia2))
			{
				this.output = materia1.copy();
				this.output.stackSize = 1;
				output.getTagCompound().removeTag("ench");
				EnchantmentUtils.addEnchantmentToItem(output, Enchantment.enchantmentsList[EnchantmentUtils.getMateriaEnchKind(materia1)], EnchantmentUtils.getMateriaEnchLv(materia1) + 1);
				flag = true;
			}
		}
		else if(materia1 != null)
		{
			if(materia1.getItemDamage() == 0)
			{
				if(expBottle != null && EnchantmentUtils.getMateriaEnchLv(materia1) < 5 && materia1.isItemEnchanted())
				{
					this.output = materia1.copy();
					this.output.stackSize = 1;
					output.getTagCompound().removeTag("ench");
					EnchantmentUtils.addEnchantmentToItem(output, Enchantment.enchantmentsList[EnchantmentUtils.getMateriaEnchKind(materia1)], EnchantmentUtils.getMateriaEnchLv(materia1) + 1);
					flag = true;
				}
				else if(exExpBottle != null && EnchantmentUtils.getMateriaEnchLv(materia1) >= 5 && materia1.isItemEnchanted())
				{
					this.output = materia1.copy();
					this.output.stackSize = 1;
					output.getTagCompound().removeTag("ench");
					EnchantmentUtils.addEnchantmentToItem(output, Enchantment.enchantmentsList[EnchantmentUtils.getMateriaEnchKind(materia1)], EnchantmentUtils.getMateriaEnchLv(materia1) + 1);
					flag = true;
				}
				else if( expBottle == null && exExpBottle == null)
				{
					this.output = new ItemStack(EnchantChanger.itemMateria, 1, 0);
					flag = true;
				}
			}
		}
		return flag;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1)
	{
		return this.output.copy();
	}

	@Override
	public int getRecipeSize()
	{
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return this.output;
	}

}