package ak.EnchantChanger.utils;

import ak.EnchantChanger.EnchantChanger;
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
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static ak.EnchantChanger.utils.ConfigurationUtils.*;
import static ak.EnchantChanger.EnchantChanger.*;
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
        GameRegistry.registerBlock(blockEnchantChanger, "EnchantChanger");
        GameRegistry.registerBlock(blockHugeMateria, "blockhugemateria");
        GameRegistry.registerBlock(blockMakoReactor, EcItemBlockMakoReactor.class, "blockmakoreactor");
        GameRegistry.registerBlock(blockLifeStream, "life_stream");
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("lifestream", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(itemBucketLifeStream), new ItemStack(Items.bucket));
        GameRegistry.registerItem(itemBucketLifeStream, "bucket_lifestream");

        GameRegistry.registerItem(itemMateria, "materia");
        GameRegistry.registerItem(itemHugeMateria, "itemhugemateria");
        GameRegistry.registerItem(itemExExpBottle, "exexpbottle");
        GameRegistry.registerItem(itemZackSword, "zacksword");
        GameRegistry.registerItem(ItemCloudSwordCore, "cloudswordcore");
        GameRegistry.registerItem(itemCloudSword, "cloudsword");
        GameRegistry.registerItem(itemSephirothSword, "masamuneblade");
        GameRegistry.registerItem(itemUltimateWeapon, "ultimateweapon");
        GameRegistry.registerItem(itemPortableEnchantChanger,
                "portableenchantchanger");
        GameRegistry.registerItem(itemPortableEnchantmentTable,
                "portableenchantmenttable");
        GameRegistry.registerItem(itemMasterMateria, "mastermateria");
        GameRegistry.registerItem(itemImitateSephirothSword,
                "imitationmasamuneblade");
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
        if (idMakoPoison < Potion.potionTypes.length) {
            if (Potion.potionTypes[idMakoPoison] == null) {
                potionMako = new EcPotionMako(idMakoPoison).setPotionName("EC|MakoPoison");
            } else {
                throw new IllegalArgumentException("idMakoPoison:id has been used another MOD");
            }
        } else {
            throw new IllegalArgumentException("idMakoPoison:Only set from 24 to 127");
        }
    }
}
