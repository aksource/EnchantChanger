package ak.EnchantChanger.Client.gui;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.inventory.EcContainerMateriaWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by A.K. on 14/02/27.
 */
public class EcGuiMateriaWindow extends GuiContainer {
    private static final ResourceLocation GUI = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcGuiMateriaWindow);
    private ItemStack itemStack;
    public EcGuiMateriaWindow(InventoryPlayer inventoryPlayer, ItemStack item, int slotnum) {
        super(new EcContainerMateriaWindow(inventoryPlayer, item, slotnum));
        itemStack = item;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        int itemRenderPosX = (int)((x + 11) / 4.0f);
        int itemRenderPosY = (int)((y + 11) / 4.0f);
        GL11.glPushMatrix();
        itemRender.zLevel = 100.0F;
        GL11.glScalef(4.0f, 4.0f, 1);
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, Minecraft.getMinecraft().renderEngine, itemStack, itemRenderPosX, itemRenderPosY);
        itemRender.zLevel = 0.0F;
        GL11.glPopMatrix();
    }

    protected void keyTyped(char c, int keycode)
    {
        if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode() || keycode == Minecraft.getMinecraft().gameSettings.keyBindDrop.getKeyCode())
        {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        } else super.keyTyped(c, keycode);
    }

    public void updateScreen()
    {
        super.updateScreen();
        if (!Minecraft.getMinecraft().thePlayer.isEntityAlive() ||Minecraft.getMinecraft().thePlayer.isDead)
        {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
