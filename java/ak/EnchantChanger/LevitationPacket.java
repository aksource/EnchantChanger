package ak.EnchantChanger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class LevitationPacket extends AbstractPacket
{
	boolean isLevitate;
	public LevitationPacket(){}
	public LevitationPacket(boolean var1) {
		this.isLevitate = var1;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			buffer.writeBoolean(isLevitate);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			isLevitate = buffer.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		EnchantChanger.livingeventhooks.readPacketData(isLevitate, player);
		
	}
	
}