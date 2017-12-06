package ak.enchantchanger.client.gui;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.inventory.EcContainerHugeMateria;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EcGuiHugeMateria extends GuiContainer {

    private static final ResourceLocation GUI = new ResourceLocation(Constants.EcAssetsDomain, Constants.TEXTURES_GUI_HUGE_MATERIA);
    private EcTileEntityHugeMateria tileEntity;

    public EcGuiHugeMateria(InventoryPlayer inventoryPlayer, EcTileEntityHugeMateria te) {
        super(new EcContainerHugeMateria(inventoryPlayer, te));
        this.tileEntity = te;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(I18n.translateToLocal("container.hugeMateria"), 8, 6, 4210752);
        fontRenderer.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        int var1;

        var1 = this.tileEntity.getMaterializingProgressScaled(18);
        this.drawTexturedModalRect(x + 97, y + 34, 176, 0, var1 + 1, 16);

    }

    @Override
    protected void keyTyped(char c, int keycode) {
        if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            Minecraft.getMinecraft().player.closeScreen();
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        EntityPlayer player = mc.player;
        if (!player.isEntityAlive() || player.isDead) {
            player.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}