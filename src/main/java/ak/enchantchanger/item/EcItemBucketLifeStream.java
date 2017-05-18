package ak.enchantchanger.item;

import ak.enchantchanger.api.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * ライフストリーム入りバケツクラス
 * Created by A.K. on 14/03/06.
 */
public class EcItemBucketLifeStream extends ItemBucket {
    public EcItemBucketLifeStream(Block block, String name) {
        super(block);
        String s = String.format("%s%s", Constants.EcTextureDomain, name);
        this.setUnlocalizedName(s);
    }

    @Override
    public boolean hasContainerItem(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getContainerItem(@Nonnull ItemStack itemStack) {
        return new ItemStack(Items.BUCKET);
    }
}
