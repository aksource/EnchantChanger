package ak.EnchantChanger;


import ak.MultiToolHolders.ItemMultiToolHolder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EcContainerMaterializer extends Container {

    public static int ResultSlotNum = 9;
    public IInventory materializeResult = new EcSlotResult(this, "MaterializerResult", ResultSlotNum);
    public static int SourceSlotNum = 9;
    public IInventory materializeSource = new EcSlotMaterializer(this, "MaterializerSource", SourceSlotNum);
    protected EcTileEntityMaterializer tileEntity;
    protected InventoryPlayer InvPlayer;
    private ArrayList<EnchantmentData> itemEnchantmentData = new ArrayList<EnchantmentData>();
    private ArrayList<EnchantmentData> materiaEnchantmentData = new ArrayList<EnchantmentData>();
//    private ArrayList<Integer> ItemEnchList = new ArrayList<Integer>();
//    private ArrayList<Integer> ItemEnchLvList = new ArrayList<Integer>();
    private ArrayList<Integer> MateriaEnchList = new ArrayList<Integer>();
    private ArrayList<Integer> MateriaEnchLvList = new ArrayList<Integer>();
    private World worldPointer;
    private boolean materiadecLv = EnchantChanger.enableDecMateriaLv;
    private static ArrayList<Integer> magicDmg = new ArrayList<Integer>();

    public EcContainerMaterializer(World par1world, InventoryPlayer inventoryPlayer) {
        InvPlayer = inventoryPlayer;
        worldPointer = par1world;
        addSlotToContainer(new EcSlotItemToEnchant(this, this.materializeResult, this.materializeSource, 0, 35, 17));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, j + i * 4 + 1, 8 + j * 18, 36 + i * 18));
            }
        }
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 1, 8, 36));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 2, 26, 36));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 3, 44, 36));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 4, 62, 36));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 5, 8, 54));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 6, 26, 54));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 7, 44, 54));
//		addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, 8, 62, 54));

        addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 0, 125, 17));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, j + i * 4 + 1, 98 + j * 18, 36 + i * 18));
            }
        }
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 1, 98, 36));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 2, 116, 36));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 3, 134, 36));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 4, 152, 36));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 5, 98, 54));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 6, 116, 54));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 7, 134, 54));
//		addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 8, 152, 54));

        bindPlayerInventory(inventoryPlayer);
        this.onCraftMatrixChanged(this.materializeSource);
    }


    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer) {
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
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack retitem = null;
        Slot slot = (Slot) this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            retitem = itemstack.copy();

            if (par2 >= 0 && par2 < ResultSlotNum + SourceSlotNum) {
                if (!this.mergeItemStack(itemstack, ResultSlotNum + SourceSlotNum, ResultSlotNum + SourceSlotNum + 36, true)) {
                    return null;
                }
            } else {
                if (itemstack.getItem() instanceof EcItemMateria) {
                    for (int i = 1; i < this.SourceSlotNum; i++) {
                        if (!((Slot) this.inventorySlots.get(i)).getHasStack()) {
                            ((Slot) this.inventorySlots.get(i)).putStack(itemstack.copy());
                            itemstack.stackSize--;
                            i = this.SourceSlotNum;
                        }
                    }
                } else if (((Slot) this.inventorySlots.get(0)).getHasStack() || !(itemstack.getItem() instanceof Item)) {
                    return null;
                } else if (itemstack.hasTagCompound() && itemstack.stackSize == 1) {
                    ((Slot) this.inventorySlots.get(0)).putStack(itemstack.copy());
                    itemstack.stackSize = 0;
                } else if (itemstack.stackSize >= 1) {
                    ((Slot) this.inventorySlots.get(0)).putStack(new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage()));
                    --itemstack.stackSize;
                }
            }

            if (itemstack.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack.stackSize == retitem.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack);
        }

        return retitem;
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        ItemStack enchitem = this.materializeSource.getStackInSlot(0);
        if (enchitem != null) {
            if (!(enchitem.getItem() instanceof Item) || (EnchantChanger.loadMTH && enchitem.getItem() instanceof ItemMultiToolHolder)) {
                return;
            }
            NBTTagList enchOnItem = enchitem.getEnchantmentTagList();
            int itemdmg = enchitem.getItemDamage();
            float dmgratio = (enchitem.getMaxDamage() == 0) ? 1 : (enchitem.getMaxDamage() - itemdmg) / enchitem.getMaxDamage();
            ItemStack Result = enchitem.copy();
            if (Result.hasTagCompound()) {
                Result.getTagCompound().removeTag("ench");
                Result.getTagCompound().removeTag("ApList");
            }
            if (enchOnItem != null) {
                int var1, var2;
                for (int i = 0; i < enchOnItem.tagCount(); ++i)
                    if (((NBTTagCompound) enchOnItem.getCompoundTagAt(i)).getShort("lvl") > 0) {
                        ((NBTTagCompound) enchOnItem.getCompoundTagAt(i)).setInteger("ap", 0);
//                        this.ItemEnchList.add((int) ((NBTTagCompound) enchOnItem.getCompoundTagAt(i)).getShort("id"));
//                        this.ItemEnchLvList.add((int) ((NBTTagCompound) enchOnItem.getCompoundTagAt(i)).getShort("lvl"));
                        var1 = enchOnItem.getCompoundTagAt(i).getShort("id");
                        var2 = enchOnItem.getCompoundTagAt(i).getShort("lvl");
                        this.itemEnchantmentData.add( new EnchantmentData(Enchantment.enchantmentsList[var1], var2));
                        if (i >= 8) {
//                            EnchantChanger.addEnchantmentToItem(Result, Enchantment.enchantmentsList[this.ItemEnchList.get(i)], this.ItemEnchLvList.get(i));
                            EnchantChanger.addEnchantmentToItem(Result, itemEnchantmentData.get(i).enchantment, itemEnchantmentData.get(i).lv);
                        }
                    }
            }
            if (this.checkMateriafromSlot(materializeSource)) {
                for (int i = 1; i < this.materializeSource.getSizeInventory(); i++) {
                    ItemStack materiaitem = this.materializeSource.getStackInSlot(i);
                    if (materiaitem == null) {
                        continue;
                    }
                    int enchLv = EnchantChanger.enchLv(materiaitem);
                    Enchantment enchKind = EnchantChanger.enchKind(materiaitem);

                    if (!EnchantChanger.isEnchantmentValid(enchKind, enchitem) || !EnchantChanger.checkLvCap(materiaitem)) {
                        for (int i1 = 0; i1 < this.ResultSlotNum; i1++) {
                            this.materializeResult.setInventorySlotContents(i1, null);
                        }
//                        this.ItemEnchList.clear();
//                        this.ItemEnchLvList.clear();
                        this.MateriaEnchList.clear();
                        this.MateriaEnchLvList.clear();
                        this.itemEnchantmentData.clear();
                        return;
                    }
                    for (EnchantmentData data : this.itemEnchantmentData) {
                        if (!data.enchantment.canApplyTogether(enchKind)) {
                            this.itemEnchantmentData.remove(data);
                        }
                    }
//                        for (int i2 = 0; i2 < this.ItemEnchList.size(); i2++) {
//                            if (!Enchantment.enchantmentsList[this.ItemEnchList.get(i2)].canApplyTogether(enchKind)) {
//                                this.ItemEnchList.remove(i2);
//                                this.ItemEnchLvList.remove(i2);
//                            }
//                        }

                    if (!this.MateriaEnchList.contains(enchKind.effectId)) {
                        this.MateriaEnchList.add(enchKind.effectId);
                        this.MateriaEnchLvList.add(enchLv);
                    }
                }
                for (EnchantmentData data : itemEnchantmentData) {
                    EnchantChanger.addEnchantmentToItem(Result, data.enchantment, data.lv);
                }
//                for (int i2 = 0; i2 < this.ItemEnchList.size(); i2++) {
//                    EnchantChanger.addEnchantmentToItem(Result, Enchantment.enchantmentsList[this.ItemEnchList.get(i2)], this.ItemEnchLvList.get(i2));
//                }
                for (int i2 = 0; i2 < this.MateriaEnchList.size(); i2++) {
                    EnchantChanger.addEnchantmentToItem(Result, Enchantment.enchantmentsList[this.MateriaEnchList.get(i2)], this.MateriaEnchLvList.get(i2));
                }
                this.materializeResult.setInventorySlotContents(0, Result);
//                this.ItemEnchList.clear();
//                this.ItemEnchLvList.clear();
                this.MateriaEnchList.clear();
                this.MateriaEnchLvList.clear();
                this.itemEnchantmentData.clear();
                for (int i = 1; i < ResultSlotNum; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }
            } else if (enchOnItem != null) {//extract enchantment from Item
                int endIndex = itemEnchantmentData.size() > 7 ? 7 :itemEnchantmentData.size();
                List<EnchantmentData> subList = itemEnchantmentData.subList(0, endIndex);
                int slotIndex = 0;
                for (EnchantmentData data : subList) {
                    int declv = (!materiadecLv) ? 0 : (dmgratio > 0.5F) ? 0 : (dmgratio > 0.25F) ? 1 : 2;
                    int decreasedLv = (data.lv - declv < 0) ? 0 : data.lv - declv;
                    int damage = this.setMateriaDmgfromEnch(data.enchantment.effectId);
                    ItemStack materia = new ItemStack(EnchantChanger.itemMateria, 1, damage);
                    EnchantChanger.addEnchantmentToItem(materia, data.enchantment, decreasedLv);
                    this.materializeResult.setInventorySlotContents(slotIndex + 1, materia);
                    slotIndex++;
                }
//                for (int i = 0; i < 8; i++) {
//                    if (i < this.ItemEnchList.size()) {
//                        int declv = (!materiadecLv) ? 0 : (dmgratio > 0.5F) ? 0 : (dmgratio > 0.25F) ? 1 : 2;
//                        int decreasedLv = (this.ItemEnchLvList.get(i) - declv < 0) ? 0 : this.ItemEnchLvList.get(i) - declv;
//                        int damage = this.setMateriaDmgfromEnch(this.ItemEnchList.get(i));
//                        ItemStack materia = new ItemStack(EnchantChanger.itemMateria, 1, damage);
//                        EnchantChanger.addEnchantmentToItem(materia, Enchantment.enchantmentsList[this.ItemEnchList.get(i)], decreasedLv);
//                        this.materializeResult.setInventorySlotContents(i + 1, materia);
//                    } else
//                        break;
//                }
                this.materializeResult.setInventorySlotContents(0, Result);
//                this.ItemEnchList.clear();
//                this.ItemEnchLvList.clear();
                this.MateriaEnchList.clear();
                this.MateriaEnchLvList.clear();
                this.itemEnchantmentData.clear();
            } else {
                for (int i = 0; i < ResultSlotNum; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }
//                this.ItemEnchList.clear();
//                this.ItemEnchLvList.clear();
                this.MateriaEnchList.clear();
                this.MateriaEnchLvList.clear();
                this.itemEnchantmentData.clear();
            }
        }
    }

    private int setMateriaDmgfromEnch(int enchID) {
        if (magicDmg.contains(enchID))
            return magicDmg.indexOf(enchID) + 1;
        else
            return 0;
    }

    @Override
    public void onContainerClosed(EntityPlayer par1EntityPlayer) {
        super.onContainerClosed(par1EntityPlayer);

        if (!this.worldPointer.isRemote) {
            if (!this.ItemSourceLeft()) {
                for (int var4 = 0; var4 < ResultSlotNum; ++var4) {
                    ItemStack var5 = this.materializeResult.getStackInSlotOnClosing(var4);
                    if (var5 != null) {
                        par1EntityPlayer.dropPlayerItemWithRandomChoice(var5, false);
                    }
                }
            }
            for (int var2 = 0; var2 < SourceSlotNum; ++var2) {
                ItemStack var3 = this.materializeSource.getStackInSlotOnClosing(var2);
                if (var3 != null) {
                    par1EntityPlayer.dropPlayerItemWithRandomChoice(var3, false);
                }
            }

        }
    }

    private boolean ItemSourceLeft() {
        return this.materializeSource.getStackInSlot(0) != null;
    }

    private boolean checkMateriafromSlot(IInventory Source) {
        boolean ret = false;
        for (int i = 0; i < Source.getSizeInventory(); i++) {
            if (Source.getStackInSlot(i) != null && Source.getStackInSlot(i).getItem() instanceof EcItemMateria)
                ret = true;
        }
        return ret;
    }
    static {
        magicDmg.add(EnchantChanger.EnchantmentMeteoId);
        magicDmg.add(EnchantChanger.EndhantmentHolyId);
        magicDmg.add(EnchantChanger.EnchantmentTelepoId);
        magicDmg.add(EnchantChanger.EnchantmentFloatId);
        magicDmg.add(EnchantChanger.EnchantmentThunderId);
    }
    //仕様変更。どうしようか迷っている。
//	 public static int ExtraItemCheck(ItemStack par1ItemStack)
//	 {
//		 int var1 = par1ItemStack.itemID;
//		 for(int i=0;i<EnchantChanger.extraSwordIDs.length;i++){
//			 if(var1 == EnchantChanger.extraSwordIDs[i])
//				 return 1;
//		 }
//		 for(int i=0;i<EnchantChanger.extraBowIDs.length;i++){
//			 if(var1 == EnchantChanger.extraBowIDs[i])
//				 return 2;
//		 }
//		 for(int i=0;i<EnchantChanger.extraToolIDs.length;i++){
//			 if(var1 == EnchantChanger.extraToolIDs[i])
//				 return 3;
//		 }
//		 for(int i=0;i<EnchantChanger.extraArmorIDs.length;i++){
//			 if(var1 == EnchantChanger.extraArmorIDs[i])
//				 return 6;
//		 }
//		 return 0;
//	 }
}