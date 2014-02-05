package ak.EnchantChanger.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
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
public class EcModelZackSword extends ModelBase
{
  //fields
    ModelRenderer Sword;
    private ResourceLocation tex = new ResourceLocation(EnchantChanger.EcAssetsDomain,EnchantChanger.EcZackSwordPNG);
	public EcModelZackSword()
	{
		textureWidth = 128;
		textureHeight = 128;
		setTextureOffset("Sword.Shape1", 0, 0);
		setTextureOffset("Sword.Shape2", 36, 0);
		setTextureOffset("Sword.Shape3", 36, 4);
		setTextureOffset("Sword.Shape4", 36, 0);
		setTextureOffset("Sword.Shape5", 36, 9);
		setTextureOffset("Sword.Shape6", 0, 49);
		setTextureOffset("Sword.Shape7", 0, 51);
		setTextureOffset("Sword.Shape8", 0, 53);
		setTextureOffset("Sword.Shape9", 0, 55);
		setTextureOffset("Sword.Shape10", 0, 57);
		setTextureOffset("Sword.Shape11", 0, 59);
		setTextureOffset("Sword.Shape12", 0, 61);
		setTextureOffset("Sword.Shape13", 0, 63);
		setTextureOffset("Sword.Shape14", 0, 65);
		setTextureOffset("Sword.Shape15", 0, 67);
		setTextureOffset("Sword.Shape16", 0, 69);
		setTextureOffset("Sword.Shape17", 0, 71);
		setTextureOffset("Sword.Shape18", 0, 73);
		setTextureOffset("Sword.Shape19", 0, 75);
		setTextureOffset("Sword.Shape20", 0, 77);
		setTextureOffset("Sword.Shape21", 0, 79);
		setTextureOffset("Sword.Shape22", 36, 30);
		setTextureOffset("Sword.Shape23", 36, 30);

		Sword = new ModelRenderer(this, "Sword");
		Sword.setRotationPoint(0F, 0F, 0F);
		setRotation(Sword, 0F, 0F, 0F);
		Sword.mirror = false;
		Sword.addBox("Shape1", -8F, 1F, 0F, 17, 48, 1);
		Sword.addBox("Shape2", -9F, 0F, -1F, 19, 1, 3);
		Sword.addBox("Shape3", -8.5F, -3F, -0.5F, 18, 3, 2);
		Sword.addBox("Shape4", -9F, -4F, -1F, 19, 1, 3);
		Sword.addBox("Shape5", -0.5F, -22F, -0.5F, 2, 18, 2);
		Sword.addBox("Shape6", -8F, 49F, 0F, 16, 1, 1);
		Sword.addBox("Shape7", -8F, 50F, 0F, 15, 1, 1);
		Sword.addBox("Shape8", -8F, 51F, 0F, 14, 1, 1);
		Sword.addBox("Shape9", -8F, 52F, 0F, 13, 1, 1);
		Sword.addBox("Shape10", -8F, 53F, 0F, 12, 1, 1);
		Sword.addBox("Shape11", -8F, 54F, 0F, 11, 1, 1);
		Sword.addBox("Shape12", -8F, 55F, 0F, 10, 1, 1);
		Sword.addBox("Shape13", -8F, 56F, 0F, 9, 1, 1);
		Sword.addBox("Shape14", -8F, 57F, 0F, 8, 1, 1);
		Sword.addBox("Shape15", -8F, 58F, 0F, 7, 1, 1);
		Sword.addBox("Shape16", -8F, 59F, 0F, 6, 1, 1);
		Sword.addBox("Shape17", -8F, 60F, 0F, 5, 1, 1);
		Sword.addBox("Shape18", -8F, 61F, 0F, 4, 1, 1);
		Sword.addBox("Shape19", -8F, 62F, 0F, 3, 1, 1);
		Sword.addBox("Shape20", -8F, 63F, 0F, 2, 1, 1);
		Sword.addBox("Shape21", -8F, 64F, 0F, 1, 1, 1);
		Sword.addBox("Shape22", 2F, 4F, 0F, 2, 2, 1);
		Sword.addBox("Shape23", 2F, 8F, 0F, 2, 2, 1);
	}
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
	    model.rotateAngleX = x;
	    model.rotateAngleY = y;
	    model.rotateAngleZ = z;
  }

  	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
	{
		super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
	}
  	@SideOnly(Side.CLIENT)
	public void renderItem(ItemStack pitem, EntityLivingBase pentity) {

		Minecraft MC = Minecraft.getMinecraft();
  		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glPushMatrix();
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MC.renderEngine.getTexture(EnchantChanger.EcZackSwordPNG));
//		MC.getTextureManager().getTexture(new ResourceLocation(EnchantChanger.EcZackSwordPNG));
		MC.renderEngine.bindTexture(tex);
		if (pentity instanceof EntityPlayer && ((EntityPlayer)pentity).isUsingItem()) {
			//Guard
			//ViewChange
			if (MC.gameSettings.thirdPersonView == 0) {
				GL11.glTranslatef(0.2F, -0.5F, 0.5F);
//				GL11.glRotatef(70F, 0F, 0F, 1F);
			}
			else
				GL11.glTranslatef(0F, -0.1F, 0F);
		} else {
			//ViewChange
			if (MC.gameSettings.thirdPersonView == 0) {
//				GL11.glTranslatef(1F, -1F, 0.5F);
//				GL11.glRotatef(90F, 0F, 1F, 0F);
//				GL11.glRotatef(20F, 0F, 0F, 1F);
			}
		}
		GL11.glTranslatef(0.5F, 0.5F, -0.05F);
		GL11.glScalef(1F, 1F, 0.4F);
//		GL11.glRotatef(0F, 1.0F, 1.0F, 0.0F);
//		GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);

		Sword.render(0.03F);
		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
