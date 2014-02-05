package ReplaceBlock;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

@Mod(modid="ReplaceBlock", name="ReplaceBlock", version="1.7srg-1",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class ReplaceBlock
{
	@Mod.Instance("ReplaceBlock")
	public static ReplaceBlock instance;

	public static String[] targetBlockID;
	public static String replaceBlockID;
	public static int targetYposMax;
	public static int targetYposMin;
	public static int chunkWidely;

	private Chunk lastChunk = null;
	private int chunkWide = 16;
	protected ArrayList<Integer> ids = new ArrayList<Integer>();

	public class LivingUpdateHook
	{
		@SubscribeEvent
		public void LivingUpdate(LivingUpdateEvent event)
		{
			if(!event.entityLiving.worldObj.isRemote)
			{
				int posX = (int)Math.floor(event.entityLiving.posX);
				int posZ = (int)Math.floor(event.entityLiving.posZ);
				int posY = (int)Math.floor(event.entityLiving.posY);
				Chunk chunk = event.entityLiving.worldObj.getChunkFromBlockCoords(posX, posZ);
				if(chunk != lastChunk && targetYposMax+chunkWidely >= posY && posY >= targetYposMin-chunkWidely)
				{
					lastChunk = chunk;
					int chunkX = chunk.xPosition * chunkWide;
					int chunkZ = chunk.zPosition * chunkWide;
					for(int x = chunkX-chunkWidely; x < chunkX + chunkWide+chunkWidely; x++){
						for(int z = chunkZ-chunkWidely; z < chunkZ + chunkWide+chunkWidely; z++)
						{
							for(int y = targetYposMax; y >= targetYposMin; y--)
							{
								if(this.isTargetBlock(event.entityLiving.worldObj.getBlock(x, y, z)))
								{
									//minecraft.getIntegratedServer().worldServerForDimension(minecraft.thePlayer.dimension).setBlock(x, y, z, replaceBlockID, 0, 3);
									Block block = GameData.blockRegistry.getObject(replaceBlockID);
									event.entityLiving.worldObj.setBlock(x, y, z, block);
								}
							}
						}
					}
				}
			}
		}
		public boolean isTargetBlock(Block block)
		{
			for(int i = 0; i < targetBlockID.length; i++)
			{
				if(targetBlockID[i].equals(this.getUniqueStrings(block)))
				{
					return true;
				}
			}
			return false;
		}
		public String getUniqueStrings(Object obj)
		{
			UniqueIdentifier uId;
			if(obj instanceof Block) {
				uId = GameRegistry.findUniqueIdentifierFor((Block) obj);
			}else {
				uId = GameRegistry.findUniqueIdentifierFor((Item) obj);
			}
			return uId.modId + ":" + uId.name;

		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		targetBlockID = config.get(Configuration.CATEGORY_GENERAL, "targetBlockNames", new String[]{"minecraft:dirt", "minecraft:bedrock", "minecraft:gravel"}).getStringList();
		replaceBlockID = config.get(Configuration.CATEGORY_GENERAL, "replaceBlockName", "minecraft:stone", "ReplaceBlockName: (format)ModId:UniqueName").getString();
		targetYposMax = config.get(Configuration.CATEGORY_GENERAL, "targetYposMax", 20, "targetYposMax,min=1,max=255").getInt();
		targetYposMax = (targetYposMax < 1)?1:(targetYposMax > 255)?255:targetYposMax;
		targetYposMin = config.get(Configuration.CATEGORY_GENERAL, "targetYposMin", 1, "targetYposMin,min=1,max=255").getInt();
		targetYposMin = (targetYposMin < 1)?1:(targetYposMin > targetYposMax)?targetYposMax:targetYposMin;
		chunkWidely = config.get(Configuration.CATEGORY_GENERAL, "chunkWidely", 3, "chankWidely,min=0,max=16").getInt();
		chunkWidely = (chunkWidely < 0)?0:(chunkWidely > 16)?16:chunkWidely;
		config.save();
	}

	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new LivingUpdateHook());
	}
}