package ak.MultiToolHolders;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.config.Configuration;
import ak.MultiToolHolders.Client.ClientProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="MultiToolHolders", name="MultiToolHolders", version="1.2e",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"MTH|Tool"}, packetHandler=PacketHandler.class)
public class MultiToolHolders
{
	public static  Item ItemMultiToolHolder3;
	public static  Item ItemMultiToolHolder5;
	public static  Item ItemMultiToolHolder7;
	public static  Item ItemMultiToolHolder9;
	public static boolean Debug;

	public static String GuiToolHolder3 ="textures/gui/ToolHolder3.png";
	public static String GuiToolHolder5 ="textures/gui/ToolHolder5.png";
	public static String GuiToolHolder7 ="textures/gui/ToolHolder7.png";
	public static String GuiToolHolder9 ="textures/gui/ToolHolder9.png";
	public static String TextureDomain = "multitoolholders:";
	public static String Assets = "multitoolholders";

	public static String itemTexture = "textures/items.png";


	@Mod.Instance("MultiToolHolders")
	public static MultiToolHolders instance;
	@SidedProxy(clientSide = "ak.MultiToolHolders.Client.ClientProxy", serverSide = "ak.MultiToolHolders.CommonProxy")
	public static CommonProxy proxy;
	public static int guiIdHolder3 = 0;
	public static int guiIdHolder5 = 1;
	public static int guiIdHolder9 = 2;
	public static int guiIdHolder7 = 3;
	
	public static final PacketPipeline packetPipeline = new PacketPipeline();


	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		Debug = config.get(Configuration.CATEGORY_GENERAL, "Debug mode", false, "For Debugger").getBoolean(false);

		config.save();
		ItemMultiToolHolder3 = (new ItemMultiToolHolder(3)).setUnlocalizedName(this.TextureDomain + "Holder3").setCreativeTab(CreativeTabs.tabTools);
		GameRegistry.registerItem(ItemMultiToolHolder3, "itemmultitoolholder3");
		ItemMultiToolHolder5 = (new ItemMultiToolHolder(5)).setUnlocalizedName(this.TextureDomain + "Holder5").setCreativeTab(CreativeTabs.tabTools);
		GameRegistry.registerItem(ItemMultiToolHolder5, "itemmultitoolholder5");
		ItemMultiToolHolder9 = (new ItemMultiToolHolder(9)).setUnlocalizedName(this.TextureDomain + "Holder9").setCreativeTab(CreativeTabs.tabTools);
		GameRegistry.registerItem(ItemMultiToolHolder9, "itemmultitoolholder9");
		ItemMultiToolHolder7 = (new ItemMultiToolHolder(7)).setUnlocalizedName(this.TextureDomain + "Holder7").setCreativeTab(CreativeTabs.tabTools);
		GameRegistry.registerItem(ItemMultiToolHolder7, "itemmultitoolholder7");
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		proxy.registerClientInformation();
		proxy.registerTileEntitySpecialRenderer();
		packetPipeline.initialise();
		FMLCommonHandler.instance().bus().register(this);
		GameRegistry.addRecipe(new ItemStack(ItemMultiToolHolder3), new Object[]{"AAA","ABA", "CCC", Character.valueOf('A'), Items.iron_ingot,Character.valueOf('B'),Blocks.chest, Character.valueOf('C'),Blocks.tripwire});
		GameRegistry.addRecipe(new ItemStack(ItemMultiToolHolder7), new Object[]{"AAA","ABA", "CCC", Character.valueOf('A'), Items.gold_ingot,Character.valueOf('B'),Blocks.chest, Character.valueOf('C'),Blocks.tripwire});
		GameRegistry.addRecipe(new ItemStack(ItemMultiToolHolder9), new Object[]{"AAA","ABA", "CCC", Character.valueOf('A'), Items.diamond,Character.valueOf('B'),Blocks.chest, Character.valueOf('C'),Blocks.tripwire});
		GameRegistry.addRecipe(new ItemStack(ItemMultiToolHolder5), new Object[]{"AAA","ABA", "CCC", Character.valueOf('A'), new ItemStack(Items.dye,1,4),Character.valueOf('B'),Blocks.chest, Character.valueOf('C'),Blocks.tripwire});
		if(this.Debug)
			DebugSystem();
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
//		AddLocalization();
	}
	@SubscribeEvent
	public void KeyPressEvent(KeyInputEvent event)
	{
		if (ClientProxy.OpenKey.isPressed()) {
			ItemMultiToolHolder.OpenKeydown = true;
		}
		if (ClientProxy.NextKey.isPressed()) {
			ItemMultiToolHolder.NextKeydown = true;
		}
		if (ClientProxy.PrevKey.isPressed()) {
			ItemMultiToolHolder.PrevKeydown = true;
		}
	}
	public void DungeonLootItemResist()
	{
		WeightedRandomChestContent Chest;

		ItemStack[] items = new ItemStack[]{new ItemStack(ItemMultiToolHolder3),new ItemStack(ItemMultiToolHolder5),new ItemStack(ItemMultiToolHolder7),new ItemStack(ItemMultiToolHolder9)};
		int[] weights = new int[]{10,5,3,1};
		for (int i= 0;i<items.length;i++)
		{
			Chest = new WeightedRandomChestContent(items[i], 0, 1, weights[i]);;
			ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR, Chest);
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST, Chest);
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST, Chest);
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER, Chest);
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR, Chest);
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY, Chest);
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING, Chest);
			ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH, Chest);
			ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, Chest);
			ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, Chest);
		}
	}
	public void DebugSystem()
	{

	}

}