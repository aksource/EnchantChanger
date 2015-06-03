package ak.EnchantChanger.inventory;

import net.minecraft.inventory.InventoryBasic;

public class EcSlotPortableEnchantmentTable extends InventoryBasic {
    /**
     * The brewing stand this slot belongs to.
     */
    final EcContainerPortableEnchantment container;

    public EcSlotPortableEnchantmentTable(EcContainerPortableEnchantment par1ContainerEnchantment, String par2Str, int par3) {
        super(par2Str, true, par3);
        this.container = par1ContainerEnchantment;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.container.onCraftMatrixChanged(this);
    }
}
