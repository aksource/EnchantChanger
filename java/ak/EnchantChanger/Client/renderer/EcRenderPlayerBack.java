package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.item.EcItemSephirothSword;
import ak.EnchantChanger.item.EcItemSephirothSwordImit;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * プレイヤーの背中に直前に持っていた追加武器を描画するクラス
 * Created by A.K. on 14/10/28.
 */
public class EcRenderPlayerBack {

    private static final float anglePlayerSneaking = 30F;
    private static final float translateValue = -0.3F;
    private static final float translateSneakingValue = 0.2F;
    private ItemStack prevHeldItem = null;
    private ItemStack nowHeldItem = null;


    @SubscribeEvent
    public void renderPlayerEvent(RenderPlayerEvent.Post event) {
        if (prevHeldItem == null) return;
        ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
        if (ConfigurationUtils.enableBackSword && !ItemStack.areItemStacksEqual(heldItem, this.prevHeldItem)
                && isBackItem(this.prevHeldItem)) {
            renderPlayerBackItem(this.prevHeldItem/*, event.renderer*/, event.entityPlayer, event.partialRenderTick);
        }
    }

    @SubscribeEvent
    public void checkPreviousItem(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.CLIENT && event.phase == TickEvent.Phase.END) {
            ItemStack heldItem = event.player.getCurrentEquippedItem();
            if (!isSwordInQuickBar(event.player.inventory)) {
                this.prevHeldItem = null;
                return;
            }
            if (heldItem == null) return;

            if (this.nowHeldItem == null) {
                this.nowHeldItem = heldItem;
            }
            if (!ItemStack.areItemStacksEqual(heldItem, this.nowHeldItem)) {
                if (isBackItem(this.nowHeldItem)) {
                    this.prevHeldItem = this.nowHeldItem;
                }
                this.nowHeldItem = heldItem;
            }
        }
    }

    private boolean isSwordInQuickBar(InventoryPlayer inventoryPlayer) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = inventoryPlayer.getStackInSlot(i);
            if (itemStack != null && itemStack.getItem() instanceof EcItemSword) {
                return true;
            }
        }
        return false;
    }

    private boolean isBackItem(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item instanceof EcItemSword;
    }

    private boolean canRenderBack(EntityPlayer player) {
        ItemStack slotZero = player.inventory.getStackInSlot(0);
        if (slotZero != null) {
            GameRegistry.UniqueIdentifier uId = GameRegistry.findUniqueIdentifierFor(slotZero.getItem());
            return uId != null && !uId.modId.equals("battouken");
        }
        return true;
    }

    private float getAngle(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -90F;
        }
        return -20F;
    }

    private float getTranslateSide(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -0.8F;
        }
        return translateValue;
    }

    private float getTranslateSneakingBack(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -0.5F;
        }
        return translateSneakingValue;
    }

    private float getTranslateY(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return 1.0F;
        }
        return 2.0F;
    }

    private void renderPlayerBackItem(ItemStack backItem/*, RenderPlayer renderPlayer*/, EntityLivingBase livingBase, float partialTicks) {
        GlStateManager.pushMatrix();//GL11.glPushMatrix();
        Minecraft mc = Minecraft.getMinecraft();
        if (backItem.getItem() instanceof EcItemSword) {
/*            GlStateManager.scale(0.8F, 0.8F, 0.8F);//GL11.glScalef(0.8F, 0.8F, 0.8F);
            GlStateManager.translate(0F, 0F, 0.4F);//GL11.glTranslatef(0F, 0F, 0.4F);
            GlStateManager.rotate(getAngle(backItem), 0F, 0F, 1.0F);//GL11.glRotatef(getAngle(backItem), 0F, 0F, 1.0F);
            if (player.isSneaking()) {
                GlStateManager.rotate(anglePlayerSneaking, 1.0F, 0F, 0F);//GL11.glRotatef(30F, 1.0F, 0F, 0F);
            }
            GlStateManager.rotate(180F, 0F, 1.0F, 0F);//GL11.glRotatef(180F, 0F, 1.0F, 0F);
            GlStateManager.translate(getTransrateX(backItem), -0.5F, 0F);//GL11.glTranslatef(getTransrateX(backItem), -0.5F, 0F);*/

            this.translateDependingYaw(backItem, livingBase, partialTicks);
            this.rotateYawSneaking(livingBase, partialTicks);
            this.rotateLivingYaw(livingBase, partialTicks, -1.0F);
            GlStateManager.rotate(-90F, 1.0F, 0F, 0F);
            GlStateManager.rotate(getAngle(backItem), 0F, 1.0F, 0F);
            GlStateManager.rotate(90F, 0F, 0F, 1.0F);
            GlStateManager.disableLighting();
            mc.getItemRenderer().renderItem(livingBase, backItem, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.enableLighting();
        }
        GlStateManager.popMatrix();//GL11.glPopMatrix();
    }

    private void rotateLivingYaw(EntityLivingBase livingBase, float partialTicks, float sign) {
        float nowYawOffset = getYaw(livingBase, partialTicks);
        GlStateManager.rotate(nowYawOffset * sign, 0.0F, 1.0F, 0.0F);
    }

    private void rotateYawSneaking(EntityLivingBase livingBase, float partialTicks) {
        float nowYawOffset = getYaw(livingBase, partialTicks);
        float radianAngle = (float) (nowYawOffset / 180F * Math.PI);
        if (livingBase.isSneaking()) {
            GlStateManager.rotate(anglePlayerSneaking, MathHelper.cos(radianAngle), 0F, MathHelper.sin(radianAngle));
        }
    }

    private void translateDependingYaw(ItemStack itemStack, EntityLivingBase livingBase, float partialTicks) {
        float nowYawOffset = getYaw(livingBase, partialTicks);
        float add90Offset= nowYawOffset + 90F;
        float radianAngle90 = (float) (add90Offset / 180F * Math.PI);
        float radianAngle = (float) (nowYawOffset / 180F * Math.PI);
        float slideBack = (!livingBase.isSneaking()) ? translateValue : this.getTranslateSneakingBack(itemStack);
        GlStateManager.translate(slideBack * MathHelper.cos(radianAngle90), getTranslateY(itemStack), slideBack * MathHelper.sin(radianAngle90));
        GlStateManager.translate(getTranslateSide(itemStack) * MathHelper.cos(radianAngle), 0F, getTranslateSide(itemStack) * MathHelper.sin(radianAngle));
    }

    private float getYaw(EntityLivingBase livingBase, float partialTicks) {
        return livingBase.prevRenderYawOffset + (livingBase.renderYawOffset - livingBase.prevRenderYawOffset) * partialTicks;
    }
}
