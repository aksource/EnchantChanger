package ak.EnchantChanger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
public class EcSlotItemToEnchant extends Slot
{
	final EcContainerMaterializer container;
	final IInventory materializeResult;
	final IInventory materializeSource;
	public EcSlotItemToEnchant(EcContainerMaterializer par1ContainerMaterializer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6)
	{
		super(par3IInventory, par4, par5, par6);
		this.container = par1ContainerMaterializer;
		this.materializeResult = par2IInventory;
		this.materializeSource = par3IInventory;
	}
	public boolean isItemValid(ItemStack par1ItemStack)
	{
		return !(par1ItemStack.getItem() instanceof EcItemMateria);
	}
	public int getSlotStackLimit()
	{
		return 1;
	}
	@Override
	public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack)
	{
		super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
		for (int i =0; i <EcContainerMaterializer.ResultSlotNum;i++)
			this.materializeResult.setInventorySlotContents(i, null);
	}
}