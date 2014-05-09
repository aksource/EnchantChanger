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
    private NBTTagCompound data;
    public PlayerInfoPacket(){}
    public PlayerInfoPacket(EntityPlayer player) {
        this.data = new NBTTagCompound();
        ExtendedPlayerData.get(player).saveNBTData(data);
    }
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        ByteBufUtils.writeTag(buffer, data);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        data = ByteBufUtils.readTag(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        ExtendedPlayerData.get(player).loadNBTData(data);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        //NOOP
    }
}
