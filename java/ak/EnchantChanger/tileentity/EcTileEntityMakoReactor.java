package ak.EnchantChanger.tileentity;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.MakoUtils;
import ak.EnchantChanger.block.EcBlockHugeMateria;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.fluid.EcMakoReactorTank;
import ak.EnchantChanger.modcoop.CoopSS;
import ak.EnchantChanger.modcoop.CoopTE;
import ak.EnchantChanger.utils.ConfigurationUtils;
import ak.EnchantChanger.utils.EnchantmentUtils;
import com.google.common.collect.Range;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentData;
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
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

//import cofh.api.energy.IEnergyConnection;

/**
 * Created by A.K. on 14/03/11.
 */
@Optional.InterfaceList(
        {@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore"),
                @Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHCore"),
                @Optional.Interface(iface = "shift.sextiarysector.api.machine.energy.IEnergyHandler", modid = "SextiarySector")}
)
public class EcTileEntityMakoReactor extends EcTileMultiPass implements IUpdatePlayerListBox, ISidedInventory, IFluidHandler/*, IEnergyHandler, IEnergyInfo , shift.sextiarysector.api.machine.energy.IEnergyHandler*/ {
    public static final int MAX_SMELTING_TIME = 200;
    public static final int SMELTING_MAKO_COST = 5;
    public static final int MAX_GENERATING_RF_TIME = 200;
    public static final int GENERATING_RF_MAKO_COST = 5;
    public static final int[] SLOTS_MATERIAL = new int[]{0, 1, 2};
    public static final int[] SLOTS_FUEL = new int[]{3};
    public static final int[] SLOTS_RESULT = new int[]{4, 5, 6, 7};
    public static final int SUM_OF_ALLSLOTS = SLOTS_MATERIAL.length + SLOTS_FUEL.length + SLOTS_RESULT.length;
    public static final Range<Integer> RANGE_MATERIAL_SLOTS = Range.closedOpen(0, 3);
    public static final Range<Integer> RANGE_FUEL_SLOTS = Range.closedOpen(3, 4);
    public static final int STEP_RF_VALUE = 10;
    public static final int MAX_OUTPUT_RF_VALUE = 100000;
    public static final int MAX_RF_CAPACITY = 100000000;
    public static final int GF_POWER = 3;
    private static final int MAX_HM_CREATING_COST = 1000 * 1024;
    private static final int[][] CONSTRUCTING_BLOCKS_INFO = new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 1},
            {0, 1, 0, 1, 2, 1, 0, 1, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0}
    };
    public int smeltingTime;
    public int generatingRFTime = MAX_GENERATING_RF_TIME;
    public EcMakoReactorTank tank = new EcMakoReactorTank(1000 * 10);
    //    public static final Range<Integer> rangeResultSlot = Range.closedOpen(4, 7);
    public byte face;
    private ItemStack[] items = new ItemStack[SUM_OF_ALLSLOTS];
    private ItemStack[] smeltingItems = new ItemStack[SLOTS_MATERIAL.length];
    private int creatingHugeMateriaPoint;
    private BlockPos HMCoord = null;
    private int outputMaxRFValue = 100;
    private int storedRFEnergy;
    private int nowRF;

    @Override
    public void markDirty() {
        super.markDirty();
        if (!this.worldObj.isRemote) {
            @SuppressWarnings("unchecked")
            List<EntityPlayer> list = this.worldObj.playerEntities;
            for (EntityPlayer player : list) {
                if (player instanceof EntityPlayerMP) {
                    ((EntityPlayerMP) player).playerNetServerHandler.sendPacket(this.getDescriptionPacket());
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("face", face);
        nbt.setShort("SmeltingTime", (short) smeltingTime);
        nbt.setInteger("createHugeMateria", creatingHugeMateriaPoint);
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                items[i].writeToNBT(nbtTagCompound);
                nbtTagList.appendTag(nbtTagCompound);
            }
        }
        nbt.setTag("Items", nbtTagList);

        NBTTagList nbtTagList2 = new NBTTagList();
        for (int i = 0; i < smeltingItems.length; i++) {
            if (smeltingItems[i] != null) {
                NBTTagCompound nbtTagCompound = new NBTTagCompound();
                nbtTagCompound.setByte("Slot", (byte) i);
                smeltingItems[i].writeToNBT(nbtTagCompound);
                nbtTagList2.appendTag(nbtTagCompound);
            }
        }
        nbt.setTag("SmeltingItems", nbtTagList2);

        nbt.setTag("makoTank", tank.writeToNBT(new NBTTagCompound()));
        nbt.setInteger("hmcoordx", HMCoord.getX());
        nbt.setInteger("hmcoordy", HMCoord.getY());
        nbt.setInteger("hmcoordz", HMCoord.getZ());

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
        for (int j = 0; j < smeltingItems.length; j++) {
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
        HMCoord = new BlockPos(hmcoordx, hmcoordy, hmcoordz);

        outputMaxRFValue = nbt.getInteger("outputMaxRFValue");
        storedRFEnergy = nbt.getInteger("storedRFEnergy");
        generatingRFTime = nbt.getInteger("generatingRFTime");
        nowRF = nbt.getInteger("nowRF");
    }

    @Override
    public void update() {
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
                setHugeMateria(HMCoord);
            }

            //魔晄バケツ・マテリアからの搬入処理
            if (!tank.isFull() && items[SLOTS_FUEL[0]] != null && MakoUtils.isMako(items[SLOTS_FUEL[0]])) {
                int makoAmount = MakoUtils.getMakoFromItem(items[SLOTS_FUEL[0]]);
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
        if (this.worldObj.rand.nextInt(ConfigurationUtils.materiaGeneratingRatio) == 0 && items[SLOTS_RESULT[SLOTS_RESULT.length - 1]] == null) {
            ItemStack materia = new ItemStack(EnchantChanger.itemMateria);
            EnchantmentData enchantmentData = EnchantmentUtils.getEnchantmentData(this.worldObj.rand);
            EnchantmentUtils.addEnchantmentToItem(materia, enchantmentData.enchantmentobj, enchantmentData.enchantmentLevel);
            items[SLOTS_RESULT[SLOTS_RESULT.length - 1]] = materia;
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
        for (EnumFacing direction : EnumFacing.values()) {
            extractable = Math.min(getOutputMaxRFValue(), getStoredRFEnergy());
            if (extractable <= 0) break;
            neighborTile = this.worldObj.getTileEntity(this.getPos().offset(direction));
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
        for (EnumFacing direction : EnumFacing.values()) {
            extractable = Math.min(getOutputMaxRFValue(), getStoredRFEnergy());
            if (extractable <= 0) break;
            neighborTile = this.worldObj.getTileEntity(this.getPos().offset(direction));
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
        IBlockState checkBlockState;
        int index;
        for (int y = -1; y <= 3; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    checkBlockState = worldObj.getBlockState(HMCoord.add(x, y, z));
                    index = (x + 1) + (z + 1) * 3;
                    if (CONSTRUCTING_BLOCKS_INFO[y + 1][index] == 1 && !isBaseBlock(checkBlockState)) {
                        return false;
                    }

                    if (CONSTRUCTING_BLOCKS_INFO[y + 1][index] == 2 && !checkBlockState.getBlock().equals(Blocks.air)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isBaseBlock(IBlockState state) {
        Block checkBlock = state.getBlock();
        int checkMeta = checkBlock.getMetaFromState(state);
        int[] oreIDs = OreDictionary.getOreIDs(getBaseBlockItemStack());
        ItemStack checkStack = new ItemStack(checkBlock, 1, checkMeta);
        if (oreIDs.length > 0) {
            for (int oreid : oreIDs) {
                List<ItemStack> oreList = OreDictionary.getOres(OreDictionary.getOreName(oreid));
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
        return this.worldObj.isBlockPowered(this.getPos());
    }

    public int getGeneratingRFMakoCost() {
        return getOutputMaxRFValue() / STEP_RF_VALUE;
    }

    public boolean canMakeHugeMateria() {
        if (HMCoord == null) return false;
        for (int i = 0; i < 3; i++) {
            if (!worldObj.isAirBlock(HMCoord.up(i))) {
                return false;
            }
        }
        return creatingHugeMateriaPoint > MAX_HM_CREATING_COST;
    }

    public boolean canSmeltThisItem(ItemStack itemStack) {
        return itemStack != null && getSmeltedItem(itemStack) != null;
    }

    public ItemStack getSmeltedItem(ItemStack itemStack) {
        return FurnaceRecipes.instance().getSmeltingResult(itemStack);
    }

    public void setFace(byte var1) {
        this.face = var1;
        EnumFacing direction = EnumFacing.getFront(var1).getOpposite();
        HMCoord = new BlockPos(this.getPos().getX() + direction.getFrontOffsetX() * 2, this.getPos().getY(), this.getPos().getZ() + direction.getFrontOffsetZ() * 2);
    }

    public void setHugeMateria(BlockPos blockPos) {
        Block hugeMateria = EnchantChanger.blockHugeMateria;
        worldObj.setBlockState(blockPos, hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.propertyParts, 0));
        worldObj.setBlockState(blockPos.up(), hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.propertyParts, 1));
        worldObj.setBlockState(blockPos.up(2), hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.propertyParts, 2));
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_RESULT;
        }

        if (side == EnumFacing.UP) {
            return SLOTS_MATERIAL;
        }

        return SLOTS_FUEL;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack item, EnumFacing side) {
        return this.isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack item, EnumFacing side) {
        return side != EnumFacing.UP || RANGE_FUEL_SLOTS.contains(slot) || item.getItem() instanceof ItemBucket;
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
    public String getName() {
        return "container.makoreactor";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @SideOnly(Side.CLIENT)
    public int getFluidAmountScaled(int scale) {
        return this.tank.getFluidAmount() * scale / this.tank.getCapacity();
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
                return item != null && MakoUtils.isMako(item);
            }
        }
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
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
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (resource.getFluid().equals(tank.getFluidType())) {
            return tank.drain(resource.amount, doDrain);
        }
        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid.getBlock() instanceof EcBlockLifeStreamFluid && !tank.isFull();
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid.getBlock() instanceof EcBlockLifeStreamFluid && !tank.isEmpty();
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int receiveEnergy(EnumFacing facing, int i, boolean b) {
        return 0;//発電のみ
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int extractEnergy(EnumFacing facing, int i, boolean b) {
        int extract = Math.min(getStoredRFEnergy(), Math.min(getOutputMaxRFValue(), i));
        if (!b) {
            addRFEnergy(-extract);
            nowRF = extract;
        }
        return extract;
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int getEnergyStored(EnumFacing facing) {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int getMaxEnergyStored(EnumFacing facing) {
        return MAX_RF_CAPACITY;
    }

//    @Optional.Method(modid = "CoFHCore")
//    @Override
//    public boolean canConnectEnergy(EnumFacing facing) {
//        TileEntity tile = this.worldObj.getTileEntity(this.getPos().offset(facing));
//        return tile instanceof IEnergyConnection;
//    }

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
//    @Override
    public int getInfoEnergyPerTick() {
        return nowRF;
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int getInfoMaxEnergyPerTick() {
        return getOutputMaxRFValue();
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int getInfoEnergyStored() {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "CoFHCore")
//    @Override
    public int getInfoMaxEnergyStored() {
        return MAX_RF_CAPACITY;
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public int addEnergy(EnumFacing from, int power, int speed, boolean simulate) {
        return 0;
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public int drawEnergy(EnumFacing from, int power, int speed, boolean simulate) {
        int extract = Math.min(getStoredRFEnergy(), Math.min(getOutputMaxRFValue(), speed));
        if (!simulate) {
            addRFEnergy(-extract);
            nowRF = extract;
        }
        return extract;
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public boolean canInterface(EnumFacing from) {
        return true;
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public int getPowerStored(EnumFacing from) {
        return 3;
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public long getSpeedStored(EnumFacing from) {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public int getMaxPowerStored(EnumFacing from) {
        return 3;
    }

    @Optional.Method(modid = "SextiarySector")
//    @Override
    public long getMaxSpeedStored(EnumFacing from) {
        return MAX_RF_CAPACITY;
    }
}
