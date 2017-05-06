package ak.enchantchanger.network;

import ak.enchantchanger.api.Constants;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

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
    }
}
