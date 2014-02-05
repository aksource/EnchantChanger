package ak.HyperDimensionalBag;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBag extends GuiContainer
{
	private ResourceLocation guiTex = new ResourceLocation(HyperDimensionalBag.Assets, HyperDimensionalBag.GuiBagTex);
	IInventory bagData;
	int metaData;
	public GuiBag(InventoryPlayer inv, IInventory data, int meta)
	{
		super(new ContainerBag(inv, data, meta));
		bagData = data;
		metaData = meta;
        short short1 = 222;
        int i = short1 - 108;
        this.ySize = i + 6 * 18;
	}
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		//draw your Gui here, only thing you need to change is the path
		int texture;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(guiTex);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
	
}