package ak.enchantchanger.eventhandler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * 追加液体バケツイベントクラス
 * Created by A.K. on 14/03/06.
 */
public class FillBucketHook {

    public static final FillBucketHook INSTANCE = new FillBucketHook();
    public static final Map<Block, Item> buckets = new HashMap<>();

    private FillBucketHook() {
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onBucketFill(FillBucketEvent event) {

        ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget());

        if (result == null)
            return;

        event.setFilledBucket(result);
        event.setResult(Event.Result.ALLOW);
    }

    private ItemStack fillCustomBucket(World world, RayTraceResult pos) {

        Block block = world.getBlockState(pos.getBlockPos()).getBlock();

        Item bucket = buckets.get(block);
        if (bucket != null && world.getBlockState(pos.getBlockPos()).getValue(BlockFluidBase.LEVEL) == 0) {
            world.setBlockToAir(pos.getBlockPos());
            return new ItemStack(bucket);
        } else
            return null;

    }
}
