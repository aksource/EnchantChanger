package ak.EnchantChanger;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/03/06.
 */
public class EcItemBucketLifeStream extends ItemBucket implements IFuelHandler{
    public EcItemBucketLifeStream(Block block) {
        super(block);
    }

    @Override
    public int getBurnTime(ItemStack fuel) {
        return 20 * 1000 * 10;
    }
}
