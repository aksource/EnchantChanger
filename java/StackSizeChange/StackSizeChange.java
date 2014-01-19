package StackSizeChange;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid="StackSizeChange", name="StackSizeChange", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class StackSizeChange
{
	@Mod.Instance("StackSizeChange")
	public static StackSizeChange instance;
	
	public static int SignMax;
	public static int DoorMax;
	public static int BedMax;
	public static int CakeMax;
	public static int BoatMax;
	public static int CartMax;
	public static int EggMax;
	public static int EnderMax;
	public static int SnowMax;
	public static int RecordMax;
	public static int SaddleMax;
	public static int BucketMax;
	public static int PotionMax;
	public static int SoupMax;
	public static int MilkMax;
	public static int BucketWaterMax;
	public static int BucketLavaMax;
	public static int EnchantBookMax;
	public static boolean BucketReplace;
	public static boolean addStackableBucket;
	public static int StackableBucketID;
	public static int SoupAmount;
	public static boolean isNetherSetWater;
	public static boolean isStackCustom;
	
	public static Item bucketEmpty;
	public static Item bucketWater;
	public static Item bucketLava;
	public static Item bucketMilk;

	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		SignMax = config.get(Configuration.CATEGORY_GENERAL, "SignMax", 64, "Sign Max Stack Size, min = 1, max = 64").getInt();
		SignMax = (SignMax<1)?1:(SignMax>64)?64:SignMax;
		DoorMax = config.get(Configuration.CATEGORY_GENERAL, "DoorMax", 64, "Door Max Stack Size, min = 1, max = 64").getInt();
		DoorMax = (DoorMax<1)?1:(DoorMax>64)?64:DoorMax;
		BedMax = config.get(Configuration.CATEGORY_GENERAL, "BedMax", 64, "Bed Max Stack Size, min = 1, max = 64").getInt();
		BedMax = (BedMax<1)?1:(BedMax>64)?64:BedMax;
		CakeMax = config.get(Configuration.CATEGORY_GENERAL, "CakeMax", 64, "Cake Max Stack Size, min = 1, max = 64").getInt();
		CakeMax = (CakeMax<1)?1:(CakeMax>64)?64:CakeMax;
		BoatMax = config.get(Configuration.CATEGORY_GENERAL, "BoatMax", 64, "Boat Max Stack Size, min = 1, max = 64").getInt();
		BoatMax = (BoatMax<1)?1:(BoatMax>64)?64:BoatMax;
		CartMax = config.get(Configuration.CATEGORY_GENERAL, "CartMax", 64, "Minecart Max Stack Size, min = 1, max = 64").getInt();
		CartMax = (CartMax<1)?1:(CartMax>64)?64:CartMax;
		EggMax = config.get(Configuration.CATEGORY_GENERAL, "EggMax", 64, "Egg Max Stack Size, min = 1, max = 64").getInt();
		EggMax = (EggMax<1)?1:(EggMax>64)?64:EggMax;
		EnderMax = config.get(Configuration.CATEGORY_GENERAL, "EnderMax", 64, "EnderParl Max Stack Size, min = 1, max = 64").getInt();
		EnderMax = (EnderMax<1)?1:(EnderMax>64)?64:EnderMax;
		SnowMax = config.get(Configuration.CATEGORY_GENERAL, "SnowMax", 64, "Snowball Max Stack Size, min = 1, max = 64").getInt();
		SnowMax = (SnowMax<1)?1:(SnowMax>64)?64:SnowMax;
		RecordMax = config.get(Configuration.CATEGORY_GENERAL, "RecordMax", 64, "Record Max Stack Size, min = 1, max = 64").getInt();
		RecordMax = (RecordMax<1)?1:(RecordMax>64)?64:RecordMax;
		SaddleMax = config.get(Configuration.CATEGORY_GENERAL, "SaddleMax", 64, "Saddle Max Stack Size, min = 1, max = 64").getInt();
		SaddleMax = (SaddleMax<1)?1:(SaddleMax>64)?64:SaddleMax;
//		BucketMax = config.get(Configuration.CATEGORY_GENERAL, "BucketMax", 64, "Bucket Max Stack Size, min = 1, max = 64").getInt();
//		BucketMax = (BucketMax<1)?1:(BucketMax>64)?64:BucketMax;
//		PotionMax = config.get(Configuration.CATEGORY_GENERAL, "PotionMax", 64, "Potion Max Stack Size, min = 1, max = 64").getInt();
//		PotionMax = (PotionMax<0)?1:(PotionMax>64)?64:PotionMax;
//		SoupMax = config.get(Configuration.CATEGORY_GENERAL, "SoupMax", 64, "Soup Max Stack Size, min = 1, max = 64").getInt();
//		SoupMax = (SoupMax<1)?1:(SoupMax>64)?64:SoupMax;
//		MilkMax = config.get(Configuration.CATEGORY_GENERAL, "MilkMax", 64, "BucketMilk Max Stack Size, min = 1, max = 64").getInt();
//		MilkMax = (MilkMax<1)?1:(MilkMax>64)?64:MilkMax;
//		BucketWaterMax = config.get(Configuration.CATEGORY_GENERAL, "BucketWaterMax", 64, "BucketWater Max Stack Size, min = 1, max = 64").getInt();
//		BucketWaterMax = (BucketWaterMax<1)?1:(BucketWaterMax>64)?64:BucketWaterMax;
//		BucketLavaMax = config.get(Configuration.CATEGORY_GENERAL, "BucketLavaMax", 64, "BucketLava Max Stack Size, min = 1, max = 64").getInt();
//		BucketLavaMax = (BucketLavaMax<1)?1:(BucketLavaMax>64)?64:BucketLavaMax;
		EnchantBookMax = config.get(Configuration.CATEGORY_GENERAL, "EnchantBookMax", 64, "EnchantableBook Max Stack Size, min = 1, max = 64").getInt();
		EnchantBookMax = (EnchantBookMax<1)?1:(EnchantBookMax>64)?64:EnchantBookMax;
//		BucketReplace = config.get(Configuration.CATEGORY_GENERAL, "BucketReplace", true).getBoolean(true);
//		addStackableBucket = config.get(Configuration.CATEGORY_GENERAL, "addStackableBucket", false).getBoolean(false);
//		StackableBucketID = config.get(Configuration.CATEGORY_GENERAL, "StackableBucketID", 17250, "only use addStackableBacket=true. on use +0..+3 IDs, min = 4096, max = 31996").getInt();
//		StackableBucketID = (StackableBucketID<4096)?4096:(StackableBucketID>31996)?31996:StackableBucketID;
//		SoupAmount = config.get(Configuration.CATEGORY_GENERAL, "SoupAmount", 6, "1.2.5 = 8, 1.3.x = 6, min=1, max=20").getInt();
//		SoupAmount = (SoupAmount<1)?1:(SoupAmount>20)?20:SoupAmount;
//		isNetherSetWater = config.get(Configuration.CATEGORY_GENERAL, "isNetherSetWater", false,"true: I would like to put the water into a StackableBucket with Nether.").getBoolean(false);
//		isStackCustom = config.get(Configuration.CATEGORY_GENERAL, "isStackCustom", true, "true: Improve the operation of the stack").getBoolean(true);
//		
		config.save();
		Items.wooden_door.setMaxStackSize(DoorMax);
		Items.minecart.setMaxStackSize(CartMax);
		Items.saddle.setMaxStackSize(SaddleMax);
		Items.iron_door.setMaxStackSize(DoorMax);
		Items.snowball.setMaxStackSize(SnowMax);
		Items.boat.setMaxStackSize(BoatMax);
		Items.chest_minecart.setMaxStackSize(CartMax);
		Items.furnace_minecart.setMaxStackSize(CartMax);
		Items.egg.setMaxStackSize(EggMax);
		Items.cake.setMaxStackSize(CakeMax);
		Items.bed.setMaxStackSize(BedMax);
		Items.ender_pearl.setMaxStackSize(EnderMax);
		Items.potionitem.setMaxStackSize(PotionMax);
		Items.record_13.setMaxStackSize(RecordMax);
		Items.record_cat.setMaxStackSize(RecordMax);
		Items.record_blocks.setMaxStackSize(RecordMax);
		Items.record_chirp.setMaxStackSize(RecordMax);
		Items.record_far.setMaxStackSize(RecordMax);
		Items.record_mall.setMaxStackSize(RecordMax);
		Items.record_mellohi.setMaxStackSize(RecordMax);
		Items.record_stal.setMaxStackSize(RecordMax);
		Items.record_strad.setMaxStackSize(RecordMax);
		Items.record_ward.setMaxStackSize(RecordMax);
		Items.record_11.setMaxStackSize(RecordMax);
		Items.record_wait.setMaxStackSize(RecordMax);
		Items.enchanted_book.setMaxStackSize(EnchantBookMax);
		Items.tnt_minecart.setMaxStackSize(CartMax);
		Items.hopper_minecart.setMaxStackSize(CartMax);

		Items.sign.setMaxStackSize(SignMax);
//		Item.bowlSoup = (new ItemSoupStack(26, SoupAmount)).setUnlocalizedName("mushroomStew").setMaxStackSize(SoupMax);
//		MinecraftForge.EVENT_BUS.register((ItemSoupStack)Item.bowlSoup);

//		if(BucketReplace)
//		{
//			bucketEmpty = (new ItemBucketStack(Blocks.air)).setUnlocalizedName("bucket").setMaxStackSize(BucketMax).setTextureName("bucket_empty");
//			bucketWater = (new ItemBucketStack(Blocks.water)).setUnlocalizedName("bucketWater").setContainerItem(bucketEmpty).setMaxStackSize(BucketWaterMax).setTextureName("bucket_water");
//			bucketLava = (new ItemBucketStack(Blocks.lava)).setUnlocalizedName("bucketLava").setContainerItem(bucketEmpty).setMaxStackSize(BucketLavaMax).setTextureName("bucket_lava");
//			bucketMilk = (new ItemBucketMilkStack()).setUnlocalizedName("milk").setContainerItem(bucketEmpty).setMaxStackSize(MilkMax).setTextureName("bucket_milk");
//			
//			Item.bucketEmpty = bucketEmpty;
//			Item.bucketWater = bucketWater;
//			Item.bucketLava = bucketLava;
//			Item.bucketMilk = bucketMilk;
//		}
//		if(!BucketReplace && addStackableBucket)
//		{
//			bucketEmpty = (new ItemBucketStack(Blocks.air)).setUnlocalizedName("stackbucket").setMaxStackSize(BucketMax).setTextureName("bucket_empty");
//			bucketWater = (new ItemBucketStack(Blocks.water)).setUnlocalizedName("stackbucketWater").setContainerItem(bucketEmpty).setMaxStackSize(BucketWaterMax).setTextureName("bucket_water");
//			bucketLava = (new ItemBucketStack(Blocks.lava)).setUnlocalizedName("stackbucketLava").setContainerItem(bucketEmpty).setMaxStackSize(BucketLavaMax).setTextureName("bucket_lava");
//			bucketMilk = (new ItemBucketMilkStack()).setUnlocalizedName("stackmilk").setContainerItem(bucketEmpty).setMaxStackSize(MilkMax).setTextureName("bucket_milk");
//			GameRegistry.registerItem(bucketEmpty, "bucket_stackable");
//			GameRegistry.registerItem(bucketWater, "water_bucket_stackable");
//			GameRegistry.registerItem(bucketLava, "lava_bucket_stackable");
//			GameRegistry.registerItem(bucketMilk, "milk_stackable");
//			addName(bucketEmpty, "Stackable Bucket",       "スタックバケツ");
//			addName(bucketWater, "Stackable Bucket Water", "スタック水入りバケツ");
//			addName(bucketLava,  "Stackable Bucket Lava",  "スタック溶岩入りバケツ");
//			addName(bucketMilk,  "Stackable Bucket Milk",  "スタック牛乳");
//			GameRegistry.addRecipe(new ItemStack(bucketEmpty, 2), new Object[]{ "I I","III"," I ", 'I',Items.iron_ingot });
//			GameRegistry.addShapelessRecipe(new ItemStack(bucketEmpty, 1), new Object[]{ Items.bucket });
//			GameRegistry.addShapelessRecipe(new ItemStack(Items.bucket, 1), new Object[]{ bucketEmpty });
//			GameRegistry.addRecipe(new ItemStack(Items.cake, 1), new Object[]
//				{ "MMM","SES","WWW", 'M',bucketMilk, 'S',Items.sugar, 'E',Items.egg, 'W',Items.wheat });
//		}
//		if(BucketReplace || addStackableBucket)
//		{
//			BehaviorSBucketFullDispense var2 = new BehaviorSBucketFullDispense();
//			BlockDispenser.field_149943_a.putObject(bucketLava, var2);
//			BlockDispenser.field_149943_a.putObject(bucketWater, var2);
//			BlockDispenser.field_149943_a.putObject(bucketEmpty, new BehaviorSBucketEmptyDispense());
//			MinecraftForge.EVENT_BUS.register((ItemBucketMilkStack)bucketMilk);
//		}
//		if(isStackCustom)
//		{
//			Item.potion = (ItemPotion)(new ItemPotionStack(117)).setUnlocalizedName("potion").setMaxStackSize(PotionMax).setTextureName("potion_bottle_empty");
//			Item.glassBottle = (new ItemGlassBottleStack(118)).setUnlocalizedName("glassBottle");
//		}
	}
	@EventHandler
	public void load(FMLInitializationEvent event)
	{

	}
	public static void addName(Item item, String enName, String jaName)
	{
		LanguageRegistry.addName(item, enName);
		LanguageRegistry.instance().addNameForObject(item, "ja_JP", jaName);
	}

	public static void addName(Block block, String enName, String jaName)
	{
		LanguageRegistry.addName(block, enName);
		LanguageRegistry.instance().addNameForObject(block, "ja_JP", jaName);
	}

	public static ItemStack addropItems(ItemStack par1ItemStack, EntityPlayer par3EntityPlayer, ItemStack addItemStack)
	{
		return addropItems(par1ItemStack, par3EntityPlayer, addItemStack, true);
	}

	public static ItemStack addropItems(ItemStack par1ItemStack, EntityPlayer par3EntityPlayer, ItemStack addItemStack, boolean dec)
	{
		return addropItems(par1ItemStack, par3EntityPlayer, addItemStack, dec, false);
	}

	public static ItemStack addropItems(ItemStack par1ItemStack, EntityPlayer par3EntityPlayer, ItemStack addItemStack, boolean dec, boolean creativeDec)
	{
		if (par3EntityPlayer.capabilities.isCreativeMode && !creativeDec || par1ItemStack == null)
		{
			return par1ItemStack;
		}

		if(dec){--par1ItemStack.stackSize;}
		
		if (par1ItemStack.stackSize <= 0)
		{
			if(!isStackCustom) return addItemStack;
			
			boolean isStackable = false;
			int slot =0;
			for (int i = 0; i < par3EntityPlayer.inventory.mainInventory.length; ++i)
			{
				if (par3EntityPlayer.inventory.mainInventory[i] != null 
				&& par3EntityPlayer.inventory.mainInventory[i].getItem() == addItemStack.getItem()
				&& par3EntityPlayer.inventory.mainInventory[i].stackSize < addItemStack.getMaxStackSize())
				{
					isStackable = true;
					slot = i;
					break;
				}
			}

			if(isStackable)
			{
				par3EntityPlayer.inventory.addItemStackToInventory(addItemStack);
				ItemStack result = par3EntityPlayer.inventory.mainInventory[slot];
				par3EntityPlayer.inventory.mainInventory[slot] = null;
				return result;
			}
			return addItemStack;
		}

		if (!par3EntityPlayer.inventory.addItemStackToInventory(addItemStack))
		{
			par3EntityPlayer.dropPlayerItemWithRandomChoice(addItemStack, false);
		}

		return par1ItemStack;

	}
}
