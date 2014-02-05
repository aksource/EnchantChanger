package ak.EnchantChanger;

import net.minecraft.inventory.InventoryBasic;

public class EcSlotPortableEnchantmentTable extends InventoryBasic
{
	/** The brewing stand this slot belongs to. */
	final EcContainerPortableEnchantment container;

	EcSlotPortableEnchantmentTable(EcContainerPortableEnchantment par1ContainerEnchantment, String par2Str, int par3)
	{
		super(par2Str, true, par3);
		this.container = par1ContainerEnchantment;
	}

	public int getInventoryStackLimit()
	{
		return 1;
	}

	public void onInventoryChanged()
	{
		super.markDirty();
		this.container.onCraftMatrixChanged(this);
	}
}
