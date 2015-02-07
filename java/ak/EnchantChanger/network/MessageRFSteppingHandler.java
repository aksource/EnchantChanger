package ak.EnchantChanger.network;

import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by A.K. on 14/10/01.
 */
public class MessageRFSteppingHandler implements IMessageHandler<MessageRFStepping, IMessage> {
    @Override
    public IMessage onMessage(MessageRFStepping message, MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        TileEntity tile = world.getTileEntity(message.xPos, message.yPos, message.zPos);
        if (tile != null && tile instanceof EcTileEntityMakoReactor) {
            ((EcTileEntityMakoReactor)tile).setOutputMaxRFValue(message.outputRFValue);
        }
        return null;
    }
}
