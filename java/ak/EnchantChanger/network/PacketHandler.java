package ak.EnchantChanger.network;

import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Created by A.K. on 14/06/02.
 */
public class PacketHandler {

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(EnchantChanger.MOD_ID.toLowerCase());

    public static void init() {
        INSTANCE.registerMessage(MessageKeyPressed.class, MessageKeyPressed.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageLevitation.class, MessageLevitation.class, 1, Side.SERVER);
        INSTANCE.registerMessage(MessagePlayerProperties.class, MessagePlayerProperties.class, 2, Side.CLIENT);
        INSTANCE.registerMessage(MessageCloudSword.class, MessageCloudSword.class, 3, Side.CLIENT);
    }
}
