package ak.EnchantChanger.inventory;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.EnchantmentLvPair;
import ak.EnchantChanger.item.EcItemMasterMateria;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.item.EcItemSword;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A.K..
 */
public class EcContainerMateriaWindow extends Container {
    private InventoryPlayer invPlayer;
    private int openSlotNum;
    private ItemStack openItem;
    public int maxSlot = 16;
    public IInventory materiaInventory = new EcSlotMateriaInventory(this, "MateriaWindow", maxSlot);
    private boolean initializing = false;

    public EcContainerMateriaWindow(InventoryPlayer inventoryPlayer, ItemStack item, int slot) {
        this.invPlayer = inventoryPlayer;
        this.openSlotNum = slot;
        this.openItem = item;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                addSlotToContainer(new EcSlotMateriaWindow(openItem, materiaInventory, j + i * 4, 88 + j * 18, 7 + i * 18));
            }
        }
        this.bindPlayerInventory(inventoryPlayer);
        this.initMateriaInventory();
    }

    protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
                        8 + j * 18, 84 + i * 18));
            }
        }

    }

    private void initMateriaInventory() {
        ItemStack item = this.invPlayer.getStackInSlot(this.openSlotNum);
        if (item == null) return;
        ItemStack materia;
        int slotnum = 0;
        if (EnchantmentUtils.hasMagic(item)) {
            byte[] magic = EnchantmentUtils.getMagic(item);
            for (byte b : magic) {
                materia = new ItemStack(EnchantChanger.itemMateria, 1, b);
                this.materiaInventory.setInventorySlotContents(slotnum, materia);
                slotnum++;
            }
        }

        if (item.isItemEnchanted()) {
            NBTTagList enchantments = item.getEnchantmentTagList();
            int id, lv, dmg;
            for (int i = 0; i < enchantments.tagCount(); i++) {
                lv = enchantments.getCompoundTagAt(i).getShort("lvl");
                if (lv > 0 && slotnum < 16) {
                    id = enchantments.getCompoundTagAt(i).getShort("id");
                    dmg = this.setMateriaDmgfromEnch(id);
                    materia = new ItemStack(EnchantChanger.itemMateria, 1, dmg);
                    EnchantmentUtils.addEnchantmentToItem(materia, Enchantment.enchantmentsList[id], lv);
                    this.materiaInventory.setInventorySlotContents(slotnum, materia);
                    slotnum++;
                }
            }
        }
        this.initializing = true;
    }

    private int setMateriaDmgfromEnch(int enchID) {
        if (enchID == ConfigurationUtils.idEnchantmentMeteor)
            return 1;
        else if (enchID == ConfigurationUtils.idEnchantmentHoly)
            return 2;
        else if (enchID == ConfigurationUtils.idEnchantmentTelepo)
            return 3;
        else if (enchID == ConfigurationUtils.idEnchantmentFloat)
            return 4;
        else if (enchID == ConfigurationUtils.idEnchantmentThunder)
            return 5;
        else
            return 0;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        ItemStack item = player.getCurrentEquippedItem();
        return item != null && !(item.getItem() instanceof EcItemMateria || item.getItem() instanceof EcItemMasterMateria) && !(EnchantChanger.loadMTH && item.getItem() instanceof ItemMultiToolHolder);
    }

    @Override
    public void onCraftMatrixChanged(IInventory par1IInventory) {
        if (!initializing) return;
        if (par1IInventory == this.materiaInventory) {
            NBTTagCompound nbt = openItem.getTagCompound();

            List<Byte> byteList = getMagicListFromInventory();
            byte[] magic = new byte[byteList.size()];

            for (int i = 0; i < magic.length; i++) {
                magic[i] = byteList.get(i);
            }
            EnchantmentUtils.setMagic(openItem, magic);

            ArrayList<EnchantmentLvPair> enchantmentList = getEnchantmentListFromInventory();

            if (openItem.isItemEnchanted()) {
                nbt.removeTag("ench");
            }

            for (EnchantmentLvPair data :  enchantmentList) {
                EnchantmentUtils.addEnchantmentToItem(openItem, data.enchantment, data.lv);
            }
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2) {
        ItemStack retitem = null;
        Slot slot = this.getSlot(par2);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            retitem = itemstack.copy();

            if (par2 >= 0 && par2 < this.maxSlot) {
                if (!this.mergeItemStack(itemstack, this.maxSlot, this.maxSlot + 36, true)) {
                    return null;
                }
            } else {
                if (itemstack.getItem() instanceof EcItemMateria) {
                    for (int i = 0; i < this.maxSlot; i++) {
                        if (!(this.getSlot(i)).getHasStack() && this.checkEnchantmentValid(itemstack)) {
                            ItemStack copyItem = itemstack.copy();
                            copyItem.stackSize = 1;
                            (this.getSlot(i)).putStack(copyItem);
                            itemstack.stackSize--;
                            break;
                        }
                    }
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

    private boolean checkEnchantmentValid(ItemStack itemStack) {
        if (itemStack.getItemDamage() > 0 && openItem.getItem() instanceof EcItemSword) {
            return true;
        }
        Enchantment enchantment = EnchantmentUtils.enchKind(itemStack);
        return EnchantmentUtils.isEnchantmentValid(enchantment, openItem);
    }

    @Override
    protected void retrySlotClick(int par1, int par2, boolean par3, EntityPlayer par4EntityPlayer){}

    @Override
    public ItemStack slotClick(int slot, int mouse, int keyboard, EntityPlayer par4EntityPlayer) {
        if (slot == this.maxSlot + this.openSlotNum)
            return null;
        else
            return super.slotClick(slot, mouse, keyboard, par4EntityPlayer);
    }

    private ArrayList<EnchantmentLvPair> getEnchantmentListFromInventory() {
        ArrayList<EnchantmentLvPair> list = new ArrayList<>();
        ItemStack slotItem;
        EnchantmentLvPair enchData;
        for (int i = 0; i < this.materiaInventory.getSizeInventory(); i++) {
            slotItem = this.materiaInventory.getStackInSlot(i);
            if (slotItem != null && slotItem.isItemEnchanted()) {
                enchData = new EnchantmentLvPair(EnchantmentUtils.enchKind(slotItem), EnchantmentUtils.enchLv(slotItem));
                list.add(enchData);
            }
        }
        return list;
    }

    private ArrayList<Byte> getMagicListFromInventory() {
        ArrayList<Byte> list = new ArrayList<>();
        ItemStack slotItem;
        for (int i = 0; i < this.materiaInventory.getSizeInventory(); i++) {
            slotItem = this.materiaInventory.getStackInSlot(i);
            if (slotItem != null && slotItem.getItemDamage() > 0) {
                list.add((byte)slotItem.getItemDamage());
            }
        }
        return list;
    }
}
