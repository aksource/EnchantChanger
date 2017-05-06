package ak.enchantchanger.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class EcSlotEnchantedItem extends Slot {
    final EcContainerMaterializer container;
    private final IInventory materializeSource;
    private final IInventory materializeResult;

    public EcSlotEnchantedItem(EcContainerMaterializer par1ContainerMaterializer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
        super(par3IInventory, par4, par5, par6);
        this.container = par1ContainerMaterializer;
        this.materializeSource = par2IInventory;
        this.materializeResult = par3IInventory;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return false;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
        super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
        for (int i = 0; i < EcContainerMaterializer.SourceSlotNum; i++) {
            this.materializeSource.decrStackSize(i, 1);
        }
        InventoryPlayer IP = par1EntityPlayer.inventory;
        for (int i = 0; i < EcContainerMaterializer.ResultSlotNum; i++) {
            ItemStack SlotStack = this.materializeResult.getStackInSlot(i);
            if (SlotStack == null) {
                continue;
            }
            if (IP.addItemStackToInventory(SlotStack.copy())) {
                this.materializeResult.setInventorySlotContents(i, null);
            } else {
                par1EntityPlayer.dropPlayerItemWithRandomChoice(SlotStack.copy(), false);
                this.materializeResult.setInventorySlotContents(i, null);
            }
        }
    }
}