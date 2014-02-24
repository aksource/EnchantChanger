package ak.EnchantChanger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ak.MultiToolHolders.ItemMultiToolHolder;

public class CloudSwordPacket extends AbstractPacket {
    int slotNum;

    public CloudSwordPacket() {
    }

    public CloudSwordPacket(int var1) {
        slotNum = var1;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(slotNum);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        slotNum = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {
        if (player.inventory.getCurrentItem() == null)
            return;
        if (player.inventory.getCurrentItem().getItem() instanceof EcItemCloudSword) {
            EcItemCloudSword sword = (EcItemCloudSword) player.getCurrentEquippedItem().getItem();
            sword.readSlotNumData(slotNum, player.getCurrentEquippedItem());
        } else if (EnchantChanger.loadMTH && player.inventory.getCurrentItem().getItem() instanceof ItemMultiToolHolder) {
            ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
            if (mth.tools.getStackInSlot(mth.SlotNum) != null && mth.tools.getStackInSlot(mth.SlotNum).getItem() instanceof EcItemCloudSword) {
                EcItemCloudSword sword = (EcItemCloudSword) mth.tools.getStackInSlot(mth.SlotNum).getItem();
                sword.readSlotNumData(slotNum, mth.tools.getStackInSlot(mth.SlotNum));
            }
        }

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

}