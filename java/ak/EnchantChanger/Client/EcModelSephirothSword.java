package ak.EnchantChanger.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class EcModelSephirothSword extends ModelBase
{
  //fields
	ModelRenderer Sword;
	private ResourceLocation tex = new ResourceLocation(EnchantChanger.EcAssetsDomain,EnchantChanger.EcSephirothSwordPNG);
	public EcModelSephirothSword()
	{
		textureWidth = 64;
		textureHeight = 64;
		setTextureOffset("Sword.Shape1", 16, 38);
		setTextureOffset("Sword.Shape2", 26, 0);
		setTextureOffset("Sword.Shape3", 16, 48);
		setTextureOffset("Sword.Shape4", 0, 0);
		setTextureOffset("Sword.Shape5", 10, 0);
		setTextureOffset("Sword.Shape6", 0, 0);
		setTextureOffset("Sword.Shape7", 10, 0);
		setTextureOffset("Sword.Shape8", 16, 52);
		setTextureOffset("Sword.Shape9", 16, 55);
		setTextureOffset("Sword.Shape10", 16, 58);
		setTextureOffset("Sword.Shape11", 16, 61);

		Sword = new ModelRenderer(this, "Sword");
		Sword.setRotationPoint(0F, 0F, 0F);
		setRotation(Sword, 0F, 0F, 0F);
//		Sword.mirror = true;
		Sword.addBox("Shape1", -4.5F, 0F, -3F, 9, 2, 6);
		Sword.addBox("Shape2", -2.5F, -32F, -1F, 5, 32, 2);
		Sword.addBox("Shape3", -2.5F, 2F, -1F, 5, 2, 2);
		Sword.addBox("Shape4", -2.5F, 4F, -1F, 3, 62, 2);
		Sword.addBox("Shape5", 0.5F, 4F, -0.5F, 2, 62, 1);
		Sword.addBox("Shape6", -2.5F, 66F, -1F, 3, 62, 2);
		Sword.addBox("Shape7", 0.5F, 66F, -0.5F, 2, 62, 1);
		Sword.addBox("Shape8", -2.5F, 128F, -0.5F, 4, 1, 1);
		Sword.addBox("Shape9", -2.5F, 129F, -0.5F, 3, 1, 1);
		Sword.addBox("Shape10", -2.5F, 130F, -0.5F, 2, 1, 1);
		Sword.addBox("Shape11", -2.5F, 131F, -0.5F, 1, 1, 1);
	}
	private void setRotation(ModelRenderer model, float x, float y, float z)
	{
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
  	@SideOnly(Side.CLIENT)
	public void renderItem(ItemStack pitem, EntityLivingBase pentity) {

		Minecraft MC = Minecraft.getMinecraft();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();

		MC.renderEngine.bindTexture(tex);
		//	    アルファブレンドを有効化する
		//GL11.glEnable(GL_BLEND);
		//    アルファブレンドの係数を設定する
		//GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		if (pentity instanceof EntityPlayer && ((EntityPlayer)pentity).isUsingItem()) {
			//Guard
			//GL11.glTranslatef(-1.0F, 0.1F, 0.3F);
			//GL11.glRotatef(25.0F, 0.0F, 1.0F, 0.0F);
			//GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
			//GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
			//ViewChnage
			if (MC.gameSettings.thirdPersonView == 0) {
				GL11.glTranslatef(0.2F, -0.5F, 0.5F);
				//GL11.glRotatef(70F, 0F, 0F, 1F);
			}
		}
		else {
			//ViewChange
			if (MC.gameSettings.thirdPersonView == 0) {
				//GL11.glTranslatef(1F, 0F, 0.5F);
				//GL11.glRotatef(90F, 0F, 1F, 0F);
				//GL11.glRotatef(20F, 0F, 0F, 1F);
			}
		}
		GL11.glScalef(1F, 1F, 0.4F);
	    GL11.glTranslatef(0.5F, 0.3F, -0.05F);
		GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);

		Sword.render(0.02F);
		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
