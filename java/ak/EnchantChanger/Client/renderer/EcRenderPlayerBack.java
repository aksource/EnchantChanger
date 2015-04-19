package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.item.EcItemSephirothSword;
import ak.EnchantChanger.item.EcItemSephirothSwordImit;
import ak.EnchantChanger.item.EcItemSword;
import ak.EnchantChanger.utils.ConfigurationUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;

/**
 * Created by A.K. on 14/10/28.
 */
public class EcRenderPlayerBack {

    private ItemStack prevHeldItem = null;
    private ItemStack nowHeldItem = null;

    @SubscribeEvent
    public void renderPlayerEvent(RenderPlayerEvent.Specials.Pre event) {
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
        for (int i= 0; i < 9; i++) {
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
        GL11.glPushMatrix();
        IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(backItem, EQUIPPED);
        if (customRenderer != null) {
            GL11.glScalef(0.8F, 0.8F, 0.8F);
            GL11.glTranslatef(0F, 0F, 0.4F);
            GL11.glRotatef(getAngleZ(backItem), 0F, 0F, 1.0F);
            if (player.isSneaking()) {
                GL11.glRotatef(30F, 1.0F, 0F, 0F);
            }
            GL11.glRotatef(180F, 0F, 1.0F, 0F);
            GL11.glTranslatef(getTransrateX(backItem), -0.5F, 0F);
            customRenderer.renderItem(EQUIPPED, backItem, new RenderBlocks(), player);
        } else {
            GL11.glTranslatef(0.7F, 0F, 0.3F);
            GL11.glRotatef(180F, 0F, 1.0F, 0F);
            GL11.glRotatef(-25F, 0F, 0F, 1.0F);
            renderUsualItem(player, backItem);
        }
        GL11.glPopMatrix();
    }

    private  void renderUsualItem(EntityPlayer entity, ItemStack stack) {
        Minecraft mc = Minecraft.getMinecraft();
        TextureManager texturemanager = mc.getTextureManager();
        IIcon icon = entity.getItemIcon(stack, 0);
        if (icon == null) {
            return;
        }

//        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        texturemanager.bindTexture(texturemanager.getResourceLocation(stack.getItemSpriteNumber()));
        Tessellator tessellator = Tessellator.instance;
        float f = icon.getMinU();
        float f1 = icon.getMaxU();
        float f2 = icon.getMinV();
        float f3 = icon.getMaxV();
        float f4 = 0.0F;
        float f5 = 0.3F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        GL11.glTranslatef(-f4, -f5, 0.0F);
//        float f6 = 1.5F;
//        GL11.glScalef(f6, f6, f6);
//        GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
//        GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
//        GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
        ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, icon.getIconWidth(),
                icon.getIconHeight(), 0.0625F);

        if (stack.hasEffect(0)/* && par3 == 0*/) {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            texturemanager.bindTexture(new ResourceLocation("textures/misc/enchanted_item_glint.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = (float) (Minecraft.getSystemTime() % 3000L) / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = (float) (Minecraft.getSystemTime() % 4873L) / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }
}
