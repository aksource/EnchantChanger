package ak.ChainDestruction;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.registry.GameRegistry;

public class InteractBlockHook
{
	private int[] blockPos = new int[]{0,0,0,0,0};
//	private ArrayList<ChunkPosition> blocklist = new ArrayList<ChunkPosition>();
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	private int minZ;
	private int maxZ;
	public boolean toggle = false;
	public boolean digUnderToggle = false;
	private boolean digUnder = ChainDestruction.digUnder;
	private boolean doChain = false;
	
	private boolean pressRegisterKey = false;
	private boolean pressDigUnderKey = false;
	
	@SubscribeEvent
	public void KeyPressEvent(KeyInputEvent event)
	{
		if(ClientProxy.registItemKey.isPressed()){
			this.pressRegisterKey = true;
		}
		if(ClientProxy.digUnderKey.isPressed()){
			this.pressDigUnderKey = true;
		}
	}
	
	@SubscribeEvent
	public void PlayerInteractBlock(PlayerInteractEvent event)
	{
		EntityPlayer player = event.entityPlayer;
		World world = player.worldObj;
		ItemStack item = event.entityPlayer.getCurrentEquippedItem();
		if(item != null && ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item.getItem())))
		{
			Block block = world.getBlock(event.x, event.y, event.z);
			if(event.action == Action.RIGHT_CLICK_BLOCK)
			{
				String chat;
				if(player.isSneaking() && !ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(block)))
				{
					ChainDestruction.enableBlocks.add(ChainDestruction.getUniqueStrings(block));
					chat = String.format("Add Block : %s", ChainDestruction.getUniqueStrings(block));
					player.addChatMessage(new ChatComponentTranslation(chat, new Object[0]));
				}
				else if(!player.isSneaking() && ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(block)))
				{
					ChainDestruction.enableBlocks.remove(ChainDestruction.getUniqueStrings(block));
					chat = String.format("Remove Block : %s", ChainDestruction.getUniqueStrings(block));
					player.addChatMessage(new ChatComponentTranslation(chat, new Object[0]));
				}
			}
			else if(event.action == Action.LEFT_CLICK_BLOCK
					&& ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(block))
					&& ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item.getItem())))
			{
				int meta = world.getBlockMetadata(event.x, event.y, event.z);
				blockPos[0] = event.x;
				blockPos[1] = event.y;
				blockPos[2] = event.z;
				blockPos[3] = event.face;
				blockPos[4] = meta;
			}
		}
	}
	@SubscribeEvent
	public void HarvestEvent(HarvestDropsEvent event)
	{
		if(!event.world.isRemote && !doChain &&  ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(event.block)) &&  event.harvester.getCurrentEquippedItem() != null && ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings( event.harvester.getCurrentEquippedItem().getItem())))
		{
			doChain = true;
			setBlockBounds(event.harvester);
			EntityItem ei;
			for(ItemStack stack:event.drops){
				ei = new EntityItem(event.world, event.harvester.posX,event.harvester.posY, event.harvester.posZ, stack);
				ei.delayBeforeCanPickup = 0;
				event.world.spawnEntityInWorld(ei);
			}
			event.drops.clear();
			ChainDestroyBlock(event.world,event.harvester,event.block, event.harvester.getCurrentEquippedItem());
			getFirstDestroyedBlock(event.world,event.harvester,event.block,  event.harvester.getCurrentEquippedItem());
//			synchronized(blocklist){
//				blocklist.clear();
//			}
			for(int i = 0;i<5;i++)
			{
				blockPos[i] = 0;
			}
			doChain = false;
		}
	}
	@SubscribeEvent
	public void LivingUpdate(LivingUpdateEvent event)
	{
		if(event.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			ItemStack item = player.getCurrentEquippedItem();
			World world = event.entityLiving.worldObj;
			String chat;
			if(world.isRemote)
			{
//				this.toggle = CDKeyHandler.regItemKeyDown && CDKeyHandler.regItemKeyUp;
				this.toggle = this.pressRegisterKey;
//				this.digUnderToggle = CDKeyHandler.digUnderKeyDown && CDKeyHandler.digUnderKeyUp;
				this.digUnderToggle = this.pressDigUnderKey;
				ChainDestruction.packetPipeline.sendToServer(new KeyHandlingPacket(toggle, digUnderToggle));
//				PacketDispatcher.sendPacketToServer(PacketHandler.getPacketRegKeyToggle(this));
			}
			if(this.toggle && item != null)
			{
				pressRegisterKey = false;
				if(player.isSneaking() && ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item)))
				{
					ChainDestruction.enableItems.remove(ChainDestruction.getUniqueStrings(item));
					chat = String.format("Remove Tool : %s", ChainDestruction.getUniqueStrings(item));
					player.addChatMessage(new ChatComponentTranslation(chat, new Object[0]));
				}
				if(!player.isSneaking() && !ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item)))
				{
					ChainDestruction.enableItems.add(ChainDestruction.getUniqueStrings(item));
					chat = String.format("Add Tool :  %s", ChainDestruction.getUniqueStrings(item));
					player.addChatMessage(new ChatComponentTranslation(chat, new Object[0]));
				}
			}
			if(this.digUnderToggle)
			{
				pressDigUnderKey = false;
				this.digUnder = !this.digUnder;
				chat = String.format("Dig Under %b", this.digUnder);
				player.addChatMessage(new ChatComponentTranslation(chat, new Object[0]));
			}
			ChainDestruction.digUnder = this.digUnder;
		}
	}
//	public void ChainMethod(World world, EntityPlayer player, ItemStack item, Block block)
//	{
//		if(ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(block)) &&  item != null && ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item.getItem())))
//		{
//			getFirstDestroyedBlock(world,player,block, item);
//			setBlockBounds(player);
//			ChainDestroyBlock(world,player,block, item);
//			blocklist.clear();
//			for(int i = 0;i<5;i++)
//			{
//				blockPos[i] = 0;
//			}
//		}
//	}
	public void getFirstDestroyedBlock(World world, EntityPlayer player, Block block, ItemStack item)
	{
		List<EntityItem> list = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(5d, 5d, 5d));
		if(list == null)
			return;
		double d0;
		double d1;
		double d2;
		float f1 = player.rotationYaw * 0.01745329F;
		int i1 = EnchantmentHelper.getFortuneModifier(player);
		for(EntityItem eItem: list){
			if(eItem.getEntityItem().getItem() instanceof ItemBlock && GameRegistry.findUniqueIdentifierFor(block).equals(GameRegistry.findUniqueIdentifierFor(eItem.getEntityItem().getItem()))
					|| GameRegistry.findUniqueIdentifierFor(block.getItemDropped(blockPos[4], world.rand, i1)).equals(GameRegistry.findUniqueIdentifierFor(eItem.getEntityItem().getItem()))){
				eItem.delayBeforeCanPickup = 0;
				d0 = player.posX - MathHelper.sin(f1) * 0.5D;
				d1 = player.posY + 0.5D;
				d2 = player.posZ + MathHelper.cos(f1) * 0.5D;
				eItem.setPosition(d0, d1, d2);
			}
		}
	}
	public void ChainDestroyBlock(World world, EntityPlayer player, Block block, ItemStack item)
	{
		ChunkPosition chunk;
		Block id;
		int meta;
		boolean flag = false;
		
		for(int side = 0;side <6;side++)
		{
			if(side == blockPos[3]){
				continue;
			}
			chunk = this.getNextChunkPosition(new ChunkPosition(blockPos[0], blockPos[1], blockPos[2]), side);
			id = world.getBlock(chunk.chunkPosX, chunk.chunkPosY, chunk.chunkPosZ);
			if(checkChunkInBounds(chunk) && block == id/* && !blocklist.contains(chunk)*/)
			{
				this.SearchBlock(world, player, block, chunk, ForgeDirection.OPPOSITES[side], item);
			}
		}
//		synchronized(blocklist){
//			Iterator it = blocklist.iterator();			
//			List<EntityItem> list;
//			while(it.hasNext() && !flag){
//				chunk = (ChunkPosition) it.next();
//				flag = destroyBlockAtPosition(world, block, player, chunk, item);
//			}
//		}
	}
	private boolean destroyBlockAtPosition(World world, Block block, EntityPlayer player, ChunkPosition chunk, ItemStack item)
	{
		boolean isMultiToolHolder = false;
		int slotNum = 0;
		IInventory tooldata = null;
//		if(ChainDestruction.loadMTH && item.getItem() instanceof ItemMultiToolHolder)
//		{
//			tooldata = ((ItemMultiToolHolder)item.getItem()).tools;
//			slotNum = ((ItemMultiToolHolder)item.getItem()).SlotNum;
//			item = ((IInventory)tooldata).getStackInSlot(slotNum);
//			isMultiToolHolder = true;
//		}
		int meta = world.getBlockMetadata(chunk.chunkPosX, chunk.chunkPosY, chunk.chunkPosZ);
		if(item == null){
			return true;
		}
		if(item.getItem().onBlockDestroyed(item, world, block, chunk.chunkPosX, chunk.chunkPosY, chunk.chunkPosZ, player)){
			if(world.setBlockToAir(chunk.chunkPosX, chunk.chunkPosY, chunk.chunkPosZ)){
				block.onBlockDestroyedByPlayer(world,chunk.chunkPosX, chunk.chunkPosY, chunk.chunkPosZ, meta);
				block.harvestBlock(world, player, MathHelper.ceiling_double_int( player.posX), MathHelper.ceiling_double_int( player.posY), MathHelper.ceiling_double_int( player.posZ), meta);
				if(item.stackSize == 0){
					destroyItem(player, item, isMultiToolHolder, tooldata, slotNum);
					return true;
				}else return false;
			}else return true;
		}else return true;
	}
	public void destroyItem(EntityPlayer player, ItemStack item, boolean isInMultiTool, IInventory tools, int slotnum)
	{
		if(isInMultiTool){
			tools.setInventorySlotContents(slotnum, null);
			MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, item));
		}else{
			player.destroyCurrentEquippedItem();
		}
	}
	public void SearchBlock(World world, EntityPlayer player, Block block, ChunkPosition chunkpos, int face, ItemStack heldItem)
	{
		Block id;
		ChunkPosition chunk;
		if(this.destroyBlockAtPosition(world, block, player, chunkpos, heldItem)){
			return;
		}
//		synchronized(blocklist){
//			blocklist.add(chunkpos);
//		}
		for(int side = 0;side < 6;side++){
			if(side == face){
				continue;
			}
			chunk = getNextChunkPosition(chunkpos, side);
			id = world.getBlock(chunk.chunkPosX, chunk.chunkPosY, chunk.chunkPosZ);
			if(checkChunkInBounds(chunk) && GameRegistry.findUniqueIdentifierFor(block).equals(GameRegistry.findUniqueIdentifierFor(id))/* && !blocklist.contains(chunk)*/){
				this.SearchBlock(world, player, block, chunk, ForgeDirection.OPPOSITES[side], heldItem);
			}
		}
	}
	private ChunkPosition getNextChunkPosition(ChunkPosition chunk, int side)
	{
		int dx = ForgeDirection.getOrientation(side).offsetX;
		int dy = ForgeDirection.getOrientation(side).offsetY;
		int dz = ForgeDirection.getOrientation(side).offsetZ;
		return new ChunkPosition(chunk.chunkPosX + dx,chunk.chunkPosY + dy,chunk.chunkPosZ + dz);
	}
	public boolean checkChunkInBounds(ChunkPosition chunk)
	{
		boolean bx,by,bz;
		bx = chunk.chunkPosX >= minX && chunk.chunkPosX <= maxX;
		by = chunk.chunkPosY >= minY && chunk.chunkPosY <= maxY;
		bz = chunk.chunkPosZ >= minZ && chunk.chunkPosZ <= maxZ;
		return bx && by && bz;
	}
	public void setBlockBounds(EntityPlayer player)
	{
		minX = blockPos[0] - ChainDestruction.maxDestroyedBlock;
		maxX = blockPos[0] + ChainDestruction.maxDestroyedBlock;
		if(ChainDestruction.digUnder)
			minY = blockPos[1] - ChainDestruction.maxDestroyedBlock;
		else if(blockPos[3] != 1)
			minY = MathHelper.floor_double(player.posY);
		else
			minY = MathHelper.floor_double(player.posY) - 1;
		maxY = blockPos[1] + ChainDestruction.maxDestroyedBlock;
		minZ = blockPos[2] - ChainDestruction.maxDestroyedBlock;
		maxZ = blockPos[2] + ChainDestruction.maxDestroyedBlock;
	}
}