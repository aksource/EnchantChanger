package ak.enchantchanger.capability;

import ak.enchantchanger.api.Constants;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * {@see IPlayerStatusHandler}の実装クラス
 * Created by A.K. on 2017/04/18.
 */
public class PlayerStatusHandlerImpl implements IPlayerStatusHandler, ICapabilitySerializable<NBTTagCompound> {
    private boolean isLevitating;
    private boolean soldierMode;
    private long apCoolingTime;
    private int limitValue;
    private long soldierWorkStartTime;
    private int mobKillCount;
    private int limitBreakCount;
    private byte limitBreakId;
    private boolean ggMode;
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityPlayerStatusHandler.CAPABILITY_PLAYER_STATUS;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return hasCapability(capability, facing) ? CapabilityPlayerStatusHandler.CAPABILITY_PLAYER_STATUS.cast(this): null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean(Constants.NBT_IS_LEVITATING, this.isLevitating);
        nbt.setBoolean(Constants.NBT_SOLDIER_MODE, this.soldierMode);
        nbt.setLong(Constants.NBT_AP_COOLING_TIME, this.apCoolingTime);
        nbt.setInteger(Constants.NBT_LIMIT_VALUE, this.limitValue);
        nbt.setLong(Constants.NBT_SOLDIER_WORK_START_TIME, this.soldierWorkStartTime);
        nbt.setInteger(Constants.NBT_MOB_KILL_COUNT, this.mobKillCount);
        nbt.setInteger(Constants.NBT_LIMIT_BREAK_COUNT, this.limitBreakCount);
        nbt.setByte(Constants.NBT_LIMIT_BREAK_ID, this.limitBreakId);
        nbt.setBoolean(Constants.NBT_GG_MODE, this.ggMode);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.isLevitating = nbt.getBoolean(Constants.NBT_IS_LEVITATING);
        this.soldierMode = nbt.getBoolean(Constants.NBT_SOLDIER_MODE);
        this.apCoolingTime = nbt.getLong(Constants.NBT_AP_COOLING_TIME);
        this.limitValue = nbt.getInteger(Constants.NBT_LIMIT_VALUE);
        this.soldierWorkStartTime = nbt.getLong(Constants.NBT_SOLDIER_WORK_START_TIME);
        this.mobKillCount = nbt.getInteger(Constants.NBT_MOB_KILL_COUNT);
        this.limitBreakCount = nbt.getInteger(Constants.NBT_LIMIT_BREAK_COUNT);
        this.limitBreakId = nbt.getByte(Constants.NBT_LIMIT_BREAK_ID);
        this.ggMode = nbt.getBoolean(Constants.NBT_GG_MODE);
    }

    @Override
    public long getApCoolingTime() {
        return this.apCoolingTime;
    }

    @Override
    public void setApCoolingTime(long time) {
        this.apCoolingTime = time;
    }

    @Override
    public boolean getSoldierMode() {
        return this.soldierMode;
    }

    @Override
    public void setSoldierMode(boolean mode) {
        this.soldierMode = mode;
    }

    @Override
    public boolean isLevitating() {
        return this.isLevitating;
    }

    @Override
    public void setLevitating(boolean isLevitating) {
        this.isLevitating = isLevitating;
    }

    @Override
    public void addLimitGaugeValue(int value) {
        this.limitValue += value;
    }

    @Override
    public int getLimitGaugeValue() {
        return this.limitValue;
    }

    @Override
    public void setLimitGaugeValue(int value) {
        this.limitValue = MathHelper.clamp_int(value, Constants.LIMIT_GAUGE_MIN, Constants.LIMIT_GAUGE_MAX);
    }

    @Override
    public boolean canLimitBreak() {
        return this.limitValue == Constants.LIMIT_GAUGE_MAX;
    }

    @Override
    public long getSoldierWorkStartTime() {
        return this.soldierWorkStartTime;
    }

    @Override
    public void setSoldierWorkStartTime(long soldierWorkStartTime) {
        this.soldierWorkStartTime = soldierWorkStartTime;
    }

    @Override
    public int getMobKillCount() {
        return this.mobKillCount;
    }

    @Override
    public void setMobKillCount(int mobKillCount) {
        this.mobKillCount = mobKillCount;
    }

    @Override
    public int getLimitBreakCount() {
        return this.limitBreakCount;
    }

    @Override
    public void setLimitBreakCount(int limitBreakCount) {
        this.limitBreakCount = limitBreakCount;
    }

    @Override
    public void decreaseLimitBreakCount() {
        this.limitBreakCount--;
    }

    @Override
    public boolean isLimitBreaking() {
        return this.limitBreakCount > 0;
    }

    @Override
    public byte getLimitBreakId() {
        return this.limitBreakId;
    }

    @Override
    public void setLimitBreakId(byte limitBreakId) {
        this.limitBreakId = limitBreakId;
    }

    @Override
    public boolean isGgMode() {
        return this.ggMode;
    }

    @Override
    public void setGgMode(boolean ggMode) {
        this.ggMode = ggMode;
    }
}
