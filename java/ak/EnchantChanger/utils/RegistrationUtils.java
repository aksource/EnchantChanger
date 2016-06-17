package ak.EnchantChanger.utils;

import ak.EnchantChanger.enchantment.*;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteor;
import ak.EnchantChanger.item.EcItemBlockMakoReactor;
import ak.EnchantChanger.potion.EcPotionMako;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import ak.EnchantChanger.tileentity.EcTileEntityMaterializer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ak.EnchantChanger.EnchantChanger.*;
import static ak.EnchantChanger.utils.ConfigurationUtils.*;
/**
 * Created by A.K. on 14/10/12.
 */
public class RegistrationUtils {

    public static void registerEnchantments() {
        enchantmentMeteor = new EcEnchantmentMeteo(idEnchantmentMeteor, 0).setName("Meteor");
        enchantmentHoly = new EcEnchantmentHoly(idEnchantmentHoly, 0).setName("Holy");
        enchantmentTelepo = new EcEnchantmentTeleport(idEnchantmentTelepo, 0).setName("Teleporting");
        enchantmentFloat = new EcEnchantmentFloat(idEnchantmentFloat, 0).setName("Floating");
        enchantmentThunder = new EcEnchantmentThunder(idEnchantmentThunder, 0).setName("Thunder");
    }

    public static void registerBlockAndItem() {
        GameRegistry.registerBlock(blockEnchantChanger);
        GameRegistry.registerBlock(blockHugeMateria);
        GameRegistry.registerBlock(blockMakoReactor, EcItemBlockMakoReactor.class);
        GameRegistry.registerBlock(blockLifeStream);
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack(fluidLifeStream.getName(), FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketLifeStream), new ItemStack(Items.bucket));
        GameRegistry.registerItem(itemBucketLifeStream);

        GameRegistry.registerItem(itemMateria);
        GameRegistry.registerItem(itemHugeMateria);
        GameRegistry.registerItem(itemExExpBottle);
        GameRegistry.registerItem(itemZackSword);
        GameRegistry.registerItem(itemCloudSwordCore);
        GameRegistry.registerItem(itemCloudSword);
        GameRegistry.registerItem(itemSephirothSword);
        GameRegistry.registerItem(itemUltimateWeapon);
        GameRegistry.registerItem(itemPortableEnchantChanger);
        GameRegistry.registerItem(itemPortableEnchantmentTable);
        GameRegistry.registerItem(itemMasterMateria);
        GameRegistry.registerItem(itemImitateSephirothSword);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(EcTileEntityMaterializer.class,
                "container.materializer");
        GameRegistry.registerTileEntity(EcTileEntityHugeMateria.class,
                "container.hugeMateria");
        GameRegistry.registerTileEntity(EcTileEntityMakoReactor.class, "container.makoReactor");
    }

    public static void registerEntities(Object mod) {
        EntityRegistry.registerModEntity(EcEntityExExpBottle.class,
                "itemExExpBottle", 0, mod, 250, 5, true);
        EntityRegistry.registerModEntity(EcEntityMeteor.class, "enchantmentMeteor", 1, mod,
                250, 5, true);
        EntityRegistry.registerModEntity(EcEntityApOrb.class, "apOrb", 2, mod,
                64, 1, false);
    }

    public static void addStatusEffect() {
        potionMako = new EcPotionMako().setPotionName("EC|MakoPoison");
    }
}
