package ak.EnchantChanger.tileentity;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.item.EcItemMasterMateria;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.utils.EnchantmentUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.oredict.OreDictionary;

public class EcTileEntityHugeMateria extends TileEntity implements IInventory {
//	private static int[][] EnchArray;
//	private static ItemStack[] MaterialArray;
//	private static ArrayList<Integer> magicArray;
	private ItemStack result = null;
    private int consumedExpBottle = 0;
	private ItemStack[] slotItems = new ItemStack[5];
	public int MaterializingTime = 0;
	public float angle = 0;

	@Override
	public int getSizeInventory()
	{
		return slotItems.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot)
	{
		return slotItems[slot];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack)
	{
		slotItems[slot] = stack;
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
	public ItemStack getStackInSlotOnClosing(int slot) {
		ItemStack stack = getStackInSlot(slot);
		if (stack != null) {
			setInventorySlotContents(slot, null);
		}
		return stack;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this &&
				player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
	}

	@Override
	public void openInventory() {}

	@Override
	public void closeInventory() {}

	@SideOnly(Side.CLIENT)
	 public int getMaterializingProgressScaled(int par1)
	{
		return this.MaterializingTime * par1/200;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		NBTTagList var2 = par1NBTTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		this.slotItems = new ItemStack[this.getSizeInventory()];

		for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
			NBTTagCompound var4 = var2.getCompoundTagAt(var3);
			int var5 = var4.getByte("Slot") & 255;

			if (var5 >= 0 && var5 < this.slotItems.length) {
				this.slotItems[var5] = ItemStack.loadItemStackFromNBT(var4);
			}
		}
		this.MaterializingTime = par1NBTTagCompound.getShort("MaterializingTime");
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setShort("MaterializingTime", (short)this.MaterializingTime);
		NBTTagList var2 = new NBTTagList();

		for (int var3 = 0; var3 < this.slotItems.length; ++var3) {
			if (this.slotItems[var3] != null) {
				NBTTagCompound var4 = new NBTTagCompound();
				var4.setByte("Slot", (byte)var3);
				this.slotItems[var3].writeToNBT(var4);
				var2.appendTag(var4);
			}
		}

		par1NBTTagCompound.setTag("Items", var2);
	}

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeToNBT(nbtTagCompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
	public void updateEntity() {
		//回転させようとしたけど、面倒だった。
//		if(this.angle >360F)
//		{
//			this.angle = 0;
//		}
//		else
//		{
//			this.angle +=1.0F;
//		}
		boolean var2 = false;

		if (!this.worldObj.isRemote) {
			if (this.canMake()) {
				++this.MaterializingTime;

				if (this.MaterializingTime == 200) {
					this.MaterializingTime = 0;
					this.makeMateria();
					var2 = true;
				}
			} else {
				this.MaterializingTime = 0;
			}
		}

		if (var2) {
			this.markDirty();
		}
	}

	@Override
	public String getInventoryName() {
		return "container.hugeMateria";
	}

	public boolean canMake() {
		ItemStack hMateria = this.getStackInSlot(0);
		ItemStack base = this.getStackInSlot(1);
		ItemStack expBottle = this.getStackInSlot(2);
		ItemStack material = this.getStackInSlot(3);
		ItemStack resultItem = this.getStackInSlot(4);
		if(base == null || !(base .getItem() instanceof EcItemMateria) || resultItem != null || material == null || (expBottle!= null && !isBottle(expBottle))) {
            return false;
        }

        if(hMateria != null && hMateria.getItem() instanceof EcItemMasterMateria) {
            int dmg = hMateria.getItemDamage();
            return dmg >= 0 && dmg < 6 && makeResult(material, dmg);
        }

        return materiaLvUp(material,expBottle);
	}
	private boolean makeResult(ItemStack material, int dmg) {
        if (EnchantmentUtils.isMaterialValid(dmg, material)) {
            this.result = EnchantmentUtils.getResult(dmg, material);
            return true;
        }
		return false;
	}

    public boolean isBottle(ItemStack item) {
        return item != null && (item.getItem().equals(Items.experience_bottle) || item.getItem().equals(EnchantChanger.itemExExpBottle));
    }

    private boolean containMasterMateria() {
        return this.getStackInSlot(0) != null && this.getStackInSlot(0).getItem() instanceof EcItemMasterMateria;
    }

	private boolean isValidItem(ItemStack item1, ItemStack item2) {
		if(item1.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return item1.getItem() == item2.getItem();
        }

        return item1.isItemEqual(item2);
	}

	private boolean materiaLvUp(ItemStack materia, ItemStack bottle) {
		if(materia.getItem() instanceof EcItemMateria
                && bottle != null
                && materia.getItemDamage() == 0
                && materia.isItemEnchanted()) {
            Enchantment enchantment = Enchantment.enchantmentsList[EnchantmentUtils.getMateriaEnchKind(materia)];
            int lv = EnchantmentUtils.getMateriaEnchLv(materia);
            ItemStack lvUpItem = EnchantmentUtils.getLvUpitem(enchantment, lv);
            consumedExpBottle = lvUpItem.stackSize;
            if (!lvUpItem.isItemEqual(bottle) || lvUpItem.stackSize > bottle.stackSize) {
                return false;
            }
			result = materia.copy();
			result.stackSize = 1;
            result.getTagCompound().removeTag("ench");
			EnchantmentUtils.addEnchantmentToItem(result, enchantment, lv + 1);
			return true;
		}
		return false;
	}
	public boolean makeMateria() {
		for(int i=1;i < 4;i++) {
            if (i != 2) {
                this.decrStackSize(i, 1);
            } else {
                if (!containMasterMateria() && consumedExpBottle > 0) {
                    this.decrStackSize(i, consumedExpBottle);
                    consumedExpBottle = 0;
                }
            }
		}
		this.setInventorySlotContents(4, result);
		return true;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

    public static void addMateriaMaterial() {

    }
}