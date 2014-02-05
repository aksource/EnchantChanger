package ak.EnchantChanger;
import net.minecraft.inventory.InventoryBasic;
class EcSlotMaterializer extends InventoryBasic
{
	/** The brewing stand this slot belongs to. */
	final EcContainerMaterializer container;

	EcSlotMaterializer(EcContainerMaterializer par1ContainerMaterializer, String par2Str, int par3)
	{
		super(par2Str, true, par3);
		this.container = par1ContainerMaterializer;
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
