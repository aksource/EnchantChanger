package ak.enchantchanger.inventory;

import net.minecraft.inventory.InventoryBasic;

class EcSlotResult extends InventoryBasic {
    final EcContainerMaterializer container;
//	private ItemStack[] ResultContents;

    public EcSlotResult(EcContainerMaterializer par1ContainerMaterializer, String par2Str, int par3) {
        super(par2Str, true, par3);
        this.container = par1ContainerMaterializer;
//		this.ResultContents = new ItemStack[EcContainerMaterializer.RESULT_SLOT_NUM];
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }
}