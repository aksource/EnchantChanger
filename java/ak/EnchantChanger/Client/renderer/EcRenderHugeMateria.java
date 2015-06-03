package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.Client.models.EcModelHMateria;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EcRenderHugeMateria extends TileEntitySpecialRenderer {
    private static final ResourceLocation tex = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcHugetex);
    private static final ResourceLocation texture16 = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/hugemateria16.png");
    private static final ResourceLocation objHugeMateria = new ResourceLocation(Constants.EcAssetsDomain, "models/hugemateria.obj");
    private EcModelHMateria Hmateria = new EcModelHMateria();

    //    private final IModelCustom modelHugeMateria;
    public EcRenderHugeMateria() {
//        modelHugeMateria = AdvancedModelLoader.loadModel(objHugeMateria);
    }

    public void doRenderModel(EcTileEntityHugeMateria tileEntityHugeMateria, double par2, double par4, double par6, float par8, int par10) {
        bindTexture(texture16);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glTranslated(par2 + 0.5d, par4 + 1.7d, par6 + 0.5d);
        GL11.glScaled(0.7d, 0.7d, 0.7d);
//        this.modelHugeMateria.renderAll();
        GL11.glPopMatrix();
    }

    public void doRender(EcTileEntityHugeMateria par1TileEntity, double par2, double par4, double par6, float par8, int par10) {
        float angle = par1TileEntity.angle;
        float height = MathHelper.sin(angle);
        bindTexture(tex);
        GL11.glPushMatrix();
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		GL11.glTranslated(par2 + 0.45, par4-0.1, par6 + 0.45);
        GL11.glTranslated(par2 + 1.2, par4 + 1, par6 - 0.25);
//		Tessellator var9 = Tessellator.instance;
//		var9.setBrightness(220);
//		GL11.glPushMatrix();
//		GL11.glScalef(1.0F,2.0F,1.0F);
        this.Hmateria.render(null, height, 0.0F, 0.0F, 0.0F, 0.0F, 0.038F);
//		GL11.glScalef(1.0F, 1.0F, 1.0F);
//		GL11.glPopMatrix();
//		GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8, int par10) {
        if (par1TileEntity != null)
            this.doRenderModel((EcTileEntityHugeMateria) par1TileEntity, par2, par4, par6, par8, par10);
    }
}
