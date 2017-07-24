package ak.enchantchanger.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class EcTileEntityMaterializer extends TileEntity implements IInventory {

    private final List<ItemStack> materializerItemstacks = new ArrayList<>(7);

    public EcTileEntityMaterializer() {}

    @Override
    public int getSizeInventory() {
        return materializerItemstacks.size();
    }


    @Override
    public ItemStack getStackInSlot(int slot) {
        return materializerItemstacks.get(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        materializerItemstacks.set(slot, stack);
        if (stack != null && stack.stackSize > getInventoryStackLimit()) {
            stack.stackSize = getInventoryStackLimit();
        }
    }

    @Override
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            if (stack.stackSize <= amt) {
                setInventorySlotContents(slot, null);
            } else {
                stack = stack.splitStack(amt);
                if (stack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        return stack;
    }


    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(@Nonnull EntityPlayer player) {
        return worldObj.getTileEntity(this.getPos()) == this &&
                player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D)) < 64;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }
    @Override
    @Nonnull
    public String getName() {
        return "container.materializer";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, @Nonnull ItemStack itemstack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }
}