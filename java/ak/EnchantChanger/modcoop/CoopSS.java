package ak.EnchantChanger.modcoop;

import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import shift.sextiarysector.api.machine.energy.IEnergyHandler;

/**
 * Created by A.K. on 14/10/06.
 */
public class CoopSS {

    public static boolean isGFEnergyHandler(TileEntity tileEntity) {
        return tileEntity instanceof IEnergyHandler;
    }

    public static int getNeedGF(TileEntity tileEntity, ForgeDirection direction, int maxSpeed) {
        if (tileEntity instanceof IEnergyHandler) {
            return ((IEnergyHandler)tileEntity).addEnergy(direction, EcTileEntityMakoReactor.GF_POWER, maxSpeed, false);
        }
        return 0;
    }
}
