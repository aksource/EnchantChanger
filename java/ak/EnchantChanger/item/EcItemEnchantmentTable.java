package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EcItemEnchantmentTable extends EcItem
{

	public EcItemEnchantmentTable(String name) {
		super(name);
		maxStackSize = 1;
		setMaxDamage(0);
        this.setTextureName(Constants.EcTextureDomain + "PortableEnchantmentTable");
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.openGui(EnchantChanger.instance, Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE,par2World,0,0,0);

		return par1ItemStack;
	}

}