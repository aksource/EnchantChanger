package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EcItemEnchantmentTable extends EcItem
{

	public EcItemEnchantmentTable(String name) {
		super(name);
		maxStackSize = 1;
		setMaxDamage(0);
        this.setTextureName(EnchantChanger.EcTextureDomain + "PortableEnchantmentTable");
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		par3EntityPlayer.openGui(EnchantChanger.instance, EnchantChanger.guiIdPortableEnchantmentTable,par2World,0,0,0);

		return par1ItemStack;
	}

}