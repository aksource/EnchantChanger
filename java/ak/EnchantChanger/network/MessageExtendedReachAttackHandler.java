package ak.EnchantChanger.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by A.K. on 14/08/20.
 */
public class MessageExtendedReachAttackHandler implements IMessageHandler<MessageExtendedReachAttack, IMessage> {
    @Override
    public IMessage onMessage(MessageExtendedReachAttack message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        Entity targetEntity = message.getEntityFromId(player.worldObj);
        player.attackTargetEntityWithCurrentItem(targetEntity);
        return null;
    }
}
