package ak.EnchantChanger.Client;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class EcModelHMateria extends ModelBase
{
	ModelRenderer Crystal;

	public EcModelHMateria()
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.Crystal = new ModelRenderer(this, 0, 0);
		this.Crystal.addBox(-16.0F, -16.0F, 0.0F, 16, 16, 16);
		this.Crystal.setRotationPoint(0.0F, 32.0F, 0.0F);
		this.Crystal.setTextureSize(64, 32);
//		this.setRotation(this.Crystal, 0.7071F, 0.0F, 0.7071F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@SideOnly(Side.CLIENT)
	public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
	{
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator var9 = Tessellator.instance;
		var9.setBrightness(220);
		GL11.glPushMatrix();
		GL11.glScalef(1.0F,2.0F,1.0F);
//		super.render(var1, var2, var3, var4, var5, var6, var7);
		GL11.glRotatef(60.0F, 0.7071F, 0.0F, 0.7071F);
//		GL11.glRotatef(var2, 1.0F, 0.0F, 1.0F);
//		this.setRotationAngles(var2, var3, var4, var5, var6, var7);
		this.Crystal.render(var7);
		GL11.glScalef(1.0F, 1.0F, 1.0F);
		GL11.glTranslatef(0.0f, var2, var2);
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	public void render()
	{
		this.Crystal.render(1.0F);
	}

	private void setRotation(ModelRenderer var1, float var2, float var3, float var4)
	{
		var1.rotateAngleX = var2;
		var1.rotateAngleY = var3;
		var1.rotateAngleZ = var4;
	}

	/**
	 * Sets the models various rotation angles.
	 */
	@Override
	public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity par7Entity)
	{
		super.setRotationAngles(var1, var2, var3, var4, var5, var6, par7Entity);
	}
}
