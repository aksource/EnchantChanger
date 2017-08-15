package ak.enchantchanger.client.renderer;

import ak.enchantchanger.entity.EcEntityApOrb;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class EcRenderApOrb extends Render<EcEntityApOrb> {
    private static final ResourceLocation experienceOrbTextures = new ResourceLocation("textures/entity/experience_orb.png");

    public EcRenderApOrb(RenderManager renderManager1) {
        super(renderManager1);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    @Override
    public void doRender(@Nonnull EcEntityApOrb entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        this.bindEntityTexture(entity);
        int i = entity.getTextureByXP();
        float f2 = (float) (i % 4 * 16) / 64.0F;
        float f3 = (float) (i % 4 * 16 + 16) / 64.0F;
        float f4 = (float) (i / 4 * 16) / 64.0F;
        float f5 = (float) (i / 4 * 16 + 16) / 64.0F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.25F;
        int j = entity.getBrightnessForRender();
        int k = j % 65536;
        int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) k / 1.0F, (float) l / 1.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f9 = 255.0F;
        float f10 = ((float) entity.apColor + partialTicks) / 2.0F;
        l = (int) ((MathHelper.sin(f10 + 0.0F) + 1.0F) * 0.5F * f9);
        int j1 = (int) f9;
        int i1 = (int) ((MathHelper.sin(f10 + 4.1887903F) + 1.0F) * 0.1F * f9);
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        float f11 = 0.3F;
        GlStateManager.scale(f11, f11, f11);//GL11.glScalef(f11, f11, f11);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        buffer.pos((double) (0.0F - f7), (double) (0.0F - f8), 0.0D).tex((double) f2, (double) f5)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((double) (f6 - f7), (double) (0.0F - f8), 0.0D).tex((double) f3, (double) f5)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos((double) (f6 - f7), (double) (1.0F - f8), 0.0D).tex((double) f3, (double) f4)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();;
        buffer.pos((double) (0.0F - f7), (double) (1.0F - f8), 0.0D).tex((double) f2, (double) f4)
                .color(l, j1, i1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();;
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getEntityTexture(@Nonnull EcEntityApOrb entity) {
        return experienceOrbTextures;
    }
}
