package ak.enchantchanger;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.block.EnumMRBaseType;
import ak.enchantchanger.recipe.EcRecipeMasterMateria;
import ak.enchantchanger.recipe.EcRecipeMateria;
import ak.enchantchanger.utils.ConfigurationUtils;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;
import java.util.Map;

import static ak.enchantchanger.utils.Blocks.blockEnchantChanger;
import static ak.enchantchanger.utils.Blocks.blockMakoReactor;
import static ak.enchantchanger.utils.Items.*;

/**
 * レシピ系登録クラス
 * Created by A.K. on 14/06/27.
 */
public class Recipes {

    public static void init() {
        Map<Enchantment, ItemStack> materiaMap = Maps.newHashMap();
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            ItemStack materiaItemStack = new ItemStack(itemMateria);
            materiaItemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
            materiaMap.put(enchantment, materiaItemStack);
        }
    }

    private static void addOreDictRecipe(IForgeRegistry<IRecipe> registry, EnumMRBaseType type) {
        ItemStack makoReactorController;
        List<ItemStack> ores = OreDictionary.getOres(type.getOreName());
        if (ores.isEmpty()) return;
        for (ItemStack itemStack : ores) {
            makoReactorController = new ItemStack(blockMakoReactor, 1, type.ordinal());
            registry.register(new ShapedOreRecipe(blockMakoReactor.getRegistryName(), makoReactorController,
                    "BBB",
                    "BMB",
                    "BBB",
                    'B', itemStack,
                    'M', new ItemStack(itemMateria, 1, 0))
                    .setRegistryName(blockMakoReactor.getRegistryName().getPath()
                            + itemStack.getItem().getRegistryName().getPath()));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        if (ConfigurationUtils.difficulty < 2)
            registry.register(new EcRecipeMateria().setRegistryName("materiaRecipe"));
        registry.register(new EcRecipeMasterMateria().setRegistryName("masterMateriaRecipe"));
        registry.register(new ShapedOreRecipe(itemZackSword.getRegistryName(), new ItemStack(itemZackSword, 1),
                " X ",
                "XX ",
                " Y ",
                'X', new ItemStack(Blocks.IRON_BLOCK),
                'Y', new ItemStack(Items.IRON_INGOT)).setRegistryName(itemZackSword.getRegistryName()));
        if (ConfigurationUtils.difficulty < 2) {
            registry.register(new ShapedOreRecipe(itemCloudSwordCore.getRegistryName(),
                    new ItemStack(itemCloudSwordCore, 1),
                    " X ",
                    "XYX",
                    " Z ",
                    'X', new ItemStack(Blocks.IRON_BLOCK),
                    'Y', new ItemStack(itemMateria, 1, 0),
                    'Z', new ItemStack(Items.IRON_INGOT)).setRegistryName(itemCloudSwordCore.getRegistryName()));
        } else {
            registry.register(new ShapedOreRecipe(itemCloudSwordCore.getRegistryName(),
                    new ItemStack(itemCloudSwordCore, 1),
                    " X ",
                    "DYD",
                    " Z ",
                    'X', new ItemStack(Blocks.IRON_BLOCK),
                    'Y', new ItemStack(itemMateria, 1, 0),
                    'Z', new ItemStack(Items.IRON_INGOT),
                    'D', new ItemStack(Items.DIAMOND)).setRegistryName(itemCloudSwordCore.getRegistryName()));
        }
        registry.register(new ShapedOreRecipe(itemSephirothSword.getRegistryName(),
                new ItemStack(itemSephirothSword, 1),
                "  A", " B ", "C  ",
                'A', new ItemStack(Items.IRON_INGOT),
                'B', new ItemStack(Items.DIAMOND_SWORD, 1, 0),
                'C', new ItemStack(itemMateria, 1, 1))
                .setRegistryName(itemSephirothSword.getRegistryName()));
        registry.register(new ShapedOreRecipe(itemUltimateWeapon.getRegistryName(),
                new ItemStack(itemUltimateWeapon, 1),
                " A ",
                "ABA",
                " C ",
                'A', new ItemStack(Blocks.DIAMOND_BLOCK),
                'B', new ItemStack(itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE),
                'C', new ItemStack(Items.STICK)).setRegistryName(itemUltimateWeapon.getRegistryName()));
        registry.register(new ShapedOreRecipe(itemImitateSephirothSword.getRegistryName(),
                new ItemStack(itemImitateSephirothSword),
                "  A",
                " A ",
                "B  ",
                'A', new ItemStack(Items.IRON_INGOT),
                'B', new ItemStack(Items.IRON_SWORD)).setRegistryName(itemImitateSephirothSword.getRegistryName()));
        registry.register(new ShapedOreRecipe(blockEnchantChanger.getRegistryName(),
                new ItemStack(blockEnchantChanger, 1),
                "XYX",
                "ZZZ",
                'X', new ItemStack(Items.DIAMOND),
                'Y', new ItemStack(Blocks.GOLD_BLOCK),
                'Z', new ItemStack(Blocks.OBSIDIAN)).setRegistryName(blockEnchantChanger.getRegistryName()));
        registry.register(new ShapedOreRecipe(itemHugeMateria.getRegistryName(), new ItemStack(itemHugeMateria),
                " A ",
                "ABA",
                " A ",
                'A', new ItemStack(Blocks.DIAMOND_BLOCK),
                'B', new ItemStack(Items.NETHER_STAR))
                .setRegistryName(itemHugeMateria.getRegistryName().getPath()
                        + Items.NETHER_STAR.getRegistryName().getPath()));
        registry.register(new ShapedOreRecipe(itemHugeMateria.getRegistryName(),
                new ItemStack(itemHugeMateria),
                " A ",
                "ABA",
                " A ",
                'A', new ItemStack(Blocks.DIAMOND_BLOCK),
                'B', new ItemStack(itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE))
                .setRegistryName(itemHugeMateria.getRegistryName().getPath()
                        + itemMasterMateria.getRegistryName().getPath()));
        registry.register(new ShapelessOreRecipe(itemPortableEnchantChanger.getRegistryName(),
                new ItemStack(itemPortableEnchantChanger, 1),
                new ItemStack(blockEnchantChanger))
                .setRegistryName(itemPortableEnchantChanger.getRegistryName()));
        registry.register(new ShapelessOreRecipe(blockEnchantChanger.getRegistryName(),
                new ItemStack(blockEnchantChanger, 1),
                new ItemStack(itemPortableEnchantChanger))
                .setRegistryName(blockEnchantChanger.getRegistryName() + "_portable"));
        registry.register(new ShapelessOreRecipe(itemPortableEnchantmentTable.getRegistryName(),
                new ItemStack(itemPortableEnchantmentTable, 1),
                new ItemStack(Blocks.ENCHANTING_TABLE))
                .setRegistryName(itemPortableEnchantmentTable.getRegistryName()));
        registry.register(new ShapelessOreRecipe(itemMasterMateria.getRegistryName(),
                new ItemStack(itemMasterMateria, 1, 0),
                new ItemStack(itemMasterMateria, 1, 1),
                new ItemStack(itemMasterMateria, 1, 2),
                new ItemStack(itemMasterMateria, 1, 3),
                new ItemStack(itemMasterMateria, 1, 4),
                new ItemStack(itemMasterMateria, 1, 5))
                .setRegistryName(itemMasterMateria.getRegistryName()));
        registry.register(new ShapedOreRecipe(itemExExpBottle.getRegistryName(),
                new ItemStack(itemExExpBottle, 8),
                "XXX", "XYX", "XXX",
                'X', new ItemStack(Items.EXPERIENCE_BOTTLE, 1, 0),
                'Y', new ItemStack(Blocks.DIAMOND_BLOCK, 1)).setRegistryName(itemExExpBottle.getRegistryName()));
        registry
                .register(new ShapedOreRecipe(Blocks.DRAGON_EGG.getRegistryName(),
                        new ItemStack(Blocks.DRAGON_EGG, 1),
                        "XXX",
                        "XYX",
                        "XXX",
                        'X', new ItemStack(Items.ENDER_EYE),
                        'Y', new ItemStack(itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE))
                        .setRegistryName(Constants.MOD_ID, Blocks.DRAGON_EGG.getRegistryName().getPath()));

        for (EnumMRBaseType type : EnumMRBaseType.values()) {
            addOreDictRecipe(registry, type);
        }
    }
}
