package ak.EnchantChanger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ak.MultiToolHolders.ItemMultiToolHolder;

public class KeyHandlingPacket extends AbstractPacket
{
	boolean doMagic;
	
	public KeyHandlingPacket(){}
	
	public KeyHandlingPacket(boolean ver1) {
		doMagic = ver1;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			buffer.writeBoolean(doMagic);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			doMagic = buffer.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		if(player.inventory.getCurrentItem() == null)
			return;
		if(player.inventory.getCurrentItem().getItem() instanceof EcItemSword)
		{
			EcItemSword sword = (EcItemSword) player.getCurrentEquippedItem().getItem();
			sword.readPacketToggleData(this.doMagic);
		}
		else if(EnchantChanger.loadMTH  && player.inventory.getCurrentItem().getItem() instanceof ItemMultiToolHolder)
		{
			ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
			if(mth.tools.getStackInSlot(mth.SlotNum) != null && mth.tools.getStackInSlot(mth.SlotNum).getItem() instanceof EcItemSword)
			{
				EcItemSword sword = (EcItemSword) mth.tools.getStackInSlot(mth.SlotNum).getItem();
				sword.readPacketToggleData(this.doMagic);
			}
		}
	}
	
}