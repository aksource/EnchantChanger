package ak.enchantchanger.tileentity;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.api.MakoUtils;
import ak.enchantchanger.block.EcBlockHugeMateria;
import ak.enchantchanger.fluid.EcMakoReactorTank;
import ak.enchantchanger.modcoop.CoopSS;
import ak.enchantchanger.modcoop.CoopTE;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.EnchantmentUtils;
import ak.enchantchanger.utils.Items;
import com.google.common.collect.Range;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//import cofh.api.energy.IEnergyConnection;

/**
 * 魔晄炉のTileEntityクラス
 * Created by A.K. on 14/03/11.
 */
@Optional.InterfaceList(
        {@Optional.Interface(iface = "cofh.api.energy.IEnergyHandler", modid = "CoFHCore"),
                @Optional.Interface(iface = "cofh.api.tileentity.IEnergyInfo", modid = "CoFHCore"),
                @Optional.Interface(iface = "shift.sextiarysector.api.machine.energy.IEnergyHandler", modid = "SextiarySector")}
)
public class EcTileEntityMakoReactor extends EcTileMultiPass implements ITickable, ISidedInventory, IFluidHandler/*, IEnergyHandler, IEnergyInfo , shift.sextiarysector.api.machine.energy.IEnergyHandler*/ {
    public static final int MAX_SMELTING_TIME = 200;
    public static final int SMELTING_MAKO_COST = 5;
    public static final int MAX_GENERATING_RF_TIME = 200;
    public static final int GENERATING_RF_MAKO_COST = 5;
    public static final int[] SLOTS_MATERIAL = new int[]{0, 1, 2};
    public static final int[] SLOTS_FUEL = new int[]{3};
    public static final int[] SLOTS_RESULT = new int[]{4, 5, 6, 7};
    public static final int SUM_OF_ALL_SLOTS = SLOTS_MATERIAL.length + SLOTS_FUEL.length + SLOTS_RESULT.length;
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
    private final List<ItemStack> items = new ArrayList<>(SUM_OF_ALL_SLOTS);
    private final List<ItemStack> smeltingItems = new ArrayList<>(SLOTS_MATERIAL.length);
    public int smeltingTime;
    public int generatingRFTime = MAX_GENERATING_RF_TIME;
    public EcMakoReactorTank tank = new EcMakoReactorTank(1000 * 10);
    //    public static final Range<Integer> rangeResultSlot = Range.closedOpen(4, 7);
    public byte face;
    private int creatingHugeMateriaPoint;
    private BlockPos posHugeMateria = null;
    private int outputMaxRFValue = 100;
    private int storedRFEnergy;
    private int nowRF;

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagCompound nbtTagCompound = super.writeToNBT(nbt);
        nbtTagCompound.setByte(ak.enchantchanger.api.Constants.NBT_REACTOR_FACE, face);
        nbtTagCompound.setShort(ak.enchantchanger.api.Constants.NBT_REACTOR_SMELTING_TIME, (short) smeltingTime);
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_CREATE_HUGE_MATERIA, creatingHugeMateriaPoint);
        NBTTagList nbtTagList = new NBTTagList();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setByte(ak.enchantchanger.api.Constants.NBT_SLOT, (byte) i);
                items.get(i).writeToNBT(nbtTagCompound1);
                nbtTagList.appendTag(nbtTagCompound1);
            }
        }
        nbtTagCompound.setTag(ak.enchantchanger.api.Constants.NBT_ITEMS, nbtTagList);

        NBTTagList nbtTagList2 = new NBTTagList();
        for (int i = 0; i < smeltingItems.size(); i++) {
            if (smeltingItems != null) {
                NBTTagCompound nbtTagCompound1 = new NBTTagCompound();
                nbtTagCompound1.setByte(ak.enchantchanger.api.Constants.NBT_SLOT, (byte) i);
                smeltingItems.get(i).writeToNBT(nbtTagCompound1);
                nbtTagList2.appendTag(nbtTagCompound1);
            }
        }
        nbtTagCompound.setTag(ak.enchantchanger.api.Constants.NBT_REACTOR_SMELTING_ITEMS, nbtTagList2);

        nbtTagCompound.setTag(ak.enchantchanger.api.Constants.NBT_REACTOR_MAKO_TANK, tank.writeToNBT(new NBTTagCompound()));
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_HMCOORDX, posHugeMateria.getX());
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_HMCOORDY, posHugeMateria.getY());
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_HMCOORDZ, posHugeMateria.getZ());

        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_OUTPUT_MAX_RF_VALUE, outputMaxRFValue);
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_STORED_RF_ENERGY, storedRFEnergy);
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_GENERATING_RF_TIME, generatingRFTime);
        nbtTagCompound.setInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_NOW_RF, nowRF);
        return nbtTagCompound;
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        face = nbt.getByte(ak.enchantchanger.api.Constants.NBT_REACTOR_FACE);
        smeltingTime = nbt.getShort(ak.enchantchanger.api.Constants.NBT_REACTOR_SMELTING_TIME);
        creatingHugeMateriaPoint = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_CREATE_HUGE_MATERIA);
        NBTTagList nbtTagList = nbt.getTagList(ak.enchantchanger.api.Constants.NBT_ITEMS, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtTagList.tagCount(); i++) {
            NBTTagCompound nbtTagCompound = nbtTagList.getCompoundTagAt(i);
            byte slot = nbtTagCompound.getByte(ak.enchantchanger.api.Constants.NBT_SLOT);
            if (slot >= 0 && slot < items.size()) {
                items.set(slot, ItemStack.loadItemStackFromNBT(nbtTagCompound));
            }
        }

        NBTTagList nbtTagList2 = nbt.getTagList(ak.enchantchanger.api.Constants.NBT_REACTOR_SMELTING_ITEMS, Constants.NBT.TAG_COMPOUND);
        for (int j = 0; j < smeltingItems.size(); j++) {
            smeltingItems.set(j, null);
        }
        for (int i = 0; i < nbtTagList2.tagCount(); i++) {
            NBTTagCompound nbtTagCompound = nbtTagList2.getCompoundTagAt(i);
            byte slot = nbtTagCompound.getByte(ak.enchantchanger.api.Constants.NBT_SLOT);
            if (slot >= 0 && slot < smeltingItems.size()) {
                smeltingItems.set(slot, ItemStack.loadItemStackFromNBT(nbtTagCompound));
            }
        }

        tank.readFromNBT(nbt.getCompoundTag(ak.enchantchanger.api.Constants.NBT_REACTOR_MAKO_TANK));
        int hmcoordx = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_HMCOORDX);
        int hmcoordy = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_HMCOORDY);
        int hmcoordz = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_HMCOORDZ);
        posHugeMateria = new BlockPos(hmcoordx, hmcoordy, hmcoordz);

        outputMaxRFValue = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_OUTPUT_MAX_RF_VALUE);
        storedRFEnergy = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_STORED_RF_ENERGY);
        generatingRFTime = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_GENERATING_RF_TIME);
        nowRF = nbt.getInteger(ak.enchantchanger.api.Constants.NBT_REACTOR_NOW_RF);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        IBlockState state = this.worldObj.getBlockState(this.pos);
        this.worldObj.notifyBlockUpdate(this.pos, state, state, 3);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.getPos(), 0, this.writeToNBT(new NBTTagCompound()));
    }

    @Override
    public void onDataPacket(@Nonnull NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
        if (pkt.getPos().equals(this.getPos())) {
            this.readFromNBT(pkt.getNbtCompound());
        }
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void update() {
        boolean upToDate = false;
        if (!this.getWorld().isRemote && isActivated()) {

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
                setHugeMateria(posHugeMateria);
            }

            //魔晄バケツ・マテリアからの搬入処理
            if (!tank.isFull() && items.size() > SLOTS_FUEL[0] && items.get(SLOTS_FUEL[0]) != null && MakoUtils.isMako(items.get(SLOTS_FUEL[0]))) {
                int makoAmount = MakoUtils.getMakoFromItem(items.get(SLOTS_FUEL[0]));
                if (tank.getFluidAmount() + makoAmount <= tank.getCapacity()) {
                    tank.fill(new FluidStack(ak.enchantchanger.utils.Blocks.fluidLifeStream, makoAmount), true);
                    items.get(SLOTS_FUEL[0]).stackSize--;
                    if (items.get(SLOTS_FUEL[0]).stackSize <= 0) {
                        setInventorySlotContents(SLOTS_FUEL[0], items.get(SLOTS_FUEL[0]).getItem().getContainerItem(items.get(SLOTS_FUEL[0])));
                    }
                    upToDate = true;
                }
            }

            if (canSmelting()) {
                for (int i = 0; i < SLOTS_MATERIAL.length; i++) {
                    if (smeltingItems.get(i) == null && canSmeltThisItem(items.get(SLOTS_MATERIAL[i]))) {
                        smeltingItems.set(i, decrStackSize(SLOTS_MATERIAL[i], 1));
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

    private void smeltItems() {
        tank.drain(SMELTING_MAKO_COST, true);
        creatingHugeMateriaPoint += SMELTING_MAKO_COST;
        ItemStack smelted;
        for (int i = 0; i < smeltingItems.size(); i++) {
            if (smeltingItems.get(i) != null) {
                smelted = getSmeltedItem(smeltingItems.get(i));
                if (items.get(SLOTS_RESULT[i]) == null) {
                    items.set(SLOTS_RESULT[i], smelted.copy());
                    smeltingItems.set(i, null);
                } else {
                    if (items.get(SLOTS_RESULT[i]).isItemEqual(smelted)
                            && ItemStack.areItemStackTagsEqual(items.get(SLOTS_RESULT[i]), smelted)
                            && items.get(SLOTS_RESULT[i]).stackSize < items.get(SLOTS_RESULT[i]).getMaxStackSize()) {
                        items.get(SLOTS_RESULT[i]).stackSize += smelted.stackSize;
                        smeltingItems.set(i, null);
                    }
                }
            }
        }
        if (this.getWorld().rand.nextInt(ConfigurationUtils.materiaGeneratingRatio) == 0 && items.get(SLOTS_RESULT[SLOTS_RESULT.length - 1]) == null) {
            ItemStack materia = new ItemStack(Items.itemMateria);
            EnchantmentData enchantmentData = EnchantmentUtils.getEnchantmentData(this.getWorld().rand);
            EnchantmentUtils.addEnchantmentToItem(materia, enchantmentData.enchantmentobj, enchantmentData.enchantmentLevel);
            items.set(SLOTS_RESULT[SLOTS_RESULT.length - 1], materia);
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
            neighborTile = this.getWorld().getTileEntity(this.getPos().offset(direction));
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
            neighborTile = this.getWorld().getTileEntity(this.getPos().offset(direction));
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
        if (posHugeMateria == null) {
            return false;
        }
        IBlockState checkBlockState;
        int index;
        for (int y = -1; y <= 3; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    checkBlockState = worldObj.getBlockState(posHugeMateria.add(x, y, z));
                    index = (x + 1) + (z + 1) * 3;
                    if (CONSTRUCTING_BLOCKS_INFO[y + 1][index] == 1 && !isBaseBlock(checkBlockState)) {
                        return false;
                    }

                    if (CONSTRUCTING_BLOCKS_INFO[y + 1][index] == 2 && !checkBlockState.getBlock().equals(Blocks.AIR)) {
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
        return this.getWorld().isBlockPowered(this.getPos());
    }

    public int getGeneratingRFMakoCost() {
        return getOutputMaxRFValue() / STEP_RF_VALUE;
    }

    public boolean canMakeHugeMateria() {
        if (posHugeMateria == null) return false;
        for (int i = 0; i < 3; i++) {
            if (!worldObj.isAirBlock(posHugeMateria.up(i))) {
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

    public void setFace(byte face) {
        this.face = face;
        EnumFacing direction = EnumFacing.getFront(face).getOpposite();
        posHugeMateria = new BlockPos(this.getPos().getX() + direction.getFrontOffsetX() * 2, this.getPos().getY(), this.getPos().getZ() + direction.getFrontOffsetZ() * 2);
    }

    public void setHugeMateria(BlockPos blockPos) {
        Block hugeMateria = ak.enchantchanger.utils.Blocks.blockHugeMateria;
        worldObj.setBlockState(blockPos, hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.PART, 0));
        worldObj.setBlockState(blockPos.up(), hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.PART, 1));
        worldObj.setBlockState(blockPos.up(2), hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.PART, 2));
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull EnumFacing side) {
        if (side == EnumFacing.DOWN) {
            return SLOTS_RESULT;
        }

        if (side == EnumFacing.UP) {
            return SLOTS_MATERIAL;
        }

        return SLOTS_FUEL;
    }

    @Override
    public boolean canInsertItem(int slot, @Nonnull ItemStack item, @Nonnull EnumFacing side) {
        return this.isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, @Nonnull ItemStack item, @Nonnull EnumFacing side) {
        return side != EnumFacing.UP || RANGE_FUEL_SLOTS.contains(slot) || item.getItem() instanceof ItemBucket;
    }

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < items.size()) {
            return items.get(slot);
        }
        return null;
    }

    @Override
    public ItemStack decrStackSize(int slot, int size) {
        if (slot < items.size() && items.get(slot) != null) {
            ItemStack returnStack;
            if (items.get(slot).stackSize <= size) {
                returnStack = items.get(slot);
                items.set(slot, null);
                return returnStack;
            } else {
                returnStack = items.get(slot).splitStack(size);
                if (items.get(slot).stackSize <= 0) {
                    items.set(size, null);
                }
                return returnStack;
            }
        }
        return null;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = getStackInSlot(slot);
        if (stack != null) {
            setInventorySlotContents(slot, null);
        }
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack item) {
        if (slot < items.size()) {
            items.set(slot, item);
            if (items.get(slot) != null && items.get(slot).stackSize > this.getInventoryStackLimit()) {
                items.get(slot).stackSize = this.getInventoryStackLimit();
            }
        }
    }

    @Override
    @Nonnull
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
    public boolean isUseableByPlayer(@Nonnull EntityPlayer player) {
        return this.getWorld().getTileEntity(this.getPos()) == this && player.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D)) <= 64;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {

    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {

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
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack item) {
        if (slot < items.size()) {
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
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentString(getName());
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return tank.getTankProperties();
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
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

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        if (resource == null) {
            return null;
        }
        if (resource.getFluid().equals(tank.getFluidType())) {
            return tank.drain(resource.amount, doDrain);
        }
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Optional.Method(modid = "CoFHCore")
    public int receiveEnergy(EnumFacing facing, int i, boolean b) {
        return 0;//発電のみ
    }

    @Optional.Method(modid = "CoFHCore")
    public int extractEnergy(EnumFacing facing, int i, boolean b) {
        int extract = Math.min(getStoredRFEnergy(), Math.min(getOutputMaxRFValue(), i));
        if (!b) {
            addRFEnergy(-extract);
            nowRF = extract;
        }
        return extract;
    }

    @Optional.Method(modid = "CoFHCore")
    public int getEnergyStored(EnumFacing facing) {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "CoFHCore")
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
    public int getInfoEnergyPerTick() {
        return nowRF;
    }

    @Optional.Method(modid = "CoFHCore")
    public int getInfoMaxEnergyPerTick() {
        return getOutputMaxRFValue();
    }

    @Optional.Method(modid = "CoFHCore")
    public int getInfoEnergyStored() {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "CoFHCore")
    public int getInfoMaxEnergyStored() {
        return MAX_RF_CAPACITY;
    }

    @Optional.Method(modid = "SextiarySector")
    public int addEnergy(EnumFacing from, int power, int speed, boolean simulate) {
        return 0;
    }

    @Optional.Method(modid = "SextiarySector")
    public int drawEnergy(EnumFacing from, int power, int speed, boolean simulate) {
        int extract = Math.min(getStoredRFEnergy(), Math.min(getOutputMaxRFValue(), speed));
        if (!simulate) {
            addRFEnergy(-extract);
            nowRF = extract;
        }
        return extract;
    }

    @Optional.Method(modid = "SextiarySector")
    public boolean canInterface(EnumFacing from) {
        return true;
    }

    @Optional.Method(modid = "SextiarySector")
    public int getPowerStored(EnumFacing from) {
        return 3;
    }

    @Optional.Method(modid = "SextiarySector")
    public long getSpeedStored(EnumFacing from) {
        return getStoredRFEnergy();
    }

    @Optional.Method(modid = "SextiarySector")
    public int getMaxPowerStored(EnumFacing from) {
        return 3;
    }

    @Optional.Method(modid = "SextiarySector")
    public long getMaxSpeedStored(EnumFacing from) {
        return MAX_RF_CAPACITY;
    }
}
