package ak.EnchantChanger;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
class EcSlotResult extends InventoryBasic
{
	/** The brewing stand this slot belongs to. */
	final EcContainerMaterializer container;
	private ItemStack[] ResultContents;

	EcSlotResult(EcContainerMaterializer par1ContainerMaterializer, String par2Str, int par3)
	{
		super(par2Str, true, par3);
		this.container = par1ContainerMaterializer;
		this.ResultContents = new ItemStack[EcContainerMaterializer.ResultSlotNum];
	}

	public int getInventoryStackLimit()
	{
		return 1;
	}
}