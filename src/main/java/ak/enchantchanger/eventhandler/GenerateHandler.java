package ak.enchantchanger.eventhandler;

import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.Items;
import com.google.common.collect.Lists;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetMetadata;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Random;

import static ak.enchantchanger.utils.Blocks.blockLifeStream;
import static ak.enchantchanger.EnchantChanger.logger;

/**
 * 地形構造物生成系処理ハンドラークラス
 * Created by A.K. on 14/10/12.
 */
public class GenerateHandler {

    //ライフストリームの地底湖を生成するつもりのコード
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void populateLifeStreamLake(PopulateChunkEvent.Populate event) {
        Random rand = event.getRand();
        if (rand.nextInt(ConfigurationUtils.lifeStreamLakeRatio) == 0) {
            int k = event.getChunkX() * 16;
            int l = event.getChunkZ() * 16;
            int x, y, z;
            x = k + rand.nextInt(16) + 8;
            y = rand.nextInt(16);
            z = l + rand.nextInt(16) + 8;
            (new WorldGenLakes(blockLifeStream)).generate(event.getWorld(), rand, new BlockPos(x, y, z));
            logger.info(String.format("LifeStreamLake is generated at (%d, %d, %d)", x, y, z));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLootTableLoading(LootTableLoadEvent event) {
        event.getTable().addPool(
                new LootPool(
                        makeMagicMateriaEntry(),
                        new LootCondition[]{},
                        new RandomValueRange(1, 1),
                        new RandomValueRange(0, 0),
                        "magic_materia_pool"
                )
        );
    }

    private LootEntry[] makeMagicMateriaEntry() {
        List<LootEntry> entryList = Lists.newArrayList();
        for (int i = 0; i < EcItemMateria.MagicMateriaNum; i++) {
            entryList.add(new LootEntryItem(
                    Items.itemMateria,
                    1,
                    1,
                    new LootFunction[]{
                            new SetMetadata(new LootCondition[]{},
                                    new RandomValueRange(1, 1))
                    },
                    new LootCondition[]{},
                    "magic_materia"
            ));
        }
        return entryList.toArray(new LootEntry[]{});
    }
}
