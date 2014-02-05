package ak.EnchantChanger;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class EcItemMasterMateria extends Item
{
//	public static final String[] MasterMateriaNames = new String[] {"Ultimatum","Protection","Water","Attack","Digging","Bow","Addition"};
//	public static final String[] MasterMateriaJPNames = new String[] {"究極","防御","水","攻撃","採掘","弓", "追加"};
	public static int MasterMateriaNum = 7;
	public EcItemMasterMateria()
	{
		super();
		setHasSubtypes(true);
		maxStackSize = 1;
		setMaxDamage(0);
        this.setTextureName(EnchantChanger.EcTextureDomain + "MasterMateria");
	}
	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		int itemDmg = par1ItemStack.getItemDamage();
		return "ItemMasterMateria." + itemDmg;
	}
	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for(int i = 0;i<this.MasterMateriaNum;i++){
			par3List.add(new ItemStack(this,1,i));
		}
	}
}