package ak.EnchantChanger.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class EcSlotItemMateria extends Slot {
    final EcContainerMaterializer container;
    final IInventory materializeSource;
    final IInventory materializeResult;

    public EcSlotItemMateria(EcContainerMaterializer par1ContainerMaterializer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
        super(par3IInventory, par4, par5, par6);
        this.container = par1ContainerMaterializer;
        this.materializeResult = par2IInventory;
        this.materializeSource = par3IInventory;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return true;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
        super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
        this.materializeResult.markDirty();
        this.materializeSource.markDirty();
    }
}