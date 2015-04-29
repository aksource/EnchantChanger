package ak.EnchantChanger;

import ak.EnchantChanger.block.EcBlockMakoReactor;
import ak.EnchantChanger.recipe.EcRecipeMasterMateria;
import ak.EnchantChanger.recipe.EcRecipeMateria;
import ak.EnchantChanger.utils.ConfigurationUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;

import java.util.ArrayList;

import static ak.EnchantChanger.EnchantChanger.*;

/**
 * Created by A.K. on 14/06/27.
 */
public class Recipes {

    public static void init() {
        ItemStack[] materiaArray = new ItemStack[256];
        for (Enchantment enchantment : Enchantment.enchantmentsList) {
            if (enchantment != null) {
                materiaArray[enchantment.effectId] = new ItemStack(EnchantChanger.itemMateria);
                materiaArray[enchantment.effectId].addEnchantment(enchantment, enchantment.getMaxLevel());
            }
        }
    }

    public static void registerRecipes() {
        RecipeSorter.register("EnchantChanger:MateriaRecipe", EcRecipeMateria.class, RecipeSorter.Category.SHAPELESS, "after:FML");
        RecipeSorter.register("EnchantChanger:MasterMateriaRecipe", EcRecipeMasterMateria.class, RecipeSorter.Category.SHAPELESS, "after:FML");
        if (ConfigurationUtils.difficulty < 2)
            GameRegistry.addRecipe(new EcRecipeMateria());
        GameRegistry.addRecipe(new EcRecipeMasterMateria());
//        GameRegistry.addShapelessRecipe(new ItemStack(itemMateria, 1, 0),
//                new ItemStack(Items.diamond, 1),
//                new ItemStack(Items.ender_pearl, 1));
        GameRegistry.addRecipe(new ItemStack(itemZackSword, 1),
                " X",
                "XX",
                " Y",
                'X', Blocks.iron_block,
                'Y', Items.iron_ingot);
        if (ConfigurationUtils.difficulty < 2) {
            GameRegistry.addRecipe(
                    new ItemStack(ItemCloudSwordCore, 1),
                    " X ",
                    "XYX",
                    " Z ",
                    'X', Blocks.iron_block,
                    'Y', new ItemStack(itemMateria, 1, 0),
                    'Z', Items.iron_ingot);
        } else {
            GameRegistry.addRecipe(
                    new ItemStack(ItemCloudSwordCore, 1),
                    " X ",
                    "DYD",
                    " Z ",
                    'X', Blocks.iron_block,
                    'Y', new ItemStack(itemMateria, 1, 0),
                    'Z', Items.iron_ingot,
                    'D', Items.diamond);
        }
        GameRegistry.addRecipe(
                new ItemStack(itemSephirothSword, 1),
                "  A", " B ", "C  ",
                'A', Items.iron_ingot,
                'B', new ItemStack(Items.diamond_sword, 1, 0),
                'C', new ItemStack(itemMateria, 1, 1));
        GameRegistry.addRecipe(
                new ItemStack(itemUltimateWeapon, 1),
                " A ",
                "ABA",
                " C ",
                'A', Blocks.diamond_block,
                'B', new ItemStack(itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE),
                'C', Items.stick);
        GameRegistry.addRecipe(new ItemStack(itemImitateSephirothSword),
                "  A",
                " A ",
                "B  ",
                'A', Items.iron_ingot,
                'B', Items.iron_sword);
        GameRegistry.addRecipe(new ItemStack(blockEnchantChanger, 1),
                "XYX",
                "ZZZ",
                'X', Items.diamond,
                'Y', Blocks.gold_block,
                'Z', Blocks.obsidian);
        GameRegistry.addRecipe(new ItemStack(itemHugeMateria),
                " A ",
                "ABA",
                " A ",
                'A', Blocks.diamond_block,
                'B', Items.nether_star);
        GameRegistry
                .addRecipe(new ItemStack(itemHugeMateria),
                        " A ",
                        "ABA",
                        " A ",
                        'A', Blocks.diamond_block,
                        'B', new ItemStack(itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(
                itemPortableEnchantChanger, 1), blockEnchantChanger);
        GameRegistry.addShapelessRecipe(new ItemStack(
                        itemPortableEnchantmentTable, 1),
                Blocks.enchanting_table);
        GameRegistry.addShapelessRecipe(new ItemStack(itemMasterMateria, 1, 0),
                new ItemStack(itemMasterMateria, 1, 1),
                new ItemStack(itemMasterMateria, 1, 2),
                new ItemStack(itemMasterMateria, 1, 3),
                new ItemStack(itemMasterMateria, 1, 4),
                new ItemStack(itemMasterMateria, 1, 5));
//        if (ConfigurationUtils.difficulty == 0)
//            GameRegistry.addRecipe(
//                    new ItemStack(Items.experience_bottle, 8),
//                    "XXX", "XYX", "XXX",
//                    'X', new ItemStack(Items.potionitem, 1, 0),
//                    'Y', new ItemStack(Items.diamond, 1));
        GameRegistry.addRecipe(new ItemStack(itemExExpBottle, 8),
                "XXX", "XYX", "XXX",
                'X', new ItemStack(Items.experience_bottle, 1, 0),
                'Y', new ItemStack(Blocks.diamond_block, 1));
        GameRegistry
                .addRecipe(new ItemStack(Blocks.dragon_egg, 1),
                        "XXX",
                        "XYX",
                        "XXX",
                        'X', Items.ender_eye,
                        'Y', new ItemStack(itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE));

        for (String baseOreName : EcBlockMakoReactor.baseBlocksOreName) {
            addOreDictRecipe(baseOreName);
        }
    }


    private static void addOreDictRecipe(String OreDictName) {
        ItemStack makoReactorController;
        ArrayList<ItemStack> ores = OreDictionary.getOres(OreDictName);
        if (ores.isEmpty()) return;
        for (ItemStack itemStack : ores) {
            makoReactorController = new ItemStack(blockMakoReactor, 1, 0);
            makoReactorController.setTagCompound(new NBTTagCompound());
            GameRegistry.UniqueIdentifier uid = GameRegistry.findUniqueIdentifierFor(itemStack.getItem());
            makoReactorController.getTagCompound().setString("EnchantChanger|baseBlock", uid.toString());
            makoReactorController.getTagCompound().setInteger("EnchantChanger|baseMeta", itemStack.getItemDamage());
            GameRegistry.addRecipe(makoReactorController,
                    "BBB",
                    "BMB",
                    "BBB",
                    'B', itemStack,
                    'M', new ItemStack(itemMateria, 1, 0));
        }
    }
}
