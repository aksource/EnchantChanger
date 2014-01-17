package LilypadBreed;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="LilypadBreed", name="LilypadBreed", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class LilypadBreed
{
	@Mod.Instance("LilypadBreed")
	public static LilypadBreed instance;

	public static int LilypadRate = 25;
	public static Block waterlily;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		LilypadRate = config.get(Configuration.CATEGORY_GENERAL, "LilypadRate", 25, "LilyPad Spown Rate(0%-100%), min = 0, max = 100").getInt();
		LilypadRate = (LilypadRate <0)?0:(LilypadRate>100)?100:LilypadRate;
		config.save();
		waterlily = (new BlockLilypadBreed()).func_149711_c(0.0F).func_149672_a(Block.field_149779_h).func_149663_c("waterlily").func_149658_d("waterlily");
		GameRegistry.registerBlock(waterlily, ItemLilypadBreed.class, "waterlily");
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new WaterlilyChangeEvent());
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.waterlily), new Object[]{new ItemStack(this.waterlily)});
	}
	public class WaterlilyChangeEvent
	{
		@SubscribeEvent
		public void waterlilyHarvest(HarvestDropsEvent event)
		{
			if(event.block == Blocks.waterlily){
				event.drops.clear();
				event.drops.add(new ItemStack(waterlily));
			}
		}
	}
}