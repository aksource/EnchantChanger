package ak.EnchantChanger.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessageKeyPressed implements IMessage {

    public byte keyIndex;

    public MessageKeyPressed(){}

    public MessageKeyPressed(byte key) {
        this.keyIndex = key;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.keyIndex = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.keyIndex);
    }
}
