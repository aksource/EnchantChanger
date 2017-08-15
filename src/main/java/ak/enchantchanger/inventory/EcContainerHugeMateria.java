package ak.enchantchanger.inventory;

import ak.enchantchanger.item.EcItemMasterMateria;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;


public class EcContainerHugeMateria extends Container {

    private final EcTileEntityHugeMateria tileEntity;
    private int lastMaterializingTime = 0;

    public EcContainerHugeMateria(InventoryPlayer inventoryPlayer, EcTileEntityHugeMateria te) {
        tileEntity = te;
        addSlotToContainer(new Slot(te, 0, 26, 17));
        addSlotToContainer(new Slot(te, 1, 26, 48));
        addSlotToContainer(new Slot(te, 2, 53, 48));
        addSlotToContainer(new Slot(te, 3, 80, 34));
        addSlotToContainer(new EcSlotMakeMateria(te, 4, 116, 34));
        bindPlayerInventory(inventoryPlayer);
    }


    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return this.tileEntity.isUsableByPlayer(player);
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        listener.sendWindowProperty(this, 0, this.tileEntity.materializingTime);
    }

    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {

            if (this.lastMaterializingTime != this.tileEntity.materializingTime) {
                listener.sendWindowProperty(this, 0, this.tileEntity.materializingTime);
            }

        }

        this.lastMaterializingTime = this.tileEntity.materializingTime;
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
        if (par1 == 0) {
            this.tileEntity.materializingTime = par2;
        }
    }

    private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack retItem = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            retItem = itemstack.copy();

            if (index >= 0 && index < 5) {
                if (!this.mergeItemStack(itemstack, 5, 5 + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack.getItem() instanceof EcItemMasterMateria) {
                    if (!this.mergeItemStack(itemstack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (itemstack.getItem() instanceof EcItemMateria && itemstack.getItemDamage() == 0) {
                    if (!this.mergeItemStack(itemstack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (tileEntity.isBottle(itemstack)) {
                    if (!this.mergeItemStack(itemstack, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    if (!this.mergeItemStack(itemstack, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }

            if (itemstack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack.getCount() == retItem.getCount()) {
                return ItemStack.EMPTY;
            }

            retItem = slot.onTake(playerIn, itemstack);
        }

        return retItem;

    }
}