package ak.EnchantChanger.api;

import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/10/12.
 */
public class ItemStackWrapper {
    private ItemStack containItemStack;
    public ItemStackWrapper(ItemStack itemStack) {
        containItemStack = itemStack;
    }

    @Override
    public int hashCode() {
        final int prime = 71;
        int result = 1;
        result = prime * result + containItemStack.getItem().hashCode();
        result = prime * result + containItemStack.getItemDamage();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        ItemStackWrapper otherItemStackWrapper = (ItemStackWrapper)obj;
        return otherItemStackWrapper.getContainItemStack().isItemEqual(otherItemStackWrapper.getContainItemStack());
    }

    @Override
    public String toString() {
        return containItemStack.toString();
    }

    public ItemStack getContainItemStack() {
        return containItemStack;
    }

    public ItemStack getContainItemStackCopy() {
        return containItemStack.copy();
    }

    public static ItemStackWrapper getItemStackWrappaer(ItemStack itemStack) {
        return new ItemStackWrapper(itemStack);
    }
}
