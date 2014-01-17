package MergeEnchantment;

import net.minecraft.item.crafting.CraftingManager;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

@Mod(modid="MergeEnchantment", name="MergeEnchantment", version="1.2b",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class MergeEnchantment
{
	@Mod.Instance("MergeEnchantment")
	public static MergeEnchantment instance;
	
	public static CraftingManager cm;
	
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		cm = CraftingManager.getInstance();
		cm.getRecipeList().add(new AddEnchantmentRecipes());
		cm.getRecipeList().add(new MergeEnchantmentRecipes());
	}
}