package ak.EnchantChanger.eventhandler;

import ak.EnchantChanger.CommonProxy;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.entity.EcEntityApOrb;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.network.MessagePlayerProperties;
import ak.EnchantChanger.network.PacketHandler;
import ak.EnchantChanger.utils.ConfigurationUtils;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

public class LivingEventHooks {
    public static final int mpTermFlight = 20 * 3;
    private static final int mpTermGG = 20;
    private static final int mpTermAbsorp = 20 * 3;
    private int[] Count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    public static boolean checkFlightItem(ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        } else if (itemstack.getItem() instanceof EcItemMateria || itemstack.getItem() instanceof EcItemSword) {
            if (itemstack.getItem() instanceof EcItemMateria) {
                return ((EcItemMateria) itemstack.getItem()).isFloatingMateria(itemstack);
            } else {
                return EcItemSword.hasFloat(itemstack);
            }
        } else {
            return false;
        }
    }

    public static boolean checkFlightItemInInv(EntityPlayer entityplayer) {
        boolean ret = false;
        for (int i = 0; i < 9; i++) {
            ItemStack var1 = entityplayer.inventory.getStackInSlot(i);
            if (checkFlightItem(var1))
                ret = checkFlightItem(var1);
        }
        return ret;
    }

    public static void setLevitationModeToNBT(EntityPlayer player, boolean levi) {
        ExtendedPlayerData.get(player).setLevitating(levi);
    }

    public static boolean getLevitationModeToNBT(EntityPlayer player) {
        return ExtendedPlayerData.get(player).isLevitating();
    }

    @SubscribeEvent
    public void LivingUpdate(LivingUpdateEvent event) {
        if (event.entityLiving != null && event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            this.doFlight(player);
            this.doGreatGospel(player);
            this.doAbsorption(player.worldObj, player);
            //EXPOrb cooldown time set 0.
            ((EntityPlayer) event.entityLiving).xpCooldown = 0;
        }
    }

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event) {
        if (event.entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
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
    public void onLivingAttackEvent(LivingAttackEvent event) {
        if (!event.entityLiving.worldObj.isRemote) {
            EntityPlayerMP player;
            ItemStack itemStack;
            if (event.source.getDamageType().equals("player") && event.source.getEntity() instanceof EntityPlayerMP) {
                player = (EntityPlayerMP) event.source.getEntity();
                itemStack = player.getCurrentEquippedItem();
                if (itemStack != null && itemStack.getItem() instanceof EcItemSword) {
                    ExtendedPlayerData.get(player).addLimitGaugeValue(1);
                    PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), player);
                }
            }

            if (event.entityLiving instanceof EntityPlayerMP) {
                player = (EntityPlayerMP) event.entityLiving;
                itemStack = player.getCurrentEquippedItem();
                if (itemStack != null && itemStack.getItem() instanceof EcItemSword) {
                    ExtendedPlayerData.get(player).addLimitGaugeValue(1);
                    PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), player);
                }
            }
        }
    }

    @SubscribeEvent
    public void onLivingDeathEvent(LivingDeathEvent event) {
        DamageSource killer = event.source;
        if (event.entityLiving instanceof EntityLiving && killer.getEntity() != null && killer.getEntity() instanceof EntityPlayer)
            spawnAPOrb((EntityLiving) event.entityLiving, (EntityPlayer) killer.getEntity());
        else if (event.entityLiving instanceof EntityPlayer && !event.entity.worldObj.isRemote) {
            EntityPlayer player = (EntityPlayer) event.entityLiving;
            NBTTagCompound playerData = new NBTTagCompound();
            (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME)).saveNBTData(playerData);
            CommonProxy.storeEntityData(player.getGameProfile().getId().toString()/*.getCommandSenderName()*/, playerData);
            ((ExtendedPlayerData) (player.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME))).saveProxyData(player);
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.entity;
            NBTTagCompound playerData = CommonProxy.getEntityData(player.getGameProfile().getId().toString());
            if (playerData != null) {
                (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME)).loadNBTData(playerData);
            }
            ((ExtendedPlayerData) (event.entity.getExtendedProperties(ExtendedPlayerData.EXT_PROP_NAME))).loadProxyData(player);
        }
    }

    private void spawnAPOrb(EntityLiving dead, EntityPlayer killer) {
        if (ConfigurationUtils.enableAPSystem && killer.getCurrentEquippedItem() != null && killer.getCurrentEquippedItem().isItemEnchanted() && !dead.worldObj.isRemote) {
            int exp = ObfuscationReflectionHelper.getPrivateValue(EntityLiving.class, dead, 1);
            long lastTime = ExtendedPlayerData.get(killer).getApCoolingTime();
            if (lastTime != 0 && lastTime - dead.worldObj.getTotalWorldTime() < 20) exp = 2;
            if (exp > 0) {
                dead.worldObj.spawnEntityInWorld(new EcEntityApOrb(dead.worldObj, dead.posX, dead.posY,
                        dead.posZ, exp / 2));
                ExtendedPlayerData.get(killer).setApCoolingTime(dead.worldObj.getTotalWorldTime());
            }
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        if (event.entity instanceof EntityPlayer) {
            ExtendedPlayerData.register((EntityPlayer) event.entity);
        }
    }

    public void doFlight(EntityPlayer player) {
        boolean allowLevitation = checkFlightAvailable(player);
        if (!allowLevitation) {
            setLevitationModeToNBT(player, false);
            return;
        }

        if (player.worldObj.isRemote) {
            EnchantChanger.proxy.doFlightOnSide(player);
        } else if (getLevitationModeToNBT(player) && MpCount(0, mpTermFlight)) {
            player.getFoodStats().addStats(-1, 1.0F);
        }
    }

    private void doGreatGospel(EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            return;
        }
        if ((player.getFoodStats().getFoodLevel() < 0 && !ConfigurationUtils.flagYOUARETERRA) || !ExtendedPlayerData.get(player).isGgMode()) {
            player.capabilities.disableDamage = false;
            return;
        }
        ItemStack playerItem = player.getCurrentEquippedItem();
        if (playerItem != null && playerItem.getItem() instanceof EcItemMateria && playerItem.getItemDamage() == 2) {
            player.capabilities.disableDamage = true;
            if (MpCount(1, mpTermGG))
                player.getFoodStats().addStats(-1, 1.0F);
        } else {
            player.capabilities.disableDamage = false;
        }
    }

//	public void checkMagic(World world, EntityPlayer player)
//	{
//		ItemStack itemstack = player.getHeldItem();
//		if (itemstack != null && itemstack.getItem() instanceof EcItemSword) {
//			EcItemSword.doMagic(itemstack, world, player);
//		}
//	}

    public void doAbsorption(World world, EntityPlayer player) {
        if (!world.isRemote && player.getFoodStats().getFoodLevel() < 20) {
            if (!MpCount(3, mpTermAbsorp)) {
                return;
            }
            ItemStack playerItem = player.getCurrentEquippedItem();
            if (playerItem != null && playerItem.getItem() instanceof EcItemMateria && playerItem.getItemDamage() == 8) {
                List EntityList = world.getEntitiesWithinAABBExcludingEntity(player, player.boundingBox.expand(
                        ConfigurationUtils.sizeAbsorbBox, ConfigurationUtils.sizeAbsorbBox, ConfigurationUtils.sizeAbsorbBox));
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
                .getFoodLevel() < 0 && !ConfigurationUtils.flagYOUARETERRA));
    }
}