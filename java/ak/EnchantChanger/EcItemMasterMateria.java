package ak.EnchantChanger;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EcItemMasterMateria extends Item
{
	public static final int MasterMateriaNum = 7;
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		int itemDmg = par1ItemStack.getItemDamage();
		return "ItemMasterMateria." + itemDmg;
	}
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<MasterMateriaNum;i++){
			par3List.add(new ItemStack(this,1,i));
		}
	}
}