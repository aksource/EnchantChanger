package ak.enchantchanger.inventory;

import ak.enchantchanger.item.EcItemBucketLifeStream;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

import static ak.enchantchanger.tileentity.EcTileEntityMakoReactor.*;

/**
 * 魔晄炉のContainerクラス
 * Created by A.K. on 14/09/20.
 */
public class EcContainerMakoReactor extends Container {
    private final EcTileEntityMakoReactor tileEntityMakoReactor;
    private int lastSmeltingTime;

    public EcContainerMakoReactor(InventoryPlayer inventoryPlayer, EcTileEntityMakoReactor te) {
        this.tileEntityMakoReactor = te;
        int i;
        for (i = 0; i < SLOTS_MATERIAL.length; i++) {
            addSlotToContainer(new Slot(te, SLOTS_MATERIAL[i], 26 + i * 18, 19));
        }

        addSlotToContainer(new Slot(te, SLOTS_FUEL[0], 44, 55));

        for (i = 0; i < SLOTS_RESULT.length - 1; i++) {
            addSlotToContainer(new Slot(te, SLOTS_RESULT[i], 116 + i * 18, 46) {
                @Override
                public boolean isItemValid(@Nonnull ItemStack itemStack) {
                    return false;
                }
            });
        }
        addSlotToContainer(new Slot(te, SLOTS_RESULT[SLOTS_RESULT.length - 1], 116 + 18, 46 + 18) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack itemStack) {
                return false;
            }
        });

        bindPlayerInventory(inventoryPlayer);
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
    public boolean canInteractWith(@Nonnull EntityPlayer entityPlayer) {
        return this.tileEntityMakoReactor.isUsableByPlayer(entityPlayer);
    }

    @Override
    public void addListener(@Nonnull IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileEntityMakoReactor);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for (IContainerListener listener : this.listeners) {

            if (this.lastSmeltingTime != this.tileEntityMakoReactor.smeltingTime) {
                listener.sendProgressBarUpdate(this, 0, this.tileEntityMakoReactor.smeltingTime);
            }

        }

        this.lastSmeltingTime = this.tileEntityMakoReactor.smeltingTime;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.tileEntityMakoReactor.smeltingTime = data;
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer player, int slotIndex) {
        ItemStack retItem = ItemStack.EMPTY;
        Slot slot = this.getSlot(slotIndex);
        if (slot.getHasStack()) {
            ItemStack item = slot.getStack();
            retItem = item.copy();
            if (slotIndex >= 0 && slotIndex < SUM_OF_ALLSLOTS) {
                if (!mergeItemStack(item, SUM_OF_ALLSLOTS, SUM_OF_ALLSLOTS + 36, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(item, retItem);
            } else {
                if (item.getItem() instanceof EcItemBucketLifeStream || item.getItem() instanceof EcItemMateria) {
                    if (!mergeItemStack(item, SLOTS_FUEL[0], SLOTS_FUEL[SLOTS_FUEL.length - 1] + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                if (!FurnaceRecipes.instance().getSmeltingResult(item).isEmpty()) {
                    if (!mergeItemStack(item, SLOTS_MATERIAL[0], SLOTS_MATERIAL[SLOTS_MATERIAL.length - 1] + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            if (item.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (item.getCount() == retItem.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, item);
        }
        return retItem;
    }
}
