package ak.enchantchanger.api;

import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/11/05.
 */
@Deprecated
public class MaterialResultPair {
    private ItemStackWrapper material;
    private ItemStack result;

    public MaterialResultPair(ItemStack mat, ItemStack res) {
        this.material = ItemStackWrapper.getItemStackWrapper(mat);
        this.result = res;
    }

    public static MaterialResultPair getMaterialResultPair(ItemStack mat, ItemStack res) {
        return new MaterialResultPair(mat, res);
    }

    public ItemStackWrapper getMaterial() {
        return material;
    }

    public ItemStack getResultCopy() {
        return result.copy();
    }
}
