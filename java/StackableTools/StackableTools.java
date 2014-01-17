package StackableTools;

import java.util.Iterator;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="StackableTools", name="StackableTools", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class StackableTools
{
	@Mod.Instance("StackableTools")
	public static StackableTools instance;
	
	public static int AxeMax;
	public static int PickaxeMax;
	public static int ShovelMax;
	public static int SwordMax;
	public static int HoeMax;
	public static int FrintMax;
	public static int BowMax;
	public static int ShearsMax;
	public static int FishingRodMax;
	public static int ArmorMax;
	public static int WebDamage;
	
	public static Item axeWoodStack;
	public static Item axeStoneStack;
	public static Item axeIronStack;
	public static Item axeDiamondStack;
	public static Item axeGoldStack;
	public static Item pickaxeWoodStack;
	public static Item pickaxeStoneStack;
	public static Item pickaxeIronStack;
	public static Item pickaxeDiamondStack;
	public static Item pickaxeGoldStack;
	public static Item shovelWoodStack;
	public static Item shovelStoneStack;
	public static Item shovelIronStack;
	public static Item shovelDiamondStack;
	public static Item shovelGoldStack;
	public static Item swordWoodStack;
	public static Item swordStoneStack;
	public static Item swordIronStack;
	public static Item swordDiamondStack;
	public static Item swordGoldStack;
	public static Item bowStack;
	public static Item helmetLeatherStack;
	public static Item plateLeatherStack;
	public static Item legsLeatherStack;
	public static Item bootsLeatherStack;
	public static Item helmetChainStack;
	public static Item plateChainStack;
	public static Item legsChainStack;
	public static Item bootsChainStack;
	public static Item helmetIronStack;
	public static Item plateIronStack;
	public static Item legsIronStack;
	public static Item bootsIronStack;
	public static Item helmetDiamondStack;
	public static Item plateDiamondStack;
	public static Item legsDiamondStack;
	public static Item bootsDiamondStack;
	public static Item helmetGoldStack;
	public static Item plateGoldStack;
	public static Item legsGoldStack;
	public static Item bootsGoldStack;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		AxeMax = config.get(Configuration.CATEGORY_GENERAL, "AxeMax", 64, "Axe Max Stack Size, min = 1, max = 64").getInt();
		AxeMax = (AxeMax<1)?1:(AxeMax>64)?64:AxeMax;
		PickaxeMax = config.get(Configuration.CATEGORY_GENERAL, "PickaxeMax", 64, "Pickaxe Max Stack Size, min = 1, max = 64").getInt();
		PickaxeMax = (PickaxeMax<1)?1:(PickaxeMax>64)?64:PickaxeMax;
		ShovelMax = config.get(Configuration.CATEGORY_GENERAL, "ShovelMax", 64, "Shovel Max Stack Size, min = 1, max = 64").getInt();
		ShovelMax = (ShovelMax<1)?1:(ShovelMax>64)?64:ShovelMax;
		SwordMax = config.get(Configuration.CATEGORY_GENERAL, "SwordMax", 64, "Sword Max Stack Size, min = 1, max = 64").getInt();
		SwordMax = (SwordMax<1)?1:(SwordMax>64)?64:SwordMax;
		HoeMax = config.get(Configuration.CATEGORY_GENERAL, "HoeMax", 64, "Hoe Max Stack Size, min = 1, max = 64").getInt();
		HoeMax = (HoeMax<1)?1:(HoeMax>64)?64:HoeMax;
		FrintMax = config.get(Configuration.CATEGORY_GENERAL, "FrintMax", 64, "Flint Max Stack Size, min = 1, max = 64").getInt();
		FrintMax = (FrintMax<1)?1:(FrintMax>64)?64:FrintMax;
		BowMax = config.get(Configuration.CATEGORY_GENERAL, "BowMax", 64, "Bow Max Stack Size, min = 1, max = 64").getInt();
		BowMax = (BowMax<1)?1:(BowMax>64)?64:BowMax;
		ShearsMax = config.get(Configuration.CATEGORY_GENERAL, "ShearsMax", 64, "Shears Max Stack Size, min = 1, max = 64").getInt();
		ShearsMax = (ShearsMax<1)?1:(ShearsMax>64)?64:ShearsMax;
		FishingRodMax = config.get(Configuration.CATEGORY_GENERAL, "FishingRodMax", 64, "FishingRod Max Stack Size, min = 1, max = 64").getInt();
		FishingRodMax = (FishingRodMax<1)?1:(FishingRodMax>64)?64:FishingRodMax;
		ArmorMax = config.get(Configuration.CATEGORY_GENERAL, "ArmorMax", 64, "Armor Max Stack Size, min = 1, max = 64").getInt();
		ArmorMax = (ArmorMax<1)?1:(ArmorMax>64)?64:ArmorMax;
		WebDamage = config.get(Configuration.CATEGORY_GENERAL, "WebDamage", 1, "Damage Destroyed Web with Sword, min = 0, max = 2").getInt();
		WebDamage = (WebDamage<0)?0:(WebDamage>2)?2:WebDamage;
		config.save();
		if(AxeMax != 1)
		{
			this.axeWoodStack 	= new ItemAxeStack(ToolMaterial.WOOD);
			GameRegistry.registerItem(axeWoodStack, "wood_axe_stack");
			this.axeStoneStack 	= new ItemAxeStack(ToolMaterial.STONE);
			GameRegistry.registerItem(axeStoneStack, "stone_axe_stack");
			this.axeIronStack 	= new ItemAxeStack(ToolMaterial.IRON);
			GameRegistry.registerItem(axeIronStack, "iron_axe_stack");
			this.axeDiamondStack = new ItemAxeStack(ToolMaterial.EMERALD);
			GameRegistry.registerItem(axeDiamondStack, "diamond_axe_stack");
			this.axeGoldStack 	= new ItemAxeStack(ToolMaterial.GOLD);
			GameRegistry.registerItem(axeGoldStack, "gold_axe_stack");
		}
		
		if(PickaxeMax != 1)
		{
			this.pickaxeWoodStack 	= new ItemPickaxeStack(ToolMaterial.WOOD);
			GameRegistry.registerItem(pickaxeWoodStack, "wood_pickaxe_stack");
			this.pickaxeStoneStack 	= new ItemPickaxeStack(ToolMaterial.STONE);
			GameRegistry.registerItem(pickaxeStoneStack, "stone_pickaxe_stack");
			this.pickaxeIronStack 	= new ItemPickaxeStack(ToolMaterial.IRON);
			GameRegistry.registerItem(pickaxeIronStack, "iron_pickaxe_stack");
			this.pickaxeDiamondStack = new ItemPickaxeStack(ToolMaterial.EMERALD);
			GameRegistry.registerItem(pickaxeDiamondStack, "diamond_pickaxe_stack");
			this.pickaxeGoldStack 	= new ItemPickaxeStack(ToolMaterial.GOLD);
			GameRegistry.registerItem(pickaxeGoldStack, "gold_pickaxe_stack");
		}
		
		if(ShovelMax != 1)
		{
			this.shovelWoodStack 	= new ItemSpadeStack(ToolMaterial.WOOD);
			GameRegistry.registerItem(shovelWoodStack, "wood_shovel_stack");
			this.shovelStoneStack 	= new ItemSpadeStack(ToolMaterial.STONE);
			GameRegistry.registerItem(shovelStoneStack, "stone_shovel_stack");
			this.shovelIronStack 	= new ItemSpadeStack(ToolMaterial.IRON);
			GameRegistry.registerItem(shovelIronStack, "iron_shovel_stack");
			this.shovelDiamondStack = new ItemSpadeStack(ToolMaterial.EMERALD);
			GameRegistry.registerItem(shovelDiamondStack, "diamond_shovel_stack");
			this.shovelGoldStack 	= new ItemSpadeStack(ToolMaterial.GOLD);
			GameRegistry.registerItem(shovelGoldStack, "gold_shovel_stack");
		}
		
		if(BowMax != 1)
		{
			this.bowStack = (ItemBow)(new ItemBowStack()).setUnlocalizedName("bow");
			GameRegistry.registerItem(bowStack, "bow_stack");
		}
		
		if(SwordMax != 1 || WebDamage != 2)
		{
			this.swordWoodStack		= new ItemSwordStack(ToolMaterial.WOOD);
			GameRegistry.registerItem(swordWoodStack, "wood_sword_stack");
			this.swordStoneStack 	= new ItemSwordStack(ToolMaterial.STONE);
			GameRegistry.registerItem(swordStoneStack, "stone_sword_stack");
			this.swordIronStack 	= new ItemSwordStack(ToolMaterial.IRON);
			GameRegistry.registerItem(swordIronStack, "iron_sword_stack");
			this.swordDiamondStack 	= new ItemSwordStack(ToolMaterial.EMERALD);
			GameRegistry.registerItem(swordDiamondStack, "diamond_sword_stack");
			this.swordGoldStack 	= new ItemSwordStack(ToolMaterial.GOLD);
			GameRegistry.registerItem(swordGoldStack, "gold_sword_stack");
		}
		
		//Diamond装備を直接置き換えないと空き装備スロットのテクスチャがバグる
		if(ArmorMax != 1)
		{
			this.helmetLeatherStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CLOTH, 0, 0);
			GameRegistry.registerItem(helmetLeatherStack, "leather_helmet_stack");
			this.plateLeatherStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CLOTH, 0, 1);
			GameRegistry.registerItem(plateLeatherStack, "leather_plate_stack");
			this.legsLeatherStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CLOTH, 0, 2);
			GameRegistry.registerItem(legsLeatherStack, "leather_legs_stack");
			this.bootsLeatherStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CLOTH, 0, 3);
			GameRegistry.registerItem(bootsLeatherStack, "leather_boots_stack");
			this.helmetChainStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CHAIN, 1, 0);
			GameRegistry.registerItem(helmetChainStack, "chain_helmet_stack");
			this.plateChainStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CHAIN, 1, 1);
			GameRegistry.registerItem(plateChainStack, "chain_plate_stack");
			this.legsChainStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.CHAIN, 1, 2);
			GameRegistry.registerItem(legsChainStack, "chain_legs_stack");
			this.bootsChainStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.CHAIN, 1, 3);
			GameRegistry.registerItem(bootsChainStack, "chain_boots_stack");
			this.helmetIronStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.IRON, 2, 0);
			GameRegistry.registerItem(helmetIronStack, "iron_helmet_stack");
			this.plateIronStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.IRON, 2, 1);
			GameRegistry.registerItem(plateIronStack, "iron_plate_stack");
			this.legsIronStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.IRON, 2, 2);
			GameRegistry.registerItem(legsIronStack, "iron_legs_stack");
			this.bootsIronStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.IRON, 2, 3);
			GameRegistry.registerItem(bootsIronStack, "iron_boots_stack");
			this.helmetDiamondStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.DIAMOND, 3, 0);
			GameRegistry.registerItem(helmetDiamondStack, "diamond_helmet_stack");
			this.plateDiamondStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.DIAMOND, 3, 1);
			GameRegistry.registerItem(plateDiamondStack, "diamond_plate_dtack");
			this.legsDiamondStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.DIAMOND, 3, 2);
			GameRegistry.registerItem(legsDiamondStack, "diamond_legs_stack");
			this.bootsDiamondStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.DIAMOND, 3, 3);
			GameRegistry.registerItem(bootsDiamondStack, "diamond_boots_stack");
			this.helmetGoldStack	= (ItemArmor)new ItemArmorStack(ArmorMaterial.GOLD, 4, 0);
			GameRegistry.registerItem(helmetGoldStack, "gold_helmet_stack");
			this.plateGoldStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.GOLD, 4, 1);
			GameRegistry.registerItem(plateGoldStack, "gold_plate_stack");
			this.legsGoldStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.GOLD, 4, 2);
			GameRegistry.registerItem(legsGoldStack, "gold_legs_stack");
			this.bootsGoldStack		= (ItemArmor)new ItemArmorStack(ArmorMaterial.GOLD, 4, 3);
			GameRegistry.registerItem(bootsGoldStack, "gold_boots_stack");
		}
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		ItemStack[] toolMaterials = new ItemStack[]{
				new ItemStack(Blocks.planks, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(Blocks.cobblestone),
				new ItemStack(Items.iron_ingot),
				new ItemStack(Items.diamond),
				new ItemStack(Items.gold_ingot)
		};
		ItemStack[] armorMaterials = new ItemStack[]{
				new ItemStack(Items.leather),
				new ItemStack(Blocks.fire),
				new ItemStack(Items.iron_ingot),
				new ItemStack(Items.diamond),
				new ItemStack(Items.gold_ingot)
		};
		ItemStack[] outputs, exchanges;
		int i;
		if(AxeMax != 1)
		{
			outputs = new ItemStack[]{
					new ItemStack(this.axeWoodStack),
					new ItemStack(this.axeStoneStack),
					new ItemStack(this.axeIronStack),
					new ItemStack(this.axeDiamondStack),
					new ItemStack(this.axeGoldStack)
			};
			exchanges = new ItemStack[]{
				new ItemStack(Items.wooden_axe),
				new ItemStack(Items.stone_axe),
				new ItemStack(Items.iron_axe),
				new ItemStack(Items.diamond_axe),
				new ItemStack(Items.golden_axe)
			};
			for(i = 0;i < toolMaterials.length;i++){
				DeleteRecipe(exchanges[i]);
				GameRegistry.addShapelessRecipe(exchanges[i], new Object[]{outputs[i]});
				GameRegistry.addRecipe(outputs[i], new Object[]{"aa", "ab"," b",'a',toolMaterials[i],'b',Items.stick});
			}
		}
		
		if(PickaxeMax != 1)
		{
			outputs = new ItemStack[]{
					new ItemStack(this.pickaxeWoodStack),
					new ItemStack(this.pickaxeStoneStack),
					new ItemStack(this.pickaxeIronStack),
					new ItemStack(this.pickaxeDiamondStack),
					new ItemStack(this.pickaxeGoldStack)
			};
			exchanges = new ItemStack[]{
				new ItemStack(Items.wooden_pickaxe),
				new ItemStack(Items.stone_pickaxe),
				new ItemStack(Items.iron_pickaxe),
				new ItemStack(Items.diamond_pickaxe),
				new ItemStack(Items.golden_pickaxe)
			};
			for(i = 0;i < toolMaterials.length;i++){
				DeleteRecipe(exchanges[i]);
				GameRegistry.addShapelessRecipe(exchanges[i], new Object[]{outputs[i]});
				GameRegistry.addRecipe(outputs[i], new Object[]{"aaa", " b "," b ",'a',toolMaterials[i],'b',Items.stick});
			}
		}
		
		if(ShovelMax != 1)
		{
			outputs = new ItemStack[]{
				new ItemStack(this.shovelWoodStack),
				new ItemStack(this.shovelStoneStack),
				new ItemStack(this.shovelIronStack),
				new ItemStack(this.shovelDiamondStack),
				new ItemStack(this.shovelGoldStack)
			};
			exchanges = new ItemStack[]{
					new ItemStack(Items.wooden_shovel),
					new ItemStack(Items.stone_shovel),
					new ItemStack(Items.iron_shovel),
					new ItemStack(Items.diamond_shovel),
					new ItemStack(Items.golden_shovel)
			};
			for(i = 0;i < toolMaterials.length;i++){
				DeleteRecipe(exchanges[i]);
				GameRegistry.addShapelessRecipe(exchanges[i], new Object[]{outputs[i]});
				GameRegistry.addRecipe(outputs[i], new Object[]{"a", "b","b",'a',toolMaterials[i],'b',Items.stick});
			}
		}
		
		if(BowMax != 1)
		{
			DeleteRecipe(new ItemStack(Items.bow));
			GameRegistry.addShapelessRecipe(new ItemStack(Items.bow), new Object[]{new ItemStack(this.bowStack)});
			GameRegistry.addRecipe(new ItemStack(this.bowStack), new Object[]{" TS", "T S", " TS", 'S',Items.string, 'T', Items.stick});
			GameRegistry.addRecipe(new ItemStack(this.bowStack), new Object[]{"ST ", "S T", "ST ", 'S',Items.string, 'T', Items.stick});
		}
		
		if(SwordMax != 1 || WebDamage != 2)
		{
			outputs = new ItemStack[]{
				new ItemStack(this.swordWoodStack),
				new ItemStack(this.swordStoneStack),
				new ItemStack(this.swordIronStack),
				new ItemStack(this.swordDiamondStack),
				new ItemStack(this.swordGoldStack)
			};
			exchanges = new ItemStack[]{
					new ItemStack(Items.wooden_sword),
					new ItemStack(Items.stone_sword),
					new ItemStack(Items.iron_sword),
					new ItemStack(Items.diamond_sword),
					new ItemStack(Items.golden_sword)
			};
			for(i = 0;i < toolMaterials.length;i++){
				DeleteRecipe(exchanges[i]);
				GameRegistry.addShapelessRecipe(exchanges[i], new Object[]{outputs[i]});
				GameRegistry.addRecipe(outputs[i], new Object[]{"a", "a","b",'a',toolMaterials[i],'b',Items.stick});
			}
		}
		
		//Diamond装備を直接置き換えないと空き装備スロットのテクスチャがバグる
		if(ArmorMax != 1)
		{
			ItemStack[] helmetout = new ItemStack[]{
					new ItemStack(this.helmetLeatherStack),
					new ItemStack(this.helmetChainStack),
					new ItemStack(this.helmetIronStack),
					new ItemStack(this.helmetDiamondStack),
					new ItemStack(this.helmetGoldStack)
			};
			ItemStack[] plateout = new ItemStack[]{
					new ItemStack(this.plateLeatherStack),
					new ItemStack(this.plateChainStack),
					new ItemStack(this.plateIronStack),
					new ItemStack(this.plateDiamondStack),
					new ItemStack(this.plateGoldStack)
			};
			ItemStack[] legsout = new ItemStack[]{
					new ItemStack(this.legsLeatherStack),
					new ItemStack(this.legsChainStack),
					new ItemStack(this.legsIronStack),
					new ItemStack(this.legsDiamondStack),
					new ItemStack(this.legsGoldStack)
			};
			ItemStack[] bootsout = new ItemStack[]{
					new ItemStack(this.bootsLeatherStack),
					new ItemStack(this.bootsChainStack),
					new ItemStack(this.bootsIronStack),
					new ItemStack(this.bootsDiamondStack),
					new ItemStack(this.bootsGoldStack)
			};
			ItemStack[] helmetchange = new ItemStack[]{
					new ItemStack(Items.leather_helmet),
					new ItemStack(Items.chainmail_helmet),
					new ItemStack(Items.iron_helmet),
					new ItemStack(Items.diamond_helmet),
					new ItemStack(Items.golden_helmet)
			};
			ItemStack[] platechange = new ItemStack[]{
					new ItemStack(Items.leather_chestplate),
					new ItemStack(Items.chainmail_chestplate),
					new ItemStack(Items.iron_chestplate),
					new ItemStack(Items.diamond_chestplate),
					new ItemStack(Items.golden_chestplate)
			};
			ItemStack[] legschange = new ItemStack[]{
					new ItemStack(Items.leather_leggings),
					new ItemStack(Items.chainmail_leggings),
					new ItemStack(Items.iron_leggings),
					new ItemStack(Items.diamond_leggings),
					new ItemStack(Items.golden_leggings)
			};
			ItemStack[] bootschange = new ItemStack[]{
					new ItemStack(Items.leather_boots),
					new ItemStack(Items.chainmail_boots),
					new ItemStack(Items.iron_boots),
					new ItemStack(Items.diamond_boots),
					new ItemStack(Items.golden_boots)
			};
			for(i = 0; i < armorMaterials.length; i ++){
				DeleteRecipe(helmetchange[i]);
				DeleteRecipe(platechange[i]);
				DeleteRecipe(legschange[i]);
				DeleteRecipe(bootschange[i]);
				GameRegistry.addShapelessRecipe(helmetchange[i], new Object[]{helmetout[i]});
				GameRegistry.addShapelessRecipe(platechange[i], new Object[]{plateout[i]});
				GameRegistry.addShapelessRecipe(legschange[i], new Object[]{legsout[i]});
				GameRegistry.addShapelessRecipe(bootschange[i], new Object[]{bootsout[i]});
				GameRegistry.addRecipe(helmetout[i], new Object[]{"aaa","a a", 'a', armorMaterials[i]});
				GameRegistry.addRecipe(plateout[i], new Object[]{"a a","aaa","aaa",'a',armorMaterials[i]});
				GameRegistry.addRecipe(legsout[i], new Object[]{"aaa","a a","a a",'a',armorMaterials[i]});
				GameRegistry.addRecipe(bootsout[i], new Object[]{"a a","a a",'a', armorMaterials[i]});
			}
		}
		if(FrintMax != 1)
		{
			Items.flint_and_steel.setMaxStackSize(FrintMax).setHasSubtypes(true);
		}
		
		if(FishingRodMax != 1)
		{
			Items.fishing_rod.setMaxStackSize(FishingRodMax).setHasSubtypes(true);
		}
		
		if(ShearsMax != 1)
		{
			Items.shears.setMaxStackSize(ShearsMax).setHasSubtypes(true);
		}
		
		if(HoeMax != 1)
		{
			Items.wooden_hoe.setMaxStackSize(HoeMax).setHasSubtypes(true);
			Items.stone_hoe.setMaxStackSize(HoeMax).setHasSubtypes(true);
			Items.iron_hoe.setMaxStackSize(HoeMax).setHasSubtypes(true);
			Items.diamond_hoe.setMaxStackSize(HoeMax).setHasSubtypes(true);
			Items.golden_hoe.setMaxStackSize(HoeMax).setHasSubtypes(true);
		}
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