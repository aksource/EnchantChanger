package ak.enchantchanger.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

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
    @Nonnull
    public ItemStack onTake(@Nonnull EntityPlayer thePlayer, @Nonnull ItemStack stack) {
        for (int i = 0; i < EcContainerMaterializer.SourceSlotNum; i++) {
            this.materializeSource.decrStackSize(i, 1);
        }
        InventoryPlayer inventoryPlayer = thePlayer.inventory;
        for (int i = 0; i < EcContainerMaterializer.RESULT_SLOT_NUM; i++) {
            ItemStack SlotStack = this.materializeResult.getStackInSlot(i);
            if (SlotStack.isEmpty()) {
                continue;
            }
            if (inventoryPlayer.addItemStackToInventory(SlotStack.copy())) {
                this.materializeResult.setInventorySlotContents(i, ItemStack.EMPTY);
            } else {
                thePlayer.dropItem(SlotStack.copy(), false);
                this.materializeResult.setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
        return super.onTake(thePlayer, stack);
    }
}