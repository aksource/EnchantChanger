package SpawnChange;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid="SpawnChange", name="SpawnChange", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class SpawnChange
{
	@Mod.Instance("SpawnChange")
	public static SpawnChange instance;
	
	public static boolean portalSpawn;
	public static int netherSpawnLightValue;
	public static int SlimeSpawnHeight;
	public static Block obsidian;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		portalSpawn = config.get(Configuration.CATEGORY_GENERAL, "portalSpawn", false, "ネザーゲートに敵スポーンを許可する。バニラは true").getBoolean(false);
		netherSpawnLightValue = config.get(Configuration.CATEGORY_GENERAL, "netherSpawnLightValue", 7, "ネザーの敵がスポーンする明るさ。バニラは 15, min=0, max=15").getInt();
		netherSpawnLightValue = (netherSpawnLightValue < 0)?0:(netherSpawnLightValue > 15)?15:netherSpawnLightValue;
		SlimeSpawnHeight = config.get(Configuration.CATEGORY_GENERAL, "SlimeSpawnHeight", 16, "スライムがスポーンする高さ。バニラは 40, min = 0, max = 255").getInt();
		SlimeSpawnHeight = (SlimeSpawnHeight <0)?0:(SlimeSpawnHeight>255)?255:SlimeSpawnHeight;
		config.save();
		if(!portalSpawn){
			obsidian = (new scBlockObsidian()).func_149711_c(50.0F).func_149752_b(2000.0F).func_149672_a(Block.field_149769_e).func_149663_c("obsidian").func_149658_d("obsidian");
			GameRegistry.registerBlock(obsidian, "obsidian_nospawn");
		}
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		if(!portalSpawn){
			GameRegistry.addShapelessRecipe(new ItemStack(obsidian), new Object[]{new ItemStack(Blocks.obsidian)});
			GameRegistry.addShapelessRecipe(new ItemStack(Blocks.obsidian), new Object[]{new ItemStack(obsidian)});
		}
		if(SlimeSpawnHeight != 40)
		{
			EntityRegistry.removeSpawn(EntitySlime.class, EnumCreatureType.monster);
			removeEntity(EntitySlime.class, "Slime", 55);
			EntityRegistry.registerGlobalEntityID(scEntitySlime.class, "Slime", 55);
			EntityRegistry.addSpawn(scEntitySlime.class, 10, 4, 4,EnumCreatureType.monster);
		}
		
		if(netherSpawnLightValue != 15)
		{
			EntityRegistry.removeSpawn(EntityGhast.class, EnumCreatureType.monster, BiomeGenBase.hell);
			removeEntity(EntityGhast.class, "Ghast", 56);
			EntityRegistry.registerGlobalEntityID(scEntityGhast.class, "Ghast", 56);
			EntityRegistry.addSpawn(scEntityGhast.class, 50, 4, 4, EnumCreatureType.monster, BiomeGenBase.hell);
			
			EntityRegistry.removeSpawn(EntityPigZombie.class, EnumCreatureType.monster, BiomeGenBase.hell);
			removeEntity(EntityPigZombie.class, "PigZombie", 57);
			EntityRegistry.registerGlobalEntityID(scEntityPigZombie.class, "PigZombie", 57);
			EntityRegistry.addSpawn(scEntityPigZombie.class, 100, 4, 4, EnumCreatureType.monster, BiomeGenBase.hell);
			
			EntityRegistry.removeSpawn(EntityMagmaCube.class, EnumCreatureType.monster, BiomeGenBase.hell);
			removeEntity(EntityMagmaCube.class, "LavaSlime", 62);
			EntityRegistry.registerGlobalEntityID(scEntityMagmaCube.class, "LavaSlime", 62);
			EntityRegistry.addSpawn(scEntityMagmaCube.class, 1, 4, 4, EnumCreatureType.monster, BiomeGenBase.hell);
//			List monsterList = BiomeGenBase.hell.getSpawnableList(EnumCreatureType.monster);
//			monsterList.clear();
//			monsterList.add(new BiomeGenBase.SpawnListEntry(scEntityGhast.class, 50, 4, 4));
//			monsterList.add(new BiomeGenBase.SpawnListEntry(scEntityPigZombie.class, 100, 4, 4));
//			monsterList.add(new BiomeGenBase.SpawnListEntry(scEntityMagmaCube.class, 1, 4, 4));
		}
	}
	private void removeEntity(Class par1class, String mobName, int id)
	{
		EntityList.classToStringMapping.remove(par1class);
		EntityList.stringToClassMapping.remove(mobName);
		EntityList.IDtoClassMapping.remove(id);
	}
}