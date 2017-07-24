package ak.enchantchanger.inventory;


import ak.MultiToolHolders.ItemMultiToolHolder;
import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.EnchantmentUtils;
import ak.enchantchanger.utils.Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static ak.enchantchanger.api.Constants.*;

public class EcContainerMaterializer extends Container {

    public static final int RESULT_SLOT_NUM = 9;
    public static final int SourceSlotNum = 9;


    public IInventory materializeResult = new EcSlotResult(this, "MaterializerResult", RESULT_SLOT_NUM);
    public IInventory materializeSource = new EcSlotMaterializer(this, "MaterializerSource", SourceSlotNum);
    protected final InventoryPlayer inventoryPlayer;
    private ArrayList<Pair<Enchantment, Integer>> itemEnchantmentLvPair = new ArrayList<>();
    private ArrayList<Pair<Enchantment, Integer>> enchantmentRemoveData = new ArrayList<>();
    private ArrayList<ResourceLocation> enchantmentList = new ArrayList<>();
    private ArrayList<Integer> enchantmentLevelList = new ArrayList<>();
    private ArrayList<Byte> magicList = new ArrayList<>();
    private ArrayList<Byte> magicAddList = new ArrayList<>();
    private World worldPointer;


    public EcContainerMaterializer(World world, InventoryPlayer inventoryPlayer) {
        this.inventoryPlayer = inventoryPlayer;
        worldPointer = world;
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
    public boolean canInteractWith(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    protected void retrySlotClick(int slotId, int clickedButton, boolean mode, @Nonnull EntityPlayer playerIn) {
    }

    private void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
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
    public ItemStack transferStackInSlot(@Nonnull EntityPlayer playerIn, int index) {
        ItemStack retItemStack = null;
        Slot slot = this.getSlot(index);

        if (slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            retItemStack = itemstack.copy();

            if (index >= 0 && index < RESULT_SLOT_NUM + SourceSlotNum) {
                if (!this.mergeItemStack(itemstack, RESULT_SLOT_NUM + SourceSlotNum, RESULT_SLOT_NUM + SourceSlotNum + 36, true)) {
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
                    itemstack.stackSize--;
                }
            }

            if (itemstack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack.stackSize == retItemStack.stackSize) {
                return null;
            }

            slot.onPickupFromSlot(playerIn, itemstack);
        }

        return retItemStack;
    }

    @Override
    public void onCraftMatrixChanged(@Nonnull IInventory inventoryIn) {
        ItemStack baseItem = this.materializeSource.getStackInSlot(0);
        if (baseItem != null) {
            if (EnchantChanger.loadMTH && baseItem.getItem() instanceof ItemMultiToolHolder) {
                return;
            }
            String tagName = EnchantmentUtils.getTagName(baseItem);
            NBTTagList enchTagList = (baseItem.hasTagCompound()) ? baseItem.getTagCompound().getTagList(tagName, Constants.NBT.TAG_COMPOUND) : null;

            if (EnchantmentUtils.hasMagic(baseItem)) {
                for (byte b : EnchantmentUtils.getMagic(baseItem)) {
                    magicList.add(b);
                }
            }

            ItemStack result = baseItem.copy();
            if (result.hasTagCompound()) {
                result.getTagCompound().removeTag(tagName);
                result.getTagCompound().removeTag(NBT_KEY_AP_LIST);
            }
            if (enchTagList != null && enchTagList.tagCount() > 0) {
                int id, level;
                for (int i = 0; i < enchTagList.tagCount(); ++i)
                    if (enchTagList.getCompoundTagAt(i).getShort(NBT_KEY_ENCHANT_LEVEL) > 0) {
                        enchTagList.getCompoundTagAt(i).setInteger(NBT_KEY_AP, 0);
                        id = enchTagList.getCompoundTagAt(i).getShort(NBT_KEY_ENCHANT_ID);
                        level = enchTagList.getCompoundTagAt(i).getShort(NBT_KEY_ENCHANT_LEVEL);
                        this.itemEnchantmentLvPair.add(Pair.of(Enchantment.getEnchantmentByID(id), level));
                        if (i >= 8) {
                            EnchantmentUtils.addEnchantmentToItem(result, itemEnchantmentLvPair.get(i).getLeft(), itemEnchantmentLvPair.get(i).getRight());
                        }
                    }
            }
            if (this.checkMateriaFromSlot(materializeSource)) {
                for (int i = 1; i < this.materializeSource.getSizeInventory(); i++) {
                    ItemStack materiaitem = this.materializeSource.getStackInSlot(i);
                    if (materiaitem == null) {
                        continue;
                    }

                    if (materiaitem.getItemDamage() == 0 && materiaitem.isItemEnchanted()) {
                        int enchLv = EnchantmentUtils.getEnchantmentLv(materiaitem);
                        Enchantment enchKind = EnchantmentUtils.getEnchantmentFromItemStack(materiaitem);

                        if (!EnchantmentUtils.isEnchantmentValid(enchKind, baseItem) || !EnchantmentUtils.checkLvCap(materiaitem)) {
                            for (int i1 = 0; i1 < RESULT_SLOT_NUM; i1++) {
                                this.materializeResult.setInventorySlotContents(i1, null);
                            }
                            this.enchantmentList.clear();
                            this.enchantmentLevelList.clear();
                            this.itemEnchantmentLvPair.clear();
                            return;
                        }

                        for (Pair<Enchantment, Integer> data : this.itemEnchantmentLvPair) {
                            if (!data.getLeft().canApplyTogether(enchKind)) {
                                this.enchantmentRemoveData.add(data);
                            }
                        }

                        this.itemEnchantmentLvPair.removeAll(this.enchantmentRemoveData);
                        this.enchantmentRemoveData.clear();

                        if (!this.enchantmentList.contains(enchKind.getRegistryName())) {
                            this.enchantmentList.add(enchKind.getRegistryName());
                            this.enchantmentLevelList.add(enchLv);
                        }
                    } else {
                        if (!magicList.contains((byte) materiaitem.getItemDamage())) {
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

                for (Pair<Enchantment, Integer> data : itemEnchantmentLvPair) {
                    EnchantmentUtils.addEnchantmentToItem(result, data.getLeft(), data.getRight());
                }
                this.itemEnchantmentLvPair.clear();
                for (int i2 = 0; i2 < this.enchantmentList.size(); i2++) {
                    EnchantmentUtils.addEnchantmentToItem(result,
                            Enchantment.getEnchantmentByLocation(this.enchantmentList.get(i2).toString()),
                            this.enchantmentLevelList.get(i2));
                }

                result = EnchantmentUtils.getBookResult(result, enchantmentList);
                this.materializeResult.setInventorySlotContents(0, result);

                for (int i = 1; i < RESULT_SLOT_NUM; i++) {
                    this.materializeResult.setInventorySlotContents(i, null);
                }
            } else if (enchTagList != null && enchTagList.tagCount() > 0) {//extract enchantment from Item
                int endIndex = itemEnchantmentLvPair.size() > 8 ? 8 : itemEnchantmentLvPair.size();
                List<Pair<Enchantment, Integer>> subList = itemEnchantmentLvPair.subList(0, endIndex);
                int slotIndex = 0;
                for (Pair<Enchantment, Integer> data : subList) {
                    if (data.getLeft() == null) break;
                    int decreasedLv = EnchantmentUtils.getDecreasedLevel(baseItem, data.getRight());
//                    int damage = this.setMateriaDmgfromEnch(data.getLeft().effectId);
                    if (decreasedLv > 0) {
                        ItemStack materia = new ItemStack(Items.itemMateria, 1, 0);
                        EnchantmentUtils.addEnchantmentToItem(materia, data.getLeft(), decreasedLv);
                        this.materializeResult.setInventorySlotContents(slotIndex + 1, materia);
                    } else {
                        this.materializeResult.setInventorySlotContents(slotIndex + 1, null);
                    }
                    slotIndex++;
                }
                result = EnchantmentUtils.getBookResult(result, itemEnchantmentLvPair.subList(endIndex, itemEnchantmentLvPair.size()));
                this.materializeResult.setInventorySlotContents(0, result);
            } else if (magicList != null) {
                result.getTagCompound().removeTag(NBT_KEY_ENCHANT_CHANGER_MAGIC);
                int slotIndex = 0;
                for (byte b : magicList) {
                    ItemStack materia = new ItemStack(Items.itemMateria, 1, b);
                    this.materializeResult.setInventorySlotContents(slotIndex + 1, materia);
                    slotIndex++;
                    if (slotIndex > 8) break;
                }
                this.materializeResult.setInventorySlotContents(0, result);
            } else {
                for (int i = 0; i < RESULT_SLOT_NUM; i++) {
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

    @Override
    public void onContainerClosed(@Nonnull EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        if (!this.worldPointer.isRemote) {
            if (!this.ItemSourceLeft()) {
                for (int rSlotIndex = 0; rSlotIndex < RESULT_SLOT_NUM; ++rSlotIndex) {
                    ItemStack stack = this.materializeResult.removeStackFromSlot(rSlotIndex);
                    if (stack != null) {
                        playerIn.dropItem(stack, false);
                    }
                }
            }
            for (int sSlotIndex = 0; sSlotIndex < SourceSlotNum; ++sSlotIndex) {
                ItemStack stack = this.materializeSource.removeStackFromSlot(sSlotIndex);
                if (stack != null) {
                    playerIn.dropItem(stack, false);
                }
            }

        }
    }

    private boolean ItemSourceLeft() {
        return this.materializeSource.getStackInSlot(0) != null;
    }

    private boolean checkMateriaFromSlot(IInventory source) {
        boolean ret = false;
        for (int i = 0; i < source.getSizeInventory(); i++) {
            if (source.getStackInSlot(i) != null && source.getStackInSlot(i).getItem() instanceof EcItemMateria)
                ret = true;
        }
        return ret;
    }
}