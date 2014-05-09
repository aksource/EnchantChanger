package ak.EnchantChanger;

import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class LivingEventHooks
{
	private boolean allowLevitation = false;
	//	private boolean isLevitation = false;
	private int flyToggleTimer = 0;
	private int sprintToggleTimer = 0;
	private int FlightMptime = 20 * 3;
	private int GGMptime = 20 * 1;
	private int AbsorpMptime = 20 * 3;
	private int[] Count = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	private int mptimer = this.FlightMptime;
    public boolean isMateriaKeyPressed = false;

	@SubscribeEvent
	public void LivingUpdate(LivingUpdateEvent event)
	{
		if (event.entityLiving != null && event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			this.Flight(player);
			this.GreatGospel(player);
			this.Absorption(player.worldObj, player);
            this.openMateriaWindow(player.worldObj, player);
		}
	}

    @SubscribeEvent
    public void fixLevitationDigSpeed(PlayerEvent.BreakSpeed event) {
        if (event.entityPlayer.getEntityData().hasKey("levitation") && event.entityPlayer.getEntityData().getBoolean("levitation")) {
            event.newSpeed = event.originalSpeed * 5.0f;
        }
    }

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		DamageSource killer = event.source;
		EntityLiving entity;
		if (event.entityLiving instanceof EntityLiving && killer.getEntity() != null && killer.getEntity() instanceof EntityPlayer)
			spawnAPOrb((EntityLiving)event.entityLiving, (EntityPlayer)killer.getEntity());
		else if (event.entityLiving instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
            NBTTagCompound playerData = new NBTTagCompound();
            (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME)).saveNBTData(playerData);
            EnchantChanger.proxy.storeEntityData(((EntityPlayer) event.entity).getCommandSenderName(), playerData);
            ((ExtendedPlayerData)(event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME))).saveProxyData((EntityPlayer) event.entity);
        }
	}

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            NBTTagCompound playerData = EnchantChanger.proxy.getEntityData(((EntityPlayer) event.entity).getCommandSenderName());
            if (playerData != null) {
                (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME)).loadNBTData(playerData);
            }
            ((ExtendedPlayerData)(event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME))).loadProxyData((EntityPlayer)event.entity);
        }
    }

    private void spawnAPOrb(EntityLiving dead, EntityPlayer killer) {
        if (EnchantChanger.enableAPSystem && killer.getCurrentEquippedItem() != null && killer.getCurrentEquippedItem().isItemEnchanted() && !dead.worldObj.isRemote) {
            int exp = ObfuscationReflectionHelper.getPrivateValue(EntityLiving.class, dead, 1);
            long lastTime = ExtendedPlayerData.get(killer).getApCoolintTime();
            if (lastTime != 0 && lastTime - dead.worldObj.getTotalWorldTime() < 20) exp = 2;
            dead.worldObj.spawnEntityInWorld(new EcEntityApOrb(dead.worldObj, dead.posX, dead.posY,
                    dead.posZ, exp / 2));
            ExtendedPlayerData.get(killer).setApCoolingTime(dead.worldObj.getTotalWorldTime());
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            ExtendedPlayerData.register((EntityPlayer)event.entity);
        }
    }

	public void Flight(EntityPlayer player)
	{
		this.allowLevitation = this.checkFlightIteminInv(player)
				&& !(player.capabilities.isCreativeMode || player.capabilities.allowFlying || player.isRiding() || (player.getFoodStats()
						.getFoodLevel() < 0 && !EnchantChanger.YouAreTera));
		if (!this.allowLevitation) {
			//			this.isLevitation = false;
			this.setModeToNBT(player, false);
			return;
		}
		player.fallDistance = 0.0f;
		if (player.worldObj.isRemote/* FMLCommonHandler.instance().getEffectiveSide().isClient()*/) {
			boolean jump = ((EntityPlayerSP) player).movementInput.jump;
			float var2 = 0.8F;
			boolean var3 = ((EntityPlayerSP) player).movementInput.moveForward >= var2;
			((EntityPlayerSP) player).movementInput.updatePlayerMoveState();
			if (this.allowLevitation && !jump && ((EntityPlayerSP) player).movementInput.jump) {
				if (this.flyToggleTimer == 0) {
					this.flyToggleTimer = 7;
				} else {
					//					this.isLevitation = !this.isLevitation;
					this.setModeToNBT(player, !this.getModeToNBT(player));
					this.flyToggleTimer = 0;
				}
			}
			boolean var4 = (float) player.getFoodStats().getFoodLevel() > 6.0F;
			if (((EntityPlayerSP) player).onGround && !var3
					&& ((EntityPlayerSP) player).movementInput.moveForward >= var2
					&& ! player.isSprinting() && var4 && !player.isUsingItem()
					&& !player.isPotionActive(Potion.blindness)) {
				if (this.sprintToggleTimer == 0) {
					this.sprintToggleTimer = 7;
				} else {
					 player.setSprinting(true);
					this.sprintToggleTimer = 0;
				}
			}
			if (this.sprintToggleTimer > 0) {
				--this.sprintToggleTimer;
			}
			if (this.flyToggleTimer > 0) {
				--this.flyToggleTimer;
			}
			if (player.onGround && /*this.isLevitation*/this.getModeToNBT(player)) {
				//				this.isLevitation = false;
				this.setModeToNBT(player, false);
			}
			//			this.isLevitation = this.getModeToNBT(player);
			if (/*this.isLevitation*/this.getModeToNBT(player)) {
				player.motionY = 0D;
				player.jumpMovementFactor = 0.1f;
				if (((EntityPlayerSP) player).movementInput.sneak) {
					player.motionY -= 0.4D;
				}

				if (((EntityPlayerSP) player).movementInput.jump) {
					player.motionY += 0.4D;
				}

			} else
				player.jumpMovementFactor = 0.02f;
			if (player.onGround && /*this.isLevitation*/this.getModeToNBT(player)) {
				//	    		this.isLevitation = false;
				this.setModeToNBT(player, false);
			}

			EnchantChanger.packetPipeline.sendToServer(new LevitationPacket(this.getModeToNBT(player)));
		}
		if (/*FMLCommonHandler.instance().getEffectiveSide().isServer()*/!player.worldObj.isRemote && this.getModeToNBT(player)) {
			if (this.mptimer == 0) {
				this.mptimer = this.FlightMptime;
				player.getFoodStats().addStats(-1, 1.0F);
			} else
				--this.mptimer;
		}
	}

	public void GreatGospel(EntityPlayer player)
	{
		if (player.capabilities.isCreativeMode) {
			return;
		}
		if ((player.getFoodStats().getFoodLevel() < 0 && !EnchantChanger.YouAreTera) || !EcItemMateria.GGEnable) {
			player.capabilities.disableDamage = false;
			return;
		}
		ItemStack playerItem = player.getCurrentEquippedItem();
		if (playerItem != null && playerItem.getItem() instanceof EcItemMateria && playerItem.getItemDamage() == 2) {
			player.capabilities.disableDamage = true;
			if (MpCount(1, GGMptime))
				player.getFoodStats().addStats(-1, 1.0f);
		} else {
			player.capabilities.disableDamage = false;
		}
	}

	public void Absorption(World world, EntityPlayer player)
	{
		if (!world.isRemote && player.getFoodStats().getFoodLevel() < 20) {
			if (!MpCount(3, AbsorpMptime)) {
				return;
			}
			ItemStack playerItem = player.getCurrentEquippedItem();
			if (playerItem != null && playerItem.getItem() instanceof EcItemMateria && playerItem.getItemDamage() == 8) {
				List EntityList = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(
						EnchantChanger.AbsorpBoxSize, EnchantChanger.AbsorpBoxSize, EnchantChanger.AbsorpBoxSize));
                for (Object aEntityList : EntityList) {
                    Entity entity = (Entity) aEntityList;
                    if (entity instanceof EntityLiving) {
                        entity.attackEntityFrom(DamageSource.generic, 1);
                        player.getFoodStats().addStats(1, 1.0f);
                    }
                }
			}
		}
	}
    private void openMateriaWindow(World world, EntityPlayer player) {
        EnchantChanger.packetPipeline.sendToServer(new KeyMateriaWindowPacket(this.isMateriaKeyPressed));

        if (isMateriaKeyPressed && !world.isRemote && canOpenMateriaWindow(player)) {
            isMateriaKeyPressed = false;
            player.openGui(EnchantChanger.instance, EnchantChanger.guiIdMateriaWindow, world, MathHelper.ceiling_double_int(player.posX), MathHelper.ceiling_double_int(player.posY), MathHelper.ceiling_double_int(player.posZ));
        } else {
            isMateriaKeyPressed = false;
        }
    }
	public boolean MpCount(int par1, int par2)
	{
		Count[par1]++;
		if (Count[par1] > par2) {
			Count[par1] = 0;
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkFlightItem(ItemStack itemstack)
	{
		if (itemstack == null) {
			return false;
		} else if (itemstack.getItem() instanceof EcItemMateria || itemstack.getItem() instanceof EcItemSword) {
			if (itemstack.getItem() instanceof EcItemMateria) {
				return itemstack.getItemDamage() == 4;
			} else {
				return EcItemSword.hasFloat(itemstack);
			}
		} else {
			return false;
		}
	}

	public void checkMagic(World world, EntityPlayer player)
	{
		ItemStack itemstack = player.getHeldItem();
		if (itemstack != null && itemstack.getItem() instanceof EcItemSword) {
			EcItemSword.doMagic(itemstack, world, player);
		}
	}

	public static boolean checkFlightIteminInv(EntityPlayer entityplayer)
	{
		boolean ret = false;
		for (int i = 0; i < 9; i++) {
			ItemStack var1 = entityplayer.inventory.getStackInSlot(i);
			if (checkFlightItem(var1))
				ret = checkFlightItem(var1);
		}
		return ret;
	}

	private void setModeToNBT(EntityPlayer player, boolean levi)
	{
		ExtendedPlayerData.get(player).setLevitating(levi);
	}

	private boolean getModeToNBT(EntityPlayer player)
	{
        return ExtendedPlayerData.get(player).isLevitating();
	}
    private boolean canOpenMateriaWindow(EntityPlayer player) {
        return ExtendedPlayerData.get(player).getSoldierMode();
    }
	public void readPacketData(boolean var1, EntityPlayer player)
	{
		try {
			this.setModeToNBT(player, var1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}