package ak.enchantchanger.eventhandler;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.entity.EcEntityApOrb;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.item.EcItemSword;
import ak.enchantchanger.utils.ConfigurationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class LivingEventHooks {
    private static final int mpTermFlight = 20 * 3;
    private static final int mpTermGG = 20;
    private static final int mpTermAbsorp = 20 * 3;
    private int[] Count = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private boolean checkFlightItem(ItemStack itemstack) {
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

    private boolean checkFlightItemInInv(EntityPlayer entityplayer) {
        boolean ret = false;
        for (int i = 0; i < 9; i++) {
            ItemStack var1 = entityplayer.inventory.getStackInSlot(i);
            if (checkFlightItem(var1))
                ret = checkFlightItem(var1);
        }
        return ret;
    }

    public void setLevitationModeToNBT(@Nonnull EntityPlayer player, boolean levi) {
        CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).setLevitating(levi);
    }

    public boolean getLevitationModeToNBT(@Nonnull EntityPlayer player) {
        return CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).isLevitating();
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingUpdate(LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            this.doFlight(player);
            this.doGreatGospel(player);
            this.doAbsorption(player.getEntityWorld(), player);
            //EXPOrb cool down time set 0.
            ((EntityPlayer) event.getEntityLiving()).xpCooldown = 0;
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onPlayerFalling(LivingFallEvent event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            if (checkFlightAvailable(player)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void fixLevitationDigSpeed(PlayerEvent.BreakSpeed event) {
        if (getLevitationModeToNBT(event.getEntityPlayer())) {
            event.setNewSpeed(event.getOriginalSpeed() * 5.0f);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingAttackEvent(LivingAttackEvent event) {
        if (!event.getEntityLiving().getEntityWorld().isRemote) {
            EntityPlayer player;
            ItemStack itemStack;
            if (event.getSource().getDamageType().equals("player") && event.getSource().getSourceOfDamage() instanceof EntityPlayer) {
                player = (EntityPlayer) event.getSource().getSourceOfDamage();
                addLimitGaugeValue(player);
            }

            if (event.getEntityLiving() instanceof EntityPlayer) {
                player = (EntityPlayer) event.getEntityLiving();
                addLimitGaugeValue(player);
            }
        }
    }

    private void addLimitGaugeValue(EntityPlayer player) {
        ItemStack itemStack = player.getHeldItemMainhand();
        if (itemStack != null && itemStack.getItem() instanceof EcItemSword) {
            CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).addLimitGaugeValue(1);
            //PacketHandler.INSTANCE.sendTo(new MessagePlayerProperties(player), (EntityPlayerMP) player);
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onLivingDeathEvent(LivingDeathEvent event) {
        DamageSource killer = event.getSource();
        if (event.getEntityLiving() instanceof EntityLiving && killer.getSourceOfDamage() != null && killer.getSourceOfDamage() instanceof EntityPlayer)
            spawnAPOrb((EntityLiving) event.getEntityLiving(), (EntityPlayer) killer.getSourceOfDamage());
    }

    private void spawnAPOrb(@Nonnull EntityLiving dead, @Nonnull EntityPlayer killer) {
        if (ConfigurationUtils.enableAPSystem
                && killer.getHeldItemMainhand() != null
                && killer.getHeldItemMainhand().isItemEnchanted()
                && !dead.getEntityWorld().isRemote) {
            Method getExperiencePoints = ReflectionHelper.findMethod(EntityLivingBase.class, dead,
                    new String[]{"getExperiencePoints", "func_70693_a"}, EntityPlayer.class);
            int exp = 0;
            try {
                @SuppressWarnings("unchecked")
                Integer ret = (Integer) getExperiencePoints.invoke(dead, killer);
                exp = ret;
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            long lastTime = CapabilityPlayerStatusHandler.getPlayerStatusHandler(killer).getApCoolingTime();
            if (lastTime != 0 && lastTime - dead.getEntityWorld().getTotalWorldTime() < 20) exp = 2;
            if (exp > 0) {
                dead.getEntityWorld().spawnEntityInWorld(new EcEntityApOrb(dead.getEntityWorld(), dead.posX, dead.posY,
                        dead.posZ, exp / 2));
                CapabilityPlayerStatusHandler.getPlayerStatusHandler(killer).setApCoolingTime(dead.getEntityWorld().getTotalWorldTime());
            }
        }
    }

    private void doFlight(EntityPlayer player) {
        boolean allowLevitation = checkFlightAvailable(player);
        if (!allowLevitation) {
            setLevitationModeToNBT(player, false);
            return;
        }

        if (player.getEntityWorld().isRemote) {
            EnchantChanger.proxy.doFlightOnSide(player);
        } else if (getLevitationModeToNBT(player) && MpCount(0, mpTermFlight)) {
            player.getFoodStats().addStats(-1, 1.0F);
        }
    }

    private void doGreatGospel(EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            return;
        }
        if ((player.getFoodStats().getFoodLevel() < 0 && !ConfigurationUtils.flagYOUARETERRA)
                || !CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).isGgMode()) {
            player.capabilities.disableDamage = false;
            return;
        }
        ItemStack playerItem = player.getHeldItemMainhand();
        if (playerItem != null && playerItem.getItem() instanceof EcItemMateria && playerItem.getItemDamage() == 2) {
            player.capabilities.disableDamage = true;
            if (MpCount(1, mpTermGG))
                player.getFoodStats().addStats(-1, 1.0F);
        } else {
            player.capabilities.disableDamage = false;
        }
    }

    private void doAbsorption(World world, EntityPlayer player) {
        if (!world.isRemote && player.getFoodStats().getFoodLevel() < 20) {
            if (!MpCount(3, mpTermAbsorp)) {
                return;
            }
            ItemStack playerItem = player.getHeldItemMainhand();
            if (playerItem != null && playerItem.getItem() instanceof EcItemMateria && playerItem.getItemDamage() == 8) {
                List EntityList = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().expand(
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

    private boolean MpCount(int par1, int par2) {

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
                && !(player.capabilities.isCreativeMode
                || player.capabilities.allowFlying
                || player.isRiding()
                || (player.getFoodStats().getFoodLevel() < 0 && !ConfigurationUtils.flagYOUARETERRA));
    }
}