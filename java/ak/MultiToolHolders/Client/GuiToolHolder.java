package ak.MultiToolHolders.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import ak.MultiToolHolders.ContainerToolHolder;
import ak.MultiToolHolders.MultiToolHolders;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiToolHolder extends GuiContainer
{
	private int Num;
	private ResourceLocation gui3 = new ResourceLocation(MultiToolHolders.Assets, MultiToolHolders.GuiToolHolder3);
	private ResourceLocation gui5 = new ResourceLocation(MultiToolHolders.Assets, MultiToolHolders.GuiToolHolder5);
	private ResourceLocation gui7 = new ResourceLocation(MultiToolHolders.Assets, MultiToolHolders.GuiToolHolder7);
	private ResourceLocation gui9 = new ResourceLocation(MultiToolHolders.Assets, MultiToolHolders.GuiToolHolder9);
	public GuiToolHolder(InventoryPlayer inventoryPlayer, IInventory par2IInventory, int num)
	{
		super(new ContainerToolHolder(inventoryPlayer, par2IInventory, num));
		this.Num = num;
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		//draw text and stuff here
		//the parameters for drawString are: string, x, y, color
		fontRendererObj.drawString(StatCollector.translateToLocal("container.toolholder"), 8, 6, 4210752);
		//draws "Inventory" or your regional equivalent
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 40, 4210752);
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	@Override
	protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
	{
		ResourceLocation res;
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if(this.Num == 3)
			res = gui3;
		else if(this.Num == 5)
			res = gui5;
		else if(this.Num == 7)
			res = gui7;
		else
			res = gui9;
		Minecraft.getMinecraft().getTextureManager().bindTexture(res);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
