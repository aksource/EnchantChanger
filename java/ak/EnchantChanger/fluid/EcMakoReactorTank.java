package ak.EnchantChanger.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

/**
 * Created by A.K. on 14/09/23.
 */
public class EcMakoReactorTank extends FluidTank {
    public EcMakoReactorTank(int cap) {
        super(cap);
    }

    //判定処理の短縮用のemptyフラグ
    public boolean isEmpty() {
        return (getFluid() == null) || (getFluid().amount <= 0);
    }

    //判定処理の短縮用の満タンフラグ
    public boolean isFull() {
        return (getFluid() != null) && (getFluid().amount == getCapacity());
    }

    //Fluid型で中身を得る
    public Fluid getFluidType() {
        return getFluid() != null ? getFluid().getFluid() : null;
    }
}
