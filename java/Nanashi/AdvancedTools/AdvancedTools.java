package Nanashi.AdvancedTools;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

@Mod(modid = "AdvancedTools", name = "AdvancedTools", version = "2.0v-Unofficial", dependencies = "required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class AdvancedTools
{
	public static int UGTools_DestroyRangeLV;
	public static int UGTools_SaftyCounter;
	public static String[] addBlockForPickaxe;
	public static String[] addBlockForShovel;
	public static String[] addBlockForAxe;
	public static boolean spawnHiGradeMob;
	public static boolean hasMultiToolHolder;
	public static boolean dropGather;

	public static boolean spawnFireZombie;
	public static boolean spawnGoldCreeper;
	public static boolean spawnHighSkeleton;
	public static boolean spawnHighSpeedCreeper;
	public static boolean spawnSkeletonSniper;
	public static boolean spawnZombieWarrior;

	public static Item RedEnhancer;
	public static Item BlueEnhancer;
	public static Item UGWoodShovel;
	public static Item UGStoneShovel;
	public static Item UGIronShovel;
	public static Item UGDiamondShovel;
	public static Item UGGoldShovel;
	public static Item UGWoodPickaxe;
	public static Item UGStonePickaxe;
	public static Item UGIronPickaxe;
	public static Item UGDiamondPickaxe;
	public static Item UGGoldPickaxe;
	public static Item UGWoodAxe;
	public static Item UGStoneAxe;
	public static Item UGIronAxe;
	public static Item UGDiamondAxe;
	public static Item UGGoldAxe;
	public static Item BlazeBlade;
	public static Item IceHold;
	public static Item AsmoSlasher;
	public static Item PlanetGuardian;
	public static Item StormBringer;
	public static Item NEGI;
	public static Item LuckLuck;
	public static Item SmashBat;
	public static Item DevilSword;
	public static Item HolySaber;
	public static Item ThrowingKnife;
	public static Item PoisonKnife;
	public static Item CrossBow;
	public static Item InfiniteSword;
	public static Item InfinitePickaxe;
	public static Item InfiniteAxe;
	public static Item InfiniteShovel;
	public static Item InfiniteHoe;
	public static Item GenocideBlade;

	public static String textureDomain = "advancedtools:";
	public static String textureassets = "advancedtools";

	@Mod.Instance("AdvancedTools")
	public static AdvancedTools instance;
	@SidedProxy(clientSide = "Nanashi.AdvancedTools.client.ClientProxy", serverSide = "Nanashi.AdvancedTools.CommonProxy")
	public static CommonProxy proxy;
	public static final CreativeTabs tabsAT = new CreativeTabAT("AdvancedTools");
	public static ArrayList<Item> list = new ArrayList<Item>();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		UGTools_DestroyRangeLV = config.get(Configuration.CATEGORY_GENERAL, "Destroy Range Lv", 1).getInt();
		UGTools_SaftyCounter = config.get(Configuration.CATEGORY_GENERAL, "Safty Counter", 100).getInt();
		spawnHiGradeMob = config.get(Configuration.CATEGORY_GENERAL, "Spawn Hi-Grade Mobs", true).getBoolean(true);
		dropGather = config.get(Configuration.CATEGORY_GENERAL, "dropGather", false,
				"drop block gather under player's foot").getBoolean(false);
		addBlockForPickaxe = config.get(
				Configuration.CATEGORY_GENERAL,
				"blocklistForUGPickaxe",
				new String[] { "minecraft:diamond_ore", "minecraft:gold_ore", "minecraft:iron_ore",
						"minecraft:coal_ore", "minecraft:lapis_ore", "minecraft:redstone_ore",
						"minecraft:lit_redstone_ore", "minecraft:quartz_ore", "minecraft:emerald_ore" })
				.getStringList();
		addBlockForShovel = config.get(Configuration.CATEGORY_GENERAL, "blocklistForUGShovel",
				new String[] { "minecraft:clay", "minecraft:gravel" }).getStringList();
		addBlockForAxe = config.get(Configuration.CATEGORY_GENERAL, "blocklistForUGAxe",
				new String[] { "minecraft:log", "minecraft:log2" }).getStringList();

		spawnFireZombie = config.get("Mob Spawn Setting", "FireZombie", true).getBoolean(true);
		spawnGoldCreeper = config.get("Mob Spawn Setting", "GoldCreeper", true).getBoolean(true);
		spawnHighSkeleton = config.get("Mob Spawn Setting", "HighSkeleton", true).getBoolean(true);
		spawnHighSpeedCreeper = config.get("Mob Spawn Setting", "HighSpeedCreeper", true).getBoolean(true);
		spawnSkeletonSniper = config.get("Mob Spawn Setting", "SkeletonSniper", true).getBoolean(true);
		spawnZombieWarrior = config.get("Mob Spawn Setting", "ZombieWarrior", true).getBoolean(true);
		config.save();
		this.itemSetup();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new PlayerClickHook());
		this.addRecipe();
		this.entitySetup();
		proxy.registerRenderInformation();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		this.hasMultiToolHolder = Loader.isModLoaded("MultiToolHolders");
	}

	public void entitySetup()
	{
		EntityRegistry.registerModEntity(Entity_ThrowingKnife.class, "ThrowingKnife", 0, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_HighSkeleton.class, "HighSkeleton", 1, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_SkeletonSniper.class, "SkeletonSniper", 2, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_ZombieWarrior.class, "ZombieWarrior", 3, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_FireZombie.class, "FireZombie", 4, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_HighSpeedCreeper.class, "HighSpeedCreeper", 5, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_GoldCreeper.class, "GoldCreeper", 6, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_BBFireBall.class, "BBFireBall", 7, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_IHFrozenMob.class, "IHFrozenMob", 8, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_PGPowerBomb.class, "PGPowerBomb", 9, this, 250, 1, true);
		EntityRegistry.registerModEntity(Entity_SBWindEdge.class, "SBWindEdge", 10, this, 250, 1, true);
		if (spawnHiGradeMob) {
			for (int i = 0; i < BiomeGenBase.getBiomeGenArray().length; i++) {
				if (BiomeGenBase.getBiomeGenArray()[i] != null
						&& BiomeGenBase.getBiomeGenArray()[i] != BiomeGenBase.hell
						&& BiomeGenBase.getBiomeGenArray()[i] != BiomeGenBase.mushroomIsland
						&& BiomeGenBase.getBiomeGenArray()[i] != BiomeGenBase.mushroomIslandShore
						&& BiomeGenBase.getBiomeGenArray()[i] != BiomeGenBase.sky
						&& BiomeGenBase.getBiomeGenArray()[i].getSpawnableList(EnumCreatureType.monster).size() >= 5) {
					if (spawnHighSkeleton)
						EntityRegistry.addSpawn(Entity_HighSkeleton.class, 2, 1, 4, EnumCreatureType.monster,
								BiomeGenBase.getBiomeGenArray()[i]);
					if (spawnSkeletonSniper)
						EntityRegistry.addSpawn(Entity_SkeletonSniper.class, 3, 1, 4, EnumCreatureType.monster,
								BiomeGenBase.getBiomeGenArray()[i]);
					if (spawnZombieWarrior)
						EntityRegistry.addSpawn(Entity_ZombieWarrior.class, 2, 1, 4, EnumCreatureType.monster,
								BiomeGenBase.getBiomeGenArray()[i]);
					if (spawnFireZombie)
						EntityRegistry.addSpawn(Entity_FireZombie.class, 3, 1, 4, EnumCreatureType.monster,
								BiomeGenBase.getBiomeGenArray()[i]);
					if (spawnHighSpeedCreeper)
						EntityRegistry.addSpawn(Entity_HighSpeedCreeper.class, 3, 4, 4, EnumCreatureType.monster,
								BiomeGenBase.getBiomeGenArray()[i]);
					if (spawnGoldCreeper)
						EntityRegistry.addSpawn(Entity_GoldCreeper.class, 1, 1, 4, EnumCreatureType.monster,
								BiomeGenBase.getBiomeGenArray()[i]);
				}
			}
		}
	}

	public void itemSetup()
	{
		RedEnhancer = (new ItemEnhancer(0)).setUnlocalizedName("RedEnhancer").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "EnhancerR");
		GameRegistry.registerItem(RedEnhancer, "redenhancer");
		list.add(this.RedEnhancer);
		BlueEnhancer = (new ItemEnhancer(1)).setUnlocalizedName("BlueEnhancer").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "EnhancerB");
		GameRegistry.registerItem(BlueEnhancer, "blueenhancer");
		list.add(this.BlueEnhancer);
		UGWoodShovel = (new ItemUGShovel(ToolMaterial.WOOD)).setUnlocalizedName("UpgradedWoodenShovel")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGWoodshovel");
		GameRegistry.registerItem(UGWoodShovel, "ugwoodshovel");
		list.add(UGWoodShovel);
		UGStoneShovel = (new ItemUGShovel(ToolMaterial.STONE, 1.5F)).setUnlocalizedName("UpgradedStoneShovel")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGStoneshovel");
		GameRegistry.registerItem(UGStoneShovel, "ugstoneshovel");
		list.add(UGStoneShovel);
		UGIronShovel = (new ItemUGShovel(ToolMaterial.IRON, 2.0F)).setUnlocalizedName("UpgradedIronShovel")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGIronshovel");
		GameRegistry.registerItem(UGIronShovel, "ugironshovel");
		list.add(UGIronShovel);
		UGDiamondShovel = (new ItemUGShovel(ToolMaterial.EMERALD, 3.0F)).setUnlocalizedName("UpgradedDiamondShovel")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGDiamondshovel");
		GameRegistry.registerItem(UGDiamondShovel, "ugdiamondshovel");
		list.add(UGDiamondShovel);
		UGGoldShovel = (new ItemUGShovel(ToolMaterial.GOLD, 2.5F)).setUnlocalizedName("UpgradedGoldenShovel")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGGoldshovel");
		GameRegistry.registerItem(UGGoldShovel, "uggoldshovel");
		list.add(UGGoldShovel);
		UGWoodPickaxe = (new ItemUGPickaxe(ToolMaterial.WOOD)).setUnlocalizedName("UpgradedWoodenPickaxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGWoodpickaxe");
		GameRegistry.registerItem(UGWoodPickaxe, "ugwoodpickaxe");
		list.add(UGWoodPickaxe);
		UGStonePickaxe = (new ItemUGPickaxe(ToolMaterial.STONE, 1.5F)).setUnlocalizedName("UpgradedStonePickaxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGStonepickaxe");
		GameRegistry.registerItem(UGStonePickaxe, "ugstonepickaxe");
		list.add(UGStonePickaxe);
		UGIronPickaxe = (new ItemUGPickaxe(ToolMaterial.IRON, 2.0F)).setUnlocalizedName("UpgradedIronPickaxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGIronpickaxe");
		GameRegistry.registerItem(UGIronPickaxe, "ugironpickaxe");
		list.add(UGIronPickaxe);
		UGDiamondPickaxe = (new ItemUGPickaxe(ToolMaterial.EMERALD, 3.0F)).setUnlocalizedName("UpgradedDiamondPickaxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGDiamondpickaxe");
		GameRegistry.registerItem(UGDiamondPickaxe, "ugdiamondpickaxe");
		list.add(UGDiamondPickaxe);
		UGGoldPickaxe = (new ItemUGPickaxe(ToolMaterial.GOLD, 2.5F)).setUnlocalizedName("UpgradedGoldenPickaxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGGoldpickaxe");
		GameRegistry.registerItem(UGGoldPickaxe, "uggoldpickaxe");
		list.add(UGGoldPickaxe);
		UGWoodAxe = (new ItemUGAxe(ToolMaterial.WOOD)).setUnlocalizedName("UpgradedWoodenAxe").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "UGWoodaxe");
		GameRegistry.registerItem(UGWoodAxe, "ugwoodaxe");
		list.add(UGWoodAxe);
		UGStoneAxe = (new ItemUGAxe(ToolMaterial.STONE, 1.5F)).setUnlocalizedName("UpgradedStoneAxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGStoneaxe");
		GameRegistry.registerItem(UGStoneAxe, "ugstoneaxe");
		list.add(UGStoneAxe);
		UGIronAxe = (new ItemUGAxe(ToolMaterial.IRON, 2.0F)).setUnlocalizedName("UpgradedIronAxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGIronaxe");
		GameRegistry.registerItem(UGIronAxe, "ugironaxe");
		list.add(UGIronAxe);
		UGDiamondAxe = (new ItemUGAxe(ToolMaterial.EMERALD, 3.0F)).setUnlocalizedName("UpgradedDiamondAxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGDiamondaxe");
		GameRegistry.registerItem(UGDiamondAxe, "ugdiamondaxe");
		list.add(UGDiamondAxe);
		UGGoldAxe = (new ItemUGAxe(ToolMaterial.GOLD, 2.5F)).setUnlocalizedName("UpgradedGoldenAxe")
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "UGGoldaxe");
		GameRegistry.registerItem(UGGoldAxe, "uggoldaxe");
		list.add(UGGoldAxe);
		BlazeBlade = (new ItemUQBlazeBlade(ToolMaterial.EMERALD, 4)).setUnlocalizedName("BlazeBlade")
				.setMaxDamage(1200).setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "BlazeBlade");
		GameRegistry.registerItem(BlazeBlade, "blazeblade");
		list.add(BlazeBlade);
		IceHold = (new ItemUQIceHold(ToolMaterial.EMERALD, 4)).setUnlocalizedName("FreezeHold").setMaxDamage(1200)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "IceHold");
		GameRegistry.registerItem(IceHold, "icehold");
		list.add(IceHold);
		AsmoSlasher = (new ItemUQAsmoSlasher(ToolMaterial.EMERALD, 4)).setUnlocalizedName("AsmoSlasher")
				.setMaxDamage(1200).setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "AsmoSlasher");
		GameRegistry.registerItem(AsmoSlasher, "asmoslasher");
		list.add(AsmoSlasher);
		PlanetGuardian = (new ItemUQPlanetGuardian(ToolMaterial.EMERALD, 4)).setUnlocalizedName("Planet Guardian")
				.setMaxDamage(1200).setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "PlanetGuardian");
		GameRegistry.registerItem(PlanetGuardian, "planetguardian");
		list.add(PlanetGuardian);
		StormBringer = (new ItemUQStormBringer(ToolMaterial.EMERALD, 4)).setUnlocalizedName("StormBringer")
				.setMaxDamage(1200).setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "StormBringer");
		GameRegistry.registerItem(StormBringer, "stormbringer");
		list.add(StormBringer);
		NEGI = (new Item_Negi(1, 0.1F, false)).setUnlocalizedName("NEGI").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "NEGI");
		GameRegistry.registerItem(NEGI, "negi");
		list.add(NEGI);
		LuckLuck = (new ItemUQLuckies(ToolMaterial.GOLD, 2)).setUnlocalizedName("Lucky&Lucky").setMaxDamage(77)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "Luckluck");
		GameRegistry.registerItem(LuckLuck, "luckluck");
		list.add(LuckLuck);
		SmashBat = (new ItemUniqueArms(ToolMaterial.WOOD, 1)).setUnlocalizedName("SmashBat").setMaxDamage(95)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "SmashBat");
		GameRegistry.registerItem(SmashBat, "smashbat");
		list.add(SmashBat);
		DevilSword = (new ItemUQDevilSword(ToolMaterial.EMERALD, 1)).setUnlocalizedName("DevilSword").setMaxDamage(427)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "GenocideBlade");
		GameRegistry.registerItem(DevilSword, "devilsword");
		list.add(DevilSword);
		HolySaber = (new ItemUQHolySaber(ToolMaterial.GOLD, 5)).setUnlocalizedName("HolySaber").setMaxDamage(280)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "HolySaber");
		GameRegistry.registerItem(HolySaber, "holysaber");
		list.add(HolySaber);
		ThrowingKnife = (new Item_ThrowingKnife(false)).setUnlocalizedName("ThrowingKnife").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "ThrowingKnife");
		GameRegistry.registerItem(ThrowingKnife, "throwingknife");
		list.add(ThrowingKnife);
		PoisonKnife = (new Item_ThrowingKnife(true)).setUnlocalizedName("PoisonKnife").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "PoisonKnife");
		GameRegistry.registerItem(PoisonKnife, "poisonknife");
		list.add(PoisonKnife);
		CrossBow = (new Item_CrossBow()).setUnlocalizedName("A_CrossBow").setCreativeTab(tabsAT)
				.setTextureName(AdvancedTools.textureDomain + "CrossBow");
		GameRegistry.registerItem(CrossBow, "crossbow");
		list.add(CrossBow);
		InfiniteSword = (new ItemUniqueArms(ToolMaterial.GOLD, 8)).setUnlocalizedName("InfiniteSword").setMaxDamage(0)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "Infinitysword");
		GameRegistry.registerItem(InfiniteSword, "infinitesword");
		list.add(InfiniteSword);
		InfinitePickaxe = (new ItemUGPickaxe(ToolMaterial.EMERALD)).setUnlocalizedName("InfinityPickaxe")
				.setMaxDamage(0).setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "Infinitypickaxe");
		GameRegistry.registerItem(InfinitePickaxe, "infinitepickaxe");
		list.add(InfinitePickaxe);
		InfiniteAxe = (new ItemUGAxe(ToolMaterial.GOLD)).setUnlocalizedName("InfinityAxe").setMaxDamage(0)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "Infinityaxe");
		GameRegistry.registerItem(InfiniteAxe, "infiniteaxe");
		list.add(InfiniteAxe);
		InfiniteShovel = (new ItemUGShovel(ToolMaterial.GOLD)).setUnlocalizedName("InfinityShovel").setMaxDamage(0)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "Infinityshovel");
		GameRegistry.registerItem(InfiniteShovel, "infiniteshovel");
		list.add(InfiniteShovel);
		InfiniteHoe = (new ItemInfHoe(ToolMaterial.GOLD)).setUnlocalizedName("InfinityHoe").setMaxDamage(0)
				.setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "Infinityhoe");
		GameRegistry.registerItem(InfiniteHoe, "infinitehoe");
		list.add(InfiniteHoe);
		GenocideBlade = (new ItemUniqueArms(ToolMaterial.EMERALD, 10000)).setUnlocalizedName("GenocideBlade")
				.setMaxDamage(0).setCreativeTab(tabsAT).setTextureName(AdvancedTools.textureDomain + "GenocideBlade");
		GameRegistry.registerItem(GenocideBlade, "genocideblade");
	}

	public void addRecipe()
	{
		Item[][] var1 = new Item[][] {
				{ UGWoodShovel, UGWoodShovel, UGStoneShovel, UGIronShovel, UGDiamondShovel, UGGoldShovel,
						InfiniteShovel },
				{ UGWoodPickaxe, UGWoodPickaxe, UGStonePickaxe, UGIronPickaxe, UGDiamondPickaxe, UGGoldPickaxe,
						InfinitePickaxe },
				{ UGWoodAxe, UGWoodAxe, UGStoneAxe, UGIronAxe, UGDiamondAxe, UGGoldAxe, InfiniteAxe } };
		Item[] var2 = new Item[] { Items.stick, Items.stick, RedEnhancer, RedEnhancer, BlueEnhancer, BlueEnhancer,
				Items.ender_eye };
		Object[] var3 = new Object[] { Blocks.log, Blocks.log2, Blocks.cobblestone, Items.iron_ingot, Items.diamond,
				Items.gold_ingot, Blocks.end_stone };
		String[][] var4 = new String[][] { { "X", "#", "Z" }, { "XXX", " # ", " Z " }, { "XX", "X#", " Z" } };
		GameRegistry.addRecipe(new ItemStack(InfiniteSword, 1), new Object[] { " #X", "XY#", "ZX ", 'X',
				Blocks.end_stone, 'Y', Items.ender_eye, 'Z', Items.stick, '#', Blocks.glowstone });
		GameRegistry.addRecipe(new ItemStack(InfiniteHoe, 1), new Object[] { "XX", " Y", " Z", 'X', Blocks.end_stone,
				'Y', Items.ender_eye, 'Z', Items.stick });
		GameRegistry.addRecipe(new ItemStack(RedEnhancer, 2), new Object[] { " X ", "XZX", " X ", 'X',
				Items.gold_nugget, 'Z', Items.redstone });
		GameRegistry.addShapelessRecipe(new ItemStack(Items.redstone), new Object[] { RedEnhancer, RedEnhancer });
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gold_ingot), new Object[] { RedEnhancer, RedEnhancer,
				RedEnhancer, RedEnhancer, RedEnhancer, RedEnhancer, RedEnhancer, RedEnhancer, RedEnhancer });
		GameRegistry.addRecipe(new ItemStack(BlueEnhancer, 2), new Object[] { "RXR", "XZX", "RXR", 'X',
				Items.gold_ingot, 'Z', Items.diamond, 'R', Items.redstone });
		GameRegistry.addShapelessRecipe(new ItemStack(Items.gold_ingot), new Object[] { BlueEnhancer, BlueEnhancer });
		GameRegistry.addShapelessRecipe(new ItemStack(Items.diamond), new Object[] { BlueEnhancer, BlueEnhancer,
				BlueEnhancer, BlueEnhancer, BlueEnhancer, BlueEnhancer, BlueEnhancer, BlueEnhancer, BlueEnhancer });

		for (int var5 = 0; var5 < var1[0].length; ++var5) {
			for (int var6 = 0; var6 < var1.length; ++var6) {
				GameRegistry.addRecipe(new ItemStack(var1[var6][var5]), new Object[] { var4[var6], 'X', var3[var5],
						'#', var2[var5], 'Z', Items.stick });
			}
		}

		GameRegistry.addRecipe(new ItemStack(BlazeBlade), new Object[] { " P#", "XEP", "IX ", 'I', Items.blaze_rod,
				'X', Items.diamond, '#', Items.iron_ingot, 'E', BlueEnhancer, 'P', Items.blaze_powder });
		GameRegistry.addRecipe(new ItemStack(IceHold), new Object[] { " P#", "XEP", "IX ", 'I', Items.stick, 'X',
				Items.iron_ingot, '#', Items.water_bucket, 'E', BlueEnhancer, 'P', Blocks.snow });
		GameRegistry.addRecipe(new ItemStack(AsmoSlasher), new Object[] { " P#", "XEP", "IX ", 'I', Items.stick, 'X',
				Items.gold_ingot, '#', Items.iron_ingot, 'E', BlueEnhancer, 'P', Items.redstone });
		GameRegistry.addRecipe(new ItemStack(PlanetGuardian), new Object[] { " ##", "#E#", "I# ", 'I', Items.stick,
				'#', Items.iron_ingot, 'E', BlueEnhancer });
		GameRegistry.addRecipe(new ItemStack(StormBringer), new Object[] { "  #", "XE ", "IX ", 'I', Items.stick, 'X',
				Items.feather, '#', Items.iron_ingot, 'E', BlueEnhancer });
		GameRegistry.addRecipe(new ItemStack(LuckLuck), new Object[] { "  #", "#E ", "I# ", 'I', Items.stick, '#',
				Items.gold_ingot, 'E', RedEnhancer });
		GameRegistry.addRecipe(new ItemStack(SmashBat), new Object[] { "#", "#", "#", '#', Blocks.log });
		GameRegistry.addRecipe(new ItemStack(SmashBat), new Object[] { "#", "#", "#", '#', Blocks.log2 });
		GameRegistry.addShapelessRecipe(new ItemStack(NEGI), new Object[] { Items.water_bucket, Items.wheat_seeds,
				Blocks.dirt });
		GameRegistry.addRecipe(new ItemStack(HolySaber), new Object[] { "XBX", "EAC", "XDX", 'A', BlazeBlade, 'B',
				IceHold, 'C', AsmoSlasher, 'D', PlanetGuardian, 'E', StormBringer, 'X', Blocks.glowstone });
		GameRegistry.addRecipe(new ItemStack(ThrowingKnife, 16), new Object[] { " X", "# ", 'X', Items.iron_ingot, '#',
				Items.stick });
		GameRegistry.addShapelessRecipe(new ItemStack(PoisonKnife), new Object[] { ThrowingKnife, Items.spider_eye });
	}

	public static String getUniqueStrings(Object obj)
	{
		UniqueIdentifier uId;
		if (obj instanceof Block) {
			uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
		} else {
			uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
		}
		return uId.modId + ":" + uId.name;

	}

	public static MovingObjectPosition setMousePoint(World world, EntityPlayer entityplayer)
	{
		float var1 = 1F;
		double Dislimit = 5.0D;
		double viewX = entityplayer.getLookVec().xCoord;
		double viewY = entityplayer.getLookVec().yCoord;
		double viewZ = entityplayer.getLookVec().zCoord;
		double PlayerposX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) var1;
		double PlayerposY = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double) var1 + 1.62D
				- (double) entityplayer.yOffset;
		double PlayerposZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) var1;
		Vec3 PlayerPosition = Vec3.createVectorHelper(PlayerposX, PlayerposY, PlayerposZ);
		Vec3 PlayerLookVec = PlayerPosition.addVector(viewX * Dislimit, viewY * Dislimit, viewZ * Dislimit);
		MovingObjectPosition MOP = world.rayTraceBlocks(PlayerPosition, PlayerLookVec, true);
		return MOP;
	}
}
