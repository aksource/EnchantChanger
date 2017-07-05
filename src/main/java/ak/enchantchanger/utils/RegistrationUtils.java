package ak.enchantchanger.utils;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.entity.EcEntityApOrb;
import ak.enchantchanger.entity.EcEntityExExpBottle;
import ak.enchantchanger.entity.EcEntityMeteor;
import ak.enchantchanger.potion.EcPotionMako;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import ak.enchantchanger.tileentity.EcTileEntityMaterializer;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ak.enchantchanger.EnchantChanger.potionMako;
import static ak.enchantchanger.utils.Blocks.*;
import static ak.enchantchanger.utils.Items.*;
/**
 * システムに登録周りのクラス
 * Created by A.K. on 14/10/12.
 */
public class RegistrationUtils {

    public static void registerBlockAndItem() {
        GameRegistry.register(blockEnchantChanger);
        GameRegistry.register(new ItemBlock(blockEnchantChanger).setRegistryName(blockEnchantChanger.getRegistryName()));
        GameRegistry.register(blockHugeMateria);
        GameRegistry.register(blockMakoReactor);
        GameRegistry.register(itemBlockMakoReactor);
        GameRegistry.register(blockLifeStream);
//        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(fluidLifeStream.getName(), FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketLifeStream), new ItemStack(bucket));
        GameRegistry.register(itemBucketLifeStream);

        GameRegistry.register(itemMateria);
        GameRegistry.register(itemHugeMateria);
        GameRegistry.register(itemExExpBottle);
        GameRegistry.register(itemZackSword);
        GameRegistry.register(itemCloudSwordCore);
        GameRegistry.register(itemCloudSword);
        GameRegistry.register(itemSephirothSword);
        GameRegistry.register(itemUltimateWeapon);
        GameRegistry.register(itemPortableEnchantChanger);
        GameRegistry.register(itemPortableEnchantmentTable);
        GameRegistry.register(itemMasterMateria);
        GameRegistry.register(itemImitateSephirothSword);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(EcTileEntityMaterializer.class,
                "container.materializer");
        GameRegistry.registerTileEntity(EcTileEntityHugeMateria.class,
                "container.hugeMateria");
        GameRegistry.registerTileEntity(EcTileEntityMakoReactor.class, "container.makoReactor");
    }

    public static void registerEntities(Object mod) {
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.MOD_ID, "itemExExpBottle"),
                EcEntityExExpBottle.class,
                "itemExExpBottle", 0, mod, 250, 5, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.MOD_ID, "enchantmentMeteor"),
                EcEntityMeteor.class, "enchantmentMeteor", 1, mod,
                250, 5, true);
        EntityRegistry.registerModEntity(new ResourceLocation(Constants.MOD_ID, "apOrb"),
                EcEntityApOrb.class, "apOrb", 2, mod,
                64, 1, false);
    }

    public static void addStatusEffect() {
        potionMako = new EcPotionMako().setPotionName("EC|MakoPoison");
    }
}
