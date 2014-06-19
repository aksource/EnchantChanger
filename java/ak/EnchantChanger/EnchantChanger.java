package ak.EnchantChanger;

import ak.EnchantChanger.Client.ClientProxy;
import ak.EnchantChanger.block.EcBlockHugeMateria;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.block.EcBlockMakoReactor;
import ak.EnchantChanger.block.EcBlockMaterialize;
import ak.EnchantChanger.enchantment.*;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteo;
import ak.EnchantChanger.eventhandler.CommonTickHandler;
import ak.EnchantChanger.eventhandler.FillBucketHook;
import ak.EnchantChanger.eventhandler.LivingEventHooks;
import ak.EnchantChanger.item.*;
import ak.EnchantChanger.network.MessageKeyPressed;
import ak.EnchantChanger.network.MessagePlayerProperties;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.potion.EcPotionMako;
import ak.EnchantChanger.recipe.EcMasterMateriaRecipe;
import ak.EnchantChanger.recipe.EcMateriaRecipe;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityMaterializer;
import ak.MultiToolHolders.ItemMultiToolHolder;
import com.ibm.icu.impl.IllegalIcuArgumentException;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    public static boolean enableLevelCap;
    public static boolean debug;
    public static float powerMeteo;
    public static float sizeMeteo;
    public static int[] extraSwordIDs;
    public static int[] extraToolIDs;
    public static int[] extraBowIDs;
    public static int[] extraArmorIDs;
    private static String[] enchantmentLevelLimits;
    private static String[] enchantmentLevelLimitInit = new String[]{
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

    public static boolean enableDecMateriaLv;
    public static boolean YouAreTera;
    public static int MateriaPotionMinutes;
    public static int Difficulty;
    public static double AbsorpBoxSize = 5D;
//    public static int MaxLv = 127;
    public static boolean enableAPSystem;
    public static boolean enableDungeonLoot;
    public static int aPBasePoint;
    public static int idMakoPoison;

    public static int EnchantmentMeteoId;
    public static Enchantment Meteo;
    public static int EndhantmentHolyId;
    public static Enchantment Holy;
    public static int EnchantmentTelepoId;
    public static Enchantment Telepo;
    public static int EnchantmentFloatId;
    public static Enchantment Float;
    public static int EnchantmentThunderId;
    public static Enchantment Thunder;

    public static final String EcMeteoPNG = "textures/items/Meteo.png";
    public static final String EcExpBottlePNG = "textures/items/ExExpBottle.png";
    public static final String EcZackSwordPNG = "textures/item/ZackSword.png";
    public static final String EcSephirothSwordPNG = "textures/item/SephirothSword.png";
    public static final String EcCloudSword2PNG = "textures/item/CloudSword-3Dtrue.png";
    public static final String EcCloudSwordCore2PNG = "textures/item/CloudSwordCore-3Dtrue.png";
    public static final String EcUltimateWeaponPNG = "textures/item/UltimaWeapon.png";
    public static final String EcGuiMaterializer = "textures/gui/materializer.png";
    public static final String EcGuiHuge = "textures/gui/HugeMateriaContainer.png";
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
    @Mod.Instance("EnchantChanger")
    public static EnchantChanger instance;
    @SidedProxy(clientSide = "ak.EnchantChanger.Client.ClientProxy", serverSide = "ak.EnchantChanger.CommonProxy")
    public static CommonProxy proxy;
    public static int guiIdMaterializer = 0;
    public static int guiIdPortableEnchantmentTable = 1;
    public static int guiIdHugeMateria = 2;
    public static int guiIdMateriaWindow = 3;
    public static final HashMap<Integer, Integer> apLimit = new HashMap<>();
    public static final HashSet<Integer> magicEnchantment = new HashSet<>();
    public static final CreativeTabs tabsEChanger = new CreativeTabEC(
            "EnchantChanger");
    public static final LivingEventHooks livingeventhooks = new LivingEventHooks();

    public static final byte MagicKEY = 0;
    public static final byte MateriaKEY = 1;

//    public static final PacketPipeline packetPipeline = new PacketPipeline();
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
                        false,
                        "TRUE:You cannot change a Materia to a enchantment over max level of the enchantment.")
                .getBoolean(false);
        debug = config.get(Configuration.CATEGORY_GENERAL, "debug mode", false,
                "デバッグ用").getBoolean(false);
        enableAPSystem = config.get(Configuration.CATEGORY_GENERAL,
                "enableAPSystem", true).getBoolean(true);
        enableDungeonLoot = config.get(Configuration.CATEGORY_GENERAL,
                "enableDungeonLoot", true).getBoolean(true);
        aPBasePoint = config.get(Configuration.CATEGORY_GENERAL, "APBAsePoint",
                200).getInt();
        idMakoPoison = config.get(Configuration.CATEGORY_GENERAL, "idMakoPoison", 100, "Mako Poison Effect Id").getInt();
        extraSwordIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra SwordIds", new int[]{267},
                "Put Ids which you want to operate as  swords.").getIntList();
        extraToolIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra ToolIds", new int[]{257},
                "Put Ids which you want to operate as  tools.").getIntList();
        extraBowIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra BowIds", new int[]{261},
                "Put Ids which you want to operate as  bows.").getIntList();
        extraArmorIDs = config.get(Configuration.CATEGORY_GENERAL,
                "Extra ArmorIds", new int[]{298},
                "Put Ids which you want to operate as  armors.").getIntList();

        enableDecMateriaLv = config
                .get(Configuration.CATEGORY_GENERAL, "enableDecMateriaLv", false,
                        "TRUE:The level of extracted Materia is decreased by the item damage")
                .getBoolean(false);
        YouAreTera = config
                .get(Configuration.CATEGORY_GENERAL,
                        "YouAreTera",
                        false,
                        "TRUE:You become Tera in FF4. It means that you can use Magic Materia when your MP is exhausted")
                .getBoolean(false);
        powerMeteo = (float) config.get(Configuration.CATEGORY_GENERAL,
                "METEO POWER", 10, "This is a power of Meteo").getInt();
        sizeMeteo = (float) config.get(Configuration.CATEGORY_GENERAL,
                "Meteo Size", 10, "This is a Size of Meteo").getInt();
        MateriaPotionMinutes = config.get(Configuration.CATEGORY_GENERAL,
                "Materia Potion Minutes", 10,
                "How long minutes Materia put potion effect to MOB or ANIMAL")
                .getInt();
        Difficulty = config.get(Configuration.CATEGORY_GENERAL, "Difficulty",
                1, "Difficulty of this MOD. 0 = Easy, 1 = Normal, 2 = Hard")
                .getInt();
        EnchantmentMeteoId = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentMeteoId", 240).getInt();
        EndhantmentHolyId = config.get(Configuration.CATEGORY_GENERAL,
                "EndhantmentHolyId", 241).getInt();
        EnchantmentTelepoId = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentTelepoId", 242).getInt();
        EnchantmentFloatId = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentFloatId", 243).getInt();
        EnchantmentThunderId = config.get(Configuration.CATEGORY_GENERAL,
                "EnchantmentThunderId", 244).getInt();
        enchantmentLevelLimits = config
                .get(Configuration.CATEGORY_GENERAL, "ApSystemLevelLimit", enchantmentLevelLimitInit,
                        "Set Enchantmets Level Limit for AP System Format EnchantmentID:LimitLv(LimitLv = 0 > DefaultMaxLevel)")
                .getStringList();
        config.save();

        itemMateria = (new EcItemMateria()).setHasSubtypes(true).setMaxDamage(0).setUnlocalizedName(
                EcTextureDomain + "Materia").setTextureName("ender_pearl").setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(itemMateria, "materia", "EnchantChanger");
        itemExExpBottle = new EcItemExExpBottle()
                .setUnlocalizedName(EcTextureDomain + "ExExpBottle").setTextureName(EcTextureDomain + "ExExpBottle")
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(itemExExpBottle, "exexpbottle",
                "EnchantChanger");
        itemZackSword = (new EcItemZackSword())
                .setUnlocalizedName(EcTextureDomain + "ZackSword").setTextureName(EcTextureDomain + "ZackSword").setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14)
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(itemZackSword, "zacksword", "EnchantChanger");
        ItemCloudSwordCore = (new EcItemCloudSwordCore())
                .setUnlocalizedName(EcTextureDomain + "CloudSwordCore").setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14)
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(ItemCloudSwordCore, "cloudswordcore",
                "EnchantChanger");
        itemCloudSword = (new EcItemCloudSword())
                .setUnlocalizedName(EcTextureDomain + "CloudSword").setTextureName(EcTextureDomain + "CloudSword").setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14)
                .setCreativeTab(null);
        GameRegistry.registerItem(itemCloudSword, "cloudsword",
                "EnchantChanger");
        itemSephirothSword = (new EcItemSephirothSword(
        )).setUnlocalizedName(
                EcTextureDomain + "MasamuneBlade").setCreativeTab(
                tabsEChanger).setTextureName(EcTextureDomain + "MasamuneBlade").setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 2);
        GameRegistry.registerItem(itemSephirothSword, "masamuneblade",
                "EnchantChanger");
        itemUltimateWeapon = (new EcItemUltimateWeapon(
        )).setUnlocalizedName(
                EcTextureDomain + "UltimateWeapon").setCreativeTab(
                tabsEChanger).setTextureName(EcTextureDomain + "UltimateWeapon").setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 14);
        GameRegistry.registerItem(itemUltimateWeapon, "ultimateweapon",
                "EnchantChanger");
        iemPortableEnchantChanger = (new EcItemMaterializer(
        )).setUnlocalizedName(
                EcTextureDomain + "PortableEnchantChanger")
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(iemPortableEnchantChanger,
                "portableenchantchanger", "EnchantChanger");
        itemPortableEnchantmentTable = (new EcItemEnchantmentTable(
        )).setUnlocalizedName(
                EcTextureDomain + "PortableEnchantmentTable")
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(itemPortableEnchantmentTable,
                "portableenchantmenttable", "EnchantChanger");
        itemMasterMateria = new EcItemMasterMateria()
                .setUnlocalizedName(EcTextureDomain + "itemMasterMateria").setTextureName("ender_pearl").setHasSubtypes(true).setMaxDamage(0).setMaxStackSize(1)
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(itemMasterMateria, "mastermateria",
                "EnchantChanger");
        itemImitateSephirothSword = (new EcItemSephirothSwordImit(
        )).setUnlocalizedName(
                EcTextureDomain + "ImitateMasamuneBlade").setCreativeTab(
                tabsEChanger).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses()).setTextureName(EcTextureDomain + "ImitateMasamuneBlade");
        GameRegistry.registerItem(itemImitateSephirothSword,
                "imitationmasamuneblade", "EnchantChanger");
        blockEnchantChanger = (new EcBlockMaterialize()).setCreativeTab(
                tabsEChanger).setBlockName("EnchantChanger").setBlockTextureName(EcTextureDomain + "EnchantChanger-top").setHardness(5.0f).setResistance(2000.0f).setLightOpacity(0);
        GameRegistry.registerBlock(blockEnchantChanger, "EnchantChanger");
        blockHugeMateria = new EcBlockHugeMateria().setHardness(5.0f).setResistance(2000.0f).setLightLevel(1.0f).setLightOpacity(0)
                .setBlockName("blockHugeMateria").setBlockTextureName("glass");
        GameRegistry.registerBlock(blockHugeMateria, "blockhugemateria");
        itemHugeMateria = new EcItemHugeMateria()
                .setUnlocalizedName(EcTextureDomain + "HugeMateria").setTextureName(EcTextureDomain + "HugeMateria")
                .setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(itemHugeMateria, "itemhugemateria",
                "EnchantChanger");
        fluidLifeStream = new Fluid("lifestream").setLuminosity(15);
        FluidRegistry.registerFluid(fluidLifeStream);
        blockLifeStream = new EcBlockLifeStreamFluid(fluidLifeStream, Material.water).setBlockName("lifestream");
        GameRegistry.registerBlock(blockLifeStream, "life_stream");
        bucketLifeStream = new EcItemBucketLifeStream(blockLifeStream).setUnlocalizedName(EcTextureDomain + "bucketLifestream").setTextureName(EnchantChanger.EcTextureDomain + "bucket_lifestream").setContainerItem(Items.bucket).setCreativeTab(tabsEChanger);
        GameRegistry.registerItem(bucketLifeStream, "bucket_lifestream");
        FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluidStack("lifestream", FluidContainerRegistry.BUCKET_VOLUME), new ItemStack(bucketLifeStream), new ItemStack(Items.bucket));
        blockMakoReactor = new EcBlockMakoReactor().setBlockName("makoreactor").setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(tabsEChanger).setBlockTextureName(EcTextureDomain + "makoreactor-side");

        Meteo = new EcEnchantmentMeteo(EnchantChanger.EnchantmentMeteoId, 0).setName("Meteo");
        Holy = new EcEnchantmentHoly(EnchantChanger.EndhantmentHolyId, 0).setName("Holy");
        Telepo = new EcEnchantmentTeleport(EnchantChanger.EnchantmentTelepoId, 0).setName("teleportTo");
        Float = new EcEnchantmentFloat(EnchantChanger.EnchantmentFloatId, 0).setName("Floating");
        Thunder = new EcEnchantmentThunder(EnchantChanger.EnchantmentThunderId, 0).setName("Thunder");
        PacketHandler.init();
        addStatusEffect();

        damageSourceMako = new DamageSource("mako").setDamageBypassesArmor();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        this.initMaps();
        MinecraftForge.EVENT_BUS.register(livingeventhooks);
        FillBucketHook.buckets.put(blockLifeStream, bucketLifeStream);
        MinecraftForge.EVENT_BUS.register(FillBucketHook.INSTANCE);
        FMLCommonHandler.instance().bus().register(this);
        FMLCommonHandler.instance().bus().register(new CommonTickHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
//        packetPipeline.initialise();
//        packetPipeline.registerPacket(KeyHandlingPacket.class);
//        packetPipeline.registerPacket(LevitationPacket.class);
//        packetPipeline.registerPacket(CloudSwordPacket.class);
//        packetPipeline.registerPacket(KeyMateriaWindowPacket.class);
//        packetPipeline.registerPacket(PlayerInfoPacket.class);
        GameRegistry.registerTileEntity(EcTileEntityMaterializer.class,
                "container.materializer");
        GameRegistry.registerTileEntity(EcTileEntityHugeMateria.class,
                "container.hugeMateria");

        EntityRegistry.registerModEntity(EcEntityExExpBottle.class,
                "itemExExpBottle", 0, this, 250, 5, true);
        EntityRegistry.registerModEntity(EcEntityMeteo.class, "Meteo", 1, this,
                250, 5, true);
        EntityRegistry.registerModEntity(EcEntityApOrb.class, "apOrb", 2, this,
                64, 1, false);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.registerRenderInformation();
        proxy.registerTileEntitySpecialRenderer();

        RecipeSorter.register("EnchantChanger:MateriaRecipe", EcMateriaRecipe.class, Category.SHAPELESS, "after:FML");
        RecipeSorter.register("EnchantChanger:MasterMateriaRecipe", EcMasterMateriaRecipe.class, Category.SHAPELESS, "after:FML");
        if (EnchantChanger.Difficulty < 2)
            GameRegistry.addRecipe(new EcMateriaRecipe());
        GameRegistry.addRecipe(new EcMasterMateriaRecipe());
        GameRegistry.addShapelessRecipe(new ItemStack(itemMateria, 1, 0),
                new ItemStack(Items.diamond, 1),
                new ItemStack(Items.ender_pearl, 1));
        GameRegistry.addRecipe(new ItemStack(itemZackSword, 1),
                " X",
                "XX",
                " Y",
                'X', Blocks.iron_block,
                'Y', Items.iron_ingot);
        if (EnchantChanger.Difficulty < 2)
            GameRegistry.addRecipe(
                    new ItemStack(ItemCloudSwordCore, 1),
                    " X ",
                    "XYX",
                    " Z ",
                    'X', Blocks.iron_block,
                    'Y', new ItemStack(itemMateria, 1, 0),
                    'Z', Items.iron_ingot);
        else
            GameRegistry.addRecipe(
                    new ItemStack(ItemCloudSwordCore, 1),
                    " X ",
                    "DYD",
                    " Z ",
                    'X', Blocks.iron_block,
                    'Y', new ItemStack(itemMateria, 1, 0),
                    'Z', Items.iron_ingot,
                    'D', Items.diamond);
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
        if (Difficulty == 0)
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
        if (enableDungeonLoot)
            this.DungeonLootItemResist();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        loadMTH = Loader.isModLoaded("MultiToolHolders");
//        packetPipeline.postInitialise();
    }

    private void initMaps() {
        magicEnchantment.add(EnchantmentMeteoId);
        magicEnchantment.add(EnchantmentFloatId);
        magicEnchantment.add(EndhantmentHolyId);
        magicEnchantment.add(EnchantmentTelepoId);
        magicEnchantment.add(EnchantmentThunderId);
        apLimit.put(0, 2 * aPBasePoint);
        apLimit.put(1, 1 * aPBasePoint);
        apLimit.put(2, 1 * aPBasePoint);
        apLimit.put(3, 1 * aPBasePoint);
        apLimit.put(4, 1 * aPBasePoint);
        apLimit.put(5, 1 * aPBasePoint);
        apLimit.put(6, 1 * aPBasePoint);
        apLimit.put(7, 1 * aPBasePoint);
        apLimit.put(16, 2 * aPBasePoint);
        apLimit.put(17, 1 * aPBasePoint);
        apLimit.put(18, 1 * aPBasePoint);
        apLimit.put(19, 1 * aPBasePoint);
        apLimit.put(20, 1 * aPBasePoint);
        apLimit.put(21, 3 * aPBasePoint);
        apLimit.put(32, 1 * aPBasePoint);
        apLimit.put(33, 1 * aPBasePoint);
        apLimit.put(34, 1 * aPBasePoint);
        apLimit.put(35, 2 * aPBasePoint);
        apLimit.put(48, 2 * aPBasePoint);
        apLimit.put(49, 1 * aPBasePoint);
        apLimit.put(50, 1 * aPBasePoint);
        apLimit.put(51, 1 * aPBasePoint);
        apLimit.put(61, 1 * aPBasePoint);
        apLimit.put(62, 1 * aPBasePoint);
        String[] idAndLimit;
        for (String enchLimit : enchantmentLevelLimits) {
            idAndLimit = enchLimit.split(":");
            levelLimitMap.put(Integer.valueOf(idAndLimit[0]), Integer.valueOf(idAndLimit[1]));
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
        if (item == null || enchantment == null || Lv < 0) {
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
        Vec3 PlayerPosition = world.getWorldVec3Pool().getVecFromPool(
                PlayerposX, PlayerposY, PlayerposZ);
        Vec3 PlayerLookVec = PlayerPosition.addVector(viewX * reach, viewY
                * reach, viewZ * reach);
        MovingObjectPosition MOP = world.rayTraceBlocks(PlayerPosition, PlayerLookVec);
        if (MOP != null)
            distBlock = MOP.hitVec.distanceTo(PlayerPosition);
        List list = world.getEntitiesWithinAABBExcludingEntity(
                player,
                player.boundingBox.addCoord(viewX * reach,
                        viewY * reach, viewZ * reach).expand(
                        (double) var1, (double) var1, (double) var1));
        double dist1 = distBlock;
        for (Object object : list) {
            Entity entity = (Entity) object;

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
        } else return true;
    }

    public static boolean isEnchantmentValid(Enchantment ench, ItemStack par1ItemStack) {
        if (ench == null) {
            return false;
        } else if (ench instanceof EnchantmentDurability) {
            return par1ItemStack.isItemStackDamageable() || ench.type.canEnchantItem(par1ItemStack.getItem());
        } else if (ench instanceof EnchantmentDigging) {
            return par1ItemStack.getItem() == Items.shears || ench.type.canEnchantItem(par1ItemStack.getItem());
        } else if (ench instanceof EnchantmentDamage || ench instanceof EnchantmentLootBonus || ench instanceof EnchantmentFireAspect) {
            return par1ItemStack.getItem() instanceof ItemTool || ench.type.canEnchantItem(par1ItemStack.getItem());
        } else if (ench instanceof EnchantmentThorns) {
            return par1ItemStack.getItem() instanceof ItemArmor || ench.type.canEnchantItem(par1ItemStack.getItem());
        } else if (ench instanceof EnchantmentUntouching) {
            return par1ItemStack.getItem() == Items.shears || ench.type.canEnchantItem(par1ItemStack.getItem());
        } else if (ench instanceof EcEnchantmentMeteo || ench instanceof EcEnchantmentHoly || ench instanceof EcEnchantmentTeleport || ench instanceof EcEnchantmentFloat || ench instanceof EcEnchantmentThunder) {
            return par1ItemStack.getItem() instanceof EcItemSword;
        } else {
            return ench.type.canEnchantItem(par1ItemStack.getItem());
        }
    }
    private void addStatusEffect() {
        if (idMakoPoison < Potion.potionTypes.length) {
            if (Potion.potionTypes[idMakoPoison] == null) {
                potionMako = new EcPotionMako(idMakoPoison).setPotionName("EC|MakoPoison");
            } else {
                throw new IllegalIcuArgumentException("idMakoPoison:id has been used another MOD");
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

    private byte getKeyIndex() {
        byte key = -1;
        if (ClientProxy.MagicKey.isPressed()) {
            key = MagicKEY;
        } else if (ClientProxy.MateriaKey.isPressed()) {
            key = MateriaKEY;
        }

        return key;
    }

    @SubscribeEvent
    public void KeyHandlingEvent(KeyInputEvent event) {
        if (FMLClientHandler.instance().getClient().inGameHasFocus && FMLClientHandler.instance().getClientPlayerEntity() != null) {
            EntityPlayer entityPlayer = FMLClientHandler.instance().getClientPlayerEntity();
            byte keyIndex = getKeyIndex();
            if (keyIndex != -1 && entityPlayer.getCurrentEquippedItem() != null) {
                PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(keyIndex));
                switch(keyIndex) {
                    case EnchantChanger.MagicKEY :doMagic(entityPlayer.getCurrentEquippedItem(), entityPlayer); break;
                }
            }
        }
    }

    private void doMagic(ItemStack itemStack, EntityPlayer player) {
        if (itemStack.getItem() instanceof EcItemSword) {
            EcItemSword.doMagic(itemStack, player.worldObj, player);
        } else if (itemStack.getItem() instanceof ItemMultiToolHolder) {
            //ツールホルダーとの連携処理。
            ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
            if (mth.tools.getStackInSlot(mth.SlotNum) != null
                    && mth.tools.getStackInSlot(mth.SlotNum).getItem() instanceof EcItemSword)
            {
                EcItemSword.doMagic(mth.tools.getStackInSlot(mth.SlotNum), player.worldObj, player);
            }
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
    public void populateLifeStreamLake(PopulateChunkEvent.Post event) {
        if (event.rand.nextInt(30) == 0) {
            int k = event.chunkX * 16;
            int l = event.chunkZ * 16;
            int x,y,z;
            x = k + event.rand.nextInt(16) + 8;
            y = event.rand.nextInt(256);
            z = l + event.rand.nextInt(16) + 8;
            (new WorldGenLakes(blockLifeStream)).generate(event.world, event.rand, x, y, z);
            logger.fine(String.format("LifeStreamLake is generated at (%d, %d, %d)", x, y, z));
        }
    }
}