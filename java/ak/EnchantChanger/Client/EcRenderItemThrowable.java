package ak.EnchantChanger.Client;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ak.EnchantChanger.EcEntityExExpBottle;
import ak.EnchantChanger.EcEntityMeteo;
import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EcRenderItemThrowable extends Render
{
	private float RenderSize;
	private ResourceLocation texMeteo = new ResourceLocation(EnchantChanger.EcAssetsDomain,EnchantChanger.EcMeteoPNG);
	private ResourceLocation texExp = new ResourceLocation(EnchantChanger.EcAssetsDomain,EnchantChanger.EcExpBottlePNG);

	public EcRenderItemThrowable(float par2)
	{
		this.RenderSize = par2;
	}

	public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef((float)par2, (float)par4, (float)par6);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glScalef(this.RenderSize, this.RenderSize, this.RenderSize);
        this.bindEntityTexture(par1Entity);
		Tessellator var10 = Tessellator.instance;

		this.func_77026_a(var10);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

	private void func_77026_a(Tessellator par1Tessellator)
	{
		float var3 = 0;
		float var4 = 1;
		float var5 = 0;
		float var6 = 1;
		float var7 = 1.0F;
		float var8 = 0.5F;
		float var9 = 0.25F;
		GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);
		par1Tessellator.addVertexWithUV((double)(0.0F - var8), (double)(0.0F - var9), 0.0D, (double)var3, (double)var6);
		par1Tessellator.addVertexWithUV((double)(var7 - var8), (double)(0.0F - var9), 0.0D, (double)var4, (double)var6);
		par1Tessellator.addVertexWithUV((double)(var7 - var8), (double)(var7 - var9), 0.0D, (double)var4, (double)var5);
		par1Tessellator.addVertexWithUV((double)(0.0F - var8), (double)(var7 - var9), 0.0D, (double)var3, (double)var5);
		par1Tessellator.draw();
	}

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return entity instanceof EcEntityMeteo ? this.texMeteo: entity instanceof EcEntityExExpBottle ? this.texExp: TextureMap.locationItemsTexture;
	}
}
