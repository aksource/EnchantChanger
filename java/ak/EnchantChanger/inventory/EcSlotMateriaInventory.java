package ak.EnchantChanger.inventory;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;

/**
 * Created by A.K..
 */
public class EcSlotMateriaInventory extends InventoryBasic {
    private Container ownerContainer;
    public EcSlotMateriaInventory(EcContainerMateriaWindow container, String invName, int invSlotNum) {
        super(invName, true,invSlotNum);
        this.ownerContainer = container;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.ownerContainer.onCraftMatrixChanged(this);
    }
}
