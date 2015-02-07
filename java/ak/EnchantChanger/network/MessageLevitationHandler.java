package ak.EnchantChanger.network;

import ak.EnchantChanger.eventhandler.LivingEventHooks;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessageLevitationHandler implements IMessageHandler<MessageLevitation, IMessage> {
    @Override
    public IMessage onMessage(MessageLevitation message, MessageContext ctx) {
        if (ctx.getServerHandler().playerEntity != null) {
            LivingEventHooks.setLevitationModeToNBT(ctx.getServerHandler().playerEntity, message.isLevitation);
        }
        return null;
    }
}
