package Booster;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class BoosterCloudPacket extends AbstractPacket
{
	boolean spawnCloud;
	public BoosterCloudPacket(){}
	
	public BoosterCloudPacket(boolean ver1) {
		spawnCloud = ver1;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			buffer.writeBoolean(spawnCloud);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			spawnCloud = buffer.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Booster.livingeventhooks.spawnCloud = this.spawnCloud;
		
	}
	
}