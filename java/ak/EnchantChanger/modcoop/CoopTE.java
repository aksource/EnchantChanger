package ak.EnchantChanger.modcoop;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by A.K. on 14/10/02.
 */
public class CoopTE {

    public static boolean isIEnergyHandler(TileEntity tileEntity) {
        return false/*tileEntity instanceof IEnergyHandler*/;
    }

    public static int getNeedRF(TileEntity tileEntity, EnumFacing direction, int maxRF) {
        if (false/*tileEntity instanceof IEnergyHandler*/) {
            return 0/*((IEnergyHandler)tileEntity).receiveEnergy(direction, maxRF, false)*/;
        }
        return 0;
    }
}
