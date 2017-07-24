package ak.enchantchanger.client.gui;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.inventory.EcContainerMaterializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EcGuiMaterializer extends GuiContainer {
    private static final ResourceLocation GUI = new ResourceLocation(Constants.EcAssetsDomain, Constants.TEXTURES_GUI_MATERIALIZER_PNG);

    public EcGuiMaterializer(World world, InventoryPlayer inventoryPlayer) {
        super(new EcContainerMaterializer(world, inventoryPlayer));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(I18n.translateToLocal("container.materializer"), 8, 6, 4210752);
        fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    protected void keyTyped(char c, int keycode) {
        if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
            Minecraft.getMinecraft().thePlayer.closeScreen();
        }
    }

    public void updateScreen() {
        super.updateScreen();
        EntityPlayer player = mc.thePlayer;
        if (!player.isEntityAlive() || player.isDead) {
            player.closeScreen();
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

}