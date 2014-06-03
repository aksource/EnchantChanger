package ak.EnchantChanger.network;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.item.EcItemSword;
import ak.MultiToolHolders.ItemMultiToolHolder;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * Created by A.K. on 14/06/02.
 */
public class MessageKeyPressed implements IMessage, IMessageHandler<MessageKeyPressed, IMessage> {

    private byte keyIndex;

    public MessageKeyPressed(){}

    public MessageKeyPressed(byte key) {
        this.keyIndex = key;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        this.keyIndex = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeByte(this.keyIndex);
    }

    @Override
    public IMessage onMessage(MessageKeyPressed message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        if (player != null && player.getCurrentEquippedItem() != null && message.keyIndex != -1) {
            switch(message.keyIndex) {
                case EnchantChanger.MagicKEY :doMagic(player.getCurrentEquippedItem(), player); break;
                case EnchantChanger.MateriaKEY:openMateriaWindow(player);break;
            }
        }
        return null;
    }

    private void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.worldObj, player);
        } else if (itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
            if (mth.tools.getStackInSlot(mth.SlotNum) != null
                    && mth.tools.getStackInSlot(mth.SlotNum).getItem() instanceof EcItemSword)
            {
                EcItemSword.doMagic(mth.tools.getStackInSlot(mth.SlotNum), player.worldObj, player);
            }
        }
    }

    private void openMateriaWindow(EntityPlayer player) {
        if (canOpenMateriaWindow(player)) {
            player.openGui(EnchantChanger.instance, EnchantChanger.guiIdMateriaWindow, player.worldObj, MathHelper.ceiling_double_int(player.posX), MathHelper.ceiling_double_int(player.posY), MathHelper.ceiling_double_int(player.posZ));
        }
    }

    private boolean canOpenMateriaWindow(EntityPlayer player) {
        return ExtendedPlayerData.get(player).getSoldierMode();
    }
}
