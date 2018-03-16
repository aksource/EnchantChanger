package ak.enchantchanger.network;

import ak.enchantchanger.utils.ConfigurationUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 2018/03/13.
 */
public class MessageCallingSyncConfigStoCHandler implements IMessageHandler<MessageCallingSyncConfigStoC, MessageSyncConfigStoC> {
    @Override
    public MessageSyncConfigStoC onMessage(MessageCallingSyncConfigStoC message, MessageContext ctx) {
        return new MessageSyncConfigStoC(!ConfigurationUtils.disableFloating);
    }
}
