package DigBedrock;

import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="DigBedrock", name="DigBedrock", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class DigBedrock
{
	@Mod.Instance("DigBedrock")
	public static DigBedrock instance;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Blocks.bedrock.func_149711_c(250.0F).setHarvestLevel("pickaxe", 3);;
	}
}