package ak.EnchantChanger.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessageLevitation implements IMessage {

    public boolean isLevitation;

    public MessageLevitation(){}

    public MessageLevitation(boolean par1) {
        this.isLevitation = par1;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.isLevitation = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.isLevitation);
    }
}
