package MergeEnchantment;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class AddEnchantmentRecipes implements IRecipe
{

	private ItemStack output =null;

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		int toolflag = 0;
		ItemStack tool = null;
		int bookflag = 0;
		ItemStack book = null;
		boolean flag =false;
		int EnchInt1;
		Enchantment ench1;
		int EnchInt2;
		Enchantment ench2;
		ItemStack craftitem;
		for(int i=0; i< inv.getSizeInventory();i++)
		{
			craftitem = inv.getStackInSlot(i);
			if(craftitem !=null)
			{
				if(craftitem.getItem().isRepairable())
				{
					toolflag++;
					tool = craftitem.copy();
				}
				else if(craftitem.getItem() instanceof ItemEnchantedBook)
				{
					bookflag++;
					book = craftitem.copy();
				}
				else
				{
					return false;
				}
			}
			else continue;
		}
		if(toolflag > 0 && toolflag < 2 && bookflag > 0 && bookflag < 2)
		{
//			System.out.println(tool);
			Map toolenchlist = EnchantmentHelper.getEnchantments(tool);
			Map bookenchlist = EnchantmentHelper.getEnchantments(book);
			Iterator var1 = bookenchlist.keySet().iterator();
			Iterator var2 = toolenchlist.keySet().iterator();
			while (var1.hasNext())
			{
				EnchInt1 = ((Integer)var1.next()).intValue();
//				System.out.println(EnchInt1);
				ench1 = Enchantment.enchantmentsList[EnchInt1];
				int var3 = toolenchlist.containsKey(Integer.valueOf(EnchInt1)) ? ((Integer)toolenchlist.get(Integer.valueOf(EnchInt1))).intValue() : 0;
				int var4 = ((Integer)bookenchlist.get(Integer.valueOf(EnchInt1))).intValue();
//				System.out.println(var4);
				int Max;
				if (var3 == var4)
				{
					Max = var4;
				}
				else
				{
					Max = Math.max(var4, var3);
				}

				var4 = Max;
				flag =  ench1.canApplyAtEnchantingTable(tool);
//				System.out.println(flag);
				while (var2.hasNext())
				{
					EnchInt2 = ((Integer)var2.next()).intValue();
					ench2 = Enchantment.enchantmentsList[EnchInt2];
					flag = ench1.canApplyTogether(ench2);
//					System.out.println(flag);
				}
				toolenchlist.put(Integer.valueOf(EnchInt1), Integer.valueOf(var4));
			}
			this.output = tool;
			int var5 = (this.output.getItemDamage() - this.output.getMaxDamage()/20 <1)? 1 : this.output.getItemDamage() - this.output.getMaxDamage()/20;
			this.output.setItemDamage(var5);
			EnchantmentHelper.setEnchantments(toolenchlist, this.output);
			return flag;
		}
		else
			return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventorycrafting)
	{
		return this.output.copy();
	}

	@Override
	public int getRecipeSize() {
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return this.output;
	}
}
