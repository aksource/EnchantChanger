package ak.enchantchanger.client.renderer;

import ak.enchantchanger.entity.EcEntityApOrb;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EcRenderApOrb extends Render<EcEntityApOrb> {
    private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");

    public EcRenderApOrb(RenderManager renderManager1) {
        super(renderManager1);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    @Override
    public void doRender(EcEntityApOrb ecEntityApOrb, double par2, double par4, double par6, float par8, float par9) {
        GlStateManager.pushMatrix();//GL11.glPushMatrix();
        GlStateManager.translate(par2, par4, par6);//GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        this.bindEntityTexture(ecEntityApOrb);
        int i = ecEntityApOrb.getTextureByXP();
        float f2 = (float) (i % 4 * 16 + 0) / 64.0F;
        float f3 = (float) (i % 4 * 16 + 16) / 64.0F;
        float f4 = (float) (i / 4 * 16 + 0) / 64.0F;
        float f5 = (float) (i / 4 * 16 + 16) / 64.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        int j = ecEntityApOrb.getBrightnessForRender(par9);
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k / 1.0F, (float) l / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);//GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f9 = 255.0F;
        float f10 = ((float) ecEntityApOrb.apColor + par9) / 2.0F;
        l = (int) ((MathHelper.sin(f10 + 0.0F) + 1.0F) * 0.5F * f9);
        int j1 = (int) f9;
        int i1 = (int) ((MathHelper.sin(f10 + 4.1887903F) + 1.0F) * 0.1F * f9);
        int k1 = l << 16 | i1 << 8 | j1;
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);//GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);//GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float f11 = 0.3F;
        GlStateManager.scale(f11, f11, f11);//GL11.glScalef(f11, f11, f11);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
//        vertexBuffer.setColorRGBA_I(k1, 128);
//        vertexBuffer.setNormal(0.0F, 1.0F, 0.0F);
        vertexBuffer.pos((double) (0.0F - f7), (double) (0.0F - f8), 0.0D).tex((double) f2, (double) f5)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexBuffer.pos((double) (f6 - f7), (double) (0.0F - f8), 0.0D).tex((double) f3, (double) f5)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexBuffer.pos((double) (f6 - f7), (double) (1.0F - f8), 0.0D).tex((double) f3, (double) f4)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();;
        vertexBuffer.pos((double) (0.0F - f7), (double) (1.0F - f8), 0.0D).tex((double) f2, (double) f4)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();;
        tessellator.draw();
        GlStateManager.disableBlend();//GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.disableRescaleNormal();//GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.popMatrix();//GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(EcEntityApOrb par1Entity) {
        return experienceOrbTextures;
    }
}
