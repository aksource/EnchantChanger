package ak.EnchantChanger.modcoop;

import cofh.api.energy.IEnergyHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by A.K. on 14/10/02.
 */
public class CoopRFAPI {

    public static boolean isIEnergyHandler(TileEntity tileEntity) {
        return tileEntity instanceof IEnergyHandler;
    }

    public static int getNeedRF(TileEntity tileEntity, ForgeDirection direction, int maxRF) {
        if (tileEntity instanceof IEnergyHandler) {
            return ((IEnergyHandler)tileEntity).receiveEnergy(direction, maxRF, false);
        }
        return 0;
    }
}
