package ak.enchantchanger.inventory;

import ak.enchantchanger.item.EcItemMateria;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class EcSlotItemToEnchant extends Slot {
    final EcContainerMaterializer container;
    private final IInventory materializeResult;
    private final IInventory materializeSource;

    public EcSlotItemToEnchant(EcContainerMaterializer par1ContainerMaterializer, IInventory par2IInventory, IInventory par3IInventory, int par4, int par5, int par6) {
        super(par3IInventory, par4, par5, par6);
        this.container = par1ContainerMaterializer;
        this.materializeResult = par2IInventory;
        this.materializeSource = par3IInventory;
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return !(itemStack.getItem() instanceof EcItemMateria);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    @Nonnull
    public ItemStack onTake(@Nonnull EntityPlayer thePlayer, @Nonnull ItemStack stack) {
        ItemStack taken = super.onTake(thePlayer, stack);
        for (int i = 0; i < EcContainerMaterializer.RESULT_SLOT_NUM; i++) {
            this.materializeResult.setInventorySlotContents(i, ItemStack.EMPTY);
        }
        return taken;
    }
}