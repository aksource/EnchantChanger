package ak.EnchantChanger;

import ak.EnchantChanger.network.MessagePlayerProperties;
import ak.EnchantChanger.network.PacketHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
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

//    public ExtendedPlayerData(EntityPlayer player) {
//        this.player = player;
//        this.isLevitating = false;
//        this.soldierMode = false;
//        this.apCoolingTime = 0;
//    }

    private static String getSaveKey(EntityPlayer player) {
        return player.getCommandSenderName() + ":" + EXT_PROP_NAME;
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new ExtendedPlayerData());
    }

    public static ExtendedPlayerData get(EntityPlayer player) {
        return (ExtendedPlayerData)player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setBoolean("isLevitating", this.isLevitating);
        nbt.setBoolean("soldierMode", this.soldierMode);
        nbt.setLong("apCoolingTime", this.apCoolingTime);
        compound.setTag(EXT_PROP_NAME, nbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound nbt = (NBTTagCompound)compound.getTag(EXT_PROP_NAME);
        this.isLevitating = nbt.getBoolean("isLevitating");
        this.soldierMode = nbt.getBoolean("soldierMode");
        this.apCoolingTime = nbt.getLong("apCoolingTime");
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

    public void saveProxyData(EntityPlayer player) {
        //NO-OP yet
    }

    public void loadProxyData(EntityPlayer player) {
        ExtendedPlayerData playerData = ExtendedPlayerData.get(player);
        NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
        if (savedData != null) { playerData.loadNBTData(savedData); }
        PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), (EntityPlayerMP)player);
    }
}
