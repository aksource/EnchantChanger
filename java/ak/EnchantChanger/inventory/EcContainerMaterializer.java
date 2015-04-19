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

    private ArrayList<Integer> enchantmentList = new ArrayList<>();
    private ArrayList<Integer> enchantmentLevelList = new ArrayList<>();
    private ArrayList<Byte> magicList = new ArrayList<>();
    private ArrayList<Byte> magicAddList = new ArrayList<>();
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
        Slot slot = this.getSlot(par2);

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
                        if (!(this.getSlot(i)).getHasStack()) {
                            (this.getSlot(i)).putStack(itemstack.copy());
                            itemstack.stackSize--;
                            i = SourceSlotNum;
                        }
                    }
                } else if ((this.getSlot(0)).getHasStack()) {
                    return null;
                } else if (itemstack.hasTagCompound() && itemstack.stackSize == 1) {
                    (this.getSlot(0)).putStack(itemstack.copy());
                    itemstack.stackSize = 0;
                } else if (itemstack.stackSize >= 1) {
                    (this.getSlot(0)).putStack(new ItemStack(itemstack.getItem(), 1, itemstack.getItemDamage()));
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
        ItemStack baseItem = this.materializeSource.getStackInSlot(0);
        if (baseItem != null) {
            if (EnchantChanger.loadMTH && baseItem.getItem() instanceof ItemMultiToolHolder) {
                return;
            }
            NBTTagList enchantmentList = baseItem.getEnchantmentTagList();

            if (EnchantmentUtils.hasMagic(baseItem)) {
                for (byte b : EnchantmentUtils.getMagic(baseItem)) {
                    magicList.add(b);
                }
            }

            ItemStack result = baseItem.copy();
            if (result.hasTagCompound()) {
                result.getTagCompound().removeTag("ench");
                result.getTagCompound().removeTag("ApList");
            }
            if (enchantmentList != null && enchantmentList.tagCount() > 0) {
                int var1, var2;
                for (int i = 0; i < enchantmentList.tagCount(); ++i)
                    if (enchantmentList.getCompoundTagAt(i).getShort("lvl") > 0) {
                        enchantmentList.getCompoundTagAt(i).setInteger("ap", 0);
                        var1 = enchantmentList.getCompoundTagAt(i).getShort("id");
                        var2 = enchantmentList.getCompoundTagAt(i).getShort("lvl");
                        this.itemEnchantmentLvPair.add( new EnchantmentLvPair((var1 >=0 && var1 < Enchantment.enchantmentsList.length) ? Enchantment.enchantmentsList[var1] : null, var2));
                        if (i >= 8) {
                           EnchantmentUtils.addEnchantmentToItem(result, itemEnchantmentLvPair.get(i).enchantment, itemEnchantmentLvPair.get(i).lv);
                        }
                    }
            }
            if (this.checkMateriafromSlot(materializeSource)) {
                for (int i = 1; i < this.materializeSource.getSizeInventory(); i++) {
                    ItemStack materiaitem = this.materializeSource.getStackInSlot(i);
                    if (materiaitem == null) {
                        continue;
                    }

                    if (materiaitem.getItemDamage() == 0 && materiaitem.isItemEnchanted()) {
                        int enchLv = EnchantmentUtils.enchLv(materiaitem);
                        Enchantment enchKind = EnchantmentUtils.enchKind(materiaitem);

                        if (!EnchantmentUtils.isEnchantmentValid(enchKind, baseItem) || !EnchantmentUtils.checkLvCap(materiaitem)) {
                            for (int i1 = 0; i1 < ResultSlotNum; i1++) {
                                this.materializeResult.setInventorySlotContents(i1, null);
                            }
                            this.enchantmentList.clear();
                            this.enchantmentLevelList.clear();
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

                        if (!this.enchantmentList.contains(enchKind.effectId)) {
                            this.enchantmentList.add(enchKind.effectId);
                            this.enchantmentLevelList.add(enchLv);
                        }
                    } else {
                        if (!magicList.contains((byte)materiaitem.getItemDamage())) {
                            this.magicAddList.add((byte) materiaitem.getItemDamage());
                        }
                    }
                }

                magicList.addAll(magicAddList);
                byte[] magic = new byte[magicList.size()];
                for (int i = 0; i < magicList.size(); i++) {
                    magic[i] = magicList.get(i);
                }
                EnchantmentUtils.setMagic(result, magic);

                for (EnchantmentLvPair data : itemEnchantmentLvPair) {
                    EnchantmentUtils.addEnchantmentToItem(result, data.enchantment, data.lv);
                }
                this.itemEnchantmentLvPair.clear();
                for (int i2 = 0; i2 < this.enchantmentList.size(); i2++) {
                    EnchantmentUtils.addEnchantmentToItem(result,  (this.enchantmentList.get(i2) < Enchantment.enchantmentsList.length)? Enchantment.enchantmentsList[this.enchantmentList.get(i2)] : null, this.enchantmentLevelList.get(i2));
                }

                this.materializeResult.setInventorySlotContents(0, result);

                for (int i = 1; i < ResultSlotNum; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }
            } else if (enchantmentList != null && enchantmentList.tagCount() > 0) {//extract enchantment from Item
                int endIndex = itemEnchantmentLvPair.size() > 8 ? 8 : itemEnchantmentLvPair.size();
                List<EnchantmentLvPair> subList = itemEnchantmentLvPair.subList(0, endIndex);
                int slotIndex = 0;
                for (EnchantmentLvPair data : subList) {
                    if (data.enchantment == null) break;
                    int decreasedLv = EnchantmentUtils.getDecreasedLevel(baseItem, data.lv);
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

                this.materializeResult.setInventorySlotContents(0, result);
            } else if (!magicList.isEmpty()) {
                result.getTagCompound().removeTag("EnchantChanger|Magic");
                int slotIndex = 0;
                for (byte b : magicList) {
                    ItemStack materia = new ItemStack(EnchantChanger.itemMateria, 1, b);
                    this.materializeResult.setInventorySlotContents(slotIndex + 1, materia);
                    slotIndex++;
                    if (slotIndex > 8) break;
                }
                this.materializeResult.setInventorySlotContents(0, result);
            } else {
                for (int i = 0; i < ResultSlotNum; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }
            }
            this.enchantmentList.clear();
            this.enchantmentLevelList.clear();
            this.itemEnchantmentLvPair.clear();
            this.magicAddList.clear();
            this.magicList.clear();
        }
    }

    //魔法系はエンチャントで管理しなくなった
    @Deprecated
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
}