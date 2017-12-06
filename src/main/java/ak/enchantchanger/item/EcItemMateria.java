package ak.enchantchanger.item;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.api.MagicType;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.capability.IPlayerStatusHandler;
import ak.enchantchanger.entity.EcEntityMeteor;
import ak.enchantchanger.utils.ConfigurationUtils;
import ak.enchantchanger.utils.EnchantmentUtils;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcItemMateria extends EcItem {
    private static final Map<ResourceLocation, ResourceLocation> ENCHANTMENT_EFFECT_MAP = new HashMap<>();

    public EcItemMateria(String name) {
        super(name);
    }

    public static void teleportPlayer(World world, EntityPlayer entityplayer) {
        if (!canMagic(entityplayer)/* || world.isRemote*/) {
            return;
        }
        Vec3d point;
        if (entityplayer.isSneaking()) {
            BlockPos spawnPoint;
            int dimID = world.provider.getDimension();
            boolean shouldTravel;
            if (entityplayer.getBedLocation(dimID) != null) {
                spawnPoint = entityplayer.getBedLocation(dimID);
                shouldTravel = false;
            } else {
                spawnPoint = world.getSpawnPoint();
                shouldTravel = true;
            }
            point = new Vec3d(spawnPoint.getX() + 0.5D, spawnPoint.getY(), spawnPoint.getZ() + 0.5D);
            teleportToChunkCoord(world, entityplayer, point, entityplayer.isSneaking(), shouldTravel, dimID);
        } else {
            point = setTeleportPoint(world, entityplayer);
            if (point != null) {
                teleportToChunkCoord(world, entityplayer, point, entityplayer.isSneaking(), false,
                        world.provider.getDimension());
            }
        }
    }

    private static void teleportToChunkCoord(World world, EntityPlayer entityplayer, Vec3d vector,
                                             boolean isSneaking, boolean telepoDim, int dimID) {
        if (!world.isRemote) {
            entityplayer.fallDistance = 0.0F;
            if (telepoDim) {
                travelDimension(entityplayer, dimID);
            }
            decreasePlayerFood(entityplayer, isSneaking ? 20 : 2);
        } else {
            for (int var2 = 0; var2 < 32; ++var2) {
                world.spawnParticle(EnumParticleTypes.PORTAL, entityplayer.posX, entityplayer.posY + world.rand.nextDouble() * 2.0D,
                        entityplayer.posZ, world.rand.nextGaussian(), 0.0D, world.rand.nextGaussian());
            }
        }
        entityplayer.setPositionAndUpdate(vector.x, vector.y, vector.z);
    }

    private static void travelDimension(EntityPlayer player, int nowDim) {
        if (nowDim != 0 && player instanceof EntityPlayerMP) {
            player.changeDimension(0);
        }
    }

    public static Vec3d setTeleportPoint(World world, EntityPlayer entityplayer) {
        double distLimit = 150.0D;
        double viewX = entityplayer.getLookVec().x;
        double viewY = entityplayer.getLookVec().y;
        double viewZ = entityplayer.getLookVec().z;
        double playerPosX = entityplayer.posX;
        double playerPosY = entityplayer.posY + 1.62D;
        double playerPosZ = entityplayer.posZ;
        Vec3d playerPosition = new Vec3d(playerPosX, playerPosY, playerPosZ);
        Vec3d playerLookVec = playerPosition.addVector(viewX * distLimit, viewY * distLimit, viewZ * distLimit);
        RayTraceResult rayTraceResult = world.rayTraceBlocks(playerPosition, playerLookVec, false);
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) {
            EnumFacing blockSide = rayTraceResult.sideHit;
            BlockPos blockPos = rayTraceResult.getBlockPos();
            if (blockSide == EnumFacing.DOWN) {
                blockPos = blockPos.down(2);
            } else {
                blockPos = blockPos.offset(blockSide);
            }
            blockPos = blockPos.add(0.5D, 0, 0.5D);

            return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        } else {
            return null;
        }
    }

    public static void doHoly(World world, EntityPlayer entityplayer) {
        if (!canMagic(entityplayer)) {
            return;
        }
        decreasePlayerFood(entityplayer, 6);
        List<EntityLivingBase> EntityList = world.getEntitiesWithinAABB(EntityLivingBase.class,
                entityplayer.getEntityBoundingBox().expand(5D, 5D, 5D));
        for (EntityLivingBase entityLivingBase : EntityList) {
            if (entityLivingBase.isEntityUndead()) {
                int var1 = MathHelper.floor(entityLivingBase.getMaxHealth() / 2);
                entityLivingBase.attackEntityFrom(DamageSource.MAGIC, var1);
            }
        }
    }

    public static void doMeteor(World world, EntityPlayer entityplayer) {
        if (!canMagic(entityplayer)) {
            return;
        }
        decreasePlayerFood(entityplayer, 6);
        Vec3d point = setTeleportPoint(world, entityplayer);
        if (point != null && !world.isRemote)
            world.spawnEntity(new EcEntityMeteor(world, point.x, (double) 200, point.z, 0.0D,
                    -1D, 0D, 0.0F, 0.0F));
    }

    public static void doThunder(World world, EntityPlayer entityplayer) {
        if (!canMagic(entityplayer)) {
            return;
        }
        decreasePlayerFood(entityplayer, 6);
        Vec3d EndPoint = setTeleportPoint(world, entityplayer);
        if (EndPoint != null && !world.isRemote) {
            world.addWeatherEffect(new EntityLightningBolt(world, EndPoint.x, EndPoint.y, EndPoint.z, false));
        }
    }

    private static void decreasePlayerFood(EntityPlayer player, int dec) {
        if (!player.capabilities.isCreativeMode) {
            player.getFoodStats().addStats(-dec, 1.0F);
        }
    }

    private static boolean canMagic(EntityPlayer player) {
        return player.getFoodStats().getFoodLevel() > 0 || ConfigurationUtils.flagYOUARETERRA
                || player.capabilities.isCreativeMode;
    }

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack itemstack, @Nonnull EntityPlayer player, @Nonnull Entity entity) {
        if (entity instanceof EntityLiving) {
            EntityLiving entityliving = (EntityLiving) entity;
            this.applyMateriaPotionEffect(itemstack, entityliving, player);
            return false;
        }
        return false;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemStack = playerIn.getHeldItem(handIn);
        // Clientかスタック数が>1の場合はそのままリターン
        if (worldIn.isRemote || itemStack.getCount() > 1)
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);

        if (itemStack.getItemDamage() == 0 && itemStack.isItemEnchanted()) {
            // 通常のマテリアは今のところ処理なし
        } else {
            // 魔法マテリアは処理あり
            IPlayerStatusHandler status = CapabilityPlayerStatusHandler.getPlayerStatusHandler(playerIn);
            switch (itemStack.getItemDamage()) {
                case 1:
                    doMeteor(worldIn, playerIn);
                    break;
                case 2:
                    if (playerIn.isSneaking()) {
                        boolean ggmode = status.isGgMode();
                        status.setGgMode(!ggmode);
                        playerIn.sendMessage(new TextComponentString("Great Gospel Mode " + status.isGgMode()));
                    } else {
                        doHoly(worldIn, playerIn);
                    }
                    break;
                case 3:
                    teleportPlayer(worldIn, playerIn);
                    break;
                case 5:
                    doThunder(worldIn, playerIn);
                    break;
                case 6:
                    doDespell(playerIn, playerIn);
                    break;
                case 7:
                    doHaste(playerIn, playerIn);
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
    }

    public void applyMateriaPotionEffect(ItemStack item, EntityLiving entity, EntityPlayer player) {
        if (item.getItemDamage() > 0) {
            switch (item.getItemDamage()) {
                case 6:
                    doDespell(player, entity);
                    return;
                case 7:
                    doHaste(player, entity);
                    player.sendMessage(new TextComponentString("Haste!"));
                    return;
                default:
            }
        } else {
            ResourceLocation registerName = EnchantmentUtils.getEnchantmentRegisterName(item);
            int lv = EnchantmentUtils.getEnchantmentLv(item);
            String EntityName = entity.toString();
            ResourceLocation potionRegName = ENCHANTMENT_EFFECT_MAP.get(registerName);
            if (potionRegName != null && Potion.getPotionFromResourceLocation(potionRegName.toString()) != null) {
                Potion potion = Potion.getPotionFromResourceLocation(potionRegName.toString());
                if (player.experienceLevel > getLevelUPEXP(item, false) || player.capabilities.isCreativeMode) {
                    entity.addPotionEffect(new PotionEffect(potion, 20 * 60 * ConfigurationUtils.minutesMateriaEffects,
                            lv));
                    player.addExperienceLevel(-getLevelUPEXP(item, false));
                    player.sendMessage(new TextComponentString(EntityName + " gets " + potion.getName()));
                    decreasePlayerFood(player, 6);
                }
            }
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack itemStack) {
        if (itemStack.getItemDamage() == 0) {
            return itemStack.isItemEnchanted() ? "ItemMateria" : "ItemMateria.Base";
        } else {
            return "ItemMateria." + MagicType.getById(itemStack.getItemDamage()).getMagicName();
        }
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (Constants.TAB_ENCHANT_CHANGER.equals(tab)) {
            items.add(new ItemStack(this, 1, 0));
            ItemStack stack1, stack2, stack4;
            for (Enchantment enchantment : Enchantment.REGISTRY) {
                stack1 = new ItemStack(this, 1, 0);
                stack1.addEnchantment(enchantment, 1);
                items.add(stack1);
                if (enchantment.getMaxLevel() > 1) {
                    stack2 = new ItemStack(this, 1, 0);
                    stack2.addEnchantment(enchantment, enchantment.getMaxLevel());
                    items.add(stack2);
                }
                if (ConfigurationUtils.debug) {
                    stack4 = new ItemStack(this, 1, 0);
                    stack4.addEnchantment(enchantment, 127);
                    items.add(stack4);
                }
            }
            for (MagicType type : MagicType.values()) {
                ItemStack magic = new ItemStack(this, 1, type.getId());
                items.add(magic);
            }
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound() && stack.getItemDamage() == 0) return;
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            String type, info;
            if (stack.isItemEnchanted()) {
                Enchantment enchantment = EnchantmentUtils.getEnchantmentFromItemStack(stack);
                type = enchantment.type.name();
                info = enchantment.getName();
            } else {
                type = "ecsword";
                info = MagicType.getById(stack.getItemDamage()).getInfoString();
                ;
            }

            String enchantmentType = "Type : " + I18n.translateToLocal("enchantmenttype." + type);
            String enchantmentInfo = "Info : " + I18n.translateToLocal("info." + info);
            tooltip.add(enchantmentType);
            tooltip.add(enchantmentInfo);
        } else {
            tooltip.add("Press " + TextFormatting.BLUE + TextFormatting.ITALIC + "Shift" + TextFormatting.RESET + TextFormatting.GRAY + " Key to get more Info.");
        }
    }

    public boolean isFloatingMateria(ItemStack itemStack) {
        return itemStack.getItemDamage() == 4;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack itemStack) {
        return itemStack.getItemDamage() > 0;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack item) {
        if (item.getItemDamage() > 0)
            return EnumRarity.RARE;
        else {
            if (EnchantmentUtils.getEnchantmentRegisterName(item) == null || EnchantmentUtils.getEnchantmentLv(item) < 6)
                return EnumRarity.COMMON;
            else if (EnchantmentUtils.getEnchantmentLv(item) < 11)
                return EnumRarity.UNCOMMON;
            else
                return EnumRarity.RARE;
        }
    }

    public int getLevelUPEXP(ItemStack item, boolean next) {
        ResourceLocation registerName = EnchantmentUtils.getEnchantmentRegisterName(item);
        int Lv = EnchantmentUtils.getEnchantmentLv(item);
        int nextLv = next ? 1 : 0;
        if (registerName == null)
            return 0;
        if (Lv < 5 || ConfigurationUtils.difficulty == 0) {
            return Enchantment.getEnchantmentByLocation(registerName.toString()).getMinEnchantability(Lv + nextLv);
        } else {
            return Enchantment.getEnchantmentByLocation(registerName.toString()).getMaxEnchantability(Lv + nextLv);
        }
    }

    public static void doDespell(EntityPlayer player, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            ((EntityLiving) entity).clearActivePotions();
            for (Potion potion : Potion.REGISTRY) {
                ((EntityLiving) entity).removePotionEffect(potion);
            }
            decreasePlayerFood(player, 2);
        }
    }

    public static void doHaste(EntityPlayer player, EntityLivingBase entityliving) {
        entityliving.addPotionEffect(new PotionEffect(MobEffects.SPEED, 20 * 60 * 5, 1));
        decreasePlayerFood(player, 2);
    }

    static {
        ENCHANTMENT_EFFECT_MAP.put(Enchantments.FIRE_PROTECTION.getRegistryName(), MobEffects.FIRE_RESISTANCE.getRegistryName());
        ENCHANTMENT_EFFECT_MAP.put(Enchantments.AQUA_AFFINITY.getRegistryName(), MobEffects.WATER_BREATHING.getRegistryName());
        ENCHANTMENT_EFFECT_MAP.put(Enchantments.FEATHER_FALLING.getRegistryName(), MobEffects.JUMP_BOOST.getRegistryName());
        ENCHANTMENT_EFFECT_MAP.put(Enchantments.PROTECTION.getRegistryName(), MobEffects.RESISTANCE.getRegistryName());
    }
}
