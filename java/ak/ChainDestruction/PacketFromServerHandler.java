package ak.ChainDestruction;

import ak.ChainDestruction.InteractBlockHook.CDClientMsg;
import ak.ChainDestruction.InteractBlockHook.CDServerMsg;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketFromServerHandler implements IMessageHandler<CDServerMsg, CDClientMsg>
{

	@Override
	public CDClientMsg onMessage(CDServerMsg message, MessageContext ctx) {
		return null;
	}
	
}