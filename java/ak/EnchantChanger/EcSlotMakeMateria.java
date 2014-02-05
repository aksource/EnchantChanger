package ak.EnchantChanger;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
public class EcSlotMakeMateria extends Slot
{
	private final IInventory tileentity;

	public EcSlotMakeMateria(IInventory par1IInventory, int par2, int par3, int par4)
	{
		super(par1IInventory, par2, par3, par4);
		this.tileentity = par1IInventory;
	}
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return false;
	}
	public int getSlotStackLimit()
	{
		return 1;
	}
}