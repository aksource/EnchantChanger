package ak.enchantchanger.network;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessagePlayerPropertiesHandler implements IMessageHandler<MessagePlayerProperties, IMessage> {
    @Override
    public IMessage onMessage(MessagePlayerProperties message, MessageContext ctx) {
//        CapabilityPlayerStatusHandler.getPlayerStatusHandler(EnchantChanger.proxy.getPlayer()).loadNBTData(message.data);
        return null;
    }
}
