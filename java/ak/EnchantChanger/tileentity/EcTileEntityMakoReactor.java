package ak.EnchantChanger.tileentity;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.fluid.EcMakoReactorTank;
import ak.EnchantChanger.item.EcItemBucketLifeStream;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.modcoop.CoopSS;
import ak.EnchantChanger.modcoop.CoopTE;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.tileentity.IEnergyInfo;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by A.K. on 14/03/11.
 */
@Optional.InterfaceList(
        {@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore"),
        @Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHCore"),
        @Optional.Interface(iface = "shift.sextiarysector.api.machine.energy.IEnergyHandler", modid = "SextiarySector")}
)
public class EcTileEntityMakoReactor extends EcTileMultiPass implements ISidedInventory, IFluidHandler, IEnergyHandler, IEnergyInfo , shift.sextiarysector.api.machine.energy.IEnergyHandler{
    public static final int MAX_SMELTING_TIME = 200;
    public static final int SMELTING_MAKO_COST = 5;
    public static final int MAX_GENERATING_RF_TIME = 200;
    public static final int GENERATING_RF_MAKO_COST = 5;
    public static final int[] SLOTS_MATERIAL = new int[]{0,1,2};
    public static final int[] SLOTS_FUEL = new int[]{3};
    public static final int[] SLOTS_RESULT = new int[]{4,5,6};
    public static final int SUM_OF_ALLSLOTS = SLOTS_MATERIAL.length + SLOTS_FUEL.length + SLOTS_RESULT.length;
    public static final Range<Integer> RANGE_MATERIAL_SLOTS = Range.closedOpen(0, 3);
    public static final Range<Integer> RANGE_FUEL_SLOTS = Range.closedOpen(3, 4);
    private static final int MAX_HM_CREATING_COST = 1000 * 1024;
    private static final int[][] CONSTRUCTING_BLOCKS_INFO = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 1},
            {0, 1, 0, 1, 2, 1, 0, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0}
    };
    public static final int STEP_RF_VALUE = 10;
    public static final int MAX_OUTPUT_RF_VALUE = 100000;
    public static final int MAX_RF_CAPACITY = 100000000;
    public static final int GF_POWER = 3;
    private ItemStack[] items = new ItemStack[SUM_OF_ALLSLOTS];
    private ItemStack[] smeltingItems = new ItemStack[SLOTS_MATERIAL.length];
    public int smeltingTime;
    public int generatingRFTime = MAX_GENERATING_RF_TIME;
    private int creatingHugeMateriaPoint;
    public EcMakoReactorTank tank = new EcMakoReactorTank(1000 * 10);
    private ChunkPosition HMCoord = null;
    private int outputMaxRFValue = 100;
    private int storedRFEnergy;

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

        nbt.setInteger("outputMaxRFValue", outputMaxRFValue);
        nbt.setInteger("storedRFEnergy", storedRFEnergy);
        nbt.setInteger("generatingRFTime", generatingRFTime);
        nbt.setInteger("nowRF", nowRF);
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

        outputMaxRFValue = nbt.getInteger("outputMaxRFValue");
        storedRFEnergy = nbt.getInteger("storedRFEnergy");
        generatingRFTime = nbt.getInteger("generatingRFTime");
        nowRF = nbt.getInteger("nowRF");
    }

    @Override
    public void updateEntity() {
        boolean upToDate = false;
        if (!this.worldObj.isRemote && isActivated()) {

            //RFの生成と出力
            if (isGenerating()) {
                if (tank.getFluidAmount() >= GENERATING_RF_MAKO_COST * getGeneratingRFMakoCost()) {
                    generatingRF();
                    upToDate = true;
                }
                if (extractRF()) upToDate = true;

                if (extractGF()) upToDate = true;
            }

            //HugeMateria生成処理
            if (canMakeHugeMateria()) {
                creatingHugeMateriaPoint -= MAX_HM_CREATING_COST;
                setHugeMateria(HMCoord.chunkPosX, HMCoord.chunkPosY, HMCoord.chunkPosZ);
            }

            //魔晄バケツ・マテリアからの搬入処理
            if (!tank.isFull() && items[SLOTS_FUEL[0]] != null && isMako(items[SLOTS_FUEL[0]])) {
                int makoAmount = getMakoFromItem(items[SLOTS_FUEL[0]]);
                if (tank.getFluidAmount() + makoAmount <= tank.getCapacity()) {
                    tank.fill(new FluidStack(EnchantChanger.fluidLifeStream, makoAmount), true);
                    items[SLOTS_FUEL[0]].stackSize--;
                    if (items[SLOTS_FUEL[0]].stackSize <= 0) {
                        setInventorySlotContents(SLOTS_FUEL[0], items[SLOTS_FUEL[0]].getItem().getContainerItem(items[SLOTS_FUEL[0]]));
                    }
                    upToDate = true;
                }
            }

            if (canSmelting()) {
                for (int i = 0; i < SLOTS_MATERIAL.length; i++) {
                    if (smeltingItems[i] == null && canSmeltThisItem(items[SLOTS_MATERIAL[i]])) {
                        smeltingItems[i] = decrStackSize(SLOTS_MATERIAL[i], 1);
                        upToDate = true;
                    }
                }
            }

            if (isSmelting()) {
                ++smeltingTime;
                if (smeltingTime == MAX_SMELTING_TIME) {
                    smeltItems();
                    smeltingTime = 0;
                    upToDate = true;
                }
            } else {
                smeltingTime = 0;
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
        tank.drain(SMELTING_MAKO_COST, true);
        creatingHugeMateriaPoint += SMELTING_MAKO_COST;
        ItemStack smelted;
        for (int i = 0; i < smeltingItems.length; i++) {
            if (smeltingItems[i] != null) {
                smelted = getSmeltedItem(smeltingItems[i]);
                if (items[SLOTS_RESULT[i]] == null) {
                    items[SLOTS_RESULT[i]] = smelted.copy();
                    smeltingItems[i] = null;
                } else {
                    if (items[SLOTS_RESULT[i]].isItemEqual(smelted)
                            && ItemStack.areItemStackTagsEqual(items[SLOTS_RESULT[i]], smelted)
                            && items[SLOTS_RESULT[i]].stackSize < items[SLOTS_RESULT[i]].getMaxStackSize()) {
                        items[SLOTS_RESULT[i]].stackSize += smelted.stackSize;
                        smeltingItems[i] = null;
                    }
                }
            }
        }
        this.markDirty();
    }

    public void generatingRF() {
        --generatingRFTime;
        addRFEnergy(getOutputMaxRFValue());
        if (generatingRFTime <= 0) {
            tank.drain(GENERATING_RF_MAKO_COST * getGeneratingRFMakoCost(), true);
            generatingRFTime = MAX_GENERATING_RF_TIME;
            creatingHugeMateriaPoint += getGeneratingRFMakoCost();
        }
    }

    public boolean extractRF() {
        boolean upToDate = false;
        int needToExtract;
        TileEntity neighborTile;
        int extractable;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            extractable = Math.min(getOutputMaxRFValue(), getStoredRFEnergy());
            if (extractable <= 0) break;
            neighborTile = this.worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (CoopTE.isIEnergyHandler(neighborTile)) {
                needToExtract = CoopTE.getNeedRF(neighborTile, direction, extractable);
                extractEnergy(direction, needToExtract, false);
                if (needToExtract > 0) {
                    upToDate = true;
                }
            }
        }
        return upToDate;
    }

    public boolean extractGF() {
        boolean upToDate = false;
        int needToExtract;
        TileEntity neighborTile;
        int extractable;
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            extractable = Math.min(getOutputMaxRFValue(), getStoredRFEnergy());
            if (extractable <= 0) break;
            neighborTile = this.worldObj.getTileEntity(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
            if (CoopSS.isGFEnergyHandler(neighborTile)) {
                needToExtract = CoopSS.getNeedGF(neighborTile, direction, extractable);
                drawEnergy(direction, GF_POWER, needToExtract, false);
                if (needToExtract > 0) {
                    upToDate = true;
                }
            }
        }
        return upToDate;
    }

    public boolean canSmelting() {
        return tank.getFluidAmount() >= SMELTING_MAKO_COST;
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
        int checkBlockMeta;
        int index;
        String blockName;
        for (int y = -1 ; y <= 3; y++) {
            for (int x = -1 ; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    checkBlock = worldObj.getBlock(HMCoord.chunkPosX + x, HMCoord.chunkPosY + y, HMCoord.chunkPosZ + z);
                    checkBlockMeta = worldObj.getBlockMetadata(HMCoord.chunkPosX + x, HMCoord.chunkPosY + y, HMCoord.chunkPosZ + z);
                    index = (x + 1) + (z + 1) * 3;
                    if (CONSTRUCTING_BLOCKS_INFO[y + 1][index] == 1 && !isBaseBlock(checkBlock, checkBlockMeta)) {
                        return false;
                    }

                    if (CONSTRUCTING_BLOCKS_INFO[y + 1][index] == 2 && !checkBlock.equals(Blocks.air)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isBaseBlock(Block checkBlock, int checkMeta) {
        int[] oreIDs = OreDictionary.getOreIDs(getBaseBlockItemStack());
        ItemStack checkStack = new ItemStack(checkBlock, 1, checkMeta);
        if (oreIDs.length > 0) {
            for (int oreid : oreIDs) {
                ArrayList<ItemStack> oreList = OreDictionary.getOres(OreDictionary.getOreName(oreid));
                for (ItemStack itemStack : oreList) {
                    if (itemStack.isItemEqual(checkStack)) {
                        return true;
                    }
                }
            }
        }
        return getBaseBlock().equals(checkBlock) && getBlockMetadata() == checkMeta;
    }

    public boolean isGenerating() {
        return isEnergyNetworkModLoaded() && isPowered() && getStoredRFEnergy() + getOutputMaxRFValue() <= MAX_RF_CAPACITY;
    }

    public boolean isEnergyNetworkModLoaded() {
        return EnchantChanger.loadTE || EnchantChanger.loadSS;
    }

    public boolean isPowered() {
        return this.worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
    }

    public boolean isMako(ItemStack itemStack) {
        return itemStack.getItem() instanceof EcItemBucketLifeStream || itemStack.getItem() instanceof EcItemMateria;
    }

    public int getMakoFromItem(ItemStack itemStack) {
        return (itemStack.getItem() instanceof EcItemBucketLifeStream) ? 1000:5;
    }

    public int getGeneratingRFMakoCost() {
        return getOutputMaxRFValue() / STEP_RF_VALUE;
    }

    public boolean canMakeHugeMateria() {
        if (HMCoord == null) return false;
        for (int i = 0; i < 3 ; i++) {
            if (!worldObj.getBlock(HMCoord.chunkPosX, HMCoord.chunkPosY + i, HMCoord.chunkPosZ).equals(Blocks.air)) {
                return false;
            }
        }
        return creatingHugeMateriaPoint > MAX_HM_CREATING_COST;
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
            return SLOTS_RESULT;
        }

        if (side == 1) {
            return SLOTS_MATERIAL;
        }

        return SLOTS_FUEL;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return this.isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return side != 1 || RANGE_FUEL_SLOTS.contains(slot) || item.getItem() instanceof ItemBucket;
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
        return this.smeltingTime * scale / MAX_SMELTING_TIME;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        if (slot < items.length) {
            if (RANGE_MATERIAL_SLOTS.contains(slot)) {
                return canSmeltThisItem(item);
            }

            if (RANGE_FUEL_SLOTS.contains(slot)) {
                return item != null && isMako(item);
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

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int receiveEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        return 0;//発電のみ
    }

    private int nowRF;

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int extractEnergy(ForgeDirection forgeDirection, int i, boolean b) {
        int extract = Math.min(getStoredRFEnergy(), Math.min(getOutputMaxRFValue(), i));
        if (!b) {
            addRFEnergy(-extract);
            nowRF = extract;
        }
        return extract;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getEnergyStored(ForgeDirection forgeDirection) {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getMaxEnergyStored(ForgeDirection forgeDirection) {
        return MAX_RF_CAPACITY;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public boolean canConnectEnergy(ForgeDirection forgeDirection) {
        TileEntity tile = this.worldObj.getTileEntity(xCoord + forgeDirection.offsetX, yCoord + forgeDirection.offsetY, zCoord + forgeDirection.offsetZ);
        return tile instanceof IEnergyConnection;
    }

    public int getOutputMaxRFValue() {
        return outputMaxRFValue;
    }

    public void setOutputMaxRFValue(int outputMaxRFValue) {
        this.outputMaxRFValue = outputMaxRFValue;
    }

    public void stepOutputMaxRFValue(int stepValue) {
        if (this.outputMaxRFValue + stepValue >= 10 && this.outputMaxRFValue + stepValue <= MAX_OUTPUT_RF_VALUE) {
            this.outputMaxRFValue += stepValue;
        }
    }

    public int getStoredRFEnergy() {
        return storedRFEnergy;
    }

    public void setStoredRFEnergy(int storedRFEnergy) {
        this.storedRFEnergy = storedRFEnergy;
    }

    public void addRFEnergy(int add) {
        this.storedRFEnergy += add;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getInfoEnergyPerTick() {
        return nowRF;
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getInfoMaxEnergyPerTick() {
        return getOutputMaxRFValue();
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getInfoEnergyStored() {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "CoFHCore")
    @Override
    public int getInfoMaxEnergyStored() {
        return MAX_RF_CAPACITY;
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public int addEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
        return 0;
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public int drawEnergy(ForgeDirection from, int power, int speed, boolean simulate) {
        int extract = Math.min(getStoredRFEnergy(), Math.min(getOutputMaxRFValue(), speed));
        if (!simulate) {
            addRFEnergy(-extract);
            nowRF = extract;
        }
        return extract;
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public boolean canInterface(ForgeDirection from) {
        return true;
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public int getPowerStored(ForgeDirection from) {
        return 3;
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public long getSpeedStored(ForgeDirection from) {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public int getMaxPowerStored(ForgeDirection from) {
        return 3;
    }

    @Optional.Method(modid = "SextiarySector")
    @Override
    public long getMaxSpeedStored(ForgeDirection from) {
        return MAX_RF_CAPACITY;
    }
}
