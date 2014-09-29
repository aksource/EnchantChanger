package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/03/06.
 */
public class EcItemBucketLifeStream extends ItemBucket {
    public EcItemBucketLifeStream(Block block, String name) {
        super(block);
        String s = String.format("%s%s", EnchantChanger.EcTextureDomain, name);
        this.setUnlocalizedName(s);
        this.setTextureName(s);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        return new ItemStack(Items.bucket);
    }
}
