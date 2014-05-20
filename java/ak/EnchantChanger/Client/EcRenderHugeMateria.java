package ak.EnchantChanger.Client;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import ak.EnchantChanger.EcTileEntityHugeMateria;
import ak.EnchantChanger.EnchantChanger;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class EcRenderHugeMateria extends TileEntitySpecialRenderer
{
	private EcModelHMateria Hmateria = new EcModelHMateria();
	private String texture = EnchantChanger.EcHugetex;
	private float angle;
	private float height;
	private static final ResourceLocation tex = new ResourceLocation(EnchantChanger.EcAssetsDomain,EnchantChanger.EcHugetex);
    private static final ResourceLocation texture16 = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/hugemateria16.png");
    private static final ResourceLocation objHugeMateria = new ResourceLocation(EnchantChanger.EcAssetsDomain, "models/hugemateria.obj");
    private final IModelCustom modelHugeMateria;
    public EcRenderHugeMateria() {
        modelHugeMateria = AdvancedModelLoader.loadModel(objHugeMateria);
    }

    public void doRenderModel(EcTileEntityHugeMateria tileEntityHugeMateria, double par2, double par4, double par6, float par8) {
        bindTexture(texture16);
        GL11.glPushMatrix();
        GL11.glTranslated(par2 + 0.5d, par4 + 1.7d, par6 + 0.5d);
        GL11.glScaled(0.7d, 0.7d, 0.7d);
        this.modelHugeMateria.renderAll();
        GL11.glPopMatrix();
    }

	public void doRender(EcTileEntityHugeMateria par1TileEntity, double par2, double par4, double par6, float par8)
	{
		this.angle = par1TileEntity.angle;
		this.height = MathHelper.sin(angle);
		bindTexture(tex);
		GL11.glPushMatrix();
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glTranslated(par2 + 0.45, par4-0.1, par6 + 0.45);
		GL11.glTranslated(par2+1.2, par4+1, par6-0.25);
//		Tessellator var9 = Tessellator.instance;
//		var9.setBrightness(220);
//		GL11.glPushMatrix();
//		GL11.glScalef(1.0F,2.0F,1.0F);
		this.Hmateria.render((Entity)null, this.height, 0.0F, 0.0F, 0.0F, 0.0F, 0.038F);
//		GL11.glScalef(1.0F, 1.0F, 1.0F);
//		GL11.glPopMatrix();
//		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
	@Override
	public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
	{
		if(par1TileEntity !=null)
			this.doRenderModel((EcTileEntityHugeMateria) par1TileEntity, par2, par4, par6, par8);
	}
}
