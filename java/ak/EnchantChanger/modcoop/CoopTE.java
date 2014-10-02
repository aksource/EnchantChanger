package ak.EnchantChanger.modcoop;

import cofh.api.energy.IEnergyConnection;
import cofh.api.tileentity.IEnergyInfo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by A.K. on 14/10/02.
 */
public class CoopTE {

    public static void addTETab(GuiContainer guiContainer, TileEntity tileEntity) {
        if (tileEntity instanceof IEnergyInfo) {

        }
    }

    public static void renderTETab(GuiContainer guiContainer, TileEntity tileEntity) {
        if (tileEntity instanceof IEnergyInfo) {

        }
    }

    public static boolean isIEnergyConnection(TileEntity tileEntity) {
        return tileEntity instanceof IEnergyConnection;
    }
}
