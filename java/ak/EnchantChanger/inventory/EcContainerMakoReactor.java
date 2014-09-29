package ak.EnchantChanger.inventory;

import ak.EnchantChanger.item.EcItemBucketLifeStream;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

import static ak.EnchantChanger.tileentity.EcTileEntityMakoReactor.*;

/**
 * Created by A.K. on 14/09/20.
 */
public class EcContainerMakoReactor extends Container {
    private EcTileEntityMakoReactor tileEntityMakoReactor;
    public EcContainerMakoReactor(InventoryPlayer inventoryPlayer, EcTileEntityMakoReactor te) {
        this.tileEntityMakoReactor = te;
        int i;
        for (i = 0; i < slotsMaterial.length; i++) {
            addSlotToContainer(new Slot(te, slotsMaterial[i], 26 + i * 18, 14));
        }

        addSlotToContainer(new Slot(te, slotsFuel[0], 44, 55));

        for (i = 0; i < slotsResult.length; i++) {
            addSlotToContainer(new Slot(te, slotsResult[i], 115 + i * 18, 46) {
                @Override
                public boolean isItemValid(ItemStack itemStack) {
                    return false;
                }
            });
        }

        bindPlayerInventory(inventoryPlayer);
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
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
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.tileEntityMakoReactor.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) {
        ItemStack retItem = null;
        Slot slot = this.getSlot(slotIndex);
        if (slot != null && slot.getHasStack()) {
            ItemStack item = slot.getStack();
            retItem = item.copy();
            if (slotIndex >= 0 && slotIndex < materiafuelresult) {
                if (!mergeItemStack(item, materiafuelresult, materiafuelresult + 36, true)) {
                    return null;
                }
                slot.onSlotChange(item, retItem);
            } else {
                if (item.getItem() instanceof EcItemBucketLifeStream || item.getItem() instanceof EcItemMateria) {
                    if (!mergeItemStack(item, slotsFuel[0], slotsFuel[slotsFuel.length - 1], false)) {
                        return null;
                    }
                }
                if (FurnaceRecipes.smelting().getSmeltingResult(item) != null) {
                    if (!mergeItemStack(item, slotsMaterial[0], slotsMaterial[slotsMaterial.length - 1], false)) {
                        return null;
                    }
                }
            }
            if (item.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (item.stackSize == retItem.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(player, item);
        }
        return retItem;
    }
}
