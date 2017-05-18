package ak.enchantchanger.capability;

import ak.enchantchanger.api.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * PlayerStatusHandlerのキャパビリティーハンドラークラス
 * Created by A.K. on 2017/04/18.
 */
public class CapabilityPlayerStatusHandler {
    public static final ResourceLocation PLAYER_STATUS = new ResourceLocation(Constants.MOD_ID, Constants.CAP_KEY_PLAYER_STATUS);
    @CapabilityInject(IPlayerStatusHandler.class)
    public static Capability<IPlayerStatusHandler> CAPABILITY_PLAYER_STATUS = null;
    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerStatusHandler.class, new Capability.IStorage<IPlayerStatusHandler>() {
            @Nullable
            @Override
            public NBTBase writeNBT(Capability<IPlayerStatusHandler> capability, IPlayerStatusHandler instance, EnumFacing side) {
                if (capability == CAPABILITY_PLAYER_STATUS) {
                    NBTTagCompound nbt = new NBTTagCompound();
                    nbt.setBoolean(Constants.NBT_IS_LEVITATING, instance.isLevitating());
                    nbt.setBoolean(Constants.NBT_SOLDIER_MODE, instance.getSoldierMode());
                    nbt.setLong(Constants.NBT_AP_COOLING_TIME, instance.getApCoolingTime());
                    nbt.setInteger(Constants.NBT_LIMIT_VALUE, instance.getLimitGaugeValue());
                    nbt.setLong(Constants.NBT_SOLDIER_WORK_START_TIME, instance.getSoldierWorkStartTime());
                    nbt.setInteger(Constants.NBT_MOB_KILL_COUNT, instance.getMobKillCount());
                    nbt.setInteger(Constants.NBT_LIMIT_BREAK_COUNT, instance.getLimitBreakCount());
                    nbt.setByte(Constants.NBT_LIMIT_BREAK_ID, instance.getLimitBreakId());
                    nbt.setBoolean(Constants.NBT_GG_MODE, instance.isGgMode());
                    return nbt;
                }
                return null;
            }

            @Override
            public void readNBT(Capability<IPlayerStatusHandler> capability, IPlayerStatusHandler instance, EnumFacing side, NBTBase nbt) {
                if (capability == CAPABILITY_PLAYER_STATUS) {
                    NBTTagCompound nbtTagCompound = (NBTTagCompound) nbt;
                    instance.setLevitating(nbtTagCompound.getBoolean(Constants.NBT_IS_LEVITATING));
                    instance.setSoldierMode(nbtTagCompound.getBoolean(Constants.NBT_SOLDIER_MODE));
                    instance.setApCoolingTime(nbtTagCompound.getLong(Constants.NBT_AP_COOLING_TIME));
                    instance.setLimitGaugeValue(nbtTagCompound.getInteger(Constants.NBT_LIMIT_VALUE));
                    instance.setSoldierWorkStartTime(nbtTagCompound.getLong(Constants.NBT_SOLDIER_WORK_START_TIME));
                    instance.setMobKillCount(nbtTagCompound.getInteger(Constants.NBT_MOB_KILL_COUNT));
                    instance.setLimitBreakCount(nbtTagCompound.getInteger(Constants.NBT_LIMIT_BREAK_COUNT));
                    instance.setLimitBreakId(nbtTagCompound.getByte(Constants.NBT_LIMIT_BREAK_ID));
                    instance.setGgMode(nbtTagCompound.getBoolean(Constants.NBT_GG_MODE));
                }
            }
        }, PlayerStatusHandlerImpl::new);
    }

    public static IPlayerStatusHandler getPlayerStatusHandler(EntityPlayer player) {
        return player.getCapability(CAPABILITY_PLAYER_STATUS, null);
    }
}
