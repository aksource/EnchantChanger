package ak.enchantchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * Created by A.K. on 2018/02/15.
 */
public class MessageBackItem implements IMessage {
    public String uuid;
    public ItemStack itemStack;
    public MessageBackItem(){}

    public MessageBackItem(String uuid, ItemStack itemStack) {
        this.uuid = uuid;
        this.itemStack = itemStack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuid = ByteBufUtils.readUTF8String(buf);
        this.itemStack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuid);
        ByteBufUtils.writeItemStack(buf, this.itemStack);
    }
}
