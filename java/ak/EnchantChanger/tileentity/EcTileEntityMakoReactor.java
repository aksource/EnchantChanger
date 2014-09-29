package ak.EnchantChanger.tileentity;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.fluid.EcMakoReactorTank;
import ak.EnchantChanger.item.EcItemBucketLifeStream;
import ak.EnchantChanger.item.EcItemMateria;
import com.google.common.collect.Range;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.List;

/**
 * Created by A.K. on 14/03/11.
 */
@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore")
public class EcTileEntityMakoReactor extends EcTileMultiPass implements ISidedInventory, IFluidHandler {
    public static final int maxSmeltingTime = 200;
    public static final int smeltingCost = 5;
    public static final int[] slotsMaterial = new int[]{0,1,2};
    public static final int[] slotsFuel = new int[]{3};
    public static final int[] slotsResult = new int[]{4,5,6};
    public static final int materiafuelresult = slotsMaterial.length + slotsFuel.length + slotsResult.length;
    public static final Range<Integer> rangeMaterialSlot = Range.closedOpen(0, 3);
    public static final Range<Integer> rangeFuelBucketSlot = Range.closedOpen(3, 4);
    private static final int maxCreatingPoint = 1000 * 1024;
    private static final Block[][] blocks = new Block[][]{
            {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block},
            {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block},
            {Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block, Blocks.iron_block},
            {Blocks.air, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.air, Blocks.iron_block, Blocks.air},
            {Blocks.air, Blocks.air, Blocks.air, Blocks.air, Blocks.iron_block, Blocks.air, Blocks.air, Blocks.air, Blocks.air}
    };
    private ItemStack[] items = new ItemStack[materiafuelresult];
    private ItemStack[] smeltingItems = new ItemStack[slotsMaterial.length];
    public int smeltingTime = 0;
    private int smeltingClientTime;
    private int creatingHugeMateriaPoint = 0;
    public EcMakoReactorTank tank = new EcMakoReactorTank(1000 * 10);
    private ChunkPosition HMCoord = null;

//    public static final Range<Integer> rangeResultSlot = Range.closedOpen(4, 7);
    public byte face;

    @Override
    public void markDirty() {
        super.markDirty();
        if (!this.worldObj.isRemote) {
            @SuppressWarnings("unchecked")
            List<EntityPlayer> list= this.worldObj.playerEntities;
            for (EntityPlayer player : list) {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP)player).playerNetServerHandler.sendPacket(this.getDescriptionPacket());
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("face", face);
        nbt.setShort("SmeltingTime", (short)smeltingTime);
        nbt.setInteger("createHugeMateria", creatingHugeMateriaPoint);
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte)i);
                items[i].writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        nbt.setTag("Items", nbtTagList);

        NBTTagList nbtTagList2 = new NBTTagList();
        for (int i = 0; i < smeltingItems.length; i++) {
            if (smeltingItems[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte)i);
                smeltingItems[i].writeToNBT(nbtTagCompound);
                nbtTagList2.appendTag(nbtTagCompound);
            }
        }
        nbt.setTag("SmeltingItems", nbtTagList2);

        nbt.setTag("makoTank", tank.writeToNBT(new NBTTagCompound()));
        nbt.setInteger("hmcoordx", HMCoord.chunkPosX);
        nbt.setInteger("hmcoordy", HMCoord.chunkPosY);
        nbt.setInteger("hmcoordz", HMCoord.chunkPosZ);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        face = nbt.getByte("face");
        smeltingTime = nbt.getShort("SmeltingTime");
        creatingHugeMateriaPoint = nbt.getInteger("createHugeMateria");
        NBTTagList nbtTagList = nbt.getTagList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            byte slot = nbtTagCompound.getByte("Slot");
            if (slot >= 0 && slot < items.length) {
                items[slot] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

        NBTTagList nbtTagList2 = nbt.getTagList("SmeltingItems", Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < smeltingItems.length ; j++) {
            smeltingItems[j] = null;
        }
        for (int i = 0; i < nbtTagList2.tagCount(); i++) {
            NBTTagCompound nbtTagCompound = nbtTagList2.getCompoundTagAt(i);
            byte slot = nbtTagCompound.getByte("Slot");
            if (slot >= 0 && slot < smeltingItems.length) {
                smeltingItems[slot] = ItemStack.loadItemStackFromNBT(nbtTagCompound);
            }
        }

        tank.readFromNBT(nbt.getCompoundTag("makoTank"));
        int hmcoordx = nbt.getInteger("hmcoordx");
        int hmcoordy = nbt.getInteger("hmcoordy");
        int hmcoordz = nbt.getInteger("hmcoordz");
        HMCoord = new ChunkPosition(hmcoordx, hmcoordy, hmcoordz);
    }

    @Override
    public void updateEntity() {
        boolean upToDate = false;
        if (!this.worldObj.isRemote && isActivated()) {
            //HugeMateria生成処理
            if (canMakeHugeMateria()) {
                creatingHugeMateriaPoint -= maxCreatingPoint;
                setHugeMateria(HMCoord.chunkPosX, HMCoord.chunkPosY, HMCoord.chunkPosZ);
            }

            //魔晄バケツ・マテリアからの搬入処理
            if (!tank.isFull() && items[slotsFuel[0]] != null && isMako(items[slotsFuel[0]])) {
                int makoAmount = getMakoFromItem(items[slotsFuel[0]]);
                if (tank.getFluidAmount() + makoAmount <= tank.getCapacity()) {
                    tank.fill(new FluidStack(EnchantChanger.fluidLifeStream, makoAmount), true);
                    items[slotsFuel[0]].stackSize--;
                    if (items[slotsFuel[0]].stackSize <= 0) {
                        setInventorySlotContents(slotsFuel[0], items[slotsFuel[0]].getItem().getContainerItem(items[slotsFuel[0]]));
                    }
                    upToDate = true;
                }
            }

            if (canSmelting()) {
                for (int i = 0; i < slotsMaterial.length; i++) {
                    if (smeltingItems[i] == null && canSmeltThisItem(items[slotsMaterial[i]])) {
                        smeltingItems[i] = decrStackSize(slotsMaterial[i], 1);
                        upToDate = true;
                    }
                }
            }

            if (isSmelting()) {
                ++smeltingTime;
                if (smeltingTime == maxSmeltingTime) {
                    smeltItems();
                    smeltingTime = 0;
                    upToDate = true;
                }
            } else {
                smeltingTime = 0;
            }
        } else {
            if (isSmelting()) {
                ++smeltingClientTime;
                if (smeltingClientTime == maxSmeltingTime || smeltingTime == 0) {
                    smeltingClientTime = 0;
                }
            }
        }

        if (upToDate) {
            this.markDirty();
        }
    }
    @Override
    public Packet getDescriptionPacket() {
        return super.getDescriptionPacket();
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
    }

    private void smeltItems() {
        tank.drain(smeltingCost, true);
        creatingHugeMateriaPoint += smeltingCost;
        ItemStack smelted;
        for (int i = 0; i < smeltingItems.length; i++) {
            if (smeltingItems[i] != null) {
                smelted = getSmeltedItem(smeltingItems[i]);
                if (items[slotsResult[i]] == null) {
                    items[slotsResult[i]] = smelted.copy();
                    smeltingItems[i] = null;
                } else {
                    if (items[slotsResult[i]].isItemEqual(smelted)
                            && ItemStack.areItemStackTagsEqual(items[slotsResult[i]], smelted)
                            && items[slotsResult[i]].stackSize < items[slotsResult[i]].getMaxStackSize()) {
                        items[slotsResult[i]].stackSize += smelted.stackSize;
                        smeltingItems[i] = null;
                    }
                }
            }
        }
        this.markDirty();
    }

    public boolean canSmelting() {
        return tank.getFluidAmount() >= smeltingCost;
    }

    public boolean isSmelting() {
        for (ItemStack itemStack : smeltingItems) {
            if (itemStack != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isActivated() {
        if (HMCoord == null) {
            return false;
        }
        Block checkBlock;
        int index;
        for (int y = -1 ; y <= 3; y++) {
            for (int x = -1 ; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    checkBlock = worldObj.getBlock(HMCoord.chunkPosX + x, HMCoord.chunkPosY + y, HMCoord.chunkPosZ + z);
                    index = (x + 1) + (z + 1) * 3;
                    if (!blocks[y + 1][index].equals(Blocks.air) && !blocks[y + 1][index].equals(checkBlock)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isMako(ItemStack itemStack) {
        return itemStack.getItem() instanceof EcItemBucketLifeStream || itemStack.getItem() instanceof EcItemMateria;
    }

    public int getMakoFromItem(ItemStack itemStack) {
        return (itemStack.getItem() instanceof EcItemBucketLifeStream) ? 1000:5;
    }

    public boolean canMakeHugeMateria() {
        if (HMCoord == null) return false;
        for (int i = 0; i < 3 ; i++) {
            if (!worldObj.getBlock(HMCoord.chunkPosX, HMCoord.chunkPosY + i, HMCoord.chunkPosZ).equals(Blocks.air)) {
                return false;
            }
        }
        return creatingHugeMateriaPoint > maxCreatingPoint;
    }

    public boolean canSmeltThisItem(ItemStack itemStack) {
        return itemStack != null && getSmeltedItem(itemStack) != null;
    }

    public ItemStack getSmeltedItem(ItemStack itemStack) {
        return FurnaceRecipes.smelting().getSmeltingResult(itemStack);
    }

    public void setFace(byte var1) {
        this.face = var1;
        ForgeDirection direction = ForgeDirection.VALID_DIRECTIONS[ForgeDirection.OPPOSITES[face]];
        HMCoord = new ChunkPosition(xCoord + direction.offsetX * 2, yCoord, zCoord + direction.offsetZ * 2);
    }

    public void setHugeMateria(int x, int y, int z) {
        Block hugeMateria = EnchantChanger.blockHugeMateria;
        worldObj.setBlock(x, y, z, hugeMateria, 0, 1);
        worldObj.setBlock(x, y + 1, z, hugeMateria, 1, 1);
        worldObj.setBlock(x, y + 2, z, hugeMateria, 2, 1);
        worldObj.notifyBlocksOfNeighborChange(x, y, z, hugeMateria);
        worldObj.notifyBlocksOfNeighborChange(x, y + 1, z, hugeMateria);
        worldObj.notifyBlocksOfNeighborChange(x, y + 2, z, hugeMateria);
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        if (side == 0) {
            return slotsResult;
        }

        if (side == 1) {
            return slotsMaterial;
        }

        return slotsFuel;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return this.isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return side != 0 || rangeFuelBucketSlot.contains(slot) || item.getItem() instanceof ItemBucket;
    }

    @Override
    public int getSizeInventory() {
        return items.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < items.length) {
            return items[slot];
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        if (slot < items.length && items[slot] != null) {
            ItemStack returnStack;
            if (items[slot].stackSize <= size) {
                returnStack = items[slot];
                items[slot] = null;
                return returnStack;
            } else {
                returnStack = items[slot].splitStack(size);
                if (items[slot].stackSize <= 0) {
                    items[slot] = null;
                }
                return returnStack;
            }
        }
        return null;
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
    public void setInventorySlotContents(int slot, ItemStack item) {
        if (slot < items.length) {
            items[slot] = item;
            if (items[slot] != null && items[slot].stackSize > this.getInventoryStackLimit()) {
                items[slot].stackSize = this.getInventoryStackLimit();
            }
        }
    }

    @Override
    public String getInventoryName() {
        return "container.makoreactor";
    }

    @Override
    public boolean hasCustomInventoryName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= 64;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @SideOnly(Side.CLIENT)
    public int getFluidAmountScaled(int scale) {
        return this.tank.getFluidAmount() * scale  / this.tank.getCapacity();
    }

    @SideOnly(Side.CLIENT)
    public int getSmeltingTimeScaled(int scale) {
        return this.smeltingClientTime * scale / maxSmeltingTime;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        if (slot < items.length) {
            if (rangeMaterialSlot.contains(slot)) {
                return canSmeltThisItem(item);
            }

            if (rangeFuelBucketSlot.contains(slot)) {
                return item != null && item.getItem() instanceof EcItemBucketLifeStream;
            }
        }
        return false;
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource == null || resource.getFluid() == null) {
            return 0;
        }
        FluidStack currentFluidStack = tank.getFluid();
        if (currentFluidStack != null && currentFluidStack.amount > 0 && !currentFluidStack.isFluidEqual(resource)) {
            return 0;
        }

        int used = tank.fill(resource, doFill);
        resource.amount -= used;
        return used;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (resource.getFluid().equals(tank.getFluidType())) {
            return tank.drain(resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getBlock() instanceof EcBlockLifeStreamFluid && !tank.isFull();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid.getBlock() instanceof EcBlockLifeStreamFluid && !tank.isEmpty();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }
}
