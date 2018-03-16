package ak.enchantchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by A.K. on 2018/03/13.
 */
public class MessageCallingSyncConfigStoC implements IMessage {
    public MessageCallingSyncConfigStoC(){}
    @Override
    public void fromBytes(ByteBuf buf) {
        // NOOP
    }

    @Override
    public void toBytes(ByteBuf buf) {
        // NOOP
    }
}
