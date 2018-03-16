package ak.enchantchanger.network;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 2018/03/03.
 */
public class MessageSyncConfigStoCHandler implements IMessageHandler<MessageSyncConfigStoC, IMessage> {
    @Override
    public IMessage onMessage(MessageSyncConfigStoC message, MessageContext ctx) {
        EntityPlayer player = EnchantChanger.proxy.getPlayer();
        if (player != null) {
            CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).setEnableLevitation(message.enableLevitation);
        }
        return null;
    }
}
