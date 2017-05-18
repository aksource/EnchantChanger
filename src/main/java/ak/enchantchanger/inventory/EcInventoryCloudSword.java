package ak.enchantchanger.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class EcInventoryCloudSword extends InventoryBasic {
    public ItemStack sword;

    public EcInventoryCloudSword(ItemStack stack) {
        super("CloudSwordData", false, 5);
        this.sword = stack;
        if (!sword.hasTagCompound()) {
            sword.setTagCompound(new NBTTagCompound());
        }
        readFromNBT(sword.getTagCompound());
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        writeToNBT(sword.getTagCompound());
    }

    @Override
    public void openInventory(EntityPlayer player) {
        super.openInventory(player);
        if (!sword.hasTagCompound()) {
            sword.setTagCompound(new NBTTagCompound());
        }
        readFromNBT(sword.getTagCompound());
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        super.closeInventory(player);
        writeToNBT(sword.getTagCompound());
    }

    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList tagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);

        for (int count = 0; count < tagList.tagCount(); ++count) {
            NBTTagCompound var4 = tagList.getCompoundTagAt(count);
            int slot = var4.getByte("Slot") & 255;

            if (slot >= 0 && slot < this.getSizeInventory()) {
                this.setInventorySlotContents(slot, new ItemStack(var4));
            }
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagList tagList = new NBTTagList();

        for (int var3 = 0; var3 < this.getSizeInventory(); ++var3) {
            if (!this.getStackInSlot(var3).isEmpty()) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) var3);
                this.getStackInSlot(var3).writeToNBT(var4);
                tagList.appendTag(var4);
            }
        }

        nbt.setTag("Items", tagList);
    }
}
