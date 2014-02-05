package ak.EnchantChanger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import ak.EnchantChanger.Client.ClientProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

@Mod(modid = "EnchantChanger", name = "EnchantChanger", version = "1.7a-universal", dependencies = "required-after:FML")
//@NetworkMod(clientSideRequired = true, serverSideRequired = false, channels = { "EC|Sw", "EC|CS", "EC|Levi" }, packetHandler = Packet_EnchantChanger.class)
public class EnchantChanger {
	public static int ExExpBottleID;
	public static Item ItemExExpBottle;
	public static int MateriaID;
	public static Item ItemMat;
	public static int ZackSwordItemID;
	public static Item ItemZackSword;
	public static int CloudSwordItemID;
	public static Item ItemCloudSword;
	public static int FirstSwordItemID;
	public static Item ItemCloudSwordCore;
	public static int SephirothSwordItemID;
	public static Item ItemSephirothSword;
	public static int UltimateWeaponItemID;
	public static Item ItemUltimateWeapon;
	public static int PortableEnchantChangerID;
	public static Item ItemPortableEnchantChanger;
	public static int PortableEnchantmentTableID;
	public static Item ItemPortableEnchantmentTable;
	public static int MasterMateriaID;
	public static Item MasterMateria;
	public static int ImitateSephSwordID;
	public static Item ItemImitateSephirothSword;
	public static int EnchantChangerID;
	public static Block BlockMat;
	public static int HugeMateriaID;
	public static Block HugeMateria;
	public static Item ItemHugeMateria;
	public static boolean LevelCap;
	public static boolean Debug;
	public static float MeteoPower;
	public static float MeteoSize;
	public static int[] extraSwordIDs;
	public static int[] extraToolIDs;
	public static int[] extraBowIDs;
	public static int[] extraArmorIDs;
	private static String[] enchantmentLevelLimits;
	private static String[] enchantmentLevelLimitInit = new String[] {
			"0:10",
			"1:10",
			"2:10",
			"3:10",
			"4:10",
			"32:0",
			"34:30",
			"303:0",
			"307:0",
			"310:0"
	};
	public static HashMap<Integer, Integer> levelLimitMap = new HashMap<Integer, Integer>();

	public static boolean DecMateriaLv;
	public static boolean YouAreTera;
	public static int MateriaPotionMinutes;
	public static int Difficulty;
	public static double AbsorpBoxSize = 5D;
	public static int MaxLv = 127;
	public static boolean enableAPSystem;
	public static boolean enableDungeonLoot;
	public static int aPBasePoint;

	public static int EnchantmentMeteoId;
	public static Enchantment Meteo;
	public static int EndhantmentHolyId;
	public static Enchantment Holy;
	public static int EnchantmentTelepoId;
	public static Enchantment Telepo;
	public static int EnchantmentFloatId;
	public static Enchantment Float;
	public static int EnchantmentThunderId;
	public static Enchantment Thunder;

	public static String EcMeteoPNG = "textures/items/Meteo.png";
	public static String EcExpBottlePNG = "textures/items/ExExpBottle.png";
	public static String EcZackSwordPNG = "textures/item/ZackSword.png";
	public static String EcSephirothSwordPNG = "textures/item/SephirothSword.png";
	public static String EcCloudSwordPNG = "textures/item/CloudSword.png";
	public static String EcCloudSword2PNG = "textures/item/CloudSword-3Dtrue.png";
	public static String EcCloudSwordCorePNG = "textures/item/CloudSwordCore.png";
	public static String EcCloudSwordCore2PNG = "textures/item/CloudSwordCore-3Dtrue.png";
	public static String EcUltimateWeaponPNG = "textures/item/UltimaWeapon.png";
	public static String EcGuiMaterializer = "textures/gui/materializer.png";
	public static String EcGuiHuge = "textures/gui/HugeMateriaContainer.png";
	public static String EcHugetex = "textures/item/hugemateriatex.png";
	public static String EcTextureDomain = "enchantchanger:";
	public static String EcAssetsDomain = "enchantchanger";

	public static boolean loadMTH = false;
	@Mod.Instance("EnchantChanger")
	public static EnchantChanger instance;
	@SidedProxy(clientSide = "ak.EnchantChanger.Client.ClientProxy", serverSide = "ak.EnchantChanger.CommonProxy")
	public static CommonProxy proxy;
	public static int guiIdMaterializer = 0;
	public static int guiIdPortableEnchantmentTable = 1;
	public static int guiIdHugeMateria = 2;
	public static HashMap<Integer, Integer> apLimit = new HashMap();
	public static HashSet<Integer> magicEnchantment = new HashSet();
	public static final CreativeTabs tabsEChanger = new CreativeTabEC(
			"EnchantChanger");
	public static LivingEventHooks livingeventhooks;
	public static final PacketPipeline packetPipeline = new PacketPipeline();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();

//		EnchantChangerID = config.get(Configuration.CATEGORY_BLOCK,
//				"EnchantChanger Id", 2000).getInt();
//		HugeMateriaID = config.get(Configuration.CATEGORY_BLOCK,
//				"Huge Materia Id", 2001).getInt();
//		ExExpBottleID = config.get(Configuration.CATEGORY_ITEM,
//				"ExExpBottle Id", 4999).getInt();
//		MateriaID = config.get(Configuration.CATEGORY_ITEM, "Materia Id", 5000)
//				.getInt();
//		ZackSwordItemID = config.get(Configuration.CATEGORY_ITEM,
//				"BasterSword Id", 5001).getInt();
//		CloudSwordItemID = config.get(Configuration.CATEGORY_ITEM,
//				"UnionSword Id", 5002).getInt();
//		FirstSwordItemID = config.get(Configuration.CATEGORY_ITEM,
//				"FirstSword Id", 5003).getInt();
//		SephirothSwordItemID = config.get(Configuration.CATEGORY_ITEM,
//				"Masamune Blade Id", 5004).getInt();
//		UltimateWeaponItemID = config.get(Configuration.CATEGORY_ITEM,
//				"Ultimate Weapon Id", 5005).getInt();
//		PortableEnchantChangerID = config.get(Configuration.CATEGORY_ITEM,
//				"Portable Enchant Changer Id", 5006).getInt();
//		PortableEnchantmentTableID = config.get(Configuration.CATEGORY_ITEM,
//				"Portable Enchantment Table Id", 5007).getInt();
//		MasterMateriaID = config.get(Configuration.CATEGORY_ITEM,
//				"Master Materia Id", 5008).getInt();
//		ImitateSephSwordID = config.get(Configuration.CATEGORY_ITEM,
//				"Imitate Masamune Blade Id", 5009).getInt();

		LevelCap = config
				.get(Configuration.CATEGORY_GENERAL,
						"LevelCap",
						false,
						"TRUE:You cannot change a Materia to a enchantment over max level of the enchantment.")
				.getBoolean(false);
		Debug = config.get(Configuration.CATEGORY_GENERAL, "Debug mode", false,
				"デバッグ用").getBoolean(false);
		enableAPSystem = config.get(Configuration.CATEGORY_GENERAL,
				"enableAPSystem", true).getBoolean(true);
		enableDungeonLoot = config.get(Configuration.CATEGORY_GENERAL,
				"enableDungeonLoot", true).getBoolean(true);
		aPBasePoint = config.get(Configuration.CATEGORY_GENERAL, "APBAsePoint",
				200).getInt();
		extraSwordIDs = config.get(Configuration.CATEGORY_GENERAL,
				"Extra SwordIds", new int[] { 267 },
				"Put Ids which you want to operate as  swords.").getIntList();
		extraToolIDs = config.get(Configuration.CATEGORY_GENERAL,
				"Extra ToolIds", new int[] { 257 },
				"Put Ids which you want to operate as  tools.").getIntList();
		extraBowIDs = config.get(Configuration.CATEGORY_GENERAL,
				"Extra BowIds", new int[] { 261 },
				"Put Ids which you want to operate as  bows.").getIntList();
		extraArmorIDs = config.get(Configuration.CATEGORY_GENERAL,
				"Extra ArmorIds", new int[] { 298 },
				"Put Ids which you want to operate as  armors.").getIntList();

		DecMateriaLv = config
				.get(Configuration.CATEGORY_GENERAL, "DecMateriaLv", false,
						"TRUE:The level of extracted Materia is decreased by the item damage")
				.getBoolean(false);
		YouAreTera = config
				.get(Configuration.CATEGORY_GENERAL,
						"YouAreTera",
						false,
						"TRUE:You become Tera in FF4. It means that you can use Magic Materia when your MP is exhausted")
				.getBoolean(false);
		MeteoPower = (float) config.get(Configuration.CATEGORY_GENERAL,
				"METEO POWER", 10, "This is a power of Meteo").getInt();
		MeteoSize = (float) config.get(Configuration.CATEGORY_GENERAL,
				"Meteo Size", 10, "This is a Size of Meteo").getInt();
		MateriaPotionMinutes = config.get(Configuration.CATEGORY_GENERAL,
				"Materia Potion Minutes", 10,
				"How long minutes Materia put potion effect to MOB or ANIMAL")
				.getInt();
		Difficulty = config.get(Configuration.CATEGORY_GENERAL, "Difficulty",
				1, "Difficulty of this MOD. 0 = Easy, 1 = Normal, 2 = Hard")
				.getInt();
		EnchantmentMeteoId = config.get(Configuration.CATEGORY_GENERAL,
				"EnchantmentMeteoId", 240).getInt();
		EndhantmentHolyId = config.get(Configuration.CATEGORY_GENERAL,
				"EndhantmentHolyId", 241).getInt();
		EnchantmentTelepoId = config.get(Configuration.CATEGORY_GENERAL,
				"EnchantmentTelepoId", 242).getInt();
		EnchantmentFloatId = config.get(Configuration.CATEGORY_GENERAL,
				"EnchantmentFloatId", 243).getInt();
		EnchantmentThunderId = config.get(Configuration.CATEGORY_GENERAL,
				"EnchantmentThunderId", 244).getInt();
		enchantmentLevelLimits = config
				.get(Configuration.CATEGORY_GENERAL, "ApSystemLevelLimit", enchantmentLevelLimitInit,
						"Set Enchantmets Level Limit for AP System Format EnchantmentID:LimitLv(LimitLv = 0 > DefaultMaxLevel)")
				.getStringList();
		config.save();

		ItemMat = (new EcItemMateria()).setUnlocalizedName(
				this.EcTextureDomain + "Materia").setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemMat, "materia", "EnchantChanger");
		ItemExExpBottle = new EcItemExExpBottle()
				.setUnlocalizedName(this.EcTextureDomain + "ExExpBottle")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemExExpBottle, "exexpbottle",
				"EnchantChanger");
		ItemZackSword = (new EcItemZackSword())
				.setUnlocalizedName(this.EcTextureDomain + "ZackSword")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemZackSword, "zacksword", "EnchantChanger");
		ItemCloudSwordCore = (new EcItemCloudSwordCore())
				.setUnlocalizedName(this.EcTextureDomain + "CloudSwordCore")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemCloudSwordCore, "cloudswordcore",
				"EnchantChanger");
		ItemCloudSword = (new EcItemCloudSword())
				.setUnlocalizedName(this.EcTextureDomain + "CloudSword")
				.setCreativeTab(null);
		GameRegistry.registerItem(ItemCloudSword, "cloudsword",
				"EnchantChanger");
		ItemSephirothSword = (new EcItemSephirothSword(
				)).setUnlocalizedName(
				this.EcTextureDomain + "MasamuneBlade").setCreativeTab(
				tabsEChanger);
		GameRegistry.registerItem(ItemSephirothSword, "masamuneblade",
				"EnchantChanger");
		ItemUltimateWeapon = (new EcItemUltimateWeapon(
			)).setUnlocalizedName(
				this.EcTextureDomain + "UltimateWeapon").setCreativeTab(
				tabsEChanger);
		GameRegistry.registerItem(ItemUltimateWeapon, "ultimateweapon",
				"EnchantChanger");
		ItemPortableEnchantChanger = (new EcItemMaterializer(
				)).setUnlocalizedName(
				this.EcTextureDomain + "PortableEnchantChanger")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemPortableEnchantChanger,
				"portableenchantchanger", "EnchantChanger");
		ItemPortableEnchantmentTable = (new EcItemEnchantmentTable(
				)).setUnlocalizedName(
				this.EcTextureDomain + "PortableEnchantmentTable")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemPortableEnchantmentTable,
				"portableenchantmenttable", "EnchantChanger");
		MasterMateria = new EcItemMasterMateria()
				.setUnlocalizedName(this.EcTextureDomain + "MasterMateria")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(MasterMateria, "mastermateria",
				"EnchantChanger");
		ItemImitateSephirothSword = (new EcItemSephirothSwordImit(
				)).setUnlocalizedName(
				this.EcTextureDomain + "ImitateMasamuneBlade").setCreativeTab(
				tabsEChanger);
		GameRegistry.registerItem(ItemImitateSephirothSword,
				"imitationmasamuneblade", "EnchantChanger");
		BlockMat = (new EcBlockMaterialize()).setCreativeTab(
				tabsEChanger).setBlockName("EnchantChanger").setHardness(5.0f).setResistance(2000.0f).setLightOpacity(0);
		GameRegistry.registerBlock(BlockMat, "EnchantChanger");
		HugeMateria = new EcBlockHugeMateria().setHardness(5.0f).setResistance(2000.0f).setLightLevel(1.0f).setLightOpacity(0)
				.setBlockName("HugeMateria");
		GameRegistry.registerBlock(HugeMateria, "blockhugemateria");
		ItemHugeMateria = new EcItemHugeMateria()
				.setUnlocalizedName(this.EcTextureDomain + "HugeMateria")
				.setCreativeTab(tabsEChanger);
		GameRegistry.registerItem(ItemHugeMateria, "itemhugemateria",
				"EnchantChanger");

		Meteo = new EcEnchantmentMeteo(this.EnchantmentMeteoId, 0);
		Holy = new EcEnchantmentHoly(this.EndhantmentHolyId, 0);
		Telepo = new EcEnchantmentTeleport(this.EnchantmentTelepoId, 0);
		Float = new EcEnchantmentFloat(this.EnchantmentFloatId, 0);
		Thunder = new EcEnchantmentThunder(this.EnchantmentThunderId, 0);
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event) {
		this.initMaps();
		livingeventhooks = new LivingEventHooks();
		MinecraftForge.EVENT_BUS.register(livingeventhooks);
		FMLCommonHandler.instance().bus().register(this);
		FMLCommonHandler.instance().bus().register(new CommonTickHandler());
		packetPipeline.initialise();
		GameRegistry.registerTileEntity(EcTileEntityMaterializer.class,
				"container.materializer");
		GameRegistry.registerTileEntity(EcTileEntityHugeMateria.class,
				"container.hugeMateria");

		EntityRegistry.registerModEntity(EcEntityExExpBottle.class,
				"ItemExExpBottle", 0, this, 250, 5, true);
		EntityRegistry.registerModEntity(EcEntityMeteo.class, "Meteo", 1, this,
				250, 5, true);
		EntityRegistry.registerModEntity(EcEntityApOrb.class, "apOrb", 2, this,
				64, 1, false);

		// "this" is an instance of the mod class
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		proxy.registerRenderInformation();
		proxy.registerTileEntitySpecialRenderer();
		//1.7で仕様変更。代わりのものが見つからない。
//		MinecraftForge.setToolClass(ItemSephirothSword, "FF7", 0);
//		Block[] pickeff = { Block.cobblestone, Block.stone, Block.sandStone,
//				Block.cobblestoneMossy, Block.oreCoal, Block.ice,
//				Block.netherrack, Block.oreLapis, Block.blockLapis,
//				Block.oreRedstone, Block.obsidian, Block.oreRedstoneGlowing };
//		for (Block block : pickeff) {
//			MinecraftForge.removeBlockEffectiveness(block, "FF7");
//			MinecraftForge.setBlockHarvestLevel(block, "FF7", 0);
//		}

		if (this.Difficulty < 2)
			GameRegistry.addRecipe(new EcMateriaRecipe());
		GameRegistry.addRecipe(new EcMasterMateriaRecipe());
		GameRegistry.addShapelessRecipe(new ItemStack(ItemMat, 1, 0),
				new Object[] { new ItemStack(Items.diamond, 1),
						new ItemStack(Items.ender_pearl, 1) });
		GameRegistry.addRecipe(new ItemStack(ItemZackSword, 1), new Object[] {
				" X", "XX", " Y", Character.valueOf('X'), Blocks.iron_block,
				Character.valueOf('Y'), Items.iron_ingot });
		if (this.Difficulty < 2)
			GameRegistry.addRecipe(
					new ItemStack(ItemCloudSwordCore, 1),
					new Object[] { " X ", "XYX", " Z ", Character.valueOf('X'),
							Blocks.iron_block, Character.valueOf('Y'),
							new ItemStack(ItemMat, 1, 0),
							Character.valueOf('Z'), Items.iron_ingot });
		else
			GameRegistry.addRecipe(
					new ItemStack(ItemCloudSwordCore, 1),
					new Object[] { " X ", "DYD", " Z ", Character.valueOf('X'),
							Blocks.iron_block, Character.valueOf('Y'),
							new ItemStack(ItemMat, 1, 0),
							Character.valueOf('Z'), Items.iron_ingot, 'D',
							Items.diamond });
		GameRegistry.addRecipe(
				new ItemStack(ItemSephirothSword, 1),
				new Object[] { "  A", " B ", "C  ", Character.valueOf('A'),
						Items.iron_ingot, Character.valueOf('B'),
						new ItemStack(Items.diamond_sword, 1, 0),
						Character.valueOf('C'), new ItemStack(ItemMat, 1, 1) });
		GameRegistry.addRecipe(
				new ItemStack(ItemUltimateWeapon, 1),
				new Object[] {
						" A ",
						"ABA",
						" C ",
						Character.valueOf('A'),
						Blocks.diamond_block,
						Character.valueOf('B'),
						new ItemStack(MasterMateria, 1,
								OreDictionary.WILDCARD_VALUE),
						Character.valueOf('C'), Items.stick });
		GameRegistry.addRecipe(new ItemStack(ItemImitateSephirothSword), "  A",
				" A ", "B  ", 'A', Items.iron_ingot, 'B', Items.iron_sword);
		GameRegistry.addRecipe(new ItemStack(BlockMat, 1),
				new Object[] { "XYX", "ZZZ", Character.valueOf('X'),
						Items.diamond, Character.valueOf('Y'), Blocks.gold_block,
						Character.valueOf('Z'), Blocks.obsidian });
		GameRegistry.addRecipe(new ItemStack(HugeMateria), new Object[] {
				" A ", "ABA", " A ", 'A', Blocks.diamond_block, 'B',
				Items.nether_star });
		GameRegistry
				.addRecipe(new ItemStack(HugeMateria), new Object[] {
						" A ",
						"ABA",
						" A ",
						'A',
						Blocks.diamond_block,
						'B',
						new ItemStack(MasterMateria, 1,
								OreDictionary.WILDCARD_VALUE) });
		GameRegistry.addShapelessRecipe(new ItemStack(
				ItemPortableEnchantChanger, 1), new Object[] { BlockMat });
		GameRegistry.addShapelessRecipe(new ItemStack(
				ItemPortableEnchantmentTable, 1),
				new Object[] { Blocks.enchanting_table });
		GameRegistry.addShapelessRecipe(new ItemStack(MasterMateria, 1, 0),
				new Object[] { new ItemStack(MasterMateria, 1, 1),
						new ItemStack(MasterMateria, 1, 2),
						new ItemStack(MasterMateria, 1, 3),
						new ItemStack(MasterMateria, 1, 4),
						new ItemStack(MasterMateria, 1, 5) });
		if (this.Difficulty == 0)
			GameRegistry.addRecipe(
					new ItemStack(Items.experience_bottle, 8),
					new Object[] { "XXX", "XYX", "XXX", Character.valueOf('X'),
							new ItemStack(Items.potionitem, 1, 0),
							Character.valueOf('Y'),
							new ItemStack(Items.diamond, 1) });
		GameRegistry.addRecipe(new ItemStack(ItemExExpBottle, 8), new Object[] {
				"XXX", "XYX", "XXX", Character.valueOf('X'),
				new ItemStack(Items.experience_bottle, 1, 0), Character.valueOf('Y'),
				new ItemStack(Blocks.diamond_block, 1) });
		GameRegistry
				.addRecipe(new ItemStack(Blocks.dragon_egg, 1), new Object[] {
						"XXX",
						"XYX",
						"XXX",
						Character.valueOf('X'),
						Items.ender_eye,
						Character.valueOf('Y'),
						new ItemStack(MasterMateria, 1,
								OreDictionary.WILDCARD_VALUE) });
		if (this.enableDungeonLoot)
			this.DungeonLootItemResist();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.loadMTH = Loader.isModLoaded("MultiToolHolders");
		packetPipeline.postInitialise();
	}

	private void initMaps() {
		this.magicEnchantment.add(this.EnchantmentMeteoId);
		this.magicEnchantment.add(this.EnchantmentFloatId);
		this.magicEnchantment.add(this.EndhantmentHolyId);
		this.magicEnchantment.add(this.EnchantmentTelepoId);
		this.magicEnchantment.add(this.EnchantmentThunderId);
		this.apLimit.put(0, 2 * aPBasePoint);
		this.apLimit.put(1, 1 * aPBasePoint);
		this.apLimit.put(2, 1 * aPBasePoint);
		this.apLimit.put(3, 1 * aPBasePoint);
		this.apLimit.put(4, 1 * aPBasePoint);
		this.apLimit.put(5, 1 * aPBasePoint);
		this.apLimit.put(6, 1 * aPBasePoint);
		this.apLimit.put(7, 1 * aPBasePoint);
		this.apLimit.put(16, 2 * aPBasePoint);
		this.apLimit.put(17, 1 * aPBasePoint);
		this.apLimit.put(18, 1 * aPBasePoint);
		this.apLimit.put(19, 1 * aPBasePoint);
		this.apLimit.put(20, 1 * aPBasePoint);
		this.apLimit.put(21, 3 * aPBasePoint);
		this.apLimit.put(32, 1 * aPBasePoint);
		this.apLimit.put(33, 1 * aPBasePoint);
		this.apLimit.put(34, 1 * aPBasePoint);
		this.apLimit.put(35, 2 * aPBasePoint);
		this.apLimit.put(48, 2 * aPBasePoint);
		this.apLimit.put(49, 1 * aPBasePoint);
		this.apLimit.put(50, 1 * aPBasePoint);
		this.apLimit.put(51, 1 * aPBasePoint);
		this.apLimit.put(61, 1 * aPBasePoint);
		this.apLimit.put(62, 1 * aPBasePoint);
		String[] idAndLimit;
		for(int i=0;i<this.enchantmentLevelLimits.length;i++){
			idAndLimit = this.enchantmentLevelLimits[i].split(":");
			this.levelLimitMap.put(Integer.valueOf(idAndLimit[0]), Integer.valueOf(idAndLimit[1]));
		}
	}

	public static String getUniqueStrings(Object obj)
	{
		UniqueIdentifier uId;
		if(obj instanceof Block) {
			uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
		}else {
			uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
		}
		return uId.modId + ":" + uId.name;

	}
	
	public static boolean isApLimit(int Id, int Lv, int ap) {
		if (EnchantChanger.getApLimit(Id, Lv) < ap)
			return true;
		else
			return false;
	}

	public static int getApLimit(int Id, int Lv) {
		if (EnchantChanger.apLimit.containsKey(Id)) {
			return ((int) EnchantChanger.apLimit.get(Id)) * (Lv / 5 + 1);
		} else
			return 150 * (Lv / 5 + 1);
	}

	public static void addEnchantmentToItem(ItemStack item,
			Enchantment enchantment, int Lv) {
		if(item == null ||enchantment == null || Lv < 0) {
			return;
		}
		if (item.stackTagCompound == null) {
			item.setTagCompound(new NBTTagCompound());
		}

		if (!item.stackTagCompound.hasKey("ench")) {
			item.stackTagCompound.setTag("ench", new NBTTagList());
		}

		NBTTagList var3 = (NBTTagList) item.stackTagCompound.getTag("ench");
		NBTTagCompound var4 = new NBTTagCompound();
		var4.setShort("id", (short) enchantment.effectId);
		var4.setShort("lvl", (short) (Lv));
		var3.appendTag(var4);
	}

	public static int getMateriaEnchKind(ItemStack item) {
		int EnchantmentKind = 256;
		for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
			if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
				EnchantmentKind = i;
				break;
			}
		}
		return EnchantmentKind;
	}

	public static int getMateriaEnchLv(ItemStack item) {
		int Lv = 0;
		for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
			if (EnchantmentHelper.getEnchantmentLevel(i, item) > 0) {
				Lv = EnchantmentHelper.getEnchantmentLevel(i, item);
				break;
			}
		}
		return Lv;
	}

	public static MovingObjectPosition getMouseOverCustom(EntityPlayer player,
			World world, double reach) {
		Entity pointedEntity = null;
		float var1 = 1F;
		double distLimit = reach;
		double distBlock = distLimit;
		double viewX = player.getLookVec().xCoord;
		double viewY = player.getLookVec().yCoord;
		double viewZ = player.getLookVec().zCoord;
		double PlayerposX = player.posX;
		// player.prevPosX + (player.posX - player.prevPosX) * (double)var1;
		double PlayerposY = player.posY + 1.62D - (double) player.yOffset;
		// player.prevPosY + (player.posY - player.prevPosY) * (double)var1;
		double PlayerposZ = player.posZ;
		// player.prevPosZ + (player.posZ - player.prevPosZ) * (double)var1;
		Vec3 PlayerPosition = world.getWorldVec3Pool().getVecFromPool(
				PlayerposX, PlayerposY, PlayerposZ);
		Vec3 PlayerLookVec = PlayerPosition.addVector(viewX * distLimit, viewY
				* distLimit, viewZ * distLimit);
		MovingObjectPosition MOP = world.rayTraceBlocks(PlayerPosition, PlayerLookVec);
		if (MOP != null)
			distBlock = MOP.hitVec.distanceTo(PlayerPosition);
		List list = world.getEntitiesWithinAABBExcludingEntity(
				player,
				player.boundingBox.addCoord(viewX * distLimit,
						viewY * distLimit, viewZ * distLimit).expand(
						(double) var1, (double) var1, (double) var1));
		double dist1 = distBlock;
		for (int i = 0; i < list.size(); ++i) {
			Entity entity = (Entity) list.get(i);

			if (entity.canBeCollidedWith()) {
				float f2 = entity.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity.boundingBox.expand(
						(double) f2, (double) f2, (double) f2);
				MovingObjectPosition movingobjectposition = axisalignedbb
						.calculateIntercept(PlayerPosition, PlayerLookVec);

				if (axisalignedbb.isVecInside(PlayerPosition)) {
					if (0.0D < dist1 || dist1 == 0.0D) {
						pointedEntity = entity;
						dist1 = 0.0D;
					}
				} else if (movingobjectposition != null) {
					double d3 = PlayerPosition
							.distanceTo(movingobjectposition.hitVec);

					if (d3 < dist1 || dist1 == 0.0D) {
						pointedEntity = entity;
						dist1 = d3;
					}
				}
			}
		}

		if (pointedEntity != null && (dist1 < distBlock || MOP == null)) {
			MOP = new MovingObjectPosition(pointedEntity);
		}
		return MOP;
	}

	public void DungeonLootItemResist() {
		WeightedRandomChestContent materiaInChest;
		for (int i = 0; i < 8; i++) {
			materiaInChest = ((EcItemMateria) ItemMat).addMateriaInChest(i, 1,
					1, 1);
			ChestGenHooks.addItem(ChestGenHooks.MINESHAFT_CORRIDOR,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_DESERT_CHEST,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_CHEST,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CORRIDOR,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_LIBRARY,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.STRONGHOLD_CROSSING,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.VILLAGE_BLACKSMITH,
					materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.BONUS_CHEST, materiaInChest);
			ChestGenHooks.addItem(ChestGenHooks.DUNGEON_CHEST, materiaInChest);
		}
	}
	
	@SubscribeEvent
	public void KeyHandlingEvent(KeyInputEvent event) {
		if(ClientProxy.MagicKey.isPressed()) {
			EcItemSword.pressMagicKey = true;
		}
	}
}