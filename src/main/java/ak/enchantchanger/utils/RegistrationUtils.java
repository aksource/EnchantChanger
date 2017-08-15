package ak.enchantchanger.utils;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.entity.EcEntityApOrb;
import ak.enchantchanger.entity.EcEntityExExpBottle;
import ak.enchantchanger.entity.EcEntityMeteor;
import ak.enchantchanger.potion.EcPotionMako;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import ak.enchantchanger.tileentity.EcTileEntityMaterializer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

import static ak.enchantchanger.EnchantChanger.makoPotionType;
import static ak.enchantchanger.EnchantChanger.potionMako;
import static ak.enchantchanger.utils.Blocks.*;
import static ak.enchantchanger.utils.Items.*;
/**
 * システム登録周りのクラス
 * Created by A.K. on 14/10/12.
 */
public class RegistrationUtils {

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(itemMateria);
        registry.register(itemHugeMateria);
        registry.register(itemExExpBottle);
        registry.register(itemZackSword);
        registry.register(itemCloudSwordCore);
        registry.register(itemCloudSword);
        registry.register(itemSephirothSword);
        registry.register(itemUltimateWeapon);
        registry.register(itemPortableEnchantChanger);
        registry.register(itemPortableEnchantmentTable);
        registry.register(itemMasterMateria);
        registry.register(itemImitateSephirothSword);

        registry.register(new ItemBlock(blockEnchantChanger).setRegistryName(blockEnchantChanger.getRegistryName()));
        registry.register(itemBlockMakoReactor);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.register(blockEnchantChanger);
        registry.register(blockHugeMateria);
        registry.register(blockMakoReactor);
        registry.register(blockLifeStream);
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerPotion(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        registry.register(potionMako);
    }
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerPotionType(RegistryEvent.Register<PotionType> event) {
        IForgeRegistry<PotionType> registry = event.getRegistry();
        registry.register(makoPotionType);
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
        makoPotionType = new PotionType(EcPotionMako.NAME, new PotionEffect(potionMako)).setRegistryName(EcPotionMako.NAME);
    }
}
