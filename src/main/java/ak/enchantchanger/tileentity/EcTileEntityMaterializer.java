package ak.enchantchanger.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class EcTileEntityMaterializer extends TileEntity implements IInventory {

    private final NonNullList<ItemStack> materializerItemstacks = NonNullList.withSize(7, ItemStack.EMPTY);

    public EcTileEntityMaterializer() {}

    @Override
    public int getSizeInventory() {
        return materializerItemstacks.size();
    }


    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return materializerItemstacks.get(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        materializerItemstacks.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amt) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                stack = stack.splitStack(amt);
                if (stack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }
        return stack;
    }


    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty()) {
            setInventorySlotContents(slot, ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return world.getTileEntity(this.getPos()) == this &&
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

    @Override
    public boolean isEmpty() {
        return materializerItemstacks.stream().filter(itemStack -> !itemStack.isEmpty()).count() > 0;
    }
}