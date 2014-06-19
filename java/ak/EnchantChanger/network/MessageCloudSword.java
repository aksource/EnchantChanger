package ak.EnchantChanger.network;

import ak.EnchantChanger.item.EcItemCloudSword;
import ak.MultiToolHolders.ItemMultiToolHolder;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessageCloudSword implements IMessage, IMessageHandler<MessageCloudSword, IMessage>{

    private byte slotNum;

    public MessageCloudSword(){}

    public MessageCloudSword(byte par1) {
        this.slotNum = par1;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotNum = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.slotNum);
    }

    @Override
    public IMessage onMessage(MessageCloudSword message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null) {
            if (itemStack.getItem() instanceof EcItemCloudSword) {
                EcItemCloudSword sword = (EcItemCloudSword) itemStack.getItem();
                sword.setSlotNum(itemStack, message.slotNum);
            } else if (itemStack.getItem() instanceof ItemMultiToolHolder) {
                ItemMultiToolHolder mth = (ItemMultiToolHolder) itemStack.getItem();
                if (mth.getInventoryFromItemStack(itemStack).getStackInSlot(mth.getSlotNumFromItemStack(itemStack)) != null && mth.getInventoryFromItemStack(itemStack).getStackInSlot(mth.getSlotNumFromItemStack(itemStack)).getItem() instanceof EcItemCloudSword) {
                    EcItemCloudSword sword = (EcItemCloudSword) mth.getInventoryFromItemStack(itemStack).getStackInSlot(mth.getSlotNumFromItemStack(itemStack)).getItem();
                    sword.setSlotNum(itemStack, message.slotNum);
                }
            }
        }
        return null;
    }
}
