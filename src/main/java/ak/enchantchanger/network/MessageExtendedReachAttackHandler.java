package ak.enchantchanger.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 14/08/20.
 */
public class MessageExtendedReachAttackHandler implements IMessageHandler<MessageExtendedReachAttack, IMessage> {
    @Override
    public IMessage onMessage(MessageExtendedReachAttack message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        Entity targetEntity = message.getEntityFromId(player.world);
        if (targetEntity != null && !targetEntity.isDead) {
            player.attackTargetEntityWithCurrentItem(targetEntity);
        }
        return null;
    }
}
