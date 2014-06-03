package ak.EnchantChanger.inventory;

import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.EnchantChanger;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K.
 */
public class EcSlotMateriaWindow extends Slot {
    private ItemStack openItem;
//    private IInventory thisInventory;
    public EcSlotMateriaWindow(ItemStack item, IInventory inv, int slot, int x, int y) {
        super(inv, slot, x, y);
        this.openItem = item;
//        this.thisInventory = inv;
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack) {
        return par1ItemStack.getItem() instanceof EcItemMateria && checkEnchantmentValid(par1ItemStack) && EnchantChanger.checkLvCap(par1ItemStack);
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    private boolean checkEnchantmentValid(ItemStack itemStack) {
        Enchantment enchantment = EnchantChanger.enchKind(itemStack);
        return EnchantChanger.isEnchantmentValid(enchantment, openItem);
    }
}
