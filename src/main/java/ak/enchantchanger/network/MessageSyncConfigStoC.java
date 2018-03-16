package ak.enchantchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by A.K. on 2018/03/03.
 */
public class MessageSyncConfigStoC implements IMessage{
    boolean enableLevitation;
    public MessageSyncConfigStoC(){}

    public MessageSyncConfigStoC(boolean enableLevitation) {
        this.enableLevitation = enableLevitation;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.enableLevitation = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.enableLevitation);
    }
}
