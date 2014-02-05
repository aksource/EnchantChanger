package ak.EnchantChanger;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class EcMasterMateriaRecipe implements IRecipe
{
	private ItemStack output = null;
	@Override
	public boolean matches(InventoryCrafting var1, World var2)
	{
		ItemStack[] materia = new ItemStack[]{null,null,null,null,null,null};
		boolean flag = false;
		ItemStack craftitem;
		for(int i=0; i< var1.getSizeInventory();i++)
		{
			craftitem = var1.getStackInSlot(i);
			if(craftitem == null)
				continue;
			if(craftitem.getItem() instanceof EcItemMateria && craftitem.isItemEnchanted())
			{
				if(materia[0] == null)
					materia[0] = craftitem;
				else if(materia[1] == null && !ItemStack.areItemStacksEqual(materia[0], craftitem))
					materia[1] = craftitem;
				else if(materia[2] == null && !ItemStack.areItemStacksEqual(materia[0], craftitem) && !ItemStack.areItemStacksEqual(materia[1], craftitem))
					materia[2] = craftitem;
				else if(materia[3] == null && !ItemStack.areItemStacksEqual(materia[0], craftitem) && !ItemStack.areItemStacksEqual(materia[1], craftitem) && !ItemStack.areItemStacksEqual(materia[2], craftitem))
					materia[3] = craftitem;
				else if(materia[4] == null && !ItemStack.areItemStacksEqual(materia[0], craftitem) && !ItemStack.areItemStacksEqual(materia[1], craftitem) && !ItemStack.areItemStacksEqual(materia[2], craftitem) && !ItemStack.areItemStacksEqual(materia[3], craftitem))
					materia[4] = craftitem;
				else if(materia[5] == null && !ItemStack.areItemStacksEqual(materia[0], craftitem) && !ItemStack.areItemStacksEqual(materia[1], craftitem) && !ItemStack.areItemStacksEqual(materia[2], craftitem) && !ItemStack.areItemStacksEqual(materia[3], craftitem) && !ItemStack.areItemStacksEqual(materia[4], craftitem))
					materia[5] = craftitem;
				else
					return false;
			}
			else
				return false;
		}
		if(materia[5] != null)
		{
			if(this.chekEnchmateria(materia, 6, 16, 21))
			{
				this.output = new ItemStack(EnchantChanger.MasterMateria,1,3);
				flag = true;
			}
			else if(this.chekEnchmateria(materia, 5, 0, 4) || this.chekEnchmateria(materia, 5, 7, 7))
			{
				this.output = new ItemStack(EnchantChanger.MasterMateria,1,1);
				flag = true;
			}
		}
		else if(materia[3] != null)
		{
			if(this.chekEnchmateria(materia, 4, 32, 35))
			{
				this.output = new ItemStack(EnchantChanger.MasterMateria,1,4);
				flag = true;
			}
			else if(this.chekEnchmateria(materia, 4, 48, 51))
			{
				this.output = new ItemStack(EnchantChanger.MasterMateria,1,5);
				flag = true;
			}
		}
		else if(materia[2] == null)
		{
			if(this.chekEnchmateria(materia, 2, 5, 6))
			{
				this.output = new ItemStack(EnchantChanger.MasterMateria,1,2);
				flag = true;
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
		return 3;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return this.output;
	}
	public boolean chekEnchmateria(ItemStack[] items, int num, int init, int end)
	{
		boolean ret = true;
		for(int i = 0;i<num;i++)
		{
			ret = ret && checkEnch(items[i], init, end);
		}
		return ret;
	}
	public boolean checkEnch(ItemStack materia, int init, int end)
	{
		boolean ret=false;
		for(int i=init;i<=end;i++)
		{
			if(EnchantmentHelper.getEnchantmentLevel(i, materia) == Enchantment.enchantmentsList[i].getMaxLevel())
			{
				ret = true;
				break;
			}
		}
		return ret;
	}
}