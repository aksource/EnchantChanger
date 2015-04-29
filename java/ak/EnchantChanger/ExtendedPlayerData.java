package ak.EnchantChanger;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.network.MessagePlayerProperties;
import ak.EnchantChanger.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * Created by A.K. on 14/04/23.
 */
public class ExtendedPlayerData implements IExtendedEntityProperties {

    public final static String EXT_PROP_NAME = "EC:ExtPlayerData";
//    private final EntityPlayer player;

    private boolean isLevitating;
    private boolean soldierMode;
    private long apCoolingTime;
    private int limitValue;
    private long soldierWorkStartTime;
    private int mobKillCount;
    private int limitBreakCount;
    private byte limitBreakId;
    private boolean ggMode;

//    public ExtendedPlayerData(EntityPlayer player) {
//        this.player = player;
//        this.isLevitating = false;
//        this.soldierMode = false;
//        this.apCoolingTime = 0;
//    }

    private static String getSaveKey(EntityPlayer player) {
        return player.getGameProfile().getId().toString() + ":" + EXT_PROP_NAME;
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new ExtendedPlayerData());
    }

    public static ExtendedPlayerData get(EntityPlayer player) {
        return (ExtendedPlayerData) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("isLevitating", this.isLevitating);
        nbt.setBoolean("soldierMode", this.soldierMode);
        nbt.setLong("apCoolingTime", this.apCoolingTime);
        nbt.setInteger("limitValue", this.limitValue);
        nbt.setLong("soldierWorkStartTime", this.soldierWorkStartTime);
        nbt.setInteger("mobKillCount", this.mobKillCount);
        nbt.setInteger("limitBreakCount", this.limitBreakCount);
        nbt.setByte("limitBreakId", this.limitBreakId);
        nbt.setBoolean("ggMode", this.ggMode);
        compound.setTag(EXT_PROP_NAME, nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
        this.isLevitating = nbt.getBoolean("isLevitating");
        this.soldierMode = nbt.getBoolean("soldierMode");
        this.apCoolingTime = nbt.getLong("apCoolingTime");
        this.limitValue = nbt.getInteger("limitValue");
        this.soldierWorkStartTime = nbt.getLong("soldierWorkStartTime");
        this.mobKillCount = nbt.getInteger("mobKillCount");
        this.limitBreakCount = nbt.getInteger("limitBreakCount");
        this.limitBreakId = nbt.getByte("limitBreakId");
        this.ggMode = nbt.getBoolean("ggMode");
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public long getApCoolingTime() {
        return this.apCoolingTime;
    }

    public void setApCoolingTime(long time) {
        this.apCoolingTime = time;
    }

    public boolean getSoldierMode() {
        return this.soldierMode;
    }

    public void setSoldierMode(boolean mode) {
        this.soldierMode = mode;
    }

    public boolean isLevitating() {
        return isLevitating;
    }

    public void setLevitating(boolean isLevitating) {
        this.isLevitating = isLevitating;
    }

    public void addLimitGaugeValue(int value) {
        limitValue = MathHelper.clamp_int(limitValue + value, 0, Constants.LIMIT_GAUGE_MAX);
    }

    public int getLimitGaugeValue() {
        return limitValue;
    }

    public void setLimitGaugeValue(int value) {
        limitValue = MathHelper.clamp_int(value, 0, Constants.LIMIT_GAUGE_MAX);
    }

    public boolean canLimitBreak() {
        return this.limitValue == Constants.LIMIT_GAUGE_MAX;
    }

    public long getSoldierWorkStartTime() {
        return soldierWorkStartTime;
    }

    public void setSoldierWorkStartTime(long soldierWorkStartTime) {
        this.soldierWorkStartTime = soldierWorkStartTime;
    }

    public int getMobKillCount() {
        return mobKillCount;
    }

    public void setMobKillCount(int mobKillCount) {
        this.mobKillCount = mobKillCount;
    }

    public int getLimitBreakCount() {
        return limitBreakCount;
    }

    public void setLimitBreakCount(int limitBreakCount) {
        this.limitBreakCount = limitBreakCount;
    }

    public void decreaseLimitBreakCount() {
        this.limitBreakCount--;
    }

    public boolean isLimitBreaking() {
        return this.limitBreakCount > 0;
    }

    public byte getLimitBreakId() {
        return limitBreakId;
    }

    public void setLimitBreakId(byte limitBreakId) {
        this.limitBreakId = limitBreakId;
    }

    public boolean isGgMode() {
        return ggMode;
    }

    public void setGgMode(boolean ggMode) {
        this.ggMode = ggMode;
    }

    public void saveProxyData(EntityPlayer player) {
        //NO-OP yet
    }

    public void loadProxyData(EntityPlayerMP player) {
        ExtendedPlayerData playerData = ExtendedPlayerData.get(player);
        NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
        if (savedData != null) {
            playerData.loadNBTData(savedData);
        }
        PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), player);
    }
}
