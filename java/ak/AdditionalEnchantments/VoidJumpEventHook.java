package ak.AdditionalEnchantments;

import java.util.Iterator;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class VoidJumpEventHook
{
	@SubscribeEvent
	public void jumpToHomeEvent(LivingHurtEvent event)
	{
		EntityLivingBase entity = event.entityLiving;
		ItemStack armor;
		boolean voidJumpEnchanted = false;
		int lv;
		for(int i = 1;i < 5; i++){
			armor = entity.getCurrentItemOrArmor(i);
			lv = EnchantmentHelper.getEnchantmentLevel(AdditionalEnchantments.idVoidJump, armor);
			if(lv > 0){
				voidJumpEnchanted = true;
				break;
			}
		}
		if(event.source == DamageSource.outOfWorld && voidJumpEnchanted){
			entity.motionX = entity.motionY = entity.motionZ = 0;
			if(!event.entityLiving.worldObj.isRemote)
				jumpToHome(entity);
			entity.fallDistance = 0;
			event.setCanceled(true);
		}
	}
	public void jumpToHome(EntityLivingBase entity)
	{
		ChunkCoordinates positions;
		World world = entity.worldObj;
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer) entity;
			int dim = world.provider.dimensionId;
			if(player.getBedLocation(dim) == null){
				tranferToDimension(0, entity);
			}else{
				positions = player.getBedLocation(dim);
				player.setPositionAndUpdate(positions.posX, positions.posY, positions.posZ);
			}
			this.spawnPortalParticle(player, world);
		}else if(entity instanceof EntityCreature){
			EntityCreature living = (EntityCreature) entity;
			positions = living.getHomePosition();
			living.setPositionAndUpdate(positions.posX, positions.posY, positions.posZ);
		}
	}
	public void tranferToDimension(int dim, EntityLivingBase entity)
	{
		if(entity instanceof EntityPlayerMP){
			EntityPlayerMP playerMP = (EntityPlayerMP) entity;
			WorldServer worldserver1 = MinecraftServer.getServer().worldServerForDimension(dim);
			ChunkCoordinates chunk = worldserver1.getSpawnPoint();
			playerMP.setPositionAndUpdate(chunk.posX, chunk.posY, chunk.posZ);
			transferPlayerToDimension(playerMP. mcServer.getConfigurationManager(), playerMP, dim, new VoidJumpTeleporter(playerMP.mcServer.worldServerForDimension(dim)));
		}
	}
	public static void transferPlayerToDimension(ServerConfigurationManager serverConf, EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter)
	{
		int j = par1EntityPlayerMP.dimension;
		WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(par1EntityPlayerMP.dimension);
		par1EntityPlayerMP.dimension = par2;
		WorldServer worldserver1 = MinecraftServer.getServer().worldServerForDimension(par1EntityPlayerMP.dimension);
		par1EntityPlayerMP.playerNetServerHandler.func_147359_a(new S07PacketRespawn(par1EntityPlayerMP.dimension, par1EntityPlayerMP.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(), par1EntityPlayerMP.theItemInWorldManager.getGameType()));
		worldserver.removePlayerEntityDangerously(par1EntityPlayerMP);
		par1EntityPlayerMP.isDead = false;
		serverConf.transferEntityToWorld(par1EntityPlayerMP, par2, worldserver, worldserver1, teleporter);
		serverConf.func_72375_a(par1EntityPlayerMP, worldserver);
		par1EntityPlayerMP.playerNetServerHandler.func_147364_a(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY, par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
		par1EntityPlayerMP.theItemInWorldManager.setWorld(worldserver1);
		serverConf.updateTimeAndWeatherForPlayer(par1EntityPlayerMP, worldserver1);
		serverConf.syncPlayerInventory(par1EntityPlayerMP);
		Iterator iterator = par1EntityPlayerMP.getActivePotionEffects().iterator();

		while (iterator.hasNext())
		{
			PotionEffect potioneffect = (PotionEffect)iterator.next();
			par1EntityPlayerMP.playerNetServerHandler.func_147359_a(new S1DPacketEntityEffect(par1EntityPlayerMP.func_145782_y(), potioneffect));
		}

		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(par1EntityPlayerMP, j, par2);
	}
	private void spawnPortalParticle(EntityLivingBase entity, World world)
	{
		for (int var2 = 0; var2 < 32; ++var2){
			world.spawnParticle("portal", entity.posX, entity.posY + world.rand.nextDouble() * 2.0D, entity.posZ, world.rand.nextGaussian(), 0.0D, world.rand.nextGaussian());
		}
	}
}