package StoneCutter;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="StoneCutter", name="StoneCutter", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class StoneCutter
{
	@Mod.Instance("StoneCutter")
	public static StoneCutter instance;
	public static ItemStoneCutter cutter;
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event)
	{
		//アイテムの登録はpreInitで行う必要あり。
		cutter = (ItemStoneCutter)(new ItemStoneCutter()).setUnlocalizedName("cutter").setTextureName("shears").setCreativeTab(CreativeTabs.tabTools);
		//1.7以降はこのメソッドで登録。
		GameRegistry.registerItem(cutter, "stone_cutter");
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		//onCraftingがイベント処理に変わったので、FMLに登録。
		FMLCommonHandler.instance().bus().register(cutter);
		//LaguageRegistryは動いていないとの噂。Langファイルの利用を推奨。
		LanguageRegistry.addName(cutter, "StoneCutter");
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone_slab, 2, 3),
				new ItemStack(Blocks.cobblestone), new ItemStack(cutter,1,32767));
	}
}