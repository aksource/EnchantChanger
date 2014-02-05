package ak.MultiToolHolders;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class ToolHolderData extends WorldSavedData implements IInventory
{
	public ItemStack[] tools = new ItemStack[9];
	private boolean init = false;
	private boolean upDate;

	public ToolHolderData(String par1Str)
	{
		super(par1Str);
	}
	public void onUpdate(World var1, EntityPlayer var2)
	{
		if(!this.init)
		{
			this.init = true;
			this.markDirty();
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
		return 9;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		return tools[var1];
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		if(tools[var1] != null)
		{
			ItemStack var3;
			if(tools[var1].stackSize <= var2)
			{
				var3 = tools[var1];
				tools[var1] = null;
				this.markDirty();
				return var3;
			}
			else
			{
				var3 = this.tools[var1].splitStack(var2);

				if (this.tools[var1].stackSize == 0)
				{
					this.tools[var1] = null;
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
		tools[var1] = var2;
	}

	@Override
	public String getInventoryName() {
		return "ToolHolder";
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

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
		this.tools = new ItemStack[9];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3)
		{
			NBTTagCompound var4 = (NBTTagCompound)var2.getCompoundTagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.tools.length)
			{
				this.tools[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound var1) {
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.tools.length; ++var3)
		{
			if (this.tools[var3] != null)
			{
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.tools[var3].writeToNBT(var4);
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
		return false;
	}
}