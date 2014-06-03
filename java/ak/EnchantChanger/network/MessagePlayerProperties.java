package ak.EnchantChanger.network;

import ak.EnchantChanger.ExtendedPlayerData;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessagePlayerProperties implements IMessage, IMessageHandler<MessagePlayerProperties, IMessage> {

    private NBTTagCompound data;

    public MessagePlayerProperties(){}

    public MessagePlayerProperties(EntityPlayer entityPlayer) {
        this.data = new NBTTagCompound();
        ExtendedPlayerData.get(entityPlayer).saveNBTData(data);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, data);
    }

    @Override
    public IMessage onMessage(MessagePlayerProperties message, MessageContext ctx) {
        ExtendedPlayerData.get(Minecraft.getMinecraft().thePlayer).loadNBTData(message.data);
        return null;
    }
}
