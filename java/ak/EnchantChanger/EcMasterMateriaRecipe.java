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
    private static final int[][] enchantmentRecipeList = {{}, {0, 1, 2, 3, 4, 7}, {5, 6, 61, 62}, {16, 17, 18, 19, 20, 21}, {32, 33, 34, 35}, {48, 49, 50, 51}};
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
		for(ItemStack itemStack : items) {
			ret = ret && checkEnch(itemStack, num);
		}
		return ret;
	}
	public boolean checkEnch(ItemStack materia, int num)
	{
		boolean ret=false;
		for(int i : enchantmentRecipeList[num]) {
			if(EnchantmentHelper.getEnchantmentLevel(i, materia) == Enchantment.enchantmentsList[i].getMaxLevel()) {
				ret = true;
				break;
			}
		}
		return ret;
	}
}