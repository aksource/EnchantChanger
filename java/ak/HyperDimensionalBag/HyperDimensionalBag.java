package ak.HyperDimensionalBag;

import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

@Mod(modid="HyperDimensionalBag", name="HyperDimensionalBag", version="1.0a",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class HyperDimensionalBag
{
	@Mod.Instance("HyperDimensionalBag")
	public static HyperDimensionalBag instance;
	@SidedProxy(clientSide = "ak.HyperDimensionalBag.ClientProxy", serverSide = "ak.HyperDimensionalBag.CommonProxy")
	public static CommonProxy proxy;
	
	public static int bagID = 30000;
	public static int guiID = 0;
	public static String GuiBagTex ="textures/gui/GuiBag.png";
	public static String TextureDomain = "hyperdimensionalbag:";
	public static String Assets = "hyperdimensionalbag";
	public static boolean loadSB = false;
	public static boolean hardRecipe;
	public static Item HDBag;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		hardRecipe = config.get(Configuration.CATEGORY_GENERAL, "HardRecipe", false).getBoolean(false);
		config.save();
		HDBag = new ItemHDBag().setUnlocalizedName(this.TextureDomain + "Bag").setTextureName(this.TextureDomain + "Bag").setCreativeTab(CreativeTabs.tabTools);
//		GameRegistry.registerItem(HDBag, "hyperdimentionalbag");
		registerItemToId(HDBag, "hyperdimentionalbag", "HyperDimensionalBag", bagID);
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PlayerPickHook());
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		for(int i = 0;i<15;i++)
			GameRegistry.addShapelessRecipe(new ItemStack(HDBag, 1, i), new ItemStack(HDBag, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Items.dye, 1, i));
		if(!hardRecipe)
			GameRegistry.addShapedRecipe(new ItemStack(HDBag, 1, 15), new Object[]{"LDL","DCD","LDL", 'L',Items.leather,'D',Items.diamond,'C',Blocks.chest});
		else
			GameRegistry.addShapedRecipe(new ItemStack(HDBag, 1, 15), new Object[]{"LDL","DCD","LDL", 'L',Items.leather,'D',Items.diamond,'C',Items.nether_star});
		
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		loadSB = Loader.isModLoaded("mod_StorageBox");
	}
	private void registerItemToId(Item item, String name, String modId, int Id)
	{
		Map<UniqueIdentifier, ModContainer> customOwners = ObfuscationReflectionHelper.getPrivateValue(GameData.class, null, 3);
        ModContainer mc = Loader.instance().activeModContainer();
        if (modId != null)
        {
            customOwners.put(new UniqueIdentifier(modId + ":" + name), mc);
        }
        if (item instanceof ItemBlock)
        {
            throw new RuntimeException("Cannot register an itemblock separately from it's block");
        }
        int itemId = GameData.itemRegistry.add(Id, name, item);
        GameData.blockRegistry.useSlot(itemId);
	}
	private void registerBlockToId(Block block, String name, String modId, int Id)
	{
		Map<UniqueIdentifier, ModContainer> customOwners = ObfuscationReflectionHelper.getPrivateValue(GameData.class, null, 3);
        ModContainer mc = Loader.instance().activeModContainer();
        if (modId != null)
        {
            customOwners.put(new UniqueIdentifier(modId + ":" + name), mc);
        }
        int blockId = GameData.blockRegistry.add(Id, name, block);
        GameData.itemRegistry.useSlot(blockId);
	}
}