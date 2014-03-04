package ak.EnchantChanger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by A.K. on 14/02/27.
 */
public class KeyMateriaWindowPacket extends AbstractPacket{
    boolean openWindow;

    public KeyMateriaWindowPacket() {}

    public KeyMateriaWindowPacket(boolean ver1) {
        openWindow = ver1;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeBoolean(openWindow);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        openWindow = buffer.readBoolean();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {
        EnchantChanger.livingeventhooks.isMateriaKeyPressed = openWindow;
    }
}
