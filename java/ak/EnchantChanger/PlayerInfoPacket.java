package ak.EnchantChanger;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by A.K. on 14/03/10.
 */
public class PlayerInfoPacket extends AbstractPacket {
    private boolean canOpenMateriaWindow;
    public PlayerInfoPacket(){}
    public PlayerInfoPacket(NBTTagCompound nbtTagCompound) {
        this.canOpenMateriaWindow = nbtTagCompound.getBoolean("EC|soldier");
    }
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeBoolean(this.canOpenMateriaWindow);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        this.canOpenMateriaWindow = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        NBTTagCompound nbt = player.getEntityData();
        nbt.setBoolean("EC|soldier", this.canOpenMateriaWindow);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        //NOOP
    }
}
