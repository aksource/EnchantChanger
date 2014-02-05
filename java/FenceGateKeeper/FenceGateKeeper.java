package FenceGateKeeper;

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="FenceGateKeeper", name="FenceGateKeeper", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class FenceGateKeeper
{
	@Mod.Instance("FenceGateKeeper")
	public static FenceGateKeeper instance;
	public static Block fenceGate;
	@Mod.EventHandler
	public void preInti(FMLPreInitializationEvent event)
	{
		fenceGate = new kpBlockFenceGate().setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setBlockName("fenceGateCustom");
	}
	public void load(FMLInitializationEvent event)
	{
		GameRegistry.registerBlock(fenceGate, "fence_gate_custom");
//		DeleteRecipe(new ItemStack(Blocks.fence_gate, 1));
//		GameRegistry.addRecipe(new ItemStack(this.fenceGate, 1), new Object[] {"#W#", "#W#", '#', Items.stick, 'W', Blocks.planks});
		GameRegistry.addShapelessRecipe(new ItemStack(Blocks.fence_gate), new Object[]{new ItemStack(fenceGate)});
		GameRegistry.addShapelessRecipe(new ItemStack(fenceGate), new Object[]{new ItemStack(Blocks.fence_gate)});
	}
	public static void DeleteRecipe(ItemStack par1ItemStack)
	{
		List recipes = CraftingManager.getInstance().getRecipeList();

		for(Iterator i = recipes.listIterator(); i.hasNext();)
		{
			IRecipe recipe = (IRecipe)i.next();
			ItemStack is = recipe.getRecipeOutput();

			if(is != null)
			{
				if(GameRegistry.findUniqueIdentifierFor(is.getItem()).equals(GameRegistry.findUniqueIdentifierFor(par1ItemStack.getItem())))
				{
					i.remove();
				}
			}
		}
	}
}