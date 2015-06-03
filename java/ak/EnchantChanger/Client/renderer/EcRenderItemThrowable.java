package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.entity.EcEntityExExpBottle;
import ak.EnchantChanger.entity.EcEntityMeteor;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class EcRenderItemThrowable extends Render {
    private static final ResourceLocation TEXTUREMETEO = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcMeteorPNG);
    private static final ResourceLocation TEXTUREEXEXPBOTTLE = new ResourceLocation(Constants.EcAssetsDomain, Constants.EcExpBottlePNG);
    private float RenderSize;

    public EcRenderItemThrowable(RenderManager renderManager1, float par2) {
        super(renderManager1);
        this.RenderSize = par2;
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(par2, par4, par6);
        GlStateManager.enableRescaleNormal();
        ;
        GlStateManager.scale(this.RenderSize, this.RenderSize, this.RenderSize);
        this.bindEntityTexture(par1Entity);

        this.func_77026_a(Tessellator.getInstance());
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    private void func_77026_a(Tessellator par1Tessellator) {
        float var3 = 0;
        float var4 = 1;
        float var5 = 0;
        float var6 = 1;
        float var7 = 1.0F;
        float var8 = 0.5F;
        float var9 = 0.25F;
        GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        par1Tessellator.getWorldRenderer().startDrawingQuads();
//        par1Tessellator.getWorldRenderer().setNormal(0.0F, 1.0F, 0.0F);
        par1Tessellator.getWorldRenderer().addVertexWithUV((double) (0.0F - var8), (double) (0.0F - var9), 0.0D, (double) var3, (double) var6);
        par1Tessellator.getWorldRenderer().addVertexWithUV((double) (var7 - var8), (double) (0.0F - var9), 0.0D, (double) var4, (double) var6);
        par1Tessellator.getWorldRenderer().addVertexWithUV((double) (var7 - var8), (double) (var7 - var9), 0.0D, (double) var4, (double) var5);
        par1Tessellator.getWorldRenderer().addVertexWithUV((double) (0.0F - var8), (double) (var7 - var9), 0.0D, (double) var3, (double) var5);
        par1Tessellator.draw();
    }

    @Override

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity) {
        return entity instanceof EcEntityMeteor ? TEXTUREMETEO : entity instanceof EcEntityExExpBottle ? TEXTUREEXEXPBOTTLE : null;
    }
}
