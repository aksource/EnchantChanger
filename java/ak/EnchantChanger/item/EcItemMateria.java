package ak.EnchantChanger.item;

import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.MateriaTeleporter;
import ak.EnchantChanger.entity.EcEntityMeteor;
import ak.EnchantChanger.utils.ConfigurationUtils;
import ak.EnchantChanger.utils.EnchantmentUtils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class EcItemMateria extends EcItem
{
	public static final String[] MateriaMagicNames = new String[] { "Black", "White", "Teleport", "Floating",
			"Thunder", "Despell", "Haste", "Absorption" };
    public static final String[] MAGIC_NAME = new String[] { "enchantment.Meteo", "enchantment.Holy", "enchantment.Teleport", "enchantment.Floating",
            "enchantment.Thunder", "enchantment.Despell", "enchantment.Haste", "enchantment.Absorption" };
	public static int MagicMateriaNum = MateriaMagicNames.length;
	public static int[] magicEnch = new int[] { ConfigurationUtils.idEnchantmentMeteor, ConfigurationUtils.idEnchantmentHoly,
			ConfigurationUtils.idEnchantmentTelepo, ConfigurationUtils.idEnchantmentFloat, ConfigurationUtils.idEnchantmentThunder};
	public static boolean GGEnable = false;

    public EcItemMateria(String name) {
        super(name);
    }

    @Override
	public boolean onLeftClickEntity(ItemStack itemstack, EntityPlayer player, Entity entity)
	{
		if (entity instanceof EntityLiving) {
			EntityLiving entityliving = (EntityLiving) entity;
			this.MateriaPotionEffect(itemstack, entityliving, player);
			return false;
		}
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
	{
        if (world.isRemote) return itemstack;
		if (itemstack.stackSize > 1) {
			return itemstack;
		}
		if (itemstack.getItemDamage() == 0 && itemstack.isItemEnchanted()) {
            //NO-OP YEAH!
//			int Lv = EnchantmentUtils.getMateriaEnchLv(itemstack);
//			if (entityplayer.isSneaking() && Lv > 1) {
//				ItemStack expBottle;
//				if (Lv > 5)
//					expBottle = new ItemStack(EnchantChanger.itemExExpBottle);
//				else
//					expBottle = new ItemStack(Items.experience_bottle);
//				if (!world.isRemote)
//					entityplayer.dropPlayerItemWithRandomChoice(expBottle, false);
//				this.addMateriaLv(itemstack, -1);
//			} else if ((entityplayer.experienceLevel >= LevelUPEXP(itemstack, true) || entityplayer.capabilities.isCreativeMode)
//					&& Lv != 0) {
//				entityplayer.addExperienceLevel(-LevelUPEXP(itemstack, true));
//				this.addMateriaLv(itemstack, 1);
//			}
		} else {
			switch (itemstack.getItemDamage()) {
			case 1:
				doMeteor(world, entityplayer);
				break;
			case 2:
				if (entityplayer.isSneaking()) {
                    boolean ggmode = ExtendedPlayerData.get(entityplayer).isGgMode();
                    ExtendedPlayerData.get(entityplayer).setGgMode(!ggmode);
					entityplayer.addChatMessage(new ChatComponentText("Great Gospel Mode " + ExtendedPlayerData.get(entityplayer).isGgMode()));
				} else {
					doHoly(world, entityplayer);
				}
				break;
			case 3:
				teleportPlayer(world, entityplayer);
				break;
			case 5:
				doThunder(world, entityplayer);
				break;
			case 6:
				doDespell(entityplayer, entityplayer);
				break;
			case 7:
				doHaste(entityplayer, entityplayer);
			}
		}
		return itemstack;
	}

//	public void addMateriaLv(ItemStack item, int addLv)
//	{
//		int EnchantmentKind = EnchantmentUtils.getMateriaEnchKind(item);
//		int Lv = EnchantmentUtils.getMateriaEnchLv(item);
//		NBTTagCompound nbt = item.getTagCompound();
//		nbt.removeTag("ench");
//		EnchantmentUtils.addEnchantmentToItem(item, Enchantment.enchantmentsList[EnchantmentKind], Lv + addLv);
//	}

	public void MateriaPotionEffect(ItemStack item, EntityLiving entity, EntityPlayer player)
	{
		if (item.getItemDamage() > 0) {
			switch (item.getItemDamage()) {
			case 6:
				doDespell(player, entity);
				return;
			case 7:
				doHaste(player, entity);
				player.addChatMessage(new ChatComponentText("Haste!"));
				return;
			default:
			}
		} else {
			int EnchantmentKind = EnchantmentUtils.getMateriaEnchKind(item);
			int Lv = EnchantmentUtils.getMateriaEnchLv(item);
			if (EnchantmentKind != 256) {
				int potionNum;
				String Message;
				String EntityName = entity.toString();
				switch (EnchantmentKind) {
				case 1:
					potionNum = 12;
					Message = "fire resistance";
					break;
				case 5:
					potionNum = 13;
					Message = "more Oxygen";
					break;
				case 2:
					potionNum = 8;
					Message = "high jump";
					break;
				case 0:
					potionNum = 11;
					Message = "defence power";
					break;
				default:
					return;
				}
				if (player.experienceLevel > LevelUPEXP(item, false) || player.capabilities.isCreativeMode) {
					entity.addPotionEffect(new PotionEffect(potionNum, 20 * 60 * ConfigurationUtils.minutesMateriaEffects,
							Lv));
					player.addExperienceLevel(-LevelUPEXP(item, false));
					player.addChatMessage(new ChatComponentText(EntityName + " gets " + Message));
                    decreasePlayerFood(player, 6);
				}
			}
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack)
	{
		if (par1ItemStack.getItemDamage() == 0) {
			return par1ItemStack.isItemEnchanted() ? "ItemMateria" : "ItemMateria.Base";
		} else if(par1ItemStack.getItemDamage() < MagicMateriaNum + 1){
			int var3 = par1ItemStack.getItemDamage() - 1;
			return "ItemMateria." + MateriaMagicNames[var3];
		} else {
			return "";
		}
	}

	@Override
    @SuppressWarnings("unchecked")
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List itemList)
	{
		itemList.add(new ItemStack(this, 1, 0));
        ItemStack stack1, stack2, /*stack3, */stack4;
		for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
			if (Enchantment.enchantmentsList[i] != null && !this.isMagicEnch(i)) {
				stack1 = new ItemStack(this, 1, 0);
				stack1.addEnchantment(Enchantment.enchantmentsList[i], 1);
				itemList.add(stack1);
                if (Enchantment.enchantmentsList[i].getMaxLevel() > 1) {
                    stack2 = new ItemStack(this, 1, 0);
                    stack2.addEnchantment(Enchantment.enchantmentsList[i], Enchantment.enchantmentsList[i].getMaxLevel());
                    itemList.add(stack2);
                }
                if (ConfigurationUtils.debug) {
					stack4 = new ItemStack(this, 1, 0);
					stack4.addEnchantment(Enchantment.enchantmentsList[i], 127);
					itemList.add(stack4);
				}
			}
		}
		for (int i = 0; i < MagicMateriaNum; i++) {
			ItemStack magic = new ItemStack(this, 1, 1 + i);
//			if (i < magicEnch.length)
//				magic.addEnchantment(Enchantment.enchantmentsList[magicEnch[i]], 1);
			itemList.add(magic);
		}
	}

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        if (!par1ItemStack.hasTagCompound() && par1ItemStack.getItemDamage() == 0) return;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            String type, info;
            if (par1ItemStack.isItemEnchanted()) {
                Enchantment enchantment = EnchantmentUtils.enchKind(par1ItemStack);
                type = enchantment.type.name();
                info = enchantment.getName();
            } else {
                type = "ecsword";
                info = MAGIC_NAME[(par1ItemStack.getItemDamage() - 1) % MAGIC_NAME.length];
            }

            String enchantmentType = "Type : " + StatCollector.translateToLocal("enchantmenttype." + type);
            String enchantmentInfo = "Info : " + StatCollector.translateToLocal("info." + info);
            par3List.add(enchantmentType);
            par3List.add(enchantmentInfo);
        } else {
            par3List.add("Press " + EnumChatFormatting.BLUE + EnumChatFormatting.ITALIC + "Shift" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY + " Key to get more Info.");
        }
    }

    public boolean isMagicEnch(int enchID)
	{
		for (int i : magicEnch)
			if (enchID == i) return true;
		return false;
	}

    public boolean isFloatingMateria(ItemStack itemStack) {
        return itemStack.getItemDamage() == 4;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack par1ItemStack, int pass)
	{
		return par1ItemStack.getItemDamage() > 0;
	}

	@Override
	public EnumRarity getRarity(ItemStack item)
	{
		if (item.getItemDamage() > 0)
			return EnumRarity.rare;
		else {
			if (EnchantmentUtils.getMateriaEnchKind(item) == 256 || EnchantmentUtils.getMateriaEnchLv(item) < 6)
				return EnumRarity.common;
			else if (EnchantmentUtils.getMateriaEnchLv(item) < 11)
				return EnumRarity.uncommon;
			else
				return EnumRarity.rare;
		}
	}

	public int LevelUPEXP(ItemStack item, boolean next)
	{
		int EnchantmentKind = EnchantmentUtils.getMateriaEnchKind(item);
		int Lv = EnchantmentUtils.getMateriaEnchLv(item);
		int nextLv = next ? 1 : 0;
		if (EnchantmentKind == 256)
			return 0;
		if (Lv < 5 || ConfigurationUtils.difficulty == 0) {
			return Enchantment.enchantmentsList[EnchantmentKind].getMinEnchantability(Lv + nextLv);
		} else {
			return Enchantment.enchantmentsList[EnchantmentKind].getMaxEnchantability(Lv + nextLv);
		}
	}

	public WeightedRandomChestContent addMateriaInChest(int kind, int par2, int par3, int par4)
	{
		ItemStack var6 = new ItemStack(this, 1, kind + 1);
		if (kind < 5)
			var6.addEnchantment(Enchantment.enchantmentsList[ConfigurationUtils.idEnchantmentMeteor + kind], 1);
		return new WeightedRandomChestContent(var6, par2, par3, par4);
	}

	public static void teleportPlayer(World world, EntityPlayer entityplayer)
	{
		if (!canMagic(entityplayer)/* || world.isRemote*/) {
			return;
		}
        Vec3 point;
		if (entityplayer.isSneaking()) {
			ChunkCoordinates spawnPoint;
			int dimID = world.provider.dimensionId;
            boolean shouldTravel;
			if (entityplayer.getBedLocation(dimID) != null) {
				spawnPoint = entityplayer.getBedLocation(dimID);
                shouldTravel = false;
			} else {
                spawnPoint = world.getSpawnPoint();
                shouldTravel = true;
			}
            point = Vec3.createVectorHelper(spawnPoint.posX + 0.5D, spawnPoint.posY, spawnPoint.posZ + 0.5D);
            teleportToChunkCoord(world, entityplayer, point, entityplayer.isSneaking(), shouldTravel, dimID);
		} else {
            point = setTeleportPoint(world, entityplayer);
			if (point != null) {
				teleportToChunkCoord(world, entityplayer, point, entityplayer.isSneaking(), false,
						world.provider.dimensionId);
			}
		}
	}

	private static void teleportToChunkCoord(World world, EntityPlayer entityplayer, Vec3 vector,
			boolean isSneaking, boolean telepoDim, int dimID) {
        if (!world.isRemote) {
//            if (vector == null) {
//                ChunkCoordinates chunk = MinecraftServer.getServer().worldServerForDimension(0).getSpawnPoint();
//                entityplayer.setPositionAndUpdate(chunk.posX, chunk.posY, chunk.posZ);
//            } else {
//                entityplayer.setPositionAndUpdate(vector.xCoord, vector.yCoord, vector.zCoord);
//            }
            entityplayer.fallDistance = 0.0F;
            if (telepoDim) {
                travelDimension(entityplayer, dimID);
            }
            decreasePlayerFood(entityplayer, isSneaking ? 20 : 2);
        } else {
            for (int var2 = 0; var2 < 32; ++var2) {
                world.spawnParticle("portal", entityplayer.posX, entityplayer.posY + world.rand.nextDouble() * 2.0D,
                        entityplayer.posZ, world.rand.nextGaussian(), 0.0D, world.rand.nextGaussian());
            }
        }
        entityplayer.setPositionAndUpdate(vector.xCoord, vector.yCoord, vector.zCoord);
	}

	private static void travelDimension(EntityPlayer player, int nowDim)
	{
		if (nowDim != 0 && player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			transferPlayerToDimension(playerMP.mcServer.getConfigurationManager(), playerMP, 0, new MateriaTeleporter(
					playerMP.mcServer.worldServerForDimension(0)));
		}
	}

	public static void transferPlayerToDimension(ServerConfigurationManager serverConf,
			EntityPlayerMP par1EntityPlayerMP, int par2, Teleporter teleporter) {
		int j = par1EntityPlayerMP.dimension;
		WorldServer worldserver = MinecraftServer.getServer().worldServerForDimension(par1EntityPlayerMP.dimension);
		par1EntityPlayerMP.dimension = par2;
		WorldServer worldserver1 = MinecraftServer.getServer().worldServerForDimension(par1EntityPlayerMP.dimension);
		par1EntityPlayerMP.playerNetServerHandler.sendPacket(new S07PacketRespawn(par1EntityPlayerMP.dimension,
				par1EntityPlayerMP.worldObj.difficultySetting, worldserver1.getWorldInfo().getTerrainType(),
				par1EntityPlayerMP.theItemInWorldManager.getGameType()));
		worldserver.removePlayerEntityDangerously(par1EntityPlayerMP);
		par1EntityPlayerMP.isDead = false;
		serverConf.transferEntityToWorld(par1EntityPlayerMP, par2, worldserver, worldserver1, teleporter);
		serverConf.func_72375_a(par1EntityPlayerMP, worldserver);
		par1EntityPlayerMP.playerNetServerHandler.setPlayerLocation(par1EntityPlayerMP.posX, par1EntityPlayerMP.posY,
				par1EntityPlayerMP.posZ, par1EntityPlayerMP.rotationYaw, par1EntityPlayerMP.rotationPitch);
		par1EntityPlayerMP.theItemInWorldManager.setWorld(worldserver1);
		serverConf.updateTimeAndWeatherForPlayer(par1EntityPlayerMP, worldserver1);
		serverConf.syncPlayerInventory(par1EntityPlayerMP);

		for (Object object : par1EntityPlayerMP.getActivePotionEffects()) {
			PotionEffect potioneffect = (PotionEffect) object;
			par1EntityPlayerMP.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(par1EntityPlayerMP
					.getEntityId(), potioneffect));
		}

		FMLCommonHandler.instance().firePlayerChangedDimensionEvent(par1EntityPlayerMP, j, par2);
	}

	public static Vec3 setTeleportPoint(World world, EntityPlayer entityplayer) {
        double var1 = 1.0D;
		double distLimit = 150.0D;
		double viewX = entityplayer.getLookVec().xCoord;
		double viewY = entityplayer.getLookVec().yCoord;
		double viewZ = entityplayer.getLookVec().zCoord;
/*        double playerPosX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * var1;
        double playerPosY = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * var1 + entityplayer.getYOffset();
        double playerPosZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * var1;*/
        double playerPosX = entityplayer.posX;
        double playerPosY = entityplayer.posY + 1.62D/*1.62D - entityplayer.getYOffset()*/;
        double playerPosZ = entityplayer.posZ;
		Vec3 playerPosition = Vec3.createVectorHelper(playerPosX,
				playerPosY, playerPosZ);
		Vec3 playerLookVec = playerPosition.addVector(viewX * distLimit, viewY * distLimit, viewZ * distLimit);
		MovingObjectPosition MOP = world.rayTraceBlocks(playerPosition, playerLookVec, false);
		if (MOP != null && MOP.typeOfHit == MovingObjectType.BLOCK) {
            int blockSide = MOP.sideHit;
            ForgeDirection direction = ForgeDirection.getOrientation(blockSide);
			double blockPosX = MOP.blockX + 0.5D + direction.offsetX;
			double blockPosY = MOP.blockY + direction.offsetY;
			double blockPosZ = MOP.blockZ + 0.5D + direction.offsetZ;
            if (blockSide == 0) blockPosY--;
			return Vec3.createVectorHelper(blockPosX, blockPosY, blockPosZ);
		} else {
			return null;
		}
	}

	public static void doHoly(World world, EntityPlayer entityplayer)
	{
		if (!canMagic(entityplayer)) {
			return;
		}
		decreasePlayerFood(entityplayer, 6);
        @SuppressWarnings("unchecked")
		List<EntityLivingBase> EntityList = world.getEntitiesWithinAABB(EntityLivingBase.class,
				entityplayer.boundingBox.expand(5D, 5D, 5D));
		for (EntityLivingBase entityLivingBase : EntityList) {
			if (entityLivingBase.isEntityUndead()) {
				int var1 = MathHelper.floor_float(entityLivingBase.getMaxHealth() / 2);
                entityLivingBase.attackEntityFrom(DamageSource.magic, var1);
			}
		}
	}

	public static void doMeteor(World world, EntityPlayer entityplayer)
	{
		if (!canMagic(entityplayer)) {
			return;
		}
		decreasePlayerFood(entityplayer, 6);
		Vec3 EndPoint = setTeleportPoint(world, entityplayer);
		if (EndPoint != null && !world.isRemote)
			world.spawnEntityInWorld(new EcEntityMeteor(world, EndPoint.xCoord, (double) 200, EndPoint.zCoord, 0.0D,
					-1D, 0D, 0.0F, 0.0F));
	}

	public static void doThunder(World world, EntityPlayer entityplayer)
	{
		if (!canMagic(entityplayer)) {
			return;
		}
		decreasePlayerFood(entityplayer, 6);
		Vec3 EndPoint = setTeleportPoint(world, entityplayer);
		if (EndPoint != null)
			world.spawnEntityInWorld(new EntityLightningBolt(world, EndPoint.xCoord, EndPoint.yCoord, EndPoint.zCoord));
	}

	public void doDespell(EntityPlayer player, Entity entity)
	{
		if (entity instanceof EntityLiving) {
			((EntityLiving) entity).clearActivePotions();
			for (int i = 0; i < 33; i++) {
				((EntityLiving) entity).removePotionEffect(i);
			}
			decreasePlayerFood(player, 2);
		}
	}

	public void doHaste(EntityPlayer player, EntityLivingBase entityliving)
	{
		entityliving.addPotionEffect(new PotionEffect(1, 20 * 60 * 5, 1));
		decreasePlayerFood(player, 2);
	}

	private static void decreasePlayerFood(EntityPlayer player, int dec)
	{
		if (!player.capabilities.isCreativeMode) {
			player.getFoodStats().addStats(-dec, 1.0F);
		}
	}

	private static boolean canMagic(EntityPlayer player)
	{
		return player.getFoodStats().getFoodLevel() > 0 || ConfigurationUtils.flagYOUARETERRA
				|| player.capabilities.isCreativeMode;
	}
}
