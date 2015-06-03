package ak.EnchantChanger.eventhandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by A.K. on 14/03/06.
 */
public class FillBucketHook {

    public static final FillBucketHook INSTANCE = new FillBucketHook();
    public static final Map<Block, Item> buckets = new HashMap<>();

    private FillBucketHook() {
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {

        ItemStack result = fillCustomBucket(event.world, event.target);

        if (result == null)
            return;

        event.result = result;
        event.setResult(Event.Result.ALLOW);
    }

    private ItemStack fillCustomBucket(World world, MovingObjectPosition pos) {

        Block block = world.getBlockState(pos.func_178782_a()/*getBlockPoa*/).getBlock();

        Item bucket = buckets.get(block);
        if (bucket != null && (Integer) world.getBlockState(pos.func_178782_a()).getValue(BlockFluidBase.LEVEL) == 0) {
            world.setBlockToAir(pos.func_178782_a());
            return new ItemStack(bucket);
        } else
            return null;

    }
}
