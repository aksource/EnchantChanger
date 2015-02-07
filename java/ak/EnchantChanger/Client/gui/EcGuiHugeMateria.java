package ak.EnchantChanger.Client.gui;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.inventory.EcContainerHugeMateria;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;
@SideOnly(Side.CLIENT)
public class EcGuiHugeMateria extends GuiContainer {

	private EcTileEntityHugeMateria tileEntity;
	private static final ResourceLocation GUI = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcGuiHuge);
	public EcGuiHugeMateria (InventoryPlayer inventoryPlayer, EcTileEntityHugeMateria te)
	{
		//the container is instanciated and passed to the superclass for handling
		super(new EcContainerHugeMateria(inventoryPlayer, te));
		this.tileEntity = te;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString(StatCollector.translateToLocal("container.hugeMateria"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
//		int texture;
		//draw your Gui here, only thing you need to change is the path
//		texture = mc.renderEngine.getTexture(EnchantChanger.EcGuiHuge);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//		this.mc.renderEngine.bindTexture(texture);
//		this.mc.renderEngine.bindTexture(EnchantChanger.EcGuiHuge);
		Minecraft.getMinecraft().getTextureManager().bindTexture(GUI);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		int var1;

		var1 = this.tileEntity.getMaterializingProgressScaled(18);
		this.drawTexturedModalRect(x + 97, y + 34, 176, 0, var1 + 1, 16);
//		this.drawTexturedModalRect(x + 115, y + 15, 176, 16, 18, var1 + 1);
//		this.drawTexturedModalRect(x + 113 + 18 - var1, y + 34, 176 + 18 - var1, 34, var1 + 1, 16);
//		this.drawTexturedModalRect(x + 115, y + 51 + 18 - var1, 176, 50 + 18 - var1, 18, var1 + 1);

	}

    @Override
	protected void keyTyped(char c, int keycode) {
		if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode()) {
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
	}

    @Override
	public void updateScreen() {
		super.updateScreen();
		if (!Minecraft.getMinecraft().thePlayer.isEntityAlive() || Minecraft.getMinecraft().thePlayer.isDead) {
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
	}

    @Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

}