package ak.ChainDestruction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class KeyHandlingPacket extends AbstractPacket
{
	boolean toggle;
	boolean digUnderToggle;
	
	public KeyHandlingPacket(){}
	
	public KeyHandlingPacket(boolean ver1, boolean ver2) {
		toggle = ver1;
		digUnderToggle = ver2;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			buffer.writeBoolean(toggle);
			buffer.writeBoolean(digUnderToggle);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			toggle = buffer.readBoolean();
			digUnderToggle = buffer.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		ChainDestruction.interactblockhook.toggle = toggle;
		ChainDestruction.interactblockhook.digUnderToggle = digUnderToggle;
		
	}
	
}