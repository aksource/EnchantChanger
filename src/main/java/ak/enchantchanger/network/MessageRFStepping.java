package ak.enchantchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by A.K. on 14/10/01.
 */
public class MessageRFStepping implements IMessage {

    public int outputRFValue;
    public int xPos;
    public int yPos;
    public int zPos;

    public MessageRFStepping() {
    }

    public MessageRFStepping(int outputRFValue, int x, int y, int z) {
        this.outputRFValue = outputRFValue;
        this.xPos = x;
        this.yPos = y;
        this.zPos = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.outputRFValue = buf.readInt();
        this.xPos = buf.readInt();
        this.yPos = buf.readInt();
        this.zPos = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.outputRFValue);
        buf.writeInt(this.xPos);
        buf.writeInt(this.yPos);
        buf.writeInt(this.zPos);
    }
}
