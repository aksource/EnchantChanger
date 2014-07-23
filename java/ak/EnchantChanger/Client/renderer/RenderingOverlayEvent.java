package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.ExtendedPlayerData;
import ak.EnchantChanger.inventory.EcInventoryCloudSword;
import ak.EnchantChanger.item.EcItemCloudSword;
import ak.EnchantChanger.item.EcItemSword;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

/**
 * Created by A.K. on 14/03/11.
 */
@SideOnly(Side.CLIENT)
public class RenderingOverlayEvent {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem itemRenderer = new RenderItem();

    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent.Text event) {
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int width = res.getScaledWidth();
        int height = res.getScaledHeight();
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack holdItem = player.getCurrentEquippedItem();
        if (holdItem != null && holdItem.getItem() instanceof EcItemSword) {
            int slot = player.inventory.currentItem;
            renderLimitGauge(player, slot, width, height, event.partialTicks);
            if (holdItem.getItem() instanceof EcItemCloudSword) {
                renderCloudSwordInventory(holdItem, event.partialTicks);
            }
        }
    }

    private void renderLimitGauge(EntityPlayer player, int slot, int width, int height, float partialTicks) {
        int limitGaugeValue = ExtendedPlayerData.get(player).getLimitGaugeValue();
        int limitGaugeMaxValue = ExtendedPlayerData.LIMIT_GAUGE_MAX;
        double ratio = (double)limitGaugeValue / (double)limitGaugeMaxValue;
        String valueString = String.valueOf(limitGaugeValue);
        int x = width / 2 - 90 + slot * 20 + 2;
        int z = height - 16 - 7;
//        renderCustomStackSizeLikeString(valueString, x, z);
        renderCustomDurationLikeBar(ratio, x, z);
    }

    private void renderCustomStackSizeLikeString(String str, int x, int y) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        int prevFontSize = this.mc.fontRenderer.FONT_HEIGHT;
        this.mc.fontRenderer.FONT_HEIGHT = 2;
        this.mc.fontRenderer.drawStringWithShadow(str, x, y, 0xFFFFFF);
        this.mc.fontRenderer.FONT_HEIGHT = prevFontSize;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    private void renderCustomDurationLikeBar(double value, int x, int y) {
        int j1 = (int)Math.round(/*13.0D - */value * 13.0D);
        int k = (int)Math.round(/*255.0D - */value * 255.0D);

//        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
//        GL11.glDisable(GL11.GL_BLEND);
        Tessellator tessellator = Tessellator.instance;
        int l = 255 - k << 16 | k << 8;
        int i1 = (255 - k) / 4 << 16 | 16128;
        this.renderQuad(tessellator, x + 2, y + 13, 13, 2, 0x000000);
        this.renderQuad(tessellator, x + 2, y + 13, 12, 1, i1);
        this.renderQuad(tessellator, x + 2, y + 13, j1, 1, l);
        //GL11.glEnable(GL11.GL_BLEND); // Forge: Disable Bled because it screws with a lot of things down the line.
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
//        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void renderCloudSwordInventory(ItemStack holdItem, float partialTicks) {
        EcInventoryCloudSword swordData = EcItemCloudSword.getInventoryFromItemStack(holdItem);
        int slot = EcItemCloudSword.getSlotNumFromItemStack(holdItem);

        for (int i = 0; i < 5; i++) {
            int xShift = (i == slot) ? 16 : 0;
            renderInventorySlot(swordData.getStackInSlot(i), EnchantChanger.cloudInvXCoord + xShift, EnchantChanger.cloudInvYCoord + i * 16);
        }
    }

    private void renderInventorySlot(ItemStack itemstack, int par2, int par3)
    {
        if (itemstack != null){
            RenderHelper.enableGUIStandardItemLighting();
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, par2, par3);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, par2, par3);
            RenderHelper.disableStandardItemLighting();
            String s = itemstack.getDisplayName();
            this.mc.fontRenderer.drawStringWithShadow(s, par2 + 16, par3 + 4, 0xFFFFFF);
        }
    }

    /**
     * Adds a quad to the tesselator at the specified position with the set width and height and color.  Args:
     * tessellator, x, y, width, height, color
     */
    private void renderQuad(Tessellator tessellator, int x, int y, int width, int height, int color)
    {
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex((double) (x + 0), (double) (y + 0), 0.0D);
        tessellator.addVertex((double) (x + 0), (double) (y + height), 0.0D);
        tessellator.addVertex((double) (x + width), (double) (y + height), 0.0D);
        tessellator.addVertex((double) (x + width), (double) (y + 0), 0.0D);
        tessellator.draw();
    }
}
