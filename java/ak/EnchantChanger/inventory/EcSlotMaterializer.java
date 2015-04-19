package ak.EnchantChanger.inventory;
import net.minecraft.inventory.InventoryBasic;
class EcSlotMaterializer extends InventoryBasic
{
	final EcContainerMaterializer container;

	EcSlotMaterializer(EcContainerMaterializer par1ContainerMaterializer, String par2Str, int par3)
	{
		super(par2Str, true, par3);
		this.container = par1ContainerMaterializer;
	}
	@Override
	public int getInventoryStackLimit()
	{
		return 1;
	}
	@Override
	public void markDirty()
	{
		super.markDirty();
		this.container.onCraftMatrixChanged(this);
	}
}
