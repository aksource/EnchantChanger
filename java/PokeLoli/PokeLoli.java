package PokeLoli;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="PokeLoli", name="PokeLoli", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class PokeLoli
{
	@Mod.Instance("PokeLoli")
	public static PokeLoli instance;
	public static Item pokeEgg;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		pokeEgg = (new plItemMobegg()).setUnlocalizedName("monsterPlacer").setTextureName("spawn_egg");
		GameRegistry.registerItem(pokeEgg, "monster_placer_custom");
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
//		MinecraftForge.EVENT_BUS.register(new PokeLoliEventHandler());
		LanguageRegistry.addName(pokeEgg, "Mob Egg");
		LanguageRegistry.instance().addNameForObject(pokeEgg, "ja_JP", "モブエッグ");
		GameRegistry.addShapelessRecipe(new ItemStack(pokeEgg, 1, 0), new Object[]{Items.egg, Items.redstone});
	}
}