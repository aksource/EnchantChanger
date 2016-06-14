package ak.EnchantChanger.Client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

/**
 * Created by A.K. on 14/10/01.
 */
@SideOnly(Side.CLIENT)
public class EcGuiMakoReactorButton extends GuiButton {
    boolean reversed;

    public EcGuiMakoReactorButton(int id, int xPos, int yPos, boolean reversed) {
        super(id, xPos, yPos, 12, 19, "");
        this.reversed = reversed;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(EcGuiMakoReactor.GUI);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int k = 120;
            int l = 176;

            if (!this.enabled) {
                l += this.width * 2;
            } else if (flag) {
                l += this.width;
            }

            if (!this.reversed) {
                k += this.height;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, l, k, this.width, this.height);
        }
    }
}
