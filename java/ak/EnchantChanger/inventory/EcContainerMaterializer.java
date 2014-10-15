package ak.EnchantChanger.inventory;


import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.EnchantmentLvPair;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMaterializer;
import ak.EnchantChanger.utils.ConfigurationUtils;
import ak.EnchantChanger.utils.EnchantmentUtils;
import ak.MultiToolHolders.ItemMultiToolHolder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
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
    private ArrayList<EnchantmentLvPair> itemEnchantmentLvPair = new ArrayList<>();
    private ArrayList<EnchantmentLvPair> enchantmentRemoveData = new ArrayList<>();

    private ArrayList<Integer> MateriaEnchList = new ArrayList<>();
    private ArrayList<Integer> MateriaEnchLvList = new ArrayList<>();
    private World worldPointer;
    private static ArrayList<Integer> magicDmg = new ArrayList<>();

    public EcContainerMaterializer(World par1world, InventoryPlayer inventoryPlayer) {
        InvPlayer = inventoryPlayer;
        worldPointer = par1world;
        addSlotToContainer(new EcSlotItemToEnchant(this, this.materializeResult, this.materializeSource, 0, 35, 17));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                addSlotToContainer(new EcSlotItemMateria(this, this.materializeResult, this.materializeSource, j + i * 4 + 1, 8 + j * 18, 36 + i * 18));
            }
        }

        addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, 0, 125, 17));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                addSlotToContainer(new EcSlotEnchantedItem(this, this.materializeSource, this.materializeResult, j + i * 4 + 1, 98 + j * 18, 36 + i * 18));
            }
        }

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
                    for (int i = 1; i < SourceSlotNum; i++) {
                        if (!((Slot) this.inventorySlots.get(i)).getHasStack()) {
                            ((Slot) this.inventorySlots.get(i)).putStack(itemstack.copy());
                            itemstack.stackSize--;
                            i = SourceSlotNum;
                        }
                    }
                } else if (((Slot) this.inventorySlots.get(0)).getHasStack()) {
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
                slot.putStack(null);
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
            if (EnchantChanger.loadMTH && enchitem.getItem() instanceof ItemMultiToolHolder) {
                return;
            }
            NBTTagList enchOnItem = enchitem.getEnchantmentTagList();
            ItemStack Result = enchitem.copy();
            if (Result.hasTagCompound()) {
                Result.getTagCompound().removeTag("ench");
                Result.getTagCompound().removeTag("ApList");
            }
            if (enchOnItem != null) {
                int var1, var2;
                for (int i = 0; i < enchOnItem.tagCount(); ++i)
                    if (enchOnItem.getCompoundTagAt(i).getShort("lvl") > 0) {
                        enchOnItem.getCompoundTagAt(i).setInteger("ap", 0);
                        var1 = enchOnItem.getCompoundTagAt(i).getShort("id");
                        var2 = enchOnItem.getCompoundTagAt(i).getShort("lvl");
                        this.itemEnchantmentLvPair.add( new EnchantmentLvPair(Enchantment.enchantmentsList[var1], var2));
                        if (i >= 8) {
                           EnchantmentUtils.addEnchantmentToItem(Result, itemEnchantmentLvPair.get(i).enchantment, itemEnchantmentLvPair.get(i).lv);
                        }
                    }
            }
            if (this.checkMateriafromSlot(materializeSource)) {
                for (int i = 1; i < this.materializeSource.getSizeInventory(); i++) {
                    ItemStack materiaitem = this.materializeSource.getStackInSlot(i);
                    if (materiaitem == null) {
                        continue;
                    }
                    int enchLv = EnchantmentUtils.enchLv(materiaitem);
                    Enchantment enchKind = EnchantmentUtils.enchKind(materiaitem);

                    if (!EnchantmentUtils.isEnchantmentValid(enchKind, enchitem) || !EnchantmentUtils.checkLvCap(materiaitem)) {
                        for (int i1 = 0; i1 < ResultSlotNum; i1++) {
                            this.materializeResult.setInventorySlotContents(i1, null);
                        }
                        this.MateriaEnchList.clear();
                        this.MateriaEnchLvList.clear();
                        this.itemEnchantmentLvPair.clear();
                        return;
                    }

                    for (EnchantmentLvPair data : this.itemEnchantmentLvPair) {
                        if (!data.enchantment.canApplyTogether(enchKind)) {
                            this.enchantmentRemoveData.add(data);
                        }
                    }

                    this.itemEnchantmentLvPair.removeAll(this.enchantmentRemoveData);
                    this.enchantmentRemoveData.clear();

                    if (!this.MateriaEnchList.contains(enchKind.effectId)) {
                        this.MateriaEnchList.add(enchKind.effectId);
                        this.MateriaEnchLvList.add(enchLv);
                    }
                }

                for (EnchantmentLvPair data : itemEnchantmentLvPair) {
                    EnchantmentUtils.addEnchantmentToItem(Result, data.enchantment, data.lv);
                }
                this.itemEnchantmentLvPair.clear();
                for (int i2 = 0; i2 < this.MateriaEnchList.size(); i2++) {
                    EnchantmentUtils.addEnchantmentToItem(Result, Enchantment.enchantmentsList[this.MateriaEnchList.get(i2)], this.MateriaEnchLvList.get(i2));
                }
                this.MateriaEnchList.clear();
                this.MateriaEnchLvList.clear();

                this.materializeResult.setInventorySlotContents(0, Result);

                for (int i = 1; i < ResultSlotNum; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }
            } else if (enchOnItem != null) {//extract enchantment from Item
                int endIndex = itemEnchantmentLvPair.size() > 7 ? 7 : itemEnchantmentLvPair.size();
                List<EnchantmentLvPair> subList = itemEnchantmentLvPair.subList(0, endIndex);
                int slotIndex = 0;
                for (EnchantmentLvPair data : subList) {
                    int decreasedLv = EnchantmentUtils.getDecreasedLevel(enchitem, data.lv);
                    int damage = this.setMateriaDmgfromEnch(data.enchantment.effectId);
                    if (decreasedLv > 0) {
                        ItemStack materia = new ItemStack(EnchantChanger.itemMateria, 1, damage);
                        EnchantmentUtils.addEnchantmentToItem(materia, data.enchantment, decreasedLv);
                        this.materializeResult.setInventorySlotContents(slotIndex + 1, materia);
                    } else {
                        this.materializeResult.setInventorySlotContents(slotIndex + 1, null);
                    }
                    slotIndex++;
                }

                this.materializeResult.setInventorySlotContents(0, Result);
                this.MateriaEnchList.clear();
                this.MateriaEnchLvList.clear();
                this.itemEnchantmentLvPair.clear();
            } else {
                for (int i = 0; i < ResultSlotNum; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }

                this.MateriaEnchList.clear();
                this.MateriaEnchLvList.clear();
                this.itemEnchantmentLvPair.clear();
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
        magicDmg.add(ConfigurationUtils.idEnchantmentMeteor);
        magicDmg.add(ConfigurationUtils.idEnchantmentHoly);
        magicDmg.add(ConfigurationUtils.idEnchantmentTelepo);
        magicDmg.add(ConfigurationUtils.idEnchantmentFloat);
        magicDmg.add(ConfigurationUtils.idEnchantmentThunder);
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