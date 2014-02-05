package ak.HyperDimensionalBag;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class BagData extends WorldSavedData implements IInventory
{
	public ItemStack[] items = new ItemStack[54];
	private boolean init = false;
	private boolean upDate;

	public BagData(String par1Str)
	{
		super(par1Str);
	}
	public void onUpdate(World var1, EntityPlayer var2)
	{
		if(!this.init)
		{
			this.init = true;
//			this.onInventoryChanged();
		}
		if(var1.getWorldTime() % 80l == 0l)
			this.upDate = true;
		if(this.upDate)
		{
			this.markDirty();
			this.upDate = false;
		}
	}
	@Override
	public int getSizeInventory()
	{
		return items.length;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return items[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		if(items[var1] != null)
		{
			ItemStack var3;
			if(items[var1].stackSize <= var2)
			{
				var3 = items[var1];
				items[var1] = null;
				this.markDirty();
				return var3;
			}
			else
			{
				var3 = this.items[var1].splitStack(var2);

				if (this.items[var1].stackSize == 0)
				{
					this.items[var1] = null;
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
		items[var1] = var2;
	}

	@Override
	public String getInventoryName() {
		return "HDBag";
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
//
//	@Override
//	public void onInventoryChanged() {
//		this.upDate = true;
//	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {
		this.markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound var1) {
		NBTTagList var2 = var1.getTagList("Items", 10);
		this.items = new ItemStack[54];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.getCompoundTagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.items.length)
			{
				this.items[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.items.length; ++var3)
		{
			if (this.items[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.items[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}
		var1.setTag("Items", var2);
	}
	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}
	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return !(itemstack.getItem() instanceof ItemHDBag);
	}
}