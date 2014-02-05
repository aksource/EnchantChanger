package ak.MultiToolHolders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class KeyHandlingPacket extends AbstractPacket
{
	boolean openGui;
	int slotNum;
	
	public KeyHandlingPacket(){}
	
	public KeyHandlingPacket(boolean ver1, int ver2) {
		openGui = ver1;
		slotNum = ver2;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			buffer.writeBoolean(openGui);
			buffer.writeInt(slotNum);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			openGui = buffer.readBoolean();
			slotNum = buffer.readInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		ItemStack holdItem = player.getCurrentEquippedItem();
		if(holdItem != null && holdItem.getItem() instanceof ItemMultiToolHolder) {
			((ItemMultiToolHolder)holdItem.getItem()).SlotNum = this.slotNum;
			((ItemMultiToolHolder)holdItem.getItem()).openKeyToggle = this.openGui;
		}
		
	}
	
}