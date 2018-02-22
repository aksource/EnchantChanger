package ak.enchantchanger.network;

import ak.enchantchanger.EnchantChanger;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by A.K. on 2018/02/15.
 */
public class MessageBackItemHandler implements IMessageHandler<MessageBackItem, IMessage> {
    @Override
    public IMessage onMessage(MessageBackItem message, MessageContext ctx) {
        String uuid = message.uuid;
        ItemStack itemStack = message.itemStack;
        EnchantChanger.proxy.addBackItemToRendererMap(uuid, itemStack);
        return null;
    }
}
