package Booster;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="Booster", name="Booster", version="1.7.2v1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"Booster"}, packetHandler=PacketHandler.class)
public class Booster
{
//	public static int BoosterID;
	public static Item Booster08;
	public static Item Booster20;

	public static int BoostPower;

	int CanBoost=BoostPower;

	public static boolean BoosterDefaultSwitch;

	public static boolean Alwaysflying = false;


	public static double movement =1d;
	public static String TextureDomain = "booster:";
	public static String Armor08_1 = "textures/armor/AR08_1.png";
	public static String Armor08_2 = "textures/armor/AR08_2.png";
	public static String Armor20_1 = "textures/armor/AR20_1.png";
	public static String Armor20_2 = "textures/armor/AR20_2.png";
	public static LivingEventHooks livingeventhooks;
	public static final PacketPipeline packetPipeline = new PacketPipeline();

	@Mod.Instance("Booster")
	public static Booster instance;
	@SidedProxy(clientSide = "Booster.ClientProxy", serverSide = "Booster.CommonProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
//		BoosterID = config.get(Configuration.CATEGORY_ITEM, "BoosterID", 20000).getInt();
		BoostPower = config.get(Configuration.CATEGORY_GENERAL, "BoostPower", 25).getInt();
		BoosterDefaultSwitch = config.get(Configuration.CATEGORY_GENERAL, "BoostPower", true).getBoolean(true);
		Alwaysflying = config.get(Configuration.CATEGORY_GENERAL, "Alwaysflying", false).getBoolean(false);
		movement = config.get(Configuration.CATEGORY_GENERAL, "movement", 1d).getDouble(1);
		config.save();
		Booster08 = new ItemBooster(ItemArmor.ArmorMaterial.IRON ,2,1, "Booster08").setUnlocalizedName(TextureDomain + "Booster08").setCreativeTab(CreativeTabs.tabCombat);
		GameRegistry.registerItem(Booster08, "booster08");
		Booster20 = new ItemBooster(ItemArmor.ArmorMaterial.DIAMOND ,3,1, "Booster20").setUnlocalizedName(TextureDomain + "Booster20").setCreativeTab(CreativeTabs.tabCombat);
		GameRegistry.registerItem(Booster20, "booster20");
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.registerClientInformation();

		livingeventhooks = new LivingEventHooks();
		MinecraftForge.EVENT_BUS.register(livingeventhooks);
		packetPipeline.initialise();
		GameRegistry.addRecipe(new ItemStack(Booster08),
				new Object[]{ "XRX","XPX","X X",
			Character.valueOf('X'),Items.iron_ingot,
			Character.valueOf('R'),Items.repeater,
			Character.valueOf('P'),Blocks.piston});

		GameRegistry.addRecipe(new ItemStack(Booster20),
				new Object[]{ "I I"," B ","IDI",
			Character.valueOf('B'),Booster08,
			Character.valueOf('I'),Items.iron_ingot,
			Character.valueOf('D'),Items.diamond});
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		packetPipeline.postInitialise();
	}
}