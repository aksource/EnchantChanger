package ak.EnchantChanger.network;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessagePlayerPropertiesHandler implements IMessageHandler<MessagePlayerProperties, IMessage>{
    @Override
    public IMessage onMessage(MessagePlayerProperties message, MessageContext ctx) {
        ExtendedPlayerData.get(EnchantChanger.proxy.getPlayer()).loadNBTData(message.data);
        return null;
    }
}
