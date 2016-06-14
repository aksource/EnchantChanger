package ak.EnchantChanger.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class EcTileEntityMaterializer extends TileEntity implements IInventory {

    private ItemStack[] materializerItemstacks;

    public EcTileEntityMaterializer() {
        materializerItemstacks = new ItemStack[7];
    }

//    public int BoolToInt(boolean par1)
//    {
//    	return (par1) ? 1:0;
//    }
    /**
     public int getItemInstToInt(ItemStack itemstack)
     {
     if(itemstack.getItem() instanceof ItemSword)
     {
     return 1;
     }
     else
     {
     if(itemstack.getItem() instanceof ItemBow)
     {
     return 2;
     }
     else
     {
     if(itemstack.getItem() instanceof ItemTool)
     {
     return 3;
     }
     else
     {
     if(itemstack.getItem() instanceof ItemArmor)
     {
     return (((ItemArmor)itemstack.getItem()).armorType == 0) ? 4 : (((ItemArmor)itemstack.getItem()).armorType == 3) ? 5 : 6 ;
     }
     else
     {
     return 0;
     }
     }
     }
     }
     }
     */
    /**
     public boolean canResult(IInventory par1IInventory)
     {
     ItemStack enchitem = par1IInventory.getStackInSlot(0);
     if (enchitem !=null)
     {
     if((getItemInstToInt(enchitem) > 0)&&(getItemInstToInt(enchitem) < 7))
     {
     return true;
     }
     else
     return false;
     }
     else
     return false;
     }
     */
    /**
     * public boolean canEnchant(IInventory par1IInventory)
     * {
     * ItemStack enchitem = par1IInventory.getStackInSlot(0);
     * ItemStack materiaitem = par1IInventory.getStackInSlot(1);
     * <p/>
     * if (!(enchitem ==null)&&!(materiaitem == null))
     * {
     * int materiaNum = materiaitem.getItemDamage();
     * switch (getItemInstToInt(enchitem))
     * {
     * case 0 :return false;
     * case 1 :
     * if((materiaNum == 1)||(materiaNum == 2)||(materiaNum == 9)||(materiaNum == 13)||(materiaNum == 14)||(materiaNum == 15))
     * {
     * return true;
     * }
     * else
     * {
     * return false;
     * }
     * case 2 :
     * if((materiaNum == 1)||(materiaNum == 12)||(materiaNum == 13)||(materiaNum == 14))
     * {
     * return true;
     * }
     * else
     * {
     * return false;
     * }
     * case 3 :
     * if((materiaNum == 3)||(materiaNum == 5)||(materiaNum == 7)||(materiaNum == 9))
     * {
     * return true;
     * }
     * else
     * {
     * return false;
     * }
     * case 4 :
     * if((materiaNum == 1)||(materiaNum == 4)||(materiaNum == 6)||(materiaNum == 8)||(materiaNum == 13))
     * {
     * return true;
     * }
     * else
     * {
     * return false;
     * }
     * case 5 :
     * if((materiaNum == 1)||(materiaNum == 6)||(materiaNum == 8)||(materiaNum == 11)||(materiaNum == 13))
     * {
     * return true;
     * }
     * else
     * {
     * return false;
     * }
     * case 6 :
     * if((materiaNum == 1)||(materiaNum == 6)||(materiaNum == 8)||(materiaNum == 13))
     * {
     * return true;
     * }
     * else
     * {
     * return false;
     * }
     * default : return false;
     * }
     * }
     * else
     * {
     * return false;
     * }
     * }
     */
    @Override
    public int getSizeInventory() {
        return materializerItemstacks.length;
    }


    @Override
    public ItemStack getStackInSlot(int slot) {
        return materializerItemstacks[slot];
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        materializerItemstacks[slot] = stack;
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
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this &&
                player.getDistanceSq(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) < 64;
    }

    @Override
    public void openInventory() {
    }

    @Override
    public void closeInventory() {
    }
    /**
     * signs and mobSpawners use this to send text and meta-data
     */


    /**
     * Reads a tile entity from NBT.
     */
    /**
     public void readFromNBT(NBTTagCompound par1NBTTagCompound)
     {
     super.readFromNBT(par1NBTTagCompound);
     NBTTagList var2 = par1NBTTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
     this.materializerItemstacks = new ItemStack[this.getSizeInventory()];

     for (int var3 = 0; var3 < var2.tagCount(); ++var3)
     {
     NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
     int var5 = var4.getByte("Slot") & 255;

     if (var5 >= 0 && var5 < this.materializerItemstacks.length)
     {
     this.materializerItemstacks[var5] = ItemStack.loadItemStackFromNBT(var4);
     }
     }
     }
     */
    /**
     * Writes a tile entity to NBT.
     */
    /**
     * public void writeToNBT(NBTTagCompound par1NBTTagCompound)
     * {
     * super.writeToNBT(par1NBTTagCompound);
     * NBTTagList var2 = new NBTTagList();
     * <p/>
     * for (int var3 = 0; var3 < this.materializerItemstacks.length; ++var3)
     * {
     * if (this.materializerItemstacks[var3] != null)
     * {
     * NBTTagCompound var4 = new NBTTagCompound();
     * var4.setByte("Slot", (byte)var3);
     * this.materializerItemstacks[var3].writeToNBT(var4);
     * var2.appendTag(var4);
     * }
     * }
     * <p/>
     * par1NBTTagCompound.setTag("Items", var2);
     * }
     */
    @Override
    public String getInventoryName() {
        return "container.materializer";
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