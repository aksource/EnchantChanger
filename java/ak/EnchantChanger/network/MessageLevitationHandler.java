package ak.EnchantChanger.network;

import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by A.K. on 14/07/31.
 */
public class MessageLevitationHandler implements IMessageHandler<MessageLevitation, IMessage> {
    @Override
    public IMessage onMessage(MessageLevitation message, MessageContext ctx) {
        if (ctx.getServerHandler().playerEntity != null) {
            EnchantChanger.livingeventhooks.setModeToNBT(ctx.getServerHandler().playerEntity, message.isLevitation);
        }
        return null;
    }
}
