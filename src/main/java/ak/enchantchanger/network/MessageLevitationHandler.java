package ak.enchantchanger.network;

import ak.enchantchanger.EnchantChanger;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessageLevitationHandler implements IMessageHandler<MessageLevitation, IMessage> {
    @Override
    public IMessage onMessage(MessageLevitation message, MessageContext ctx) {
        if (ctx.getServerHandler().player != null) {
            EnchantChanger.livingeventhooks.setLevitationModeToNBT(ctx.getServerHandler().player, message.isLevitation);
        }
        return null;
    }
}
