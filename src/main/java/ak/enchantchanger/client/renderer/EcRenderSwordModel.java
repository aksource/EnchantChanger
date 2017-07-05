package ak.enchantchanger.client.renderer;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.client.forge.model.IModelCustom;
import ak.enchantchanger.client.forge.model.obj.WavefrontObject;
import ak.enchantchanger.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

@SideOnly(Side.CLIENT)
public class EcRenderSwordModel {

    public static final EcRenderSwordModel INSTANCE = new EcRenderSwordModel();
    private final IModelCustom zackSwordModel = new WavefrontObject(Constants.zackSwordObj);
    private final IModelCustom ultimateWeaponModel = new WavefrontObject(Constants.ultimateWeaponObj);
    private final IModelCustom masamuneModel = new WavefrontObject(Constants.masamuneObj);
    private final IModelCustom firstSwordModel = new WavefrontObject(Constants.firstSwordObj);
    private final IModelCustom unionSwordModel = new WavefrontObject(Constants.unionSwordObj);
    private Minecraft mc;

    private EcRenderSwordModel() {
        mc = Minecraft.getMinecraft();
//        zackSwordModel = new WavefrontObject(zackSwordObj);
//        ultimateWeaponModel = new WavefrontObject(ultimateWeaponObj);
//        masamuneModel = new WavefrontObject(masamuneObj);
//        firstSwordModel = new WavefrontObject(firstSwordObj);
//        unionSwordModel = new WavefrontObject(unionSwordObj);
    }

    //Obj仕様renderメソッド
    public void renderSwordModel(ItemStack item, boolean isHanded) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.7F, 0.38F, 0.0F);
        GlStateManager.rotate(40.0F, 0.0F, 0.0F, 1.0F);
//        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) GL11.glTranslatef(0.3f, 0.2f, 0);
        if (item.getItem() instanceof EcItemZackSword) {
            renderZackSwordModel(item, 0.12F);
        }
        if (item.getItem() instanceof EcItemCloudSword) {
            renderUnionSwordModel(item, 0.12F, isHanded);
        }
        if (item.getItem() instanceof EcItemCloudSwordCore) {
            renderFirstSwordModel(item, 0.12F);
        }
        if (item.getItem() instanceof EcItemSephirothSword
                || item.getItem() instanceof EcItemSephirothSwordImit) {
            renderMasamuneModel(item, 0.12F, isHanded);
        }
        if (item.getItem() instanceof EcItemUltimateWeapon) {
            renderUltimateWeaponModel(item, 0.12F);
        }
        GlStateManager.popMatrix();
    }

    private void renderZackSwordModel(ItemStack item, float size) {
        GlStateManager.scale(size, size, size);
        mc.renderEngine.bindTexture(Constants.zackSwordEdge);
        zackSwordModel.renderPart("Sword_sword");
        mc.renderEngine.bindTexture(Constants.zackSwordBox);
        zackSwordModel.renderPart("box");
        mc.renderEngine.bindTexture(Constants.zackSwordCylinder);
        zackSwordModel.renderPart("Cylinder");
    }

    private void renderUltimateWeaponModel(ItemStack item, float size) {
        GlStateManager.scale(size, size, size);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        mc.renderEngine.bindTexture(Constants.ultimateWeaponSword);
        ultimateWeaponModel.renderPart("sword");
        GlStateManager.disableBlend();
        mc.renderEngine.bindTexture(Constants.ultimateWeaponEmblem);
        ultimateWeaponModel.renderPart("emblem01_e01");
        ultimateWeaponModel.renderPart("emblem02_e02");
        mc.renderEngine.bindTexture(Constants.ultimateWeaponPipe01);
        ultimateWeaponModel.renderPart("pipe01_p01");
        ultimateWeaponModel.renderPart("pipe02");
        ultimateWeaponModel.renderPart("pipe03_p03");
        ultimateWeaponModel.renderPart("pipe04_p04");
        mc.renderEngine.bindTexture(Constants.ultimateWeaponPipe02);
        ultimateWeaponModel.renderPart("pipe05_p05");
        ultimateWeaponModel.renderPart("pipe06_p06");
        ultimateWeaponModel.renderPart("pipe07_p07");
        ultimateWeaponModel.renderPart("pipe08_p08");
        mc.renderEngine.bindTexture(Constants.ultimateWeaponHandguard);
        ultimateWeaponModel.renderPart("handguard01_h01");
        ultimateWeaponModel.renderPart("handguard02_h02");
        mc.renderEngine.bindTexture(Constants.ultimateWeaponGrip);
        ultimateWeaponModel.renderPart("grip");
    }

    private void renderMasamuneModel(ItemStack itme, float size, boolean isHanded) {
        GlStateManager.scale(size, size, size);
        mc.renderEngine.bindTexture(Constants.masamuneSword);
        masamuneModel.renderPart("sword");
        mc.renderEngine.bindTexture(Constants.masamuneGrip);
        masamuneModel.renderPart("grip");
        if (!isHanded) {
            //黒テクスチャなので流用
            mc.renderEngine.bindTexture(Constants.butterflyedgeGrip);
            masamuneModel.renderPart("sheath");
        }
    }

    private void renderFirstSwordModel(ItemStack item, float size) {
        EcItemCloudSwordCore cloudSwordCore = (EcItemCloudSwordCore) item.getItem();
        boolean isActive = cloudSwordCore.isActive(item);
        GlStateManager.scale(size, size, size);
        renderUnionSwordCore(isActive);
    }

    private void renderUnionSwordCore(boolean isActive) {
        mc.renderEngine.bindTexture(Constants.firstSwordCenter);
        firstSwordModel.renderPart("centerplate_center");
        mc.renderEngine.bindTexture(Constants.firstSwordGrip);
        firstSwordModel.renderPart("grip_Cylinder");
        if (isActive) {
            mc.renderEngine.bindTexture(Constants.firstSwordEdge);
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.5f, 0, 0);
            firstSwordModel.renderPart("edge01");
            GlStateManager.translate(1.0f, 0, 0);
            firstSwordModel.renderPart("edge02");
            GlStateManager.popMatrix();
            mc.renderEngine.bindTexture(Constants.firstSwordCase);
            firstSwordModel.renderPart("boxC");
        } else {
            mc.renderEngine.bindTexture(Constants.firstSwordEdge);
            firstSwordModel.renderPart("edge01");
            firstSwordModel.renderPart("edge02");
            mc.renderEngine.bindTexture(Constants.firstSwordCase);
            firstSwordModel.renderPart("boxO");
        }
    }

    private void renderUnionSwordModel(ItemStack item, float size, boolean isHanded) {
        boolean isLimitBreak = mc.player.isSneaking() && isHanded;
        GlStateManager.pushMatrix();
        GlStateManager.scale(size, size, size);
        renderUnionSwordCore(isLimitBreak);
        renderOrganix(isLimitBreak);
        renderButterflyEdge("01", isLimitBreak);
        renderButterflyEdge("02", isLimitBreak);
        renderRuneBlade("01", isLimitBreak, size);
        renderRuneBlade("02", isLimitBreak, size);
        GlStateManager.popMatrix();
    }

    private void renderOrganix(boolean limitBreak) {
        if (limitBreak) {
            GlStateManager.popMatrix();
            GlStateManager.rotate(-15F, 0F, 0F, 1.0F);
            GlStateManager.translate(0F, 2.0F, 0F);
        }
        mc.renderEngine.bindTexture(Constants.organixEdge);
        unionSwordModel.renderPart("Organix_Org");
        mc.renderEngine.bindTexture(Constants.organixGrip);
        unionSwordModel.renderPart("OGrip01_OG01");
        unionSwordModel.renderPart("OGrip02_OG02");
        if (limitBreak) {
            GlStateManager.popMatrix();
        }
    }

    private void renderButterflyEdge(String str, boolean limitBreak) {
        String edge = String.format("ButterflyEdge%s_BE%s", str, str);
        String grip = String.format("BGrip%s_BG%s", str, str);
        if (limitBreak) {
            GlStateManager.pushMatrix();
            GlStateManager.rotate(15F, 0F, 0F, 1.0F);
            GlStateManager.translate(0F, 2.0F, 0F);
        }
        mc.renderEngine.bindTexture(Constants.butterflyedgeEdge);
        unionSwordModel.renderPart(edge);
        mc.renderEngine.bindTexture(Constants.butterflyedgeGrip);
        unionSwordModel.renderPart(grip);
        if (limitBreak) {
            GlStateManager.popMatrix();
        }
    }

    private void renderRuneBlade(String str, boolean limitBreak, float size) {
        String edge = String.format("RuneBlade%s_RE%s", str, str);
        String hand = String.format("RuneGuard%s_RH%s", str, str);
        String grip = String.format("RGrip%s_RG%s", str, str);
        if (limitBreak) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0F, 5.0F, 0F);
        }
        mc.renderEngine.bindTexture(Constants.runebladeEdge);
        unionSwordModel.renderPart(edge);

        if (limitBreak) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.46925D, 0.68262D, 0D);
            GlStateManager.rotate(-45F, 0F, 0F, 1.0F);
            GlStateManager.translate(0.46925D, -0.68262D, 0D);

        }
        mc.renderEngine.bindTexture(Constants.runebladeHand);
        unionSwordModel.renderPart(hand);

        if (limitBreak) {
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.translate(-0.36411D, 0.68262D, 0D);
            GlStateManager.rotate(180F, 0F, 0F, 1.0F);
            GlStateManager.translate(0.36411D, -0.68262D, 0D);

        }
        mc.renderEngine.bindTexture(Constants.runebladeGrip);
        unionSwordModel.renderPart(grip);
        if (limitBreak) {
            GlStateManager.popMatrix();
            GlStateManager.popMatrix();
        }
    }
}