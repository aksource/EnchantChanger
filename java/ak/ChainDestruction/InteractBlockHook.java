package ak.ChainDestruction;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
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
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
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
		if(ClientProxy.registItemKey.func_151468_f()){
			this.pressRegisterKey = true;
		}
		if(ClientProxy.digUnderKey.func_151468_f()){
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
			Block block = world.func_147439_a(event.x, event.y, event.z);
			if(event.action == Action.RIGHT_CLICK_BLOCK)
			{
				String chat;
				if(player.isSneaking() && !ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(block)))
				{
					ChainDestruction.enableBlocks.add(ChainDestruction.getUniqueStrings(block));
					chat = String.format("Add Block : %s", ChainDestruction.getUniqueStrings(block));
					player.func_146105_b(new ChatComponentTranslation(chat, new Object[0]));
				}
				else if(!player.isSneaking() && ChainDestruction.enableBlocks.contains(ChainDestruction.getUniqueStrings(block)))
				{
					ChainDestruction.enableBlocks.remove(ChainDestruction.getUniqueStrings(block));
					chat = String.format("Remove Block : %s", ChainDestruction.getUniqueStrings(block));
					player.func_146105_b(new ChatComponentTranslation(chat, new Object[0]));
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
				ei.field_145804_b = 0;
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
				if(this.digUnderToggle)
				{
					pressDigUnderKey = false;
					this.digUnder = !this.digUnder;
					chat = String.format("Dig Under %b", this.digUnder);
					player.func_146105_b(new ChatComponentTranslation(chat, new Object[0]));
				}
				this.writePacketData((EntityClientPlayerMP) player);
//				PacketDispatcher.sendPacketToServer(PacketHandler.getPacketRegKeyToggle(this));
			}
			if(this.toggle && item != null)
			{
				pressRegisterKey = false;
				if(player.isSneaking() && ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item)))
				{
					ChainDestruction.enableItems.remove(ChainDestruction.getUniqueStrings(item));
					chat = String.format("Remove Tool : %s", ChainDestruction.getUniqueStrings(item));
					player.func_146105_b(new ChatComponentTranslation(chat, new Object[0]));
				}
				if(!player.isSneaking() && !ChainDestruction.enableItems.contains(ChainDestruction.getUniqueStrings(item)))
				{
					ChainDestruction.enableItems.add(ChainDestruction.getUniqueStrings(item));
					chat = String.format("Add Tool :  %s", ChainDestruction.getUniqueStrings(item));
					player.func_146105_b(new ChatComponentTranslation(chat, new Object[0]));
				}
				this.writePacketData((EntityClientPlayerMP) player);
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
					|| GameRegistry.findUniqueIdentifierFor(block.func_149650_a(blockPos[4], world.rand, i1)).equals(GameRegistry.findUniqueIdentifierFor(eItem.getEntityItem().getItem()))){
				eItem.field_145804_b = 0;
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
			id = world.func_147439_a(chunk.field_151329_a, chunk.field_151327_b, chunk.field_151328_c);
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
		int meta = world.getBlockMetadata(chunk.field_151329_a, chunk.field_151327_b, chunk.field_151328_c);
		if(item == null){
			return true;
		}
		if(item.getItem().func_150894_a(item, world, block, chunk.field_151329_a, chunk.field_151327_b, chunk.field_151328_c, player)){
			if(world.func_147468_f(chunk.field_151329_a, chunk.field_151327_b, chunk.field_151328_c)){
				block.func_149664_b(world,chunk.field_151329_a, chunk.field_151327_b, chunk.field_151328_c, meta);
				block.func_149636_a(world, player, MathHelper.ceiling_double_int( player.posX), MathHelper.ceiling_double_int( player.posY), MathHelper.ceiling_double_int( player.posZ), meta);
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
			id = world.func_147439_a(chunk.field_151329_a, chunk.field_151327_b, chunk.field_151328_c);
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
		return new ChunkPosition(chunk.field_151329_a + dx,chunk.field_151327_b + dy,chunk.field_151328_c + dz);
	}
	public boolean checkChunkInBounds(ChunkPosition chunk)
	{
		boolean bx,by,bz;
		bx = chunk.field_151329_a >= minX && chunk.field_151329_a <= maxX;
		by = chunk.field_151327_b >= minY && chunk.field_151327_b <= maxY;
		bz = chunk.field_151328_c >= minZ && chunk.field_151328_c <= maxZ;
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
 	public void writePacketData(EntityClientPlayerMP player)
 	{
 		ByteBuf data = Unpooled.buffer(2);
 		try{
 			data.writeBoolean(this.toggle);
 			data.writeBoolean(this.digUnder);
 			FMLProxyPacket packet = new FMLProxyPacket(data, "ChainDestructionPacket");
 	        player.sendQueue.func_147297_a(packet);
 		}catch (Exception e){
 			e.printStackTrace();
 		}
 	}
 	
 	public class CDServerMsg implements IMessage
 	{
 		public boolean toggle1;
 		public boolean toggle2;
 		@Override
 		public void fromBytes(ByteBuf buf) {
 			try{
 				toggle1 = buf.readBoolean();
 				toggle2 = buf.readBoolean();
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 		}

 		@Override
 		public void toBytes(ByteBuf buf) {
 			try{
 				buf.writeBoolean(toggle1);
 				buf.writeBoolean(toggle2);
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 		}
 		
 	}
 	
 	public class CDClientMsg implements IMessage
 	{
 		public boolean toggle1;
 		public boolean toggle2;
 		@Override
 		public void fromBytes(ByteBuf buf) {
 			try{
 				toggle1 = buf.getBoolean(0);
 				toggle2 = buf.getBoolean(1);
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 		}

 		@Override
 		public void toBytes(ByteBuf buf) {
 			try{
 				buf.setBoolean(0, toggle1);
 				buf.setBoolean(1, toggle2);
 			}catch(Exception e){
 				e.printStackTrace();
 			}
 		}
 	}
}