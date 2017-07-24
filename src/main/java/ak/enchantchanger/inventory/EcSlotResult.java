package ak.enchantchanger.inventory;

import net.minecraft.inventory.InventoryBasic;

class EcSlotResult extends InventoryBasic {
    final EcContainerMaterializer container;

    public EcSlotResult(EcContainerMaterializer par1ContainerMaterializer, String par2Str, int par3) {
        super(par2Str, true, par3);
        this.container = par1ContainerMaterializer;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}