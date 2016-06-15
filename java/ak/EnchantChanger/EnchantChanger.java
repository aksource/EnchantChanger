package ak.EnchantChanger;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.api.MakoUtils;
import ak.EnchantChanger.block.EcBlockHugeMateria;
import ak.EnchantChanger.block.EcBlockLifeStreamFluid;
import ak.EnchantChanger.block.EcBlockMakoReactor;
import ak.EnchantChanger.block.EcBlockMaterializer;
import ak.EnchantChanger.eventhandler.*;
import ak.EnchantChanger.item.*;
import ak.EnchantChanger.modcoop.CoopMCE;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.utils.EnchantmentUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.logging.Logger;

import static ak.EnchantChanger.Recipes.registerRecipes;
import static ak.EnchantChanger.api.Constants.LIFESTREAM_FLOW_RL;
import static ak.EnchantChanger.api.Constants.LIFESTREAM_STILL_RL;
import static ak.EnchantChanger.utils.ConfigurationUtils.enableDungeonLoot;
import static ak.EnchantChanger.utils.ConfigurationUtils.initConfig;
import static ak.EnchantChanger.utils.RegistrationUtils.*;

@Mod(modid = Constants.MOD_ID,
        name = Constants.MOD_NAME,
        version = Constants.MOD_VERSION,
        updateJSON = "",
        dependencies = "required-after:PotionExtensionCore",
        useMetadata = true,
        acceptedMinecraftVersions = Constants.MOD_MC_VERSION)
public class EnchantChanger {

    public static final LivingEventHooks livingeventhooks = new LivingEventHooks();
    //Logger
    public static final Logger logger = Logger.getLogger(EnchantChanger.class.getSimpleName());
    public static Item itemExExpBottle;
    public static Item itemMateria;
    public static Item itemZackSword;
    public static Item itemCloudSword;
    public static Item itemCloudSwordCore;
    public static Item itemSephirothSword;
    public static Item itemUltimateWeapon;
    public static Item itemPortableEnchantChanger;
    public static Item itemPortableEnchantmentTable;
    public static Item itemMasterMateria;
    public static Item itemImitateSephirothSword;
    public static Block blockEnchantChanger;
    public static Block blockHugeMateria;
    public static Item itemHugeMateria;
    public static Block blockLifeStream;
    public static Fluid fluidLifeStream;
    public static Item itemBucketLifeStream;
    public static Block blockMakoReactor;
    public static Potion potionMako;
    public static DamageSource damageSourceMako;
    public static Material materialMako = new MaterialLiquid(MapColor.grassColor);
    public static boolean loadMTH = false;
    public static boolean loadBC = false;
    public static boolean loadIC = false;
    public static boolean loadTE = false;
    public static boolean loadUE = false;
    public static boolean loadMCE = false;
    public static boolean loadSS = false;
    @Mod.Instance("EnchantChanger")
    public static EnchantChanger instance;
    @SidedProxy(clientSide = "ak.EnchantChanger.Client.ClientProxy", serverSide = "ak.EnchantChanger.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        initConfig(config);

        itemMateria = (new EcItemMateria("Materia")).setHasSubtypes(true).setMaxDamage(0)/*.setTextureName("ender_pearl")*/;
        itemExExpBottle = new EcItemExExpBottle("ExExpBottle");
        itemZackSword = (new EcItemZackSword("ZackSword")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemCloudSwordCore = (new EcItemCloudSwordCore("CloudSwordCore")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14);
        itemCloudSword = (new EcItemCloudSword("CloudSword")).setMaxDamage(Item.ToolMaterial.IRON.getMaxUses() * 14).setCreativeTab(null);
        itemSephirothSword = (new EcItemSephirothSword("MasamuneBlade")).setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 2);
        itemUltimateWeapon = (new EcItemUltimateWeapon("UltimateWeapon")).setMaxDamage(Item.ToolMaterial.EMERALD.getMaxUses() * 14);
        itemPortableEnchantChanger = (new EcItemMaterializer("PortableEnchantChanger"));
        itemPortableEnchantmentTable = (new EcItemEnchantmentTable("PortableEnchantmentTable"));
        itemMasterMateria = new EcItemMasterMateria("itemMasterMateria")/*.setTextureName("ender_pearl")*/.setHasSubtypes(true).setMaxDamage(0).setMaxStackSize(1);
        itemImitateSephirothSword = (new EcItemSephirothSwordImit("ImitateMasamuneBlade"))/*.setTextureName(Constants.EcTextureDomain + "MasamuneBlade")*/;
        blockEnchantChanger = (new EcBlockMaterializer()).setUnlocalizedName("EnchantChanger").setCreativeTab(Constants.TAB_ENCHANT_CHANGER)/*.setBlockTextureName(Constants.EcTextureDomain + "EnchantChanger-top")*/.setHardness(5.0f).setResistance(2000.0f).setLightOpacity(0);
        blockHugeMateria = new EcBlockHugeMateria().setHardness(5.0f).setResistance(2000.0f).setLightLevel(1.0f).setLightOpacity(0).setUnlocalizedName("blockHugeMateria")/*.setBlockTextureName("glass")*/;
        itemHugeMateria = new EcItemHugeMateria("HugeMateria");
        fluidLifeStream = new Fluid("lifestream", LIFESTREAM_STILL_RL, LIFESTREAM_FLOW_RL).setLuminosity(15);
        FluidRegistry.registerFluid(fluidLifeStream);
        blockLifeStream = new EcBlockLifeStreamFluid(fluidLifeStream, materialMako).setUnlocalizedName("lifestream");
        blockMakoReactor = new EcBlockMakoReactor().setUnlocalizedName("makoreactor").setHardness(5.0f).setResistance(10.0f).setStepSound(Block.soundTypeMetal).setCreativeTab(Constants.TAB_ENCHANT_CHANGER)/*.setBlockTextureName(Constants.EcTextureDomain + "makoreactor-side")*/;
        itemBucketLifeStream = new EcItemBucketLifeStream(blockLifeStream, "bucket_lifestream").setContainerItem(Items.bucket).setCreativeTab(Constants.TAB_ENCHANT_CHANGER);

        registerBlockAndItem();
        registerEnchantments();
        proxy.registerPreRenderInformation();
        PacketHandler.init();
        addStatusEffect();
        damageSourceMako = new DamageSource("mako").setDamageBypassesArmor();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(livingeventhooks);
        FillBucketHook.buckets.put(blockLifeStream, itemBucketLifeStream);
        MinecraftForge.EVENT_BUS.register(FillBucketHook.INSTANCE);
        MinecraftForge.EVENT_BUS.register(itemPortableEnchantmentTable);
        FMLCommonHandler.instance().bus().register(proxy);
        PlayerCustomDataHandler playerCustomDataHandler = new PlayerCustomDataHandler();
        MinecraftForge.EVENT_BUS.register(playerCustomDataHandler);
        FMLCommonHandler.instance().bus().register(playerCustomDataHandler);
        FMLCommonHandler.instance().bus().register(new CommonTickHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new GenerateHandler());

        registerTileEntities();

        registerEntities(this);

        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        proxy.registerTileEntitySpecialRenderer();

        registerRecipes();

        proxy.registerRenderInformation();
        if (enableDungeonLoot) {
            GenerateHandler.DungeonLootItemResist();
        }
        loadMTH = Loader.isModLoaded("MultiToolHolders");
        loadMCE = Loader.isModLoaded("mceconomy2");
        loadSS = Loader.isModLoaded("SextiarySector");
        loadTE = Loader.isModLoaded("CoFHCore");
        if (loadMCE) {
            MinecraftForge.EVENT_BUS.register(new CoopMCE());
        }
    }

    @Mod.EventHandler
    public void imcEvent(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equals("registerExtraMateria") && message.isNBTMessage()) {
                proxy.registerExtraMateriaRendering(message.getNBTValue());
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        EnchantmentUtils.initMaps();
        MakoUtils.init();
    }

}