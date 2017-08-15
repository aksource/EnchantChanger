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
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Timer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStack, @Nonnull World worldIn, @Nonnull EntityPlayer playerIn,
                                                    @Nonnull EnumHand handIn) {
        if (worldIn.isRemote && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
            PacketHandler.INSTANCE.sendToServer(new MessageKeyPressed(Constants.CtrlKEY));
        }
        doMagic(playerIn.getHeldItem(handIn), worldIn, playerIn);
        return super.onItemRightClick(itemStack, worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(@Nonnull ItemStack itemStack, @Nonnull EntityPlayer player, @Nonnull List<String> list, boolean advToolTip) {
        if (EnchantmentUtils.hasMagic(itemStack)) {
            for (byte b : EnchantmentUtils.getMagic(itemStack)) {
                list.add(TextFormatting.LIGHT_PURPLE + I18n.translateToLocal(MagicType.getById(b).getInfoString()));
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
        player.addChatMessage(new TextComponentString("LIMIT BREAK!!"));
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
        ItemStack prevItem = player.getHeldItemMainhand();
        player.setHeldItem(EnumHand.MAIN_HAND, stack);
        player.attackTargetEntityWithCurrentItem(entity);
        player.setHeldItem(EnumHand.MAIN_HAND, prevItem);
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