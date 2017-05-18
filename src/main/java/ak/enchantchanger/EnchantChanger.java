package ak.enchantchanger;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.api.MakoUtils;
import ak.enchantchanger.capability.CapabilityEventHook;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.eventhandler.CommonTickHandler;
import ak.enchantchanger.eventhandler.FillBucketHook;
import ak.enchantchanger.eventhandler.GenerateHandler;
import ak.enchantchanger.eventhandler.LivingEventHooks;
import ak.enchantchanger.modcoop.CoopMCE;
import ak.enchantchanger.network.PacketHandler;
import ak.enchantchanger.utils.Blocks;
import ak.enchantchanger.utils.EnchantmentUtils;
import ak.enchantchanger.utils.Items;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.logging.Logger;

import static ak.enchantchanger.Recipes.registerRecipes;
import static ak.enchantchanger.utils.ConfigurationUtils.enableDungeonLoot;
import static ak.enchantchanger.utils.ConfigurationUtils.initConfig;
import static ak.enchantchanger.utils.RegistrationUtils.*;

@Mod(modid = Constants.MOD_ID,
        name = Constants.MOD_NAME,
        version = Constants.MOD_VERSION,
        dependencies = "required-after:forge",
        useMetadata = true,
        acceptedMinecraftVersions = Constants.MOD_MC_VERSION)
public class EnchantChanger {

    public static final LivingEventHooks livingeventhooks = new LivingEventHooks();
    //Logger
    public static final Logger logger = Logger.getLogger(EnchantChanger.class.getSimpleName());
    public static Potion potionMako;
    public static DamageSource damageSourceMako;
    public static boolean loadMTH = false;
    public static boolean loadBC = false;
    public static boolean loadIC = false;
    public static boolean loadTE = false;
    public static boolean loadUE = false;
    public static boolean loadMCE = false;
    public static boolean loadSS = false;
    @Mod.Instance(Constants.MOD_ID)
    public static EnchantChanger instance;
    @SidedProxy(clientSide = "ak.enchantchanger.client.ClientProxy", serverSide = "ak.enchantchanger.CommonProxy")
    public static CommonProxy proxy;

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(
                event.getSuggestedConfigurationFile());
        initConfig(config);
        Blocks.init();
        Items.init();
        registerBlockAndItem();
        proxy.registerPreRenderInformation();
        PacketHandler.init();
        addStatusEffect();
        damageSourceMako = new DamageSource("mako").setDamageBypassesArmor();
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        // レビテト等のイベントフック登録
        MinecraftForge.EVENT_BUS.register(livingeventhooks);
        // ライフストリーム登録
        FillBucketHook.buckets.put(Blocks.blockLifeStream, ak.enchantchanger.utils.Items.itemBucketLifeStream);
        // バケツ汲み取りイベント登録
        MinecraftForge.EVENT_BUS.register(FillBucketHook.INSTANCE);
        // 携帯エンチャントテーブルイベント登録
        MinecraftForge.EVENT_BUS.register(ak.enchantchanger.utils.Items.itemPortableEnchantmentTable);
        // キーイベント登録
        MinecraftForge.EVENT_BUS.register(proxy);
        // 共通TickHandler登録
        MinecraftForge.EVENT_BUS.register(new CommonTickHandler());
        // ライフストリームの地形生成登録
        MinecraftForge.TERRAIN_GEN_BUS.register(new GenerateHandler());
        // キャパビリティーイベントの登録
        MinecraftForge.EVENT_BUS.register(new CapabilityEventHook());
        // キャパビリティーの登録
        CapabilityPlayerStatusHandler.register();

        // TileEntity登録
        registerTileEntities();

        // Entity登録
        registerEntities(this);

        // GuiHandler登録
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
        // TESR登録
        proxy.registerTileEntitySpecialRenderer();

        // レシピ登録
        registerRecipes();
        // 描画系登録
        proxy.registerRenderInformation();
        if (enableDungeonLoot) {
            // チェストへのアイテム登録
            MinecraftForge.EVENT_BUS.register(new GenerateHandler());
        }
        loadMTH = Loader.isModLoaded(Constants.MOD_ID_MULTITOOLHOLDERS);
        loadMCE = Loader.isModLoaded(Constants.MOD_ID_MCECONOMY_2);
        loadSS = Loader.isModLoaded(Constants.MOD_ID_SEXTIARYSECTOR);
        loadTE = Loader.isModLoaded(Constants.MOD_ID_COFHCORE);
        if (loadMCE) {
            // MCEとの連携用イベント登録
            MinecraftForge.EVENT_BUS.register(new CoopMCE());
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void imcEvent(FMLInterModComms.IMCEvent event) {
        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
            if (message.key.equals("register_extra_materia") && message.isNBTMessage()) {
                // 他MODの追加エンチャント連携用処理
                proxy.registerExtraMateriaRendering(message.getNBTValue());
            }
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        EnchantmentUtils.initMaps();
        MakoUtils.init();
    }

}