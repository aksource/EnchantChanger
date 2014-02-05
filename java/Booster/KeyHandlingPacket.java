package Booster;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public class KeyHandlingPacket extends AbstractPacket
{
	boolean boosterKey;
	
	public KeyHandlingPacket(){}
	
	public KeyHandlingPacket(boolean ver1) {
		boosterKey = ver1;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			buffer.writeBoolean(boosterKey);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		try {
			boosterKey = buffer.readBoolean();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		Booster.livingeventhooks.boosterSwitch = this.boosterKey;
		
	}
	
}