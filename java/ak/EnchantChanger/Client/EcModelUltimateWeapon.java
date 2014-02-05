package ak.EnchantChanger.Client;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EcModelUltimateWeapon extends ModelBase
{
	//fields
	ModelRenderer				Sword;
	//private Minecraft MC = FMLClientHandler.instance().getClient();
	private ResourceLocation	tex	= new ResourceLocation(EnchantChanger.EcAssetsDomain,
											EnchantChanger.EcUltimateWeaponPNG);

	public EcModelUltimateWeapon()
	{
		textureWidth = 64;
		textureHeight = 32;
		setTextureOffset("Sword.tsuka", 20, 0);
		setTextureOffset("Sword.core", 0, -2);
		setTextureOffset("Sword.blade08", 28, 4);
		setTextureOffset("Sword.blade10", 28, 7);
		setTextureOffset("Sword.blade12", 28, 10);
		setTextureOffset("Sword.blade14", 28, 13);
		setTextureOffset("Sword.blade15", 28, 16);
		setTextureOffset("Sword.blade16", 30, 16);
		setTextureOffset("Sword.edge01", 30, 17);
		setTextureOffset("Sword.edge02", 29, 17);
		setTextureOffset("Sword.edge03", 41, 5);
		setTextureOffset("Sword.edge04", 39, 8);
		setTextureOffset("Sword.edge05", 37, 11);
		setTextureOffset("Sword.edge06", 30, 17);
		setTextureOffset("Sword.edge07", 30, 17);
		setTextureOffset("Sword.edge08", 30, 17);
		setTextureOffset("Sword.edge09", 30, 17);
		setTextureOffset("Sword.edge10", 30, 17);
		setTextureOffset("Sword.edge11", 30, 17);
		setTextureOffset("Sword.edge12", 0, 0);
		setTextureOffset("Sword.edge13", 0, 0);
		setTextureOffset("Sword.edge14", 0, -1);
		setTextureOffset("Sword.edge15", 0, -1);
		setTextureOffset("Sword.emb01", 21, 2);
		setTextureOffset("Sword.emb02", 50, 27);
		setTextureOffset("Sword.emb03", 21, 2);
		setTextureOffset("Sword.emb04", 19, 3);
		setTextureOffset("Sword.emb05", 20, 3);
		setTextureOffset("Sword.emb06", 21, 2);
		setTextureOffset("Sword.emb07", 21, 2);
		setTextureOffset("Sword.emb08", 21, 2);
		setTextureOffset("Sword.emb09", 21, 2);
		setTextureOffset("Sword.emb10", 50, 27);
		setTextureOffset("Sword.emb11", 21, 2);
		setTextureOffset("Sword.emb12", 19, 3);
		setTextureOffset("Sword.emb13", 20, 3);
		setTextureOffset("Sword.emb14", 21, 2);
		setTextureOffset("Sword.emb15", 21, 2);
		setTextureOffset("Sword.emb16", 21, 2);
		setTextureOffset("Sword.tsuba01", 20, 16);
		setTextureOffset("Sword.tsuba02", 20, 16);
		setTextureOffset("Sword.tsuba03", 20, 16);
		setTextureOffset("Sword.tsuba04", 20, 16);
		setTextureOffset("Sword.tsuba05", 20, 16);
		setTextureOffset("Sword.tsuba06", 20, 16);
		setTextureOffset("Sword.tsuba07", 20, 16);
		setTextureOffset("Sword.tsuba08", 20, 16);
		setTextureOffset("Sword.tsuba09", 20, 21);
		setTextureOffset("Sword.tsuba10", 28, 0);
		setTextureOffset("Sword.tsuba11", 20, 21);
		setTextureOffset("Sword.tsuba12", 28, 0);
		setTextureOffset("Sword.tsuba13", 28, 0);
		setTextureOffset("Sword.tsuba14", 28, 0);
		setTextureOffset("Sword.tsuba15", 28, 0);
		setTextureOffset("Sword.tsuba16", 20, 21);
		setTextureOffset("Sword.tsuba17", 20, 21);
		setTextureOffset("Sword.tsuba18", 28, 0);
		setTextureOffset("Sword.tsuba19", 28, 0);
		setTextureOffset("Sword.tsuba20", 28, 0);
		setTextureOffset("Sword.tsuba21", 28, 0);
		setTextureOffset("Sword.tsuba22", 28, 0);

		Sword = new ModelRenderer(this, "Sword");
		Sword.setRotationPoint(14F, -6F, 0F);
		setRotation(Sword, 0F, 0F, 0F);
		Sword.mirror = true;
		Sword.addBox("tsuka", -0.5F, -11F, -0.5F, 2, 16, 2);
		Sword.addBox("core", -3F, 6F, -0.5F, 7, 32, 2);
		Sword.addBox("blade08", -2.5F, 38F, -0.5F, 6, 1, 2);
		Sword.addBox("blade10", -2F, 39F, -0.5F, 5, 1, 2);
		Sword.addBox("blade12", -1.5F, 40F, -0.5F, 4, 1, 2);
		Sword.addBox("blade14", -1F, 41F, -0.5F, 3, 1, 2);
		Sword.addBox("blade15", -0.5F, 42F, -0.5F, 2, 1, 2);
		Sword.addBox("blade16", 0F, 43F, -0.5F, 1, 1, 2);
		Sword.addBox("edge01", 0F, 44F, 0F, 1, 1, 1);
		Sword.addBox("edge02", -0.5F, 43F, 0F, 2, 1, 1);
		Sword.addBox("edge03", -1F, 42F, 0F, 3, 1, 1);
		Sword.addBox("edge04", -1.5F, 41F, 0F, 4, 1, 1);
		Sword.addBox("edge05", -2F, 40F, 0F, 5, 1, 1);
		Sword.addBox("edge06", 2.5F, 39F, 0F, 1, 1, 1);
		Sword.addBox("edge07", -2.5F, 39F, 0F, 1, 1, 1);
		Sword.addBox("edge08", 3F, 38F, 0F, 1, 1, 1);
		Sword.addBox("edge09", -3F, 38F, 0F, 1, 1, 1);
		Sword.addBox("edge10", 3.5F, 37F, 0F, 1, 1, 1);
		Sword.addBox("edge11", -3.5F, 37F, 0F, 1, 1, 1);
		Sword.addBox("edge12", 4F, 35F, 0F, 1, 2, 1);
		Sword.addBox("edge13", -4F, 35F, 0F, 1, 2, 1);
		Sword.addBox("edge14", 3.5F, 6F, 0F, 1, 29, 1);
		Sword.addBox("edge15", -3.5F, 6F, 0F, 1, 29, 1);
		Sword.addBox("emb01", 0F, 5F, -1F, 1, 1, 1);
		Sword.addBox("emb02", 0F, 6F, -1F, 1, 1, 1);
		Sword.addBox("emb03", 0F, 7F, -1F, 1, 2, 1);
		Sword.addBox("emb04", 1F, 6F, -1F, 1, 1, 1);
		Sword.addBox("emb05", -1F, 6F, -1F, 1, 1, 1);
		Sword.addBox("emb06", 1.5F, 7F, -1F, 1, 2, 1);
		Sword.addBox("emb07", -1.5F, 7F, -1F, 1, 2, 1);
		Sword.addBox("emb08", 0F, 9F, -1F, 1, 2, 1);
		Sword.addBox("emb09", 0F, 5F, 1F, 1, 1, 1);
		Sword.addBox("emb10", 0F, 6F, 1F, 1, 1, 1);
		Sword.addBox("emb11", 0F, 7F, 1F, 1, 2, 1);
		Sword.addBox("emb12", 1F, 6F, 1F, 1, 1, 1);
		Sword.addBox("emb13", -1F, 6F, 1F, 1, 1, 1);
		Sword.addBox("emb14", -1.5F, 7F, 1F, 1, 2, 1);
		Sword.addBox("emb15", 1.5F, 7F, 1F, 1, 2, 1);
		Sword.addBox("emb16", 0F, 9F, 1F, 1, 2, 1);
		Sword.addBox("tsuba01", 1.5F, 3F, 0F, 3, 1, 1);
		Sword.addBox("tsuba02", 2.5F, 2F, 0F, 2, 1, 1);
		Sword.addBox("tsuba03", 2.5F, 1F, 0F, 2, 1, 1);
		Sword.addBox("tsuba04", 3F, 0F, 0F, 1, 1, 1);
		Sword.addBox("tsuba05", -3.5F, 3F, 0F, 3, 1, 1);
		Sword.addBox("tsuba06", -3.5F, 2F, 0F, 2, 1, 1);
		Sword.addBox("tsuba07", -3.5F, 1F, 0F, 2, 1, 1);
		Sword.addBox("tsuba08", -3F, 0F, 0F, 1, 1, 1);
		Sword.addBox("tsuba09", 1.5F, 4F, -0.5F, 3, 1, 2);
		Sword.addBox("tsuba10", 4F, 4F, 0F, 3, 1, 1);
		Sword.addBox("tsuba11", 0F, 5F, -0.5F, 4, 1, 2);
		Sword.addBox("tsuba12", 4F, 5F, 0F, 1, 1, 1);
		Sword.addBox("tsuba13", 4.5F, 5.5F, 0F, 1, 1, 1);
		Sword.addBox("tsuba14", 5F, 6F, 0F, 1, 1, 1);
		Sword.addBox("tsuba15", 5.5F, 6.5F, 0F, 1, 1, 1);
		Sword.addBox("tsuba16", -3.5F, 4F, -0.5F, 3, 1, 2);
		Sword.addBox("tsuba17", -3F, 5F, -0.5F, 4, 1, 2);
		Sword.addBox("tsuba18", -6F, 4F, 0F, 3, 1, 1);
		Sword.addBox("tsuba19", -4F, 5F, 0F, 1, 1, 1);
		Sword.addBox("tsuba20", -4.5F, 5.5F, 0F, 1, 1, 1);
		Sword.addBox("tsuba21", -5F, 6F, 0F, 1, 1, 1);
		Sword.addBox("tsuba22", -5.5F, 6.5F, 0F, 1, 1, 1);
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
		//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MC.renderEngine.getTexture(EnchantChanger.EcUltimateWeaponPNG));
		//		MC.getTextureManager().getTexture(new ResourceLocation(EnchantChanger.EcUltimateWeaponPNG));
		MC.renderEngine.bindTexture(tex);
		//	    アルファブレンドを有効化する
		GL11.glEnable(GL_BLEND);
		//    アルファブレンドの係数を設定する
		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		/**
		if (pentity instanceof EntityPlayer && ((EntityPlayer)pentity).isUsingItem()) {
			//Guard
			//GL11.glTranslatef(-1.0F, 0.1F, 0.3F);
			GL11.glRotatef(25.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
			GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
			//ViewChange
			if (MC.gameSettings.thirdPersonView == 0) {
				GL11.glTranslatef(1.3F, 0.0F, 0.5F);
				GL11.glRotatef(70F, 0F, 0F, 1F);
			}
		} else {
			//ViewChange
			if (MC.gameSettings.thirdPersonView == 0) {
				GL11.glTranslatef(1F, -1F, 0.5F);
				GL11.glRotatef(90F, 0F, 1F, 0F);
				GL11.glRotatef(20F, 0F, 0F, 1F);
			}
		}
		 */
		GL11.glTranslatef(0F, 0F, 0F);
		GL11.glScalef(1F, 1F, 0.4F);
		GL11.glRotatef(0F, 1.0F, 1.0F, 0.0F);
		GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);
		//GL11.glTranslatef(0.05F, 0.0F, 0.0F);

		Sword.render(0.05F);
		GL11.glPopMatrix();
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	}
}
