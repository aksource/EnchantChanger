package ak.enchantchanger.inventory;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.item.EcItemMasterMateria;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.item.EcItemSword;
import ak.enchantchanger.utils.EnchantmentUtils;
import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.enchantchanger.utils.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static ak.enchantchanger.api.Constants.NBT_KEY_ENCHANT_ID;
import static ak.enchantchanger.api.Constants.NBT_KEY_ENCHANT_LEVEL;

/**
 * Created by A.K..
 */
public class EcContainerMateriaWindow extends Container {
    private static final int MAX_SLOT = 16;
    private final IInventory materiaInventory = new EcSlotMateriaInventory(this, "MateriaWindow", MAX_SLOT);
    private final InventoryPlayer invPlayer;
    private final int openSlotNum;
    private ItemStack openItem;
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

    private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
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
        if (item.isEmpty()) return;
        ItemStack materia;
        int slotnum = 0;
        if (EnchantmentUtils.hasMagic(item)) {
            byte[] magic = EnchantmentUtils.getMagic(item);
            for (byte b : magic) {
                materia = new ItemStack(Items.itemMateria, 1, b);
                this.materiaInventory.setInventorySlotContents(slotnum, materia);
                slotnum++;
            }
        }

        if (EnchantmentUtils.isEnchanted(item)) {
            NBTTagList enchantments = item.getEnchantmentTagList();
            int id, lv, dmg;
            for (int i = 0; i < enchantments.tagCount(); i++) {
                lv = enchantments.getCompoundTagAt(i).getShort(NBT_KEY_ENCHANT_LEVEL);
                if (lv > 0 && slotnum < 16) {
                    id = enchantments.getCompoundTagAt(i).getShort(NBT_KEY_ENCHANT_ID);
                    materia = new ItemStack(Items.itemMateria, 1, 0);
                    EnchantmentUtils.addEnchantmentToItem(materia, Enchantment.getEnchantmentByID(id), lv);
                    this.materiaInventory.setInventorySlotContents(slotnum, materia);
                    slotnum++;
                }
            }
        }
        this.initializing = true;
    }

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        ItemStack item = player.getHeldItemMainhand();
        return !item.isEmpty()
                && !(item.getItem() instanceof EcItemMateria || item.getItem() instanceof EcItemMasterMateria)
                && !(EnchantChanger.loadMTH && item.getItem() instanceof ItemMultiToolHolder);
    }

    @Override
    public void onCraftMatrixChanged(@Nonnull IInventory inventoryIn) {
        if (!initializing) return;
        if (inventoryIn == this.materiaInventory) {
            NBTTagCompound nbt = openItem.getTagCompound();

            List<Byte> byteList = getMagicListFromInventory();
            byte[] magic = new byte[byteList.size()];

            for (int i = 0; i < magic.length; i++) {
                magic[i] = byteList.get(i);
            }
            EnchantmentUtils.setMagic(openItem, magic);

            ArrayList<Pair<Enchantment, Integer>> enchantmentList = getEnchantmentListFromInventory();

            if (EnchantmentUtils.isEnchanted(openItem)) {
                String tagName = EnchantmentUtils.getTagName(openItem);
                nbt.removeTag(tagName);
            }

            if (openItem.hasTagCompound() && openItem.getTagCompound().getKeySet().size() == 0) {
                openItem.setTagCompound(null);
            }

            for (Pair<Enchantment, Integer> data : enchantmentList) {
                EnchantmentUtils.addEnchantmentToItem(openItem, data.getLeft(), data.getRight());
            }
            ItemStack result = EnchantmentUtils.getBookResult(openItem, enchantmentList);
            if (!result.isItemEqual(openItem)) {
                openItem = result;
                this.getSlotFromInventory(invPlayer, openSlotNum).putStack(result);
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack retItemStack = ItemStack.EMPTY;
        Slot slot = this.getSlot(index);

        if (slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            retItemStack = itemstack.copy();

            if (index >= 0 && index < MAX_SLOT) {
                if (!this.mergeItemStack(itemstack, MAX_SLOT, MAX_SLOT + 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (itemstack.getItem() instanceof EcItemMateria) {
                    for (int i = 0; i < MAX_SLOT; i++) {
                        if (!(this.getSlot(i)).getHasStack() && this.checkEnchantmentValid(itemstack)) {
                            ItemStack copyItem = itemstack.copy();
                            copyItem.setCount(1);
                            (this.getSlot(i)).putStack(copyItem);
                            itemstack.shrink(1);
                            break;
                        }
                    }
                }
            }

            if (itemstack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack.getCount() == retItemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack);
        }

        return retItemStack;
    }

    private boolean checkEnchantmentValid(ItemStack itemStack) {
        if (itemStack.getItemDamage() > 0 && openItem.getItem() instanceof EcItemSword) {
            return true;
        }
        Enchantment enchantment = EnchantmentUtils.getEnchantmentFromItemStack(itemStack);
        return EnchantmentUtils.isEnchantmentValid(enchantment, openItem);
    }

    @Override
    protected void retrySlotClick(int slotId, int clickedButton, boolean mode, @Nonnull EntityPlayer playerIn) {
    }

    @Override
    @Nonnull
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, @Nonnull EntityPlayer player) {
        if (slotId == MAX_SLOT + this.openSlotNum) {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Nonnull
    private ArrayList<Pair<Enchantment, Integer>> getEnchantmentListFromInventory() {
        ArrayList<Pair<Enchantment, Integer>> list = new ArrayList<>();
        ItemStack slotItem;
        Pair<Enchantment, Integer> enchData;
        for (int i = 0; i < this.materiaInventory.getSizeInventory(); i++) {
            slotItem = this.materiaInventory.getStackInSlot(i);
            if (!slotItem.isEmpty() && EnchantmentUtils.isEnchanted(slotItem)) {
                enchData = Pair.of(EnchantmentUtils.getEnchantmentFromItemStack(slotItem), EnchantmentUtils.getEnchantmentLv(slotItem));
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
            if (!slotItem.isEmpty() && slotItem.getItemDamage() > 0) {
                list.add((byte) slotItem.getItemDamage());
            }
        }
        return list;
    }

    public ItemStack getOpenItem() {
        return openItem;
    }
}
