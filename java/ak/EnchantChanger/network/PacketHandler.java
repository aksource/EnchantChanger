package ak.EnchantChanger.network;

import ak.EnchantChanger.api.Constants;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by A.K. on 14/06/02.
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MOD_ID.toLowerCase());

    public static void init() {
        INSTANCE.registerMessage(MessageKeyPressedHandler.class, MessageKeyPressed.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageLevitationHandler.class, MessageLevitation.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessagePlayerPropertiesHandler.class, MessagePlayerProperties.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(MessageCloudSwordHandler.class, MessageCloudSword.class, 3, Side.CLIENT);
        INSTANCE.registerMessage(MessageExtendedReachAttackHandler.class, MessageExtendedReachAttack.class, 4, Side.SERVER);
        INSTANCE.registerMessage(MessageRFSteppingHandler.class, MessageRFStepping.class, 5, Side.SERVER);
        INSTANCE.registerMessage(MessagePlayerJoinInAnnouncementHandler.class, MessagePlayerJoinInAnnouncement.class, 6, Side.SERVER);
    }
}
