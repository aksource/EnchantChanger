package ak.ChainDestruction;
import ak.ChainDestruction.InteractBlockHook.CDClientMsg;
import ak.ChainDestruction.InteractBlockHook.CDServerMsg;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketFromClientHandler implements IMessageHandler<CDClientMsg, CDServerMsg>
{
	@Override
	public CDServerMsg onMessage(CDClientMsg message, MessageContext ctx) {
		ChainDestruction.instance.interactblockhook.toggle = message.toggle1;
		ChainDestruction.instance.interactblockhook.digUnderToggle = message.toggle2;
		return null;
	}
}