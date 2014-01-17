package ak.BigItems;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="BigItems", name="BigItems", version="1.0a",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class BigItems
{
	@Mod.Instance("BigItems")
	public static BigItems instance;
	@SidedProxy(clientSide = "ak.BigItems.Client.ClientProxy", serverSide = "ak.BigItems.CommonProxy")
	public static CommonProxy proxy;


	public static String[] ItemIDs;
	public static double Scale;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		ItemIDs = config.get(Configuration.CATEGORY_GENERAL, "Big Item Ids", new String[]{"minecraft:wooden_sword","minecraft:stone_sword","minecraft:iron_sword","minecraft:diamond_sword","minecraft:golden_sword"}, "Put Item Names which you want to make it big.").getStringList();
		Scale = config.get(Configuration.CATEGORY_GENERAL, "Item Scale", 2.0d, "Item Scale").getDouble(2.0d);
		config.save();
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		proxy.registerClientInformation();
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
//		for(int i = 0;i<ItemIDs.length;i++)
//			System.out.println(ItemIDs[i]);
	}
}