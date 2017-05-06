package ak.enchantchanger.utils;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.enchantment.*;
import ak.enchantchanger.entity.EcEntityApOrb;
import ak.enchantchanger.entity.EcEntityExExpBottle;
import ak.enchantchanger.entity.EcEntityMeteor;
import ak.enchantchanger.item.EcItemBlockMakoReactor;
import ak.enchantchanger.potion.EcPotionMako;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import ak.enchantchanger.tileentity.EcTileEntityMaterializer;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;

import static ak.enchantchanger.utils.ConfigurationUtils.*;

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
        GameRegistry.registerBlock(EnchantChanger.blockEnchantChanger, "EnchantChanger");
        GameRegistry.registerBlock(EnchantChanger.blockHugeMateria, "blockhugemateria");
        GameRegistry.registerBlock(EnchantChanger.blockMakoReactor, EcItemBlockMakoReactor.class, "blockmakoreactor");
        GameRegistry.registerBlock(EnchantChanger.blockLifeStream, "life_stream");
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("lifestream", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(EnchantChanger.itemBucketLifeStream), new ItemStack(Items.bucket));

        GameRegistry.registerItem(EnchantChanger.itemMateria, "materia");
        GameRegistry.registerItem(EnchantChanger.itemHugeMateria, "itemhugemateria");
        GameRegistry.registerItem(EnchantChanger.itemExExpBottle, "exexpbottle");
        GameRegistry.registerItem(EnchantChanger.itemZackSword, "zacksword");
        GameRegistry.registerItem(EnchantChanger.ItemCloudSwordCore, "cloudswordcore");
        GameRegistry.registerItem(EnchantChanger.itemCloudSword, "cloudsword");
        GameRegistry.registerItem(EnchantChanger.itemSephirothSword, "masamuneblade");
        GameRegistry.registerItem(EnchantChanger.itemUltimateWeapon, "ultimateweapon");
        GameRegistry.registerItem(EnchantChanger.itemPortableEnchantChanger,
                "portableenchantchanger");
        GameRegistry.registerItem(EnchantChanger.itemPortableEnchantmentTable,
                "portableenchantmenttable");
        GameRegistry.registerItem(EnchantChanger.itemMasterMateria, "mastermateria");
        GameRegistry.registerItem(EnchantChanger.itemImitateSephirothSword,
                "imitationmasamuneblade");
        GameRegistry.registerItem(EnchantChanger.itemBucketLifeStream, "bucket_lifestream");
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
                EnchantChanger.potionMako = new EcPotionMako(idMakoPoison).setPotionName("EC|MakoPoison");
            } else {
                throw new IllegalArgumentException("idMakoPoison:id has been used another MOD");
            }
        } else {
            throw new IllegalArgumentException("idMakoPoison:Only set from 24 to 127");
        }
    }
}
