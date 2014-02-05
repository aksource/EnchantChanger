package ak.EnchantChanger.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import ak.EnchantChanger.EcContainerMaterializer;
import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class EcGuiMaterializer extends GuiContainer
{
	private ResourceLocation gui = new ResourceLocation(EnchantChanger.EcAssetsDomain, EnchantChanger.EcGuiMaterializer);
	public EcGuiMaterializer (World world, InventoryPlayer inventoryPlayer)
	{
		//the container is instanciated and passed to the superclass for handling
		super(new EcContainerMaterializer(world, inventoryPlayer));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString(StatCollector.translateToLocal("container.materializer"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2,int par3) {
		//draw your Gui here, only thing you need to change is the path
//		int texture;
//		texture = mc.renderEngine.getTexture(EnchantChanger.EcGuiMaterializer);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//		this.mc.renderEngine.bindTexture(texture);
//		this.mc.renderEngine.bindTexture(EnchantChanger.EcGuiMaterializer);
		Minecraft.getMinecraft().getTextureManager().bindTexture(gui);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	protected void keyTyped(char c, int keycode)
	{
		if (keycode == 1 || keycode == Minecraft.getMinecraft().gameSettings.keyBindInventory.getKeyCode())
		{
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
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