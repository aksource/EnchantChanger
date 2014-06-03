package ak.EnchantChanger.inventory;

import ak.EnchantChanger.item.EcItemCloudSword;
import ak.EnchantChanger.item.EcItemCloudSwordCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EcInventoryCloudSword implements IInventory{
    public EcCloudSwordData data;

    public EcInventoryCloudSword(ItemStack stack, World world) {
        if(stack.getItem() instanceof EcItemCloudSword)
            data = ((EcItemCloudSword)stack.getItem()).getSwordData(stack,world);
        else if(stack.getItem() instanceof EcItemCloudSwordCore)
            data = ((EcItemCloudSwordCore)stack.getItem()).getSwordData(stack, world);
    }
    @Override
    public int getSizeInventory()
    {
        return 5;
    }

    @Override
    public ItemStack getStackInSlot(int var1)
    {
        return data.swords[var1];
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2)
    {
        if(data.swords[var1] != null)
        {
            ItemStack var3;
            if(data.swords[var1].stackSize <= var2)
            {
                var3 = data.swords[var1];
                data.swords[var1] = null;
                this.markDirty();
                return var3;
            }
            else
            {
                var3 = data.swords[var1].splitStack(var2);

                if (data.swords[var1].stackSize == 0)
                {
                    data.swords[var1] = null;
                }

                this.markDirty();
                return var3;
            }
        }
        else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1)
    {
        return null;
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        data.swords[var1] = var2;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

	@Override
	public void markDirty() {
		data.upDate = true;
	}

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return true;
    }

    @Override
    public void openInventory() {}

    @Override
    public void closeInventory() {}
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return false;
    }
    @Override
    public String getInventoryName() {
        return "CloudSwordData";
    }
    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }
}
