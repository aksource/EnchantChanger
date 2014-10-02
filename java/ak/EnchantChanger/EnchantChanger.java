package ak.EnchantChanger;

import ak.EnchantChanger.block.EcBlockHugeMateria;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.block.EcBlockMakoReactor;
import ak.EnchantChanger.block.EcBlockMaterializer;
import ak.EnchantChanger.enchantment.*;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteor;
import ak.EnchantChanger.eventhandler.CommonTickHandler;
import ak.EnchantChanger.eventhandler.FillBucketHook;
import ak.EnchantChanger.eventhandler.LivingEventHooks;
import ak.EnchantChanger.item.*;
import ak.EnchantChanger.modcoop.CoopMCE;
import ak.EnchantChanger.network.MessagePlayerProperties;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.potion.EcPotionMako;
import ak.EnchantChanger.recipe.EcRecipeMasterMateria;
import ak.EnchantChanger.recipe.EcRecipeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import ak.EnchantChanger.tileentity.EcTileEntityMaterializer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import java.util.*;
import java.util.logging.Logger;

@Mod(modid = "EnchantChanger", name = "EnchantChanger", version = "@VERSION@", dependencies = "required-after:Forge@[10.12.1.1090,)", useMetadata = true)
public class EnchantChanger {

    public static final String MOD_ID = "EnchantChanger";
    public static Item itemExExpBottle;
    public static Item itemMateria;
    public static Item itemZackSword;
    public static Item itemCloudSword;
    public static Item ItemCloudSwordCore;
    public static Item itemSephirothSword;
    public static Item itemUltimateWeapon;
    public static Item iemPortableEnchantChanger;
    public static Item itemPortableEnchantmentTable;
    public static Item itemMasterMateria;
    public static Item itemImitateSephirothSword;
    public static Block blockEnchantChanger;
    public static Block blockHugeMateria;
    public static Item itemHugeMateria;
    public static Block blockLifeStream;
    public static Fluid fluidLifeStream;
    public static Item bucketLifeStream;
    public static Block blockMakoReactor;
    public static Potion potionMako;
    public static DamageSource damageSourceMako;

    public static boolean enableLevelCap = true;
    public static boolean debug = false;
    public static float powerMeteor;
    public static float sizeMeteor;
    public static int[] extraSwordIDs = new int[]{267};
    public static int[] extraToolIDs = new int[]{257};
    public static int[] extraBowIDs = new int[]{261};
    public static int[] extraArmorIDs = new int[]{298};

    private static String[] enchantmentLevelLimits = new String[]{
            "0:10",
            "1:10",
            "2:10",
            "3:10",
            "4:10",
            "32:0",
            "34:30",
            "303:0",
            "307:0",
            "310:0"
    };
    public static HashMap<Integer, Integer> levelLimitMap = new HashMap<>();

    private static String[] enchantmentAPCoefficients = new String[]{
            "0:2",
            "1:1",
            "2:1",
            "3:1",
            "4:1",
            "5:1",
            "6:1",
            "7:1",
            "16:2",
            "17:1",
            "18:1",
            "19:1",
            "20:1",
            "21:3",
            "32:1",
            "33:1",
            "34:1",
            "35:2",
            "48:2",
            "49:1",
            "50:1",
            "51:1",
            "61:1",
            "62:1"
    };
    public static HashMap<Integer, Integer> coefficientMap = new HashMap<>();
    public static boolean enableDecMateriaLv = false;
    public static boolean flagYOUARETERRA = false;
    public static int minutesMateriaEffects = 10;
    public static int difficulty = 2;
    public static int enchantChangerCost = 5;
    public static double sizeAbsorbBox = 5D;
//    public static int MaxLv = 127;
    public static boolean enableAPSystem = true;
    public static boolean enableDungeonLoot = true;
    public static int pointAPBase = 200;
    public static int idMakoPoison = 100;

    public static int cloudInvXCoord = 0;
    public static int cloudInvYCoord = 0;

    public static int soldierSalary = 10;
    public static int lifeStreamLakeRatio = 256;

    public static int idEnchantmentMeteor = 240;
    public static Enchantment enchantmentMeteor;
    public static int idEnchantmentHoly = 241;
    public static Enchantment enchantmentHoly;
    public static int idEnchantmentTelepo = 242;
    public static Enchantment enchantmentTelepo;
    public static int idEnchantmentFloat = 243;
    public static Enchantment enchantmentFloat;
    public static int idEnchantmentThunder = 244;
    public static Enchantment enchantmentThunder;

    public static final String EcMeteorPNG = "textures/items/Meteo.png";
    public static final String EcExpBottlePNG = "textures/items/ExExpBottle.png";
    public static final String EcZackSwordPNG = "textures/item/ZackSword.png";
    public static final String EcSephirothSwordPNG = "textures/item/SephirothSword.png";
    public static final String EcCloudSword2PNG = "textures/item/CloudSword-3Dtrue.png";
    public static final String EcCloudSwordCore2PNG = "textures/item/CloudSwordCore-3Dtrue.png";
    public static final String EcUltimateWeaponPNG = "textures/item/UltimaWeapon.png";
    public static final String EcGuiMaterializer = "textures/gui/materializer.png";
    public static final String EcGuiHuge = "textures/gui/HugeMateriaContainer.png";
    public static final String EcGuiMako = "textures/gui/MakoReactorContainer.png";
    public static final String EcGuiMateriaWindow = "textures/gui/MaterializingContainer.png";
    public static final String EcPotionEffect = "textures/gui/potioneffect.png";
    public static final String EcHugetex = "textures/item/hugemateriatex.png";
    public static final String EcTextureDomain = "enchantchanger:";
    public static final String EcAssetsDomain = "enchantchanger";

    public static boolean loadMTH = false;
    public static boolean loadBC = false;
    public static boolean loadIC = false;
    public static boolean loadTE = false;
    public static boolean loadUE = false;
    public static boolean loadMCE = false;
    @Mod.Instance("EnchantChanger")
    public static EnchantChanger instance;
    @SidedProxy(clientSide = "ak.EnchantChanger.Client.ClientProxy", serverSide = "ak.EnchantChanger.CommonProxy")
    public static CommonProxy proxy;
    public static int guiIdMaterializer = 0;
    public static int guiIdPortableEnchantmentTable = 1;
    public static int guiIdHugeMateria = 2;
    public static int guiIdMateriaWindow = 3;
    public static int guiIdMakoReactor = 4;
    public static final HashMap<Integer, Integer> apLimit = new HashMap<>();
    public static final HashSet<Integer> magicEnchantment = new HashSet<>();
    public static final CreativeTabs tabsEChanger = new CreativeTabEC(
            "EnchantChanger");
    public static final LivingEventHooks livingeventhooks = new LivingEventHooks();

    public static final byte MagicKEY = 0;
    public static final byte MateriaKEY = 1;
    public static final byte CtrlKEY = 2;

    //Logger
    public static final Logger logger = Logger.getLogger("EnchantChanger");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        config.load();
        enableLevelCap = config
                .get(Configuration.CATEGORY_GENERAL,
                        "enableLevelCap",
                        enableLevelCap,
                        "TRUE:You cannot change a Materia to a enchantment over max level of the enchantment.")
                .getBoolean(enableLevelCap);
        debug = config.get(Configuration.CATEGORY_GENERAL, "debug mode", debug,
                "デバッグ用").getBoolean(debug);
        enableAPSystem = config.get(Configuration.CATEGORY_GENERAL,
                "enableAPSystem", enableAPSystem).getBoolean(enableAPSystem);
        enableDungeonLoot = config.get(Configuration.CATEGORY_GENERAL,
                "enableDungeonLoot", enableDungeonLoot).getBoolean(enableDungeonLoot);
        pointAPBase = config.get(Configuration.CATEGORY_GENERAL, "APBasePoint",
                pointAPBase).getInt();
        idMakoPoison = config.get(Configuration.CATEGORY_GENERAL, "idMakoPoison", idMakoPoison, "Mako Poison Effect Id").getInt();
        extraSwordIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra SwordIds", extraSwordIDs,
                "Put Ids which you want to operate as  swords.").getIntList();
        extraToolIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra ToolIds", extraToolIDs,
                "Put Ids which you want to operate as  tools.").getIntList();
        extraBowIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra BowIds", extraBowIDs,
                "Put Ids which you want to operate as  bows.").getIntList();
        extraArmorIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra ArmorIds", extraArmorIDs,
                "Put Ids which you want to operate as  armors.").getIntList();

        enableDecMateriaLv = config
                .get(Configuration.CATEGORY_GENERAL, "enableDecMateriaLv", enableDecMateriaLv,
                        "TRUE:The level of extracted Materia is decreased by the item damage")
                .getBoolean(enableDecMateriaLv);
        flagYOUARETERRA = config
                .get(Configuration.CATEGORY_GENERAL,
                        "flagYOUARETERRA",
                        flagYOUARETERRA,
                        "TRUE:You become Tera in FF4. It means that you can use Magic Materia when your MP is exhausted")
                .getBoolean(flagYOUARETERRA);
        powerMeteor = (float) config.get(Configuration.CATEGORY_GENERAL,
                "METEOR POWER", 10, "This is a power of Meteor").getInt();
        sizeMeteor = (float) config.get(Configuration.CATEGORY_GENERAL,
                "Meteor Size", 10, "This is a Size of Meteor").getInt();
        minutesMateriaEffects = config.get(Configuration.CATEGORY_GENERAL,
                "Materia Potion Minutes", minutesMateriaEffects,
                "How long minutes Materia put potion effect to MOB or ANIMAL")
                .getInt();
        difficulty = config.get(Configuration.CATEGORY_GENERAL, "Difficulty",
                difficulty, "Difficulty of this MOD. 0 = Easy, 1 = Normal, 2 = Hard")
                .getInt();
        idEnchantmentMeteor = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentMeteorId", idEnchantmentMeteor).getInt();
        idEnchantmentHoly = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentHolyId", idEnchantmentHoly).getInt();
        idEnchantmentTelepo = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentTelepoId", idEnchantmentTelepo).getInt();
        idEnchantmentFloat = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentFloatId", idEnchantmentFloat).getInt();
        idEnchantmentThunder = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentThunderId", idEnchantmentThunder).getInt();
        enchantmentLevelLimits = config
                .get(Configuration.CATEGORY_GENERAL, "ApSystemLevelLimit", enchantmentLevelLimits,
                        "Set Enchantmets Level Limit for AP System Format EnchantmentID:LimitLv(LimitLv = 0 > DefaultMaxLevel)")
                .getStringList();
        enchantmentAPCoefficients = config.get(Configuration.CATEGORY_GENERAL, "ApSystemCoefficientList", enchantmentAPCoefficients,
                "Set Coefficients of AP System. Format EnchantmentsID:Coefficient").getStringList();
        cloudInvXCoord = config.get(Configuration.CATEGORY_GENERAL, "CloudSwordHUDxCoordinate", cloudInvXCoord).getInt();
        cloudInvYCoord = config.get(Configuration.CATEGORY_GENERAL, "CloudSwordHUDyCoordinate", cloudInvYCoord).getInt();
        enchantChangerCost = config.get(Configuration.CATEGORY_GENERAL, "EnchantChangerOpenCost", enchantChangerCost, "Cost to open EnchantChanger or Materia Window when mods difficulty is hard").getInt();
        soldierSalary = config.get(Configuration.CATEGORY_GENERAL, "SoldiersSalary" , soldierSalary, "Monthly Salary of soldier.").getInt();
        config.save();

        itemMateria = (new EcItemMateria("Materia")).setHasSubtypes(true).setMaxDamage(0).setTextureName("ender_pearl");
        itemExExpBottle = new EcItemExExpBottle("ExExpBottle");
        itemZackSword = (new EcItemZackSword("ZackSword")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        ItemCloudSwordCore = (new EcItemCloudSwordCore("CloudSwordCore")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemCloudSword = (new EcItemCloudSword("CloudSword")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14).setCreativeTab(null);
        itemSephirothSword = (new EcItemSephirothSword("MasamuneBlade")).setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 2);
        itemUltimateWeapon = (new EcItemUltimateWeapon("UltimateWeapon")).setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 14);
        iemPortableEnchantChanger = (new EcItemMaterializer("PortableEnchantChanger"));
        itemPortableEnchantmentTable = (new EcItemEnchantmentTable("PortableEnchantmentTable"));
        itemMasterMateria = new EcItemMasterMateria("itemMasterMateria").setTextureName("ender_pearl").setHasSubtypes(true).setMaxDamage(0).setMaxStackSize(1);
        itemImitateSephirothSword = (new EcItemSephirothSwordImit("ImitateMasamuneBlade"));
        blockEnchantChanger = (new EcBlockMaterializer()).setBlockName("EnchantChanger").setCreativeTab(tabsEChanger).setBlockTextureName(EcTextureDomain + "EnchantChanger-top").setHardness(5.0f).setResistance(2000.0f).setLightOpacity(0);
        blockHugeMateria = new EcBlockHugeMateria().setHardness(5.0f).setResistance(2000.0f).setLightLevel(1.0f).setLightOpacity(0).setBlockName("blockHugeMateria").setBlockTextureName("glass");
        itemHugeMateria = new EcItemHugeMateria("HugeMateria");
        fluidLifeStream = new Fluid("lifestream").setLuminosity(15);
        FluidRegistry.registerFluid(fluidLifeStream);
        blockLifeStream = new EcBlockLifeStreamFluid(fluidLifeStream, Material.water).setBlockName("lifestream");
        bucketLifeStream = new EcItemBucketLifeStream(blockLifeStream, "bucket_lifestream").setContainerItem(Items.bucket).setCreativeTab(tabsEChanger);
        blockMakoReactor = new EcBlockMakoReactor().setBlockName("makoreactor").setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(tabsEChanger).setBlockTextureName(EcTextureDomain + "makoreactor-side");

        registerBlockAndItem();
        registerEnchantments();
        PacketHandler.init();
        addStatusEffect();
        damageSourceMako = new DamageSource("mako").setDamageBypassesArmor();
    }

    private void registerEnchantments() {
        enchantmentMeteor = new EcEnchantmentMeteo(EnchantChanger.idEnchantmentMeteor, 0).setName("Meteor");
        enchantmentHoly = new EcEnchantmentHoly(EnchantChanger.idEnchantmentHoly, 0).setName("Holy");
        enchantmentTelepo = new EcEnchantmentTeleport(EnchantChanger.idEnchantmentTelepo, 0).setName("Teleporting");
        enchantmentFloat = new EcEnchantmentFloat(EnchantChanger.idEnchantmentFloat, 0).setName("Floating");
        enchantmentThunder = new EcEnchantmentThunder(EnchantChanger.idEnchantmentThunder, 0).setName("Thunder");
    }


    private void registerBlockAndItem() {
        GameRegistry.registerBlock(blockEnchantChanger, "EnchantChanger");
        GameRegistry.registerBlock(blockHugeMateria, "blockhugemateria");
        GameRegistry.registerBlock(blockMakoReactor, EcItemBlockMakoReactor.class, "blockmakoreactor");
        GameRegistry.registerBlock(blockLifeStream, "life_stream");
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("lifestream", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketLifeStream), new ItemStack(Items.bucket));

        GameRegistry.registerItem(itemMateria, "materia");
        GameRegistry.registerItem(itemHugeMateria, "itemhugemateria");
        GameRegistry.registerItem(itemExExpBottle, "exexpbottle");
        GameRegistry.registerItem(itemZackSword, "zacksword");
        GameRegistry.registerItem(ItemCloudSwordCore, "cloudswordcore");
        GameRegistry.registerItem(itemCloudSword, "cloudsword");
        GameRegistry.registerItem(itemSephirothSword, "masamuneblade");
        GameRegistry.registerItem(itemUltimateWeapon, "ultimateweapon");
        GameRegistry.registerItem(iemPortableEnchantChanger,
                "portableenchantchanger");
        GameRegistry.registerItem(itemPortableEnchantmentTable,
                "portableenchantmenttable");
        GameRegistry.registerItem(itemMasterMateria, "mastermateria");
        GameRegistry.registerItem(itemImitateSephirothSword,
                "imitationmasamuneblade");
        GameRegistry.registerItem(bucketLifeStream, "bucket_lifestream");
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        this.initMaps();
        MinecraftForge.EVENT_BUS.register(livingeventhooks);
        FillBucketHook.buckets.put(blockLifeStream, bucketLifeStream);
        MinecraftForge.EVENT_BUS.register(FillBucketHook.INSTANCE);
        FMLCommonHandler.instance().bus().register(proxy);
        FMLCommonHandler.instance().bus().register(new CommonTickHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(this);

        registerTileEntities();

        registerEntities();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.registerRenderInformation();
        proxy.registerTileEntitySpecialRenderer();

        registerRecipes();

        if (enableDungeonLoot) {
            this.DungeonLootItemResist();
        }
        loadMTH = Loader.isModLoaded("MultiToolHolders");
        loadMCE = Loader.isModLoaded("mceconomy2");
        loadTE = Loader.isModLoaded("CoFHCore");
        if (loadMCE) {
            MinecraftForge.EVENT_BUS.register(new CoopMCE());
        }
    }

    private void registerTileEntities() {
        GameRegistry.registerTileEntity(EcTileEntityMaterializer.class,
                "container.materializer");
        GameRegistry.registerTileEntity(EcTileEntityHugeMateria.class,
                "container.hugeMateria");
        GameRegistry.registerTileEntity(EcTileEntityMakoReactor.class, "container.makoReactor");
//        GameRegistry.registerTileEntity(EcTileMultiPass.class, "tile.multipass");
    }

    private void registerEntities() {
        EntityRegistry.registerModEntity(EcEntityExExpBottle.class,
                "itemExExpBottle", 0, this, 250, 5, true);
        EntityRegistry.registerModEntity(EcEntityMeteor.class, "enchantmentMeteor", 1, this,
                250, 5, true);
        EntityRegistry.registerModEntity(EcEntityApOrb.class, "apOrb", 2, this,
                64, 1, false);
    }

    private void registerRecipes() {
        RecipeSorter.register("EnchantChanger:MateriaRecipe", EcRecipeMateria.class, Category.SHAPELESS, "after:FML");
        RecipeSorter.register("EnchantChanger:MasterMateriaRecipe", EcRecipeMasterMateria.class, Category.SHAPELESS, "after:FML");
        if (EnchantChanger.difficulty < 2)
            GameRegistry.addRecipe(new EcRecipeMateria());
        GameRegistry.addRecipe(new EcRecipeMasterMateria());
        GameRegistry.addShapelessRecipe(new ItemStack(itemMateria, 1, 0),
                new ItemStack(Items.diamond, 1),
                new ItemStack(Items.ender_pearl, 1));
        GameRegistry.addRecipe(new ItemStack(itemZackSword, 1),
                " X",
                "XX",
                " Y",
                'X', Blocks.iron_block,
                'Y', Items.iron_ingot);
        if (EnchantChanger.difficulty < 2) {
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
                        'B', new ItemStack(itemMasterMateria, 1,  OreDictionary.WILDCARD_VALUE));
        GameRegistry.addShapelessRecipe(new ItemStack(
                iemPortableEnchantChanger, 1), blockEnchantChanger);
        GameRegistry.addShapelessRecipe(new ItemStack(
                        itemPortableEnchantmentTable, 1),
                Blocks.enchanting_table);
        GameRegistry.addShapelessRecipe(new ItemStack(itemMasterMateria, 1, 0),
                new ItemStack(itemMasterMateria, 1, 1),
                new ItemStack(itemMasterMateria, 1, 2),
                new ItemStack(itemMasterMateria, 1, 3),
                new ItemStack(itemMasterMateria, 1, 4),
                new ItemStack(itemMasterMateria, 1, 5));
        if (difficulty == 0)
            GameRegistry.addRecipe(
                    new ItemStack(Items.experience_bottle, 8),
                    "XXX", "XYX", "XXX",
                    'X', new ItemStack(Items.potionitem, 1, 0),
                    'Y', new ItemStack(Items.diamond, 1));
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
        ItemStack makoReactorController;
        ArrayList<ItemStack> ores;
        for (String baseOreName: EcBlockMakoReactor.baseBlocksOreName) {
            ores = OreDictionary.getOres(baseOreName);
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

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {  }

    private void initMaps() {
        makeMapFromArray(coefficientMap, enchantmentAPCoefficients);
        for (Integer integer : coefficientMap.keySet()) {
            apLimit.put(integer, coefficientMap.get(integer) * pointAPBase);
        }
        magicEnchantment.add(idEnchantmentMeteor);
        magicEnchantment.add(idEnchantmentFloat);
        magicEnchantment.add(idEnchantmentHoly);
        magicEnchantment.add(idEnchantmentTelepo);
        magicEnchantment.add(idEnchantmentThunder);
//        apLimit.put(0, 2 * pointAPBase);
//        apLimit.put(1, 1 * pointAPBase);
//        apLimit.put(2, 1 * pointAPBase);
//        apLimit.put(3, 1 * pointAPBase);
//        apLimit.put(4, 1 * pointAPBase);
//        apLimit.put(5, 1 * pointAPBase);
//        apLimit.put(6, 1 * pointAPBase);
//        apLimit.put(7, 1 * pointAPBase);
//        apLimit.put(16, 2 * pointAPBase);
//        apLimit.put(17, 1 * pointAPBase);
//        apLimit.put(18, 1 * pointAPBase);
//        apLimit.put(19, 1 * pointAPBase);
//        apLimit.put(20, 1 * pointAPBase);
//        apLimit.put(21, 3 * pointAPBase);
//        apLimit.put(32, 1 * pointAPBase);
//        apLimit.put(33, 1 * pointAPBase);
//        apLimit.put(34, 1 * pointAPBase);
//        apLimit.put(35, 2 * pointAPBase);
//        apLimit.put(48, 2 * pointAPBase);
//        apLimit.put(49, 1 * pointAPBase);
//        apLimit.put(50, 1 * pointAPBase);
//        apLimit.put(51, 1 * pointAPBase);
//        apLimit.put(61, 1 * pointAPBase);
//        apLimit.put(62, 1 * pointAPBase);
        makeMapFromArray(levelLimitMap, enchantmentLevelLimits);
    }

    private void makeMapFromArray(Map<Integer, Integer> map, String[] array) {
        String[] split;
        for (String enchLimit : array) {
            split = enchLimit.split(":");
            map.put(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
        }
    }

    public static String getUniqueStrings(Object obj) {
        UniqueIdentifier uId;
        if (obj instanceof Block) {
            uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
        } else {
            uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
        }
        return uId.toString();

    }

    public static boolean isApLimit(int Id, int Lv, int ap) {
        return EnchantChanger.getApLimit(Id, Lv) < ap;
    }

    public static int getApLimit(int Id, int Lv) {
        if (EnchantChanger.apLimit.containsKey(Id)) {
            return EnchantChanger.apLimit.get(Id) * (Lv / 5 + 1);
        } else
            return 150 * (Lv / 5 + 1);
    }

    public static void addEnchantmentToItem(ItemStack item,
                                            Enchantment enchantment, int Lv) {
        if (item == null || enchantment == null || Lv <= 0) {
            return;
        }
        if (item.stackTagCompound == null) {
            item.setTagCompound(new NBTTagCompound());
        }

        if (!item.stackTagCompound.hasKey("ench", 9)) {
            item.stackTagCompound.setTag("ench", new NBTTagList());
        }

        NBTTagList var3 =item.stackTagCompound.getTagList("ench", 10);
        NBTTagCompound var4 = new NBTTagCompound();
        var4.setShort("id", (short) enchantment.effectId);
        var4.setShort("lvl", (short) (Lv));
        var3.appendTag(var4);
    }

    public static int getMateriaEnchKind(ItemStack item) {
        int EnchantmentKind = 256;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                EnchantmentKind = i;
                break;
            }
        }
        return EnchantmentKind;
    }

    public static int getMateriaEnchLv(ItemStack item) {
        int Lv = 0;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                Lv = EnchantmentHelper.getEnchantmentLevel(i, item);
                break;
            }
        }
        return Lv;
    }

    public static int getDecreasedLevel(ItemStack itemStack, int originalLevel) {
        if (enableDecMateriaLv) {
            float dmgratio = (itemStack.getMaxDamage() == 0) ? 1 : (itemStack.getMaxDamage() - itemStack.getItemDamage()) / itemStack.getMaxDamage();
            int declv = (dmgratio > 0.5F) ? 0 : (dmgratio > 0.25F) ? 1 : 2;
            return (originalLevel - declv < 0) ? 0 : originalLevel - declv;
        } else return originalLevel;
    }

    public static MovingObjectPosition getMouseOverCustom(EntityPlayer player,
                                                          World world, double reach) {
        Entity pointedEntity = null;
        float var1 = 1F;
        double distBlock = reach;
        double viewX = player.getLookVec().xCoord;
        double viewY = player.getLookVec().yCoord;
        double viewZ = player.getLookVec().zCoord;
        double PlayerposX = player.posX;
        // player.prevPosX + (player.posX - player.prevPosX) * (double)var1;
        double PlayerposY = player.posY + 1.62D - player.getYOffset();
        // player.prevPosY + (player.posY - player.prevPosY) * (double)var1;
        double PlayerposZ = player.posZ;
        // player.prevPosZ + (player.posZ - player.prevPosZ) * (double)var1;
        Vec3 PlayerPosition = Vec3.createVectorHelper(
                PlayerposX, PlayerposY, PlayerposZ);
        Vec3 PlayerLookVec = PlayerPosition.addVector(viewX * reach, viewY
                * reach, viewZ * reach);
        MovingObjectPosition MOP = world.rayTraceBlocks(PlayerPosition, PlayerLookVec);
        if (MOP != null)
            distBlock = MOP.hitVec.distanceTo(PlayerPosition);
        @SuppressWarnings("unchecked")
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(
                player,
                player.boundingBox.addCoord(viewX * reach,
                        viewY * reach, viewZ * reach).expand(
                        (double) var1, (double) var1, (double) var1));
        double dist1 = distBlock;
        for (Entity entity : list) {

            if (entity.canBeCollidedWith()) {
                float f2 = entity.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity.boundingBox.expand(
                        (double) f2, (double) f2, (double) f2);
                MovingObjectPosition movingobjectposition = axisalignedbb
                        .calculateIntercept(PlayerPosition, PlayerLookVec);

                if (axisalignedbb.isVecInside(PlayerPosition)) {
                    if (0.0D < dist1 || dist1 == 0.0D) {
                        pointedEntity = entity;
                        dist1 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = PlayerPosition
                            .distanceTo(movingobjectposition.hitVec);

                    if (d3 < dist1 || dist1 == 0.0D) {
                        pointedEntity = entity;
                        dist1 = d3;
                    }
                }
            }
        }

        if (pointedEntity != null && (dist1 < distBlock || MOP == null)) {
            MOP = new MovingObjectPosition(pointedEntity);
        }
        return MOP;
    }

    public static Enchantment enchKind(ItemStack item) {
        int EnchantmentKind = -1;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                EnchantmentKind = i;
                break;
            }
        }
        return EnchantmentKind != -1 ? Enchantment.enchantmentsList[EnchantmentKind] : Enchantment.enchantmentsList[0];
    }

    public static int enchLv(ItemStack item) {
        int Lv = 0;
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
                Lv = EnchantmentHelper.getEnchantmentLevel(i, item);
                break;
            }
        }
        return Lv;
    }

    public static boolean checkLvCap(ItemStack materia) {
        if (enableLevelCap) {
            int ench = EnchantChanger.getMateriaEnchKind(materia);
            int lv = EnchantChanger.getMateriaEnchLv(materia);
            return !(Enchantment.enchantmentsList[ench].getMaxLevel() < lv);
        }
        return true;
    }

    public static boolean isEnchantmentValid(Enchantment ench, ItemStack par1ItemStack) {
        if (ench == null) {
            return false;
        }
        if (ench instanceof EnchantmentDurability) {
            return par1ItemStack.isItemStackDamageable() || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentDigging) {
            return par1ItemStack.getItem() == Items.shears || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentDamage || ench instanceof EnchantmentLootBonus || ench instanceof EnchantmentFireAspect) {
            return par1ItemStack.getItem() instanceof ItemTool || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentThorns) {
            return par1ItemStack.getItem() instanceof ItemArmor || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EnchantmentUntouching) {
            return par1ItemStack.getItem() == Items.shears || ench.type.canEnchantItem(par1ItemStack.getItem());
        }
        if (ench instanceof EcEnchantmentMeteo || ench instanceof EcEnchantmentHoly || ench instanceof EcEnchantmentTeleport || ench instanceof EcEnchantmentFloat || ench instanceof EcEnchantmentThunder) {
            return par1ItemStack.getItem() instanceof EcItemSword;
        }
        return ench.type.canEnchantItem(par1ItemStack.getItem());
    }
    private void addStatusEffect() {
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
    public void DungeonLootItemResist() {
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

    @SubscribeEvent
    public void loggedIn(PlayerEvent.PlayerLoggedInEvent event) {

    }

    @SubscribeEvent
    public void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {

    }

    @SubscribeEvent
    public void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.player.worldObj.isRemote) {
            PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(event.player), (EntityPlayerMP)event.player);
        }
    }

    @SubscribeEvent
    public void changedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.player.worldObj.isRemote) {
            PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(event.player), (EntityPlayerMP)event.player);
        }
    }
    //ライフストリームの地底湖を生成するつもりのコード
    @SubscribeEvent
    public void populateLifeStreamLake(PopulateChunkEvent.Populate event) {
        if (event.rand.nextInt(lifeStreamLakeRatio) == 0) {
            int k = event.chunkX * 16;
            int l = event.chunkZ * 16;
            int x,y,z;
            x = k + event.rand.nextInt(16) + 8;
            y = event.rand.nextInt(16);
            z = l + event.rand.nextInt(16) + 8;
            (new WorldGenLakes(blockLifeStream)).generate(event.world, event.rand, x, y, z);
            logger.info(String.format("LifeStreamLake is generated at (%d, %d, %d)", x, y, z));
        }
    }
}