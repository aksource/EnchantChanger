package ak.enchantchanger.client.renderer;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.item.EcItemSephirothSword;
import ak.enchantchanger.item.EcItemSephirothSwordImit;
import ak.enchantchanger.item.EcItemSword;
import ak.enchantchanger.utils.ConfigurationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;

/**
 * プレイヤーの背中に直前に持っていた追加武器を描画するクラス
 * Created by A.K. on 14/10/28.
 */
public class EcRenderPlayerBack {

    private static final float anglePlayerSneaking = 30F;
    private static final float translateValue = -0.1F;
    private static final float translateSneakingValue = 0F;

    private final ConcurrentHashMap<String, ItemStack> prevHeldMap = new ConcurrentHashMap<>();


    @SubscribeEvent
    @SuppressWarnings("unused")
    public void renderPlayerEvent(RenderPlayerEvent.Post event) {
        EntityPlayer entityPlayer = event.getEntityPlayer();
        String uuidString = entityPlayer.getGameProfile().getId().toString();
        ItemStack prevHeldItem = prevHeldMap.getOrDefault(uuidString, ItemStack.EMPTY);
        if (checkToRender(entityPlayer, prevHeldItem)) {
            renderPlayerBackItem(prevHeldItem, entityPlayer, event.getPartialRenderTick(), event.getX(), event.getY(), event.getZ());
        }
    }

    private boolean checkToRender(EntityPlayer entityPlayer, ItemStack prevHeldItem) {
        return ConfigurationUtils.enableBackSword
                && !prevHeldItem.isEmpty()
                && isBackItem(prevHeldItem)
                && canRenderBack(entityPlayer)
                && !ItemStack.areItemStacksEqual(prevHeldItem, entityPlayer.getHeldItemMainhand())
                && (isSwordInQuickBar(entityPlayer.inventory, prevHeldItem) || entityPlayer instanceof EntityOtherPlayerMP);
    }

    private boolean isSwordInQuickBar(@Nonnull InventoryPlayer inventoryPlayer, @Nonnull ItemStack prevHeldItem) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inventoryPlayer.getStackInSlot(i);
            if (!itemStack.isEmpty() && ItemStack.areItemStacksEqual(itemStack, prevHeldItem)) {
                return true;
            }
        }
        return false;
    }

    private boolean isBackItem(@Nonnull ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof EcItemSword;
    }

    private boolean canRenderBack(@Nonnull EntityPlayer player) {
        ItemStack slotZero = player.inventory.getStackInSlot(0);
        if (!slotZero.isEmpty() && slotZero.getItem().getRegistryName() != null) {
            ResourceLocation registryName = slotZero.getItem().getRegistryName();
            return !registryName.toString().matches(Constants.BATTOUKEN_MOD_ID);
        }
        return true;
    }

    private float getAngle(@Nonnull ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -90F;
        }
        return -160F;
    }

    private float getTranslateSide(@Nonnull ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -0.8F;
        }
        return translateValue;
    }

    private float getTranslateSneakingBack(@Nonnull ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -0.5F;
        }
        return translateSneakingValue;
    }

    private float getTranslateY(@Nonnull ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return 0.7F;
        }
        return 1.5F;
    }

    private void renderPlayerBackItem(@Nonnull ItemStack backItem, @Nonnull EntityLivingBase livingBase, float partialTicks, double x, double y, double z) {
        if (livingBase instanceof EntityOtherPlayerMP) {
            GlStateManager.translate(x, y, z);
        }
        GlStateManager.pushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        this.translateDependingYaw(backItem, livingBase, partialTicks);
        this.rotateYawSneaking(livingBase, partialTicks);
        this.rotateLivingYaw(livingBase, partialTicks, -1.0F);
        GlStateManager.rotate(getAngle(backItem), 0F, 0F, 1.0F);
        GlStateManager.disableLighting();
        mc.getItemRenderer().renderItem(livingBase, backItem, ItemCameraTransforms.TransformType.HEAD);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    private void rotateLivingYaw(@Nonnull EntityLivingBase livingBase, float partialTicks, float sign) {
        float nowYawOffset = getYaw(livingBase, partialTicks);
        GlStateManager.rotate(nowYawOffset * sign, 0.0F, 1.0F, 0.0F);
    }

    private void rotateYawSneaking(@Nonnull EntityLivingBase livingBase, float partialTicks) {
        float nowYawOffset = getYaw(livingBase, partialTicks);
        float radianAngle = (float) (nowYawOffset / 180F * Math.PI);
        if (livingBase.isSneaking()) {
            GlStateManager.rotate(anglePlayerSneaking, MathHelper.cos(radianAngle), 0F, MathHelper.sin(radianAngle));
        }
    }

    private void translateDependingYaw(@Nonnull ItemStack itemStack, @Nonnull EntityLivingBase livingBase, float partialTicks) {
        float nowYawOffset = getYaw(livingBase, partialTicks);
        float add90Offset = nowYawOffset + 90F;
        float radianAngle90 = (float) (add90Offset / 180F * Math.PI);
        float radianAngle = (float) (nowYawOffset / 180F * Math.PI);
        float slideBack = (!livingBase.isSneaking()) ? translateValue : this.getTranslateSneakingBack(itemStack);
        GlStateManager.translate(slideBack * MathHelper.cos(radianAngle90), getTranslateY(itemStack), slideBack * MathHelper.sin(radianAngle90));
        GlStateManager.translate(getTranslateSide(itemStack) * MathHelper.cos(radianAngle), 0F, getTranslateSide(itemStack) * MathHelper.sin(radianAngle));
    }

    private float getYaw(@Nonnull EntityLivingBase livingBase, float partialTicks) {
        return livingBase.prevRenderYawOffset + (livingBase.renderYawOffset - livingBase.prevRenderYawOffset) * partialTicks;
    }

    public void addBackItem(String uuid, ItemStack itemStack) {
        prevHeldMap.put(uuid, itemStack);
    }
}
