package ak.EnchantChanger;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class EcSlotPortableEnchantment extends Slot
{
	/** The brewing stand this slot belongs to. */
	final EcContainerPortableEnchantment container;

	EcSlotPortableEnchantment(EcContainerPortableEnchantment par1ContainerEnchantment, IInventory par2IInventory, int par3, int par4, int par5)
	{
		super(par2IInventory, par3, par4, par5);
		this.container = par1ContainerEnchantment;
	}

	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return true;
	}
}
