package ak.EnchantChanger.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessageCloudSword implements IMessage{

    public byte slotNum;

    public MessageCloudSword(){}

    public MessageCloudSword(byte par1) {
        this.slotNum = par1;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotNum = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.slotNum);
    }
}
