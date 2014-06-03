package ak.EnchantChanger.network;

import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessageLevitation implements IMessage, IMessageHandler<MessageLevitation, IMessage>{

    private boolean isLevitation;

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

    @Override
    public IMessage onMessage(MessageLevitation message, MessageContext ctx) {
        if (ctx.getServerHandler().playerEntity != null) {
            EnchantChanger.livingeventhooks.setModeToNBT(ctx.getServerHandler().playerEntity, message.isLevitation);
        }
        return null;
    }
}
