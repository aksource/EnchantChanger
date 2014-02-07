package LilypadBreed;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.BonemealEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "LilypadBreed", name = "LilypadBreed", version = "1.7srg-1", dependencies = "required-after:FML")
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
		LilypadRate = config.get(Configuration.CATEGORY_GENERAL, "LilypadRate", 25,
				"LilyPad Spown Rate(0%-100%), min = 0, max = 100").getInt();
		LilypadRate = (LilypadRate < 0) ? 0 : (LilypadRate > 100) ? 100 : LilypadRate;
		config.save();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void BreedEvent(BonemealEvent event) {

		for (int x = -2 + event.x; x < 3 + event.x; x++) {
			for (int z = -2 + event.z; z < 3 + event.z; z++) {
				if (event.world.getBlock(x, event.y - 1, z) == Blocks.water
						&& event.world.getBlock(x, event.y, z) == Blocks.air)
				{
					if (x == event.x && z == event.z
							|| event.world.rand.nextInt(100) < LilypadBreed.LilypadRate)
					{
						event.world.setBlock(x, event.y, z, Blocks.waterlily);
					}
				}
			}
		}
		event.setResult(Result.ALLOW);
	}
}