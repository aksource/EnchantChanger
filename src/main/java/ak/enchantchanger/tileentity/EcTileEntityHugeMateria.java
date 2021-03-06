package ak.enchantchanger.tileentity;

import ak.enchantchanger.api.MasterMateriaUtils;
import ak.enchantchanger.item.EcItemMasterMateria;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.EnchantmentUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EcTileEntityHugeMateria extends TileEntity implements ITickable, IInventory {
    public int materializingTime = 0;
    public float angle = 0;
    private ItemStack result = null;
    private int consumedExpBottle = 0;
    private final NonNullList<ItemStack> slotItems = NonNullList.withSize(5, ItemStack.EMPTY);

    @Override
    public int getSizeInventory() {
        return slotItems.size();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return slotItems.get(slot);
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        slotItems.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > getInventoryStackLimit()) {
            stack.setCount(getInventoryStackLimit());
        }
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slot, int amt) {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty()) {
            if (stack.getCount() <= amt) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                stack = stack.splitStack(amt);
                if (stack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }
        return stack;
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (!stack.isEmpty()) {
            setInventorySlotContents(slot, ItemStack.EMPTY);
        }
        return stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return world.getTileEntity(this.getPos()) == this &&
                player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.65D)) < 64;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    @SideOnly(Side.CLIENT)
    public int getMaterializingProgressScaled(int i) {
        return this.materializingTime * i / 200;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        NBTTagList tagList = nbtTagCompound.getTagList("Items", Constants.NBT.TAG_COMPOUND);

        for (int count = 0; count < tagList.tagCount(); ++count) {
            NBTTagCompound tag = tagList.getCompoundTagAt(count);
            int slot = tag.getByte("Slot") & 255;

            if (slot < this.slotItems.size()) {
                this.slotItems.set(slot, new ItemStack(tag));
            }
        }
        this.materializingTime = nbtTagCompound.getShort("materializingTime");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbtTagCompound) {
        NBTTagCompound nbt = super.writeToNBT(nbtTagCompound);
        nbt.setShort("materializingTime", (short) this.materializingTime);
        NBTTagList nbtTagList = new NBTTagList();

        for (int slot = 0; slot < this.slotItems.size(); ++slot) {
            if (!this.slotItems.get(slot).isEmpty()) {
                NBTTagCompound var4 = new NBTTagCompound();
                var4.setByte("Slot", (byte) slot);
                this.slotItems.get(slot).writeToNBT(var4);
                nbtTagList.appendTag(var4);
            }
        }

        nbt.setTag("Items", nbtTagList);
        return nbt;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
        if (pkt.getPos().equals(this.getPos())) {
            this.readFromNBT(pkt.getNbtCompound());
        }
    }

    @Override
    public void update() {
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

        if (!this.world.isRemote) {
            if (this.canMake()) {
                ++this.materializingTime;

                if (this.materializingTime == 200) {
                    this.materializingTime = 0;
                    this.makeMateria();
                    var2 = true;
                }
            } else {
                this.materializingTime = 0;
            }
        }

        if (var2) {
            this.markDirty();
        }
    }

    @Override
    @Nonnull
    public String getName() {
        return "container.hugeMateria";
    }

    public boolean canMake() {
        ItemStack hMateria = this.getStackInSlot(0);
        ItemStack base = this.getStackInSlot(1);
        ItemStack expBottle = this.getStackInSlot(2);
        ItemStack material = this.getStackInSlot(3);
        ItemStack resultItem = this.getStackInSlot(4);
        if (base.isEmpty()
                || !(base.getItem() instanceof EcItemMateria)
                || !resultItem.isEmpty()
                || material.isEmpty()
                || (!expBottle.isEmpty() && !isBottle(expBottle))) {
            return false;
        }

        if (!hMateria.isEmpty() && hMateria.getItem() instanceof EcItemMasterMateria) {
            int dmg = hMateria.getItemDamage();
            return dmg >= 0 && dmg < 6 && makeResult(material, dmg);
        }

        return materiaLvUp(material, expBottle);
    }

    private boolean makeResult(ItemStack material, int dmg) {
        if (MasterMateriaUtils.isMaterialValid(dmg, material)) {
            this.result = MasterMateriaUtils.getResult(dmg, material);
            return true;
        }
        return false;
    }

    public boolean isBottle(ItemStack item) {
        return !item.isEmpty() && (item.getItem().equals(Items.EXPERIENCE_BOTTLE) || item.getItem().equals(ak.enchantchanger.utils.Items.itemExExpBottle));
    }

    private boolean containMasterMateria() {
        return !this.getStackInSlot(0).isEmpty() && this.getStackInSlot(0).getItem() instanceof EcItemMasterMateria;
    }

    private boolean isValidItem(ItemStack item1, ItemStack item2) {
        if (item1.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            return item1.getItem() == item2.getItem();
        }

        return item1.isItemEqual(item2);
    }

    private boolean materiaLvUp(ItemStack materia, ItemStack bottle) {
        if (materia.getItem() instanceof EcItemMateria
                && bottle != null
                && materia.getItemDamage() == 0
                && materia.isItemEnchanted()) {
            Enchantment enchantment = EnchantmentUtils.getEnchantmentFromItemStack(materia);
            int lv = EnchantmentUtils.getEnchantmentLv(materia);
            ItemStack lvUpItem = EnchantmentUtils.getLvUpItem(enchantment, lv);
            consumedExpBottle = lvUpItem.getCount();
            if (!lvUpItem.isItemEqual(bottle) || lvUpItem.getCount() > bottle.getCount()) {
                return false;
            }
            result = materia.copy();
            result.getTagCompound().removeTag("ench");
            EnchantmentUtils.addEnchantmentToItem(result, enchantment, lv + 1);
            return true;
        }
        return false;
    }

    public boolean makeMateria() {
        for (int i = 1; i < 4; i++) {
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
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int i, @Nonnull ItemStack itemstack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public boolean isEmpty() {
        return slotItems.stream().filter(itemStack -> !itemStack.isEmpty()).count() == 0;
    }
}