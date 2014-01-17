package FarmOptimize;

import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="FarmOptimize", name="FarmOptimize", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class FarmOptimize
{
	@Mod.Instance("FarmOptimize")
	public static FarmOptimize instance;
	
	public static int SugarcaneSpeed;
	public static int SugarcaneLimit;
	public static boolean SugarcaneGrowWater;
	public static boolean SugarcaneUsefulBonemeal;
	public static int SugarcaneUsedBonemealLimit;
	public static int CactusSpeed;
	public static int CactusLimit;
	public static boolean CactusUsefulBonemeal;
	public static int CactusUsedBonemealLimit;
	public static int growSpeedPunpkin;
	public static int growSpeedWaterMelon;
	public static int growSpeedCrops;
	public static int growSpeedCarrot;
	public static int growSpeedPotato;
	public static int growSpeedSapling;
	public static int MushroomSpeed;
	public static int MushroomLimit;
	public static byte MushroomArea;
	public static int growSpeedNetherWart;
	public static int growSpeedCocoa;
	public static int growSpeedVine;
	
	public static Block reed;
	public static Block cactus;
	public static Block pumpkinStem;
	public static Block melonStem;
	public static Block crops;
	public static Block carrot;
	public static Block potato;
	public static Block sapling;
	public static Block mushroomBrown;
	public static Block mushroomRed;
	public static Block netherStalk;
	public static Block cocoaPlant;
	public static Block vine;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		SugarcaneSpeed = config.get(Configuration.CATEGORY_GENERAL, "SugarcaneSpeed", 15, "0:noWait  15:default, min = 0, max = 15").getInt();
		SugarcaneSpeed = (SugarcaneSpeed < 0) ? 0: (SugarcaneSpeed > 15)? 15:SugarcaneSpeed;
		SugarcaneLimit = config.get(Configuration.CATEGORY_GENERAL, "SugarcaneLimit", 3, "min = 1, max = 250").getInt();
		SugarcaneLimit = (SugarcaneLimit < 1) ? 1: (SugarcaneLimit > 250)?250:SugarcaneLimit;
		SugarcaneGrowWater = config.get(Configuration.CATEGORY_GENERAL, "SugarcaneGrowWater", false).getBoolean(false);
		SugarcaneUsefulBonemeal = config.get(Configuration.CATEGORY_GENERAL, "SugarcaneUsefulBonemeal", false).getBoolean(false);
		SugarcaneUsedBonemealLimit = config.get(Configuration.CATEGORY_GENERAL, "SugarcaneUsedBonemealLimit", 3, "min = 1, max = 250").getInt();
		SugarcaneUsedBonemealLimit = (SugarcaneUsedBonemealLimit <1)?1:(SugarcaneUsedBonemealLimit >250)?250:SugarcaneUsedBonemealLimit;
		CactusSpeed = config.get(Configuration.CATEGORY_GENERAL, "CactusSpeed", 15, "0:noWait  15:default, min = 0, max = 15").getInt();
		CactusSpeed = (CactusSpeed <0)?0:(CactusSpeed>15)?15:CactusSpeed;
		CactusLimit = config.get(Configuration.CATEGORY_GENERAL, "CactusLimit", 3, "min = 1, max = 250").getInt();
		CactusLimit = (CactusLimit < 1) ? 1: (CactusLimit > 250)?250:CactusLimit;
		CactusUsefulBonemeal = config.get(Configuration.CATEGORY_GENERAL, "CactusUsefulBonemeal", false).getBoolean(false);
		CactusUsedBonemealLimit = config.get(Configuration.CATEGORY_GENERAL, "CactusUsedBonemealLimit", 3, "min = 1, max = 250").getInt();
		CactusUsedBonemealLimit = (CactusUsedBonemealLimit < 1) ? 1: (CactusUsedBonemealLimit > 250)?250:CactusUsedBonemealLimit;
		growSpeedPunpkin = config.get(Configuration.CATEGORY_GENERAL, "growSpeedPunpkin", 100, "0:noWait  1:fast  100:default, min = 0, max = 100").getInt();
		growSpeedPunpkin = (growSpeedPunpkin <0)?0:(growSpeedPunpkin>100)?100:growSpeedPunpkin;
		growSpeedWaterMelon = config.get(Configuration.CATEGORY_GENERAL, "growSpeedWaterMelon", 100, "0:noWait  1:fast  100:default, min = 0, max = 100").getInt();
		growSpeedWaterMelon = (growSpeedWaterMelon <0)?0:(growSpeedWaterMelon>100)?100:growSpeedWaterMelon;
		growSpeedCrops = config.get(Configuration.CATEGORY_GENERAL, "growSpeedCrops", 100, "0:noWait  1:fast  100:default, min = 0, max = 100").getInt();
		growSpeedCrops = (growSpeedCrops <0)?0:(growSpeedCrops>100)?100:growSpeedCrops;
		growSpeedCarrot = config.get(Configuration.CATEGORY_GENERAL, "growSpeedCarrot", 100, "0:noWait  1:fast  100:default, min = 0, max = 100").getInt();
		growSpeedCarrot = (growSpeedCarrot <0)?0:(growSpeedCarrot>100)?100:growSpeedCarrot;
		growSpeedPotato = config.get(Configuration.CATEGORY_GENERAL, "growSpeedPotato", 100, "0:noWait  1:fast  100:default, min = 0, max = 100").getInt();
		growSpeedPotato = (growSpeedPotato <0)?0:(growSpeedPotato>100)?100:growSpeedPotato;
		growSpeedSapling = config.get(Configuration.CATEGORY_GENERAL, "growSpeedSapling", 7, "0:noWait  1:fast  100:default, min = 0, max = 7").getInt();
		growSpeedSapling = (growSpeedSapling <0)?0:(growSpeedSapling>7)?7:growSpeedSapling;
		MushroomSpeed = config.get(Configuration.CATEGORY_GENERAL, "MushroomSpeed", 25, "0:noWait  1:fast  100:default, min = 0, max = 25").getInt();
		MushroomSpeed = (MushroomSpeed <0)?0:(MushroomSpeed>25)?25:MushroomSpeed;
		MushroomLimit = config.get(Configuration.CATEGORY_GENERAL, "MushroomLimit", 5, "area in mushroomLimit  5:default, min = 1, max = 81").getInt();
		MushroomLimit = (MushroomLimit <1)?1:(MushroomLimit>81)?81:MushroomLimit;
		MushroomArea = (byte) config.get(Configuration.CATEGORY_GENERAL, "MushroomArea", 4, "mushroom search area  4:default, min = 0, max = 4").getInt();
		MushroomArea = (MushroomArea <0)?0:(MushroomArea>4)?4:MushroomArea;
		growSpeedNetherWart = config.get(Configuration.CATEGORY_GENERAL, "growSpeedNetherWart", 10, "0:noWait  1:fast  100:default, min = 0, max = 10").getInt();
		growSpeedNetherWart = (growSpeedNetherWart <0)?0:(growSpeedNetherWart>10)?10:growSpeedNetherWart;
		growSpeedCocoa = config.get(Configuration.CATEGORY_GENERAL, "growSpeedCocoa", 5, "0:noWait  1:fast  100:default, min = 0, max = 5").getInt();
		growSpeedCocoa = (growSpeedCocoa <0)?0:(growSpeedCocoa>5)?5:growSpeedCocoa;
		growSpeedVine = config.get(Configuration.CATEGORY_GENERAL, "growSpeedVine", 4, "0:noWait  4:default  -1:noGrow, min = -1, max = 64").getInt();
		growSpeedVine = (growSpeedVine <-1)?-1:(growSpeedVine>64)?64:growSpeedVine;
		config.save();
		if(SugarcaneSpeed != 15 || SugarcaneLimit != 3 || SugarcaneUsefulBonemeal || SugarcaneGrowWater)
		{
	    	reed = (new foBlockReed()).func_149711_c(0.0F).func_149672_a(Block.field_149779_h).func_149663_c("reeds").func_149658_d("reeds");
			if(SugarcaneUsefulBonemeal) MinecraftForge.EVENT_BUS.register((foBlockReed)reed);
		}
		
		if(CactusSpeed != 15 || CactusLimit != 3 || CactusUsefulBonemeal)
		{
			cactus = (new foBlockCactus()).func_149711_c(0.4F).func_149672_a(Block.field_149775_l).func_149663_c("cactus").func_149658_d("cactus");
			if(CactusUsefulBonemeal) MinecraftForge.EVENT_BUS.register((foBlockCactus)cactus);
		}
		
		if(growSpeedPunpkin != 100)
		{
			pumpkinStem = (new foBlockStem(Block.pumpkin)).func_149711_c(0.0F).func_149672_a(Block.field_149766_f).func_149663_c("pumpkinStem").func_149658_d("melon_stem");
		}
		
		if(growSpeedWaterMelon != 100)
		{
			melonStem = (new foBlockStem(Block.melon)).func_149711_c(0.0F).func_149672_a(Block.field_149766_f).func_149663_c("pumpkinStem").func_149658_d("pumpkin_stem");
		}
		
		if(growSpeedCrops != 100)
		{
			crops = (new foBlockCrops()).func_149663_c("crops").func_149658_d("wheat");
		}
		
		if(growSpeedCarrot != 100)
		{
			carrot = (new foBlockCarrot()).func_149663_c("carrots").func_149658_d("carrots");
		}
		
		if(growSpeedPotato != 100)
		{
			potato = (new foBlockPotato()).func_149663_c("potatoes").func_149658_d("potatoes");
		}
		
		if(growSpeedSapling != 7)
		{
			sapling = (new foBlockSapling()).func_149711_c(0.0F).func_149672_a(Block.field_149779_h).func_149663_c("sapling").func_149658_d("sapling");
		}
		
		if(MushroomSpeed != 25 || MushroomLimit != 5 || MushroomArea != 4)
		{
			mushroomBrown = (new foBlockMushroom())).func_149711_c(0.0F).func_149672_a(Block.field_149779_h).func_149715_a(0.125F).func_149663_c("mushroom").func_149658_d("mushroom_brown");
			mushroomRed = (new foBlockMushroom()).func_149711_c(0.0F).func_149672_a(Block.field_149779_h).func_149663_c("mushroom").func_149658_d("mushroom_red");
		}
		
		if(growSpeedNetherWart != 10)
		{
			netherStalk = (new foBlockNetherStalk()).func_149663_c("netherStalk").func_149658_d("nether_wart");
		}
		
		if(growSpeedCocoa != 5)
		{
			cocoaPlant = (new foBlockCocoa()).func_149711_c(0.2F).func_149752_b(5.0F).func_149672_a(Block.field_149766_f).func_149663_c("cocoa").func_149658_d("cocoa");
		}
		
		if(growSpeedVine != 4)
		{
			vine = (new foBlockVine()).func_149711_c(0.2F).func_149672_a(Block.field_149779_h).func_149663_c("vine").func_149658_d("vine");
		}
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		if(SugarcaneSpeed != 15 || SugarcaneLimit != 3 || SugarcaneUsefulBonemeal || SugarcaneGrowWater)
		{
			if(SugarcaneUsefulBonemeal) MinecraftForge.EVENT_BUS.register((foBlockReed)reed);
		}
		
		if(CactusSpeed != 15 || CactusLimit != 3 || CactusUsefulBonemeal)
		{
			if(CactusUsefulBonemeal) MinecraftForge.EVENT_BUS.register((foBlockCactus)cactus);
		}
	}
}