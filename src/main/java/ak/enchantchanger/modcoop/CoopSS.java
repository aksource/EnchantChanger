package ak.enchantchanger.modcoop;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

/**
 * Created by A.K. on 14/10/06.
 */
public class CoopSS {

    public static boolean isGFEnergyHandler(TileEntity tileEntity) {
        return false/*tileEntity instanceof IEnergyHandler*/;
    }

    public static int getNeedGF(TileEntity tileEntity, EnumFacing direction, int maxSpeed) {
        if (false/*tileEntity instanceof IEnergyHandler*/) {
            return 0/*((IEnergyHandler)tileEntity).addEnergy(direction, EcTileEntityMakoReactor.GF_POWER, maxSpeed, false)*/;
        }
        return 0;
    }
}
