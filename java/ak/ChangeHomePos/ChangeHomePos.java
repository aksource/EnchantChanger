package ak.ChangeHomePos;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.village.Village;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;

@Mod(modid="ChangeHomePos", name="ChangeHomePos", version="1.0",dependencies="required-after:FML")
//@NetworkMod(clientSideRequired=true, serverSideRequired=false)

public class ChangeHomePos
{
	@Mod.Instance("ChangeHomePos")
	public static ChangeHomePos instance;
	public static String posChangeItem;
	public static ChunkPosition pos = new ChunkPosition(0, -1, 0);
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		posChangeItem = config.get(Configuration.CATEGORY_GENERAL, "HomePosChangeItemIDs", "minecraft:arrow").getString();
		config.save();
	}
	@Mod.EventHandler
	public void load(FMLInitializationEvent event)
	{
//		MinecraftForge.EVENT_BUS.register(new interactVillegerEventHook());
	}
	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}
	
	public class interactVillegerEventHook
	{
		@SubscribeEvent
		public void interactVilleger(EntityInteractEvent event)
		{
			ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
			String chat;
			if(event.target instanceof EntityVillager && heldItem != null && getUniqueStrings(heldItem.getItem()).equals(posChangeItem) && pos.chunkPosY != -1)
			{
				Village vil = ObfuscationReflectionHelper.getPrivateValue(EntityVillager.class, (EntityVillager)event.target, 3);
				if(vil == null)
					return;
				((EntityVillager)event.target).setHomeArea(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ, (int)((float)(vil.getVillageRadius() * 0.6F)));
				chat = String.format("Set Home Pos x: %d y: %d z: %d", pos.chunkPosX,pos.chunkPosY,pos.chunkPosZ);
				event.entityPlayer.addChatMessage(new ChatComponentTranslation(chat, new Object[]{}));
				event.setCanceled(true);
			}
		}
		@SubscribeEvent
		public void interactBlock(PlayerInteractEvent event)
		{
			String chat;
			if(event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK 
					&& event.entityPlayer.getCurrentEquippedItem() != null 
					&& getUniqueStrings(event.entityPlayer.getCurrentEquippedItem()).equals(posChangeItem))
			{
				pos = new ChunkPosition(event.x, event.y, event.z);
				chat = String.format("Regist Home Pos x: %d y: %d z: %d", event.x,event.y,event.z);
				event.entityPlayer.addChatMessage(new ChatComponentTranslation(chat, new Object[]{}));
			}
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
}