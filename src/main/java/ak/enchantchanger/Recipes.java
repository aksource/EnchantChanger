package ak.enchantchanger;

import ak.enchantchanger.block.EnumMRBaseType;
import ak.enchantchanger.recipe.EcRecipeMasterMateria;
import ak.enchantchanger.recipe.EcRecipeMateria;
import ak.enchantchanger.utils.ConfigurationUtils;
import com.google.common.collect.Maps;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.List;
import java.util.Map;

/**
 * レシピ系登録クラス
 * Created by A.K. on 14/06/27.
 */
public class Recipes {

    public static void init() {
        Map<Enchantment, ItemStack> materiaMap = Maps.newHashMap();
        for (Enchantment enchantment : Enchantment.REGISTRY) {
            ItemStack materiaItemStack = new ItemStack(ak.enchantchanger.utils.Items.itemMateria);
            materiaItemStack.addEnchantment(enchantment, enchantment.getMaxLevel());
            materiaMap.put(enchantment, materiaItemStack);
        }
    }

    public static void registerRecipes() {
        RecipeSorter.register("enchantchanger:MateriaRecipe", EcRecipeMateria.class, RecipeSorter.Category.SHAPELESS, "after:FML");
        RecipeSorter.register("enchantchanger:MasterMateriaRecipe", EcRecipeMasterMateria.class, RecipeSorter.Category.SHAPELESS, "after:FML");
        if (ConfigurationUtils.difficulty < 2)
            GameRegistry.addRecipe(new EcRecipeMateria());
        GameRegistry.addRecipe(new EcRecipeMasterMateria());
        GameRegistry.addRecipe(new ItemStack(ak.enchantchanger.utils.Items.itemZackSword, 1),
                " X",
                "XX",
                " Y",
                'X', Blocks.IRON_BLOCK,
                'Y', Items.IRON_INGOT);
        if (ConfigurationUtils.difficulty < 2) {
            GameRegistry.addRecipe(
                    new ItemStack(ak.enchantchanger.utils.Items.itemCloudSwordCore, 1),
                    " X ",
                    "XYX",
                    " Z ",
                    'X', Blocks.IRON_BLOCK,
                    'Y', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 0),
                    'Z', Items.IRON_INGOT);
        } else {
            GameRegistry.addRecipe(
                    new ItemStack(ak.enchantchanger.utils.Items.itemCloudSwordCore, 1),
                    " X ",
                    "DYD",
                    " Z ",
                    'X', Blocks.IRON_BLOCK,
                    'Y', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 0),
                    'Z', Items.IRON_INGOT,
                    'D', Items.DIAMOND);
        }
        GameRegistry.addRecipe(
                new ItemStack(ak.enchantchanger.utils.Items.itemSephirothSword, 1),
                "  A", " B ", "C  ",
                'A', Items.IRON_INGOT,
                'B', new ItemStack(Items.DIAMOND_SWORD, 1, 0),
                'C', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 1));
        GameRegistry.addRecipe(
                new ItemStack(ak.enchantchanger.utils.Items.itemUltimateWeapon, 1),
                " A ",
                "ABA",
                " C ",
                'A', Blocks.DIAMOND_BLOCK,
                'B', new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE),
                'C', Items.STICK);
        GameRegistry.addRecipe(new ItemStack(ak.enchantchanger.utils.Items.itemImitateSephirothSword),
                "  A",
                " A ",
                "B  ",
                'A', Items.IRON_INGOT,
                'B', Items.IRON_SWORD);
        GameRegistry.addRecipe(new ItemStack(ak.enchantchanger.utils.Blocks.blockEnchantChanger, 1),
                "XYX",
                "ZZZ",
                'X', Items.DIAMOND,
                'Y', Blocks.GOLD_BLOCK,
                'Z', Blocks.OBSIDIAN);
        GameRegistry.addRecipe(new ItemStack(ak.enchantchanger.utils.Items.itemHugeMateria),
                " A ",
                "ABA",
                " A ",
                'A', Blocks.DIAMOND_BLOCK,
                'B', Items.NETHER_STAR);
        GameRegistry
                .addRecipe(new ItemStack(ak.enchantchanger.utils.Items.itemHugeMateria),
                        " A ",
                        "ABA",
                        " A ",
                        'A', Blocks.DIAMOND_BLOCK,
                        'B', new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(
                ak.enchantchanger.utils.Items.itemPortableEnchantChanger, 1), ak.enchantchanger.utils.Blocks.blockEnchantChanger);
        GameRegistry.addShapelessRecipe(new ItemStack(
                ak.enchantchanger.utils.Blocks.blockEnchantChanger, 1), ak.enchantchanger.utils.Items.itemPortableEnchantChanger);
        GameRegistry.addShapelessRecipe(new ItemStack(
                        ak.enchantchanger.utils.Items.itemPortableEnchantmentTable, 1),
                Blocks.ENCHANTING_TABLE);
        GameRegistry.addShapelessRecipe(new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 0),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 1),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 2),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 3),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 4),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 5));
        GameRegistry.addRecipe(new ItemStack(ak.enchantchanger.utils.Items.itemExExpBottle, 8),
                "XXX", "XYX", "XXX",
                'X', new ItemStack(Items.EXPERIENCE_BOTTLE, 1, 0),
                'Y', new ItemStack(Blocks.DIAMOND_BLOCK, 1));
        GameRegistry
                .addRecipe(new ItemStack(Blocks.DRAGON_EGG, 1),
                        "XXX",
                        "XYX",
                        "XXX",
                        'X', Items.ENDER_EYE,
                        'Y', new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE));

        for (EnumMRBaseType type : EnumMRBaseType.values()) {
            addOreDictRecipe(type);
        }
    }


    private static void addOreDictRecipe(EnumMRBaseType type) {
        ItemStack makoReactorController;
        List<ItemStack> ores = OreDictionary.getOres(type.getOreName());
        if (ores.isEmpty()) return;
        for (ItemStack itemStack : ores) {
            makoReactorController = new ItemStack(ak.enchantchanger.utils.Blocks.blockMakoReactor, 1, type.ordinal());
            GameRegistry.addRecipe(makoReactorController,
                    "BBB",
                    "BMB",
                    "BBB",
                    'B', itemStack,
                    'M', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 0));
        }
    }
}
