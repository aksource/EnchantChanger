package ak.EnchantChanger.eventhandler;

import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.utils.ConfigurationUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

import static ak.EnchantChanger.EnchantChanger.*;

/**
 * Created by A.K. on 14/10/12.
 */
public class GenerateHandler {
    public static void DungeonLootItemResist() {
        WeightedRandomChestContent materiaInChest;
        for (int i = 0; i < 8; i++) {
            materiaInChest = ((EcItemMateria) itemMateria).addMateriaInChest(i, 1,
                    1, 1);
            ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,
                    materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, materiaInChest);
            ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, materiaInChest);
        }
    }

    //ライフストリームの地底湖を生成するつもりのコード
    @SubscribeEvent
    public void populateLifeStreamLake(PopulateChunkEvent.Populate event) {
        if (event.rand.nextInt(ConfigurationUtils.lifeStreamLakeRatio) == 0) {
            int k = event.chunkX * 16;
            int l = event.chunkZ * 16;
            int x, y, z;
            x = k + event.rand.nextInt(16) + 8;
            y = event.rand.nextInt(16);
            z = l + event.rand.nextInt(16) + 8;
            (new WorldGenLakes(blockLifeStream)).generate(event.world, event.rand, x, y, z);
            logger.info(String.format("LifeStreamLake is generated at (%d, %d, %d)", x, y, z));
        }
    }
}
