package ak.EnchantChanger.recipe;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.item.EcItemMateria;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class EcRecipeMasterMateria implements IRecipe
{
	private ItemStack output = null;
    private static final int[][] enchantmentRecipeList = {{}, {0, 1, 2, 3, 4, 7}, {5, 6, 61, 62}, {16, 17, 18, 19, 20, 21}, {32, 33, 34, 35}, {48, 49, 50, 51}};
	@Override
	public boolean matches(InventoryCrafting var1, World var2)
	{
		ItemStack[] materia = new ItemStack[]{null,null,null,null,null,null};
//		boolean flag = false;
		ItemStack craftItem;
		for(int i=0; i< var1.getSizeInventory();i++)
		{
			craftItem = var1.getStackInSlot(i);
			if(craftItem == null)
				continue;
			if(craftItem.getItem() instanceof EcItemMateria && craftItem.isItemEnchanted())
			{
				if(materia[0] == null)
					materia[0] = craftItem;
				else if(materia[1] == null && !ItemStack.areItemStacksEqual(materia[0], craftItem))
					materia[1] = craftItem;
				else if(materia[2] == null && !ItemStack.areItemStacksEqual(materia[0], craftItem) && !ItemStack.areItemStacksEqual(materia[1], craftItem))
					materia[2] = craftItem;
				else if(materia[3] == null && !ItemStack.areItemStacksEqual(materia[0], craftItem) && !ItemStack.areItemStacksEqual(materia[1], craftItem) && !ItemStack.areItemStacksEqual(materia[2], craftItem))
					materia[3] = craftItem;
				else if(materia[4] == null && !ItemStack.areItemStacksEqual(materia[0], craftItem) && !ItemStack.areItemStacksEqual(materia[1], craftItem) && !ItemStack.areItemStacksEqual(materia[2], craftItem) && !ItemStack.areItemStacksEqual(materia[3], craftItem))
					materia[4] = craftItem;
				else if(materia[5] == null && !ItemStack.areItemStacksEqual(materia[0], craftItem) && !ItemStack.areItemStacksEqual(materia[1], craftItem) && !ItemStack.areItemStacksEqual(materia[2], craftItem) && !ItemStack.areItemStacksEqual(materia[3], craftItem) && !ItemStack.areItemStacksEqual(materia[4], craftItem))
					materia[5] = craftItem;
				else
					return false;
			}
			else
				return false;
		}
        return searchMateria(materia);
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

    private boolean searchMateria(ItemStack[] items) {
        for (int i = 1; i < 6; i++) {
            if (checkEnchmateria(items, i)) {
                this.output = new ItemStack(EnchantChanger.itemMasterMateria, 1, i);
                return true;
            }
        }
        return false;
    }

	public boolean checkEnchmateria(ItemStack[] items, int num)
	{
		boolean ret = true;
		for(int index = 0; index < enchantmentRecipeList[num].length;index++) {
			ret = ret && checkEnch(items[index], num);
		}
		return ret;
	}
	public boolean checkEnch(ItemStack materia, int num)
	{
		boolean ret=false;
		for(int i : enchantmentRecipeList[num]) {
			if(EnchantmentHelper.getEnchantmentLevel(i, materia) >= Enchantment.enchantmentsList[i].getMaxLevel()) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}