package ak.EnchantChanger.eventhandler;

import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.network.MessageLevitation;
import ak.EnchantChanger.network.PacketHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

public class LivingEventHooks
{
	//	private boolean isLevitation = false;
	private int flyToggleTimer = 0;
	private int sprintToggleTimer = 0;
	private static final int FlightMptime = 20 * 3;
	private static final int GGMptime = 20;
	private static final int AbsorpMptime = 20 * 3;
    private static final float moveFactor = 0.4F;
	private int[] Count = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
//	private int mptimer = FlightMptime;
//    public boolean isMateriaKeyPressed = false;

	@SubscribeEvent
	public void LivingUpdate(LivingUpdateEvent event)
	{
		if (event.entityLiving != null && event.entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entityLiving;
			this.Flight(player);
			this.GreatGospel(player);
			this.Absorption(player.worldObj, player);
//            this.openMateriaWindow(player.worldObj, player);
            //EXPOrb cooldown time set 0.
            ((EntityPlayer)event.entityLiving).xpCooldown = 0;
		}
	}

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)event.entityLiving;
            if (checkFlightAvailable(player)) {
                event.setCanceled(true);
            }
        }

    }

    @SubscribeEvent
    public void fixLevitationDigSpeed(PlayerEvent.BreakSpeed event) {
        if (ExtendedPlayerData.get(event.entityPlayer).isLevitating()) {
            event.newSpeed = event.originalSpeed * 5.0f;
        }
    }

	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		DamageSource killer = event.source;
		if (event.entityLiving instanceof EntityLiving && killer.getEntity() != null && killer.getEntity() instanceof EntityPlayer)
			spawnAPOrb((EntityLiving)event.entityLiving, (EntityPlayer)killer.getEntity());
		else if (event.entityLiving instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
            NBTTagCompound playerData = new NBTTagCompound();
            (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME)).saveNBTData(playerData);
            CommonProxy.storeEntityData(event.entity.getCommandSenderName(), playerData);
            ((ExtendedPlayerData)(event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME))).saveProxyData((EntityPlayer) event.entity);
        }
	}

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
        {
            NBTTagCompound playerData = CommonProxy.getEntityData(event.entity.getCommandSenderName());
            if (playerData != null) {
                (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME)).loadNBTData(playerData);
            }
            ((ExtendedPlayerData)(event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME))).loadProxyData((EntityPlayer)event.entity);
        }
    }

    private void spawnAPOrb(EntityLiving dead, EntityPlayer killer) {
        if (EnchantChanger.enableAPSystem && killer.getCurrentEquippedItem() != null && killer.getCurrentEquippedItem().isItemEnchanted() && !dead.worldObj.isRemote) {
            int exp = ObfuscationReflectionHelper.getPrivateValue(EntityLiving.class, dead, 1);
            long lastTime = ExtendedPlayerData.get(killer).getApCoolingTime();
            if (lastTime != 0 && lastTime - dead.worldObj.getTotalWorldTime() < 20) exp = 2;
            if (exp > 0 ) {
                dead.worldObj.spawnEntityInWorld(new EcEntityApOrb(dead.worldObj, dead.posX, dead.posY,
                        dead.posZ, exp / 2));
                ExtendedPlayerData.get(killer).setApCoolingTime(dead.worldObj.getTotalWorldTime());
            }
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
        boolean allowLevitation = checkFlightAvailable(player);
		if (!allowLevitation) {
			this.setModeToNBT(player, false);
			return;
		}

		if (player.worldObj.isRemote) {
			boolean jump = ((EntityPlayerSP) player).movementInput.jump;
			float var2 = 0.8F;
			boolean var3 = ((EntityPlayerSP) player).movementInput.moveForward >= var2;
			((EntityPlayerSP) player).movementInput.updatePlayerMoveState();
			if (!jump && ((EntityPlayerSP) player).movementInput.jump) {
				if (this.flyToggleTimer == 0) {
					this.flyToggleTimer = 7;
				} else {
					this.setModeToNBT(player, !this.getModeToNBT(player));
					this.flyToggleTimer = 0;
				}
			}
			boolean var4 = (float) player.getFoodStats().getFoodLevel() > 6.0F;

            //Sprint判定。updatePlayerMoveStateしているので、再度判定する必要が有る。
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
            //Sprint判定ここまで。

			if (this.flyToggleTimer > 0) {
				--this.flyToggleTimer;
			}

            boolean var5 = this.getModeToNBT(player);
			if (var5) {
                movePlayerY(player);
                movePlayerXZ(player);
			}

			if (player.onGround && var5) {
				this.setModeToNBT(player, false);
			}

            PacketHandler.INSTANCE.sendToServer(new MessageLevitation(this.getModeToNBT(player)));

		} else if (this.getModeToNBT(player) && MpCount(0, FlightMptime)) {
//				player.getFoodStats().addStats(-1, 1.0F);
            player.getFoodStats().addExhaustion(4.0F);
		}
	}

    private void movePlayerY(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP)player;

        player.motionY = 0.0D;

        if (playerSP.movementInput.sneak) {
            player.motionY -= moveFactor;
        }

        if (playerSP.movementInput.jump) {
            player.motionY += moveFactor;
        }
    }

    private void movePlayerXZ(EntityPlayer player) {
        EntityPlayerSP playerSP = (EntityPlayerSP)player;
        float moveForward = playerSP.movementInput.moveForward;
        float moveStrafe = playerSP.movementInput.moveStrafe;

        if (moveForward != 0 || moveStrafe != 0) {
            player.motionX = player.motionZ = 0;
        }
        player.moveFlying(moveStrafe, moveForward, moveFactor * 1.2F);

    }

	private void GreatGospel(EntityPlayer player)
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
                player.getFoodStats().addStats(-1, 1.0F);
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

	public boolean MpCount(int par1, int par2) {

		Count[par1]++;
		if (Count[par1] > par2) {
			Count[par1] = 0;
			return true;
		} else {
			return false;
		}
	}

    private boolean checkFlightAvailable(EntityPlayer player) {
        return checkFlightItemInInv(player)
                && !(player.capabilities.isCreativeMode || player.capabilities.allowFlying || player.isRiding() || (player.getFoodStats()
                .getFoodLevel() < 0 && !EnchantChanger.YouAreTera));
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

//	public void checkMagic(World world, EntityPlayer player)
//	{
//		ItemStack itemstack = player.getHeldItem();
//		if (itemstack != null && itemstack.getItem() instanceof EcItemSword) {
//			EcItemSword.doMagic(itemstack, world, player);
//		}
//	}

	public static boolean checkFlightItemInInv(EntityPlayer entityplayer)
	{
		boolean ret = false;
		for (int i = 0; i < 9; i++) {
			ItemStack var1 = entityplayer.inventory.getStackInSlot(i);
			if (checkFlightItem(var1))
				ret = checkFlightItem(var1);
		}
		return ret;
	}

	public void setModeToNBT(EntityPlayer player, boolean levi)
	{
		ExtendedPlayerData.get(player).setLevitating(levi);
	}

	public boolean getModeToNBT(EntityPlayer player)
	{
        return ExtendedPlayerData.get(player).isLevitating();
	}
}