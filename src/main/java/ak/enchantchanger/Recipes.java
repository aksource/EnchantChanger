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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.IForgeRegistry;

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

    private static void addOreDictRecipe(IForgeRegistry<IRecipe> registry, EnumMRBaseType type) {
        ItemStack makoReactorController;
        List<ItemStack> ores = OreDictionary.getOres(type.getOreName());
        ResourceLocation rl = new ResourceLocation("", "");
        if (ores.isEmpty()) return;
        for (ItemStack itemStack : ores) {
            makoReactorController = new ItemStack(ak.enchantchanger.utils.Blocks.blockMakoReactor, 1, type.ordinal());
            registry.register(new ShapedOreRecipe(rl, makoReactorController,
                    "BBB",
                    "BMB",
                    "BBB",
                    'B', itemStack,
                    'M', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 0)).setRegistryName(ak.enchantchanger.utils.Blocks.blockMakoReactor.getRegistryName().getResourcePath() + itemStack.getItem().getRegistryName().getResourcePath()));
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        IForgeRegistry<IRecipe> registry = event.getRegistry();
        if (ConfigurationUtils.difficulty < 2)
            registry.register(new EcRecipeMateria().setRegistryName("materiaRecipe"));
        registry.register(new EcRecipeMasterMateria().setRegistryName("masterMateriaRecipe"));
        ResourceLocation rl = new ResourceLocation("", "");
        registry.register(new ShapedOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Items.itemZackSword, 1),
                " X",
                "XX",
                " Y",
                'X', Blocks.IRON_BLOCK,
                'Y', Items.IRON_INGOT).setRegistryName(ak.enchantchanger.utils.Items.itemZackSword.getRegistryName()));
        if (ConfigurationUtils.difficulty < 2) {
            registry.register(new ShapedOreRecipe(rl,
                    new ItemStack(ak.enchantchanger.utils.Items.itemCloudSwordCore, 1),
                    " X ",
                    "XYX",
                    " Z ",
                    'X', Blocks.IRON_BLOCK,
                    'Y', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 0),
                    'Z', Items.IRON_INGOT).setRegistryName(ak.enchantchanger.utils.Items.itemCloudSwordCore.getRegistryName()));
        } else {
            registry.register(new ShapedOreRecipe(rl,
                    new ItemStack(ak.enchantchanger.utils.Items.itemCloudSwordCore, 1),
                    " X ",
                    "DYD",
                    " Z ",
                    'X', Blocks.IRON_BLOCK,
                    'Y', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 0),
                    'Z', Items.IRON_INGOT,
                    'D', Items.DIAMOND).setRegistryName(ak.enchantchanger.utils.Items.itemCloudSwordCore.getRegistryName()));
        }
        registry.register(new ShapedOreRecipe(rl,
                new ItemStack(ak.enchantchanger.utils.Items.itemSephirothSword, 1),
                "  A", " B ", "C  ",
                'A', Items.IRON_INGOT,
                'B', new ItemStack(Items.DIAMOND_SWORD, 1, 0),
                'C', new ItemStack(ak.enchantchanger.utils.Items.itemMateria, 1, 1)).setRegistryName(ak.enchantchanger.utils.Items.itemSephirothSword.getRegistryName()));
        registry.register(new ShapedOreRecipe(rl,
                new ItemStack(ak.enchantchanger.utils.Items.itemUltimateWeapon, 1),
                " A ",
                "ABA",
                " C ",
                'A', Blocks.DIAMOND_BLOCK,
                'B', new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE),
                'C', Items.STICK).setRegistryName(ak.enchantchanger.utils.Items.itemUltimateWeapon.getRegistryName()));
        registry.register(new ShapedOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Items.itemImitateSephirothSword),
                "  A",
                " A ",
                "B  ",
                'A', Items.IRON_INGOT,
                'B', Items.IRON_SWORD).setRegistryName(ak.enchantchanger.utils.Items.itemImitateSephirothSword.getRegistryName()));
        registry.register(new ShapedOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Blocks.blockEnchantChanger, 1),
                "XYX",
                "ZZZ",
                'X', Items.DIAMOND,
                'Y', Blocks.GOLD_BLOCK,
                'Z', Blocks.OBSIDIAN).setRegistryName(ak.enchantchanger.utils.Blocks.blockEnchantChanger.getRegistryName()));
        registry.register(new ShapedOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Items.itemHugeMateria),
                " A ",
                "ABA",
                " A ",
                'A', Blocks.DIAMOND_BLOCK,
                'B', Items.NETHER_STAR).setRegistryName(ak.enchantchanger.utils.Items.itemHugeMateria.getRegistryName()));
        registry
                .register(new ShapedOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Items.itemHugeMateria),
                        " A ",
                        "ABA",
                        " A ",
                        'A', Blocks.DIAMOND_BLOCK,
                        'B', new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE)).setRegistryName(ak.enchantchanger.utils.Items.itemHugeMateria.getRegistryName()));
        registry.register(new ShapelessOreRecipe(rl, new ItemStack(
                ak.enchantchanger.utils.Items.itemPortableEnchantChanger, 1), ak.enchantchanger.utils.Blocks.blockEnchantChanger).setRegistryName(ak.enchantchanger.utils.Items.itemPortableEnchantChanger.getRegistryName()));
        registry.register(new ShapelessOreRecipe(rl, new ItemStack(
                ak.enchantchanger.utils.Items.itemPortableEnchantmentTable, 1),
                Blocks.ENCHANTING_TABLE).setRegistryName(ak.enchantchanger.utils.Items.itemPortableEnchantmentTable.getRegistryName()));
        registry.register(new ShapelessOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 0),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 1),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 2),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 3),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 4),
                new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, 5)).setRegistryName(ak.enchantchanger.utils.Items.itemMasterMateria.getRegistryName()));
        registry.register(new ShapedOreRecipe(rl, new ItemStack(ak.enchantchanger.utils.Items.itemExExpBottle, 8),
                "XXX", "XYX", "XXX",
                'X', new ItemStack(Items.EXPERIENCE_BOTTLE, 1, 0),
                'Y', new ItemStack(Blocks.DIAMOND_BLOCK, 1)).setRegistryName(ak.enchantchanger.utils.Items.itemExExpBottle.getRegistryName()));
        registry
                .register(new ShapedOreRecipe(rl, new ItemStack(Blocks.DRAGON_EGG, 1),
                        "XXX",
                        "XYX",
                        "XXX",
                        'X', Items.ENDER_EYE,
                        'Y', new ItemStack(ak.enchantchanger.utils.Items.itemMasterMateria, 1, OreDictionary.WILDCARD_VALUE)).setRegistryName(Constants.MOD_ID, Blocks.DRAGON_EGG.getRegistryName().getResourcePath()));

        for (EnumMRBaseType type : EnumMRBaseType.values()) {
            addOreDictRecipe(registry, type);
        }
    }
}
