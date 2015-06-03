package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.item.EcItemSephirothSword;
import ak.EnchantChanger.item.EcItemSephirothSwordImit;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.utils.ConfigurationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;

/**
 * Created by A.K. on 14/10/28.
 */
public class EcRenderPlayerBack {

    public static final float anglePlayerSneaking = 30F;
    private ItemStack prevHeldItem = null;
    private ItemStack nowHeldItem = null;


    @SubscribeEvent
    public void renderPlayerEvent(RenderPlayerEvent.Specials.Post event) {
        if (prevHeldItem == null) return;
        ItemStack heldItem = event.entityPlayer.getCurrentEquippedItem();
        if (ConfigurationUtils.enableBackSword && !ItemStack.areItemStacksEqual(heldItem, this.prevHeldItem)
                && isBackItem(this.prevHeldItem)) {
            renderPlayerBackItem(this.prevHeldItem, event.renderer, event.entityPlayer);
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

    private float getAngleZ(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return -25F;
        }
        return 0F;
    }

    private float getTransrateX(ItemStack itemStack) {
        if (itemStack.getItem() instanceof EcItemSephirothSword || itemStack.getItem() instanceof EcItemSephirothSwordImit) {
            return 0.2F;
        }
        return -0.3F;
    }

    private void renderPlayerBackItem(ItemStack backItem, RenderPlayer renderPlayer, EntityPlayer player) {
        GlStateManager.pushMatrix();//GL11.glPushMatrix();

        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(backItem, EQUIPPED);
        if (customRenderer != null) {
            GlStateManager.scale(0.8F, 0.8F, 0.8F);//GL11.glScalef(0.8F, 0.8F, 0.8F);
            GlStateManager.translate(0F, 0F, 0.4F);//GL11.glTranslatef(0F, 0F, 0.4F);
            GlStateManager.rotate(getAngleZ(backItem), 0F, 0F, 1.0F);//GL11.glRotatef(getAngleZ(backItem), 0F, 0F, 1.0F);
            if (player.isSneaking()) {
                GlStateManager.rotate(anglePlayerSneaking, 1.0F, 0F, 0F);//GL11.glRotatef(30F, 1.0F, 0F, 0F);
            }
            GlStateManager.rotate(180F, 0F, 1.0F, 0F);//GL11.glRotatef(180F, 0F, 1.0F, 0F);
            GlStateManager.translate(getTransrateX(backItem), -0.5F, 0F);//GL11.glTranslatef(getTransrateX(backItem), -0.5F, 0F);
            //todo use IModel
            //customRenderer.renderItem(EQUIPPED, backItem, new RenderBlocks(), player);
        } else {
            GlStateManager.translate(0.7F, 0F, 0.3F);//GL11.glTranslatef(0.7F, 0F, 0.3F);
            GlStateManager.rotate(180F, 0F, 1.0F, 0F);//GL11.glRotatef(180F, 0F, 1.0F, 0F);
            GlStateManager.rotate(-25F, 0F, 0F, 1.0F);//GL11.glRotatef(-25F, 0F, 0F, 1.0F);
            if (player.isSneaking()) {
                GlStateManager.rotate(anglePlayerSneaking, 1.0F, 0F, 0F);//GL11.glRotatef(30F, 1.0F, 0F, 0F);
            }
            renderUsualItem(player, backItem);
        }
        GlStateManager.popMatrix();//GL11.glPopMatrix();
    }

    private void renderUsualItem(EntityPlayer entity, ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.getItemRenderer().renderItem(entity, stack, ItemCameraTransforms.TransformType.THIRD_PERSON);
    }
}
