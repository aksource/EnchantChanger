package ak.enchantchanger.item;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.api.ICustomModelItem;
import ak.enchantchanger.api.ICustomReachItem;
import ak.enchantchanger.api.MagicType;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.capability.IPlayerStatusHandler;
import ak.enchantchanger.client.ClientInputUtils;
import ak.enchantchanger.network.MessageExtendedReachAttack;
import ak.enchantchanger.network.MessageKeyPressed;
import ak.enchantchanger.network.PacketHandler;
import ak.enchantchanger.utils.EnchantmentUtils;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class EcItemSword extends ItemSword implements ICustomReachItem, ICustomModelItem {

    public EcItemSword(ToolMaterial toolMaterial, String name) {
        super(toolMaterial);
        String s = String.format("%s%s", Constants.EcTextureDomain, name);
        this.setUnlocalizedName(s);
        this.setNoRepair();
        this.setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
    }

    public static void doMagic(ItemStack itemStack, World world,
                               EntityPlayer player) {
        byte[] byteArray = EnchantmentUtils.getMagic(itemStack);
        for(byte magicId : byteArray) {
            MagicType.getById(magicId).getConsumer().accept(world, player);
        }
    }

    public static boolean hasFloat(ItemStack itemstack) {
        return hasFloatNBT(itemstack);
    }

    private static boolean hasFloatNBT(ItemStack itemStack) {
        for (byte b : EnchantmentUtils.getMagic(itemStack)) {
            if (b == Constants.MAGIC_ID_FLOAT) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer player,
                                     @Nonnull Entity entity) {
        IPlayerStatusHandler data = CapabilityPlayerStatusHandler.getPlayerStatusHandler(player);
        if (data.isLimitBreaking() && data.getLimitBreakId() == Constants.LIMIT_BREAK_OMNISLASH_FIRST) {
            entity.hurtResistantTime = 0;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn,
                                                    @Nonnull EnumHand handIn) {
        if (worldIn.isRemote && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(Constants.CtrlKEY));
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean advToolTip) {
        if (EnchantmentUtils.hasMagic(itemStack)) {
            for (byte b : EnchantmentUtils.getMagic(itemStack)) {
                list.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal(EcItemMateria.MAGIC_NAME[b - 1]));
            }
        }
    }

    @Override
    @Nonnull
    public Set<String> getToolClasses(@Nonnull ItemStack stack) {
        return ImmutableSet.of("pickaxe");
    }

    @Override
    public int getHarvestLevel(@Nonnull ItemStack stack, @Nonnull String toolClass,
                               @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        return toolClass.equals("pickaxe") ? 2 : 0;
    }

    @Override
    public boolean onEntitySwing(@Nonnull EntityLivingBase entityLiving, @Nonnull ItemStack stack) {
        if (entityLiving.getEntityWorld().isRemote) {
            Minecraft mc = Minecraft.getMinecraft();
            Timer timer = ObfuscationReflectionHelper.getPrivateValue(Minecraft.class, mc, Constants.FIELD_INDEX_MINECRAFT_TIMER);
            RayTraceResult mop = ClientInputUtils.getMouseOverSpecialReach(entityLiving, this.getReach(stack), timer.renderPartialTicks);
            if (mop !=null && mop.typeOfHit == RayTraceResult.Type.ENTITY && mop.entityHit != null) {
                mc.objectMouseOver = mop;
                mc.pointedEntity = mop.entityHit;
                PacketHandler.INSTANCE.sendToServer(new MessageExtendedReachAttack(mop.entityHit));
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    private void doLimitBreak(ItemStack itemStack, EntityPlayer player) {
        byte limitBreakId = CapabilityPlayerStatusHandler.getPlayerStatusHandler(player).getLimitBreakId();
        if (limitBreakId == Constants.LIMIT_BREAK_OMNISLASH_FIRST) {
            doOmniSlashFirst(player);
        }
        if (limitBreakId == Constants.LIMIT_BREAK_POWER_UP) {
            doPowerUp(player);
        }
        player.sendMessage(new TextComponentString("LIMIT BREAK!!"));
    }

    private void doOmniSlashFirst(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, Constants.LIMIT_BREAK_TIME, 3));
    }

    private void doPowerUp(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(MobEffects.SPEED, Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(MobEffects.WATER_BREATHING, Constants.LIMIT_BREAK_TIME, 3));
        player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, Constants.LIMIT_BREAK_TIME, 3));
    }

    // 内蔵武器切り替え用攻撃メソッドの移植
    void attackTargetEntityWithTheItem(Entity entity,
                                              EntityPlayer player, @Nonnull ItemStack stack, boolean cancelHurt) {
        if (MinecraftForge.EVENT_BUS.post(new AttackEntityEvent(player,
                entity))) {
            return;
        }
        if (!stack.isEmpty()
                && stack.getItem().onLeftClickEntity(stack, player, entity)) {
            return;
        }
        if (entity.canBeAttackedWithItem()) {
            if (!entity.hitByEntity(player)) {
                float var2 = (float) this.getItemStrength(stack);
                if (player.isPotionActive(MobEffects.STRENGTH)) {
                    var2 += 3 << player.getActivePotionEffect(
                            MobEffects.STRENGTH).getAmplifier();
                }

                if (player.isPotionActive(MobEffects.WEAKNESS)) {
                    var2 -= 2 << player.getActivePotionEffect(MobEffects.WEAKNESS)
                            .getAmplifier();
                }

                int var3 = 0;
                int var4 = 0;

                if (entity instanceof EntityLivingBase) {
                    var4 = this.getEnchantmentModifierLiving(stack, player,
                            (EntityLivingBase) entity);
                    var3 += EnchantmentHelper.getEnchantmentLevel(
                            Enchantments.KNOCKBACK, stack);
                }

                if (player.isSprinting()) {
                    ++var3;
                }

                if (var2 > 0 || var4 > 0) {
                    boolean var5 = player.fallDistance > 0.0F
                            && !player.onGround && !player.isOnLadder()
                            && !player.isInWater()
                            && !player.isPotionActive(MobEffects.BLINDNESS)
                            && player.getRidingEntity() == null
                            && entity instanceof EntityLivingBase;

                    if (var5 && var2 > 0) {
                        var2 *= 1.5F;
                    }

                    var2 += var4;
                    boolean var6 = false;
                    int var7 = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);

                    if (entity instanceof EntityLivingBase && var7 > 0
                            && !entity.isBurning()) {
                        var6 = true;
                        entity.setFire(1);
                    }

                    boolean var8 = entity.attackEntityFrom(
                            DamageSource.causePlayerDamage(player), var2);

                    if (var8) {
                        if (var3 > 0) {
                            entity.addVelocity(
                                    (double) (-MathHelper
                                            .sin(player.rotationYaw
                                                    * (float) Math.PI / 180.0F)
                                            * (float) var3 * 0.5F),
                                    0.1D,
                                    (double) (MathHelper.cos(player.rotationYaw
                                            * (float) Math.PI / 180.0F)
                                            * (float) var3 * 0.5F));
                            player.motionX *= 0.6D;
                            player.motionZ *= 0.6D;
                            player.setSprinting(false);
                        }

                        if (var5) {
                            player.onCriticalHit(entity);
                        }

                        if (var4 > 0) {
                            player.onEnchantmentCritical(entity);
                        }

                        if (var2 >= 18) {
                            player.addStat(AchievementList.OVERKILL);
                        }

                        player.setLastAttacker(entity);

                        if (entity instanceof EntityLivingBase) {
                            EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entity, player);
                        }
                        EnchantmentHelper.applyArthropodEnchantments(player, entity);
                        Object object = entity;

                        if (entity instanceof EntityDragonPart) {
                            IEntityMultiPart ientitymultipart = ((EntityDragonPart) entity).entityDragonObj;

                            if (ientitymultipart != null && ientitymultipart instanceof EntityLivingBase) {
                                object = ientitymultipart;
                            }
                        }

                        if (!stack.isEmpty() && object instanceof EntityLivingBase) {
                            stack.hitEntity((EntityLivingBase) object, player);
                            if (cancelHurt)
                                entity.hurtResistantTime = 0;
                            if (stack.isEmpty()) {
                                this.destroyTheItem(player, stack, EnumHand.MAIN_HAND);
                            }
                        }
                    }

                    if (entity instanceof EntityLivingBase) {
                        player.addStat(StatList.DAMAGE_DEALT,
                                Math.round(var2 * 10.0F));

                        if (var7 > 0 && var8) {
                            entity.setFire(var7 * 4);
                        } else if (var6) {
                            entity.extinguish();
                        }
                    }

                    player.addExhaustion(0.3F);
                }
            }
        }
    }

    private double getItemStrength(ItemStack item) {
        Multimap multimap = item.getAttributeModifiers(EntityEquipmentSlot.MAINHAND);
        double d1 = 0;
        if (!multimap.isEmpty()) {

            for (Object object : multimap.entries()) {
                Entry entry = (Entry) object;
                AttributeModifier attributemodifier = (AttributeModifier) entry
                        .getValue();
                if (attributemodifier.getOperation() != 1
                        && attributemodifier.getOperation() != 2) {
                    d1 = attributemodifier.getAmount();
                } else {
                    d1 = attributemodifier.getAmount() * 100.0D;
                }
            }
        }
        return d1;
    }

    private int getEnchantmentModifierLiving(ItemStack stack,
                                            EntityLivingBase attacker, EntityLivingBase enemy) {
        int calc = 0;
        if (stack != null) {
            NBTTagList nbttaglist = stack.getEnchantmentTagList();

            if (nbttaglist != null) {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    short short1 = nbttaglist.getCompoundTagAt(i)
                            .getShort("id");
                    short short2 = nbttaglist.getCompoundTagAt(i)
                            .getShort("lvl");
                    Enchantment enchantment = Enchantment.getEnchantmentByID(short1);
                    if (enchantment != null) {
                        calc += enchantment.calcDamageByCreature(short2, enemy.getCreatureAttribute());
                    }
                }
            }
        }
        return calc > 0 ? 1 + attacker.getEntityWorld().rand.nextInt(calc) : 0;
    }

    public void destroyTheItem(EntityPlayer player, ItemStack orig, EnumHand hand) {
    }

    @Override
    public double getReach(@Nonnull ItemStack itemStack) {
        return 4.0D;
    }

    //ServerOnly
    public void doCtrlKeyAction(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        IPlayerStatusHandler data = CapabilityPlayerStatusHandler.getPlayerStatusHandler(entityPlayer);
        if (entityPlayer.isSneaking() && data.canLimitBreak()) {
            data.setLimitGaugeValue(0);
            data.setLimitBreakCount(Constants.LIMIT_BREAK_TIME);
            doLimitBreak(itemStack, entityPlayer);
        } else {
            byte id = (byte) (data.getLimitBreakId() + 1);
            data.setLimitBreakId(id);
        }
    }
}