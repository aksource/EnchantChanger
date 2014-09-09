package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.Client.models.*;
import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.item.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class EcRenderSwordModel implements IItemRenderer
{
	private static final EcModelUltimateWeapon UModel = new EcModelUltimateWeapon();
	private static final EcModelCloudSwordCore2 CCModel = new EcModelCloudSwordCore2();
	private static final EcModelCloudSword2 CModel = new EcModelCloudSword2();
	private static final EcModelSephirothSword SModel = new EcModelSephirothSword();
	private static final EcModelZackSword ZModel = new EcModelZackSword();

    private static final ResourceLocation zackSwordObj = new ResourceLocation(EnchantChanger.EcAssetsDomain, "models/bustersword.obj");
    private final IModelCustom zackSwordModel;
    private static final ResourceLocation zackSwordEdge = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/bustersword256-edge.png");
    private static final ResourceLocation zackSwordBox = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/bustersword256-box.png");
    private static final ResourceLocation zackSwordCylinder = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/bustersword256-cylinder.png");

    private static final ResourceLocation ultimateWeaponObj = new ResourceLocation(EnchantChanger.EcAssetsDomain, "models/ultimateweapon.obj");
    private final IModelCustom ultimateWeaponModel;
    private static final ResourceLocation ultimateWeaponSword = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/ultimateweapon256-sword.png");
    private static final ResourceLocation ultimateWeaponEmblem = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/ultimateweapon256-emblem.png");
    private static final ResourceLocation ultimateWeaponHandguard = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/ultimateweapon256-handguard.png");
    private static final ResourceLocation ultimateWeaponGrip = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/ultimateweapon256-grip.png");
    private static final ResourceLocation ultimateWeaponPipe01 = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/ultimateweapon256-pipe01.png");
    private static final ResourceLocation ultimateWeaponPipe02 = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/ultimateweapon256-pipe02.png");

    private static final ResourceLocation masamuneObj = new ResourceLocation(EnchantChanger.EcAssetsDomain, "models/masamune.obj");
    private final IModelCustom masamuneModel;
    private static final ResourceLocation masamuneSword = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/masamune256-sword.png");
    private static final ResourceLocation masamuneGrip = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/masamune256-grip.png");

    private static final ResourceLocation firstSwordObj = new ResourceLocation(EnchantChanger.EcAssetsDomain, "models/firstsword.obj");
    private final IModelCustom firstSwordModel;
    private static final ResourceLocation firstSwordEdge = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/firstsword256-edge.png");
    private static final ResourceLocation firstSwordCenter = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/firstsword256-center.png");
    private static final ResourceLocation firstSwordCase = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/firstsword256-case.png");
    private static final ResourceLocation firstSwordGrip = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/firstsword256-grip.png");

    private static final ResourceLocation unionSwordObj = new ResourceLocation(EnchantChanger.EcAssetsDomain, "models/unionsword.obj");
    private final IModelCustom unionSwordModel;
    private static final ResourceLocation organixEdge = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/organix256-edge.png");;
    private static final ResourceLocation organixGrip = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/organix256-grip.png");;
    private static final ResourceLocation butterflyedgeEdge = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/butterflyedge256-edge.png");;
    private static final ResourceLocation butterflyedgeGrip = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/butterflyedge256-grip.png");;
    private static final ResourceLocation runebladeEdge = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/runeblade256-edge.png");;
    private static final ResourceLocation runebladeHand = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/runeblade256-hand.png");;
    private static final ResourceLocation runebladeGrip = new ResourceLocation(EnchantChanger.EcAssetsDomain, "textures/item/runeblade256-grip.png");;

    private Minecraft mc;

    public EcRenderSwordModel() {
        mc = Minecraft.getMinecraft();
        zackSwordModel = AdvancedModelLoader.loadModel(zackSwordObj);
        ultimateWeaponModel = AdvancedModelLoader.loadModel(ultimateWeaponObj);
        masamuneModel = AdvancedModelLoader.loadModel(masamuneObj);
        firstSwordModel = AdvancedModelLoader.loadModel(firstSwordObj);
        unionSwordModel = AdvancedModelLoader.loadModel(unionSwordObj);
    }

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED
				|| type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (item.getItem() instanceof EcItemSword) {
            renderSwordModel(item, (EntityLivingBase) data[1], type);
        }
	}

    //Obj仕様renderメソッド
    private void renderSwordModel(ItemStack item, EntityLivingBase entityLivingBase, ItemRenderType type) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.7F, 0.38F, 0.0F);
        GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
//        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) GL11.glTranslatef(0.3f, 0.2f, 0);
        if (item.getItem() instanceof EcItemZackSword) {
            renderZackSwordModel(item, 0.12F);
        }
        if (item.getItem() instanceof EcItemCloudSword) {
            renderUnionSwordModel(item, 0.12F);
        }
        if (item.getItem() instanceof EcItemCloudSwordCore) {
            renderFirstSwordModel(item, 0.12F);
        }
        if (item.getItem() instanceof EcItemSephirothSword
                || item.getItem() instanceof EcItemSephirothSwordImit) {
            renderMasamuneModel(item, 0.12F);
        }
        if (item.getItem() instanceof EcItemUltimateWeapon) {
            renderUltimateWeaponModel(item, 0.12F);
        }
        GL11.glPopMatrix();
    }

    private void renderZackSwordModel(ItemStack item, float size) {
        GL11.glScalef(size, size, size);
        mc.renderEngine.bindTexture(zackSwordEdge);
        zackSwordModel.renderPart("Sword_sword");
        mc.renderEngine.bindTexture(zackSwordBox);
        zackSwordModel.renderPart("box");
        mc.renderEngine.bindTexture(zackSwordCylinder);
        zackSwordModel.renderPart("Cylinder");
    }

    private void renderUltimateWeaponModel(ItemStack item, float size) {
        GL11.glScalef(size, size, size);
        GL11.glEnable(GL_BLEND);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        mc.renderEngine.bindTexture(ultimateWeaponSword);
        ultimateWeaponModel.renderPart("sword");
        GL11.glDisable(GL_BLEND);
        mc.renderEngine.bindTexture(ultimateWeaponEmblem);
        ultimateWeaponModel.renderPart("emblem01_e01");
        ultimateWeaponModel.renderPart("emblem02_e02");
        mc.renderEngine.bindTexture(ultimateWeaponPipe01);
        ultimateWeaponModel.renderPart("pipe01_p01");
        ultimateWeaponModel.renderPart("pipe02");
        ultimateWeaponModel.renderPart("pipe03_p03");
        ultimateWeaponModel.renderPart("pipe04_p04");
        mc.renderEngine.bindTexture(ultimateWeaponPipe02);
        ultimateWeaponModel.renderPart("pipe05_p05");
        ultimateWeaponModel.renderPart("pipe06_p06");
        ultimateWeaponModel.renderPart("pipe07_p07");
        ultimateWeaponModel.renderPart("pipe08_p08");
        mc.renderEngine.bindTexture(ultimateWeaponHandguard);
        ultimateWeaponModel.renderPart("handguard01_h01");
        ultimateWeaponModel.renderPart("handguard02_h02");
        mc.renderEngine.bindTexture(ultimateWeaponGrip);
        ultimateWeaponModel.renderPart("grip");
    }

    private void renderMasamuneModel(ItemStack itme, float size) {
        GL11.glScalef(size, size, size);
        mc.renderEngine.bindTexture(masamuneSword);
        masamuneModel.renderPart("sword");
        mc.renderEngine.bindTexture(masamuneGrip);
        masamuneModel.renderPart("grip");
    }

    private void renderFirstSwordModel(ItemStack item, float size) {
        EcItemCloudSwordCore cloudSwordCore = (EcItemCloudSwordCore)item.getItem();
        boolean isActive = cloudSwordCore.isActive(item);
        GL11.glScalef(size, size, size);
        renderUnionSwordCore(isActive);
    }

    private void renderUnionSwordCore(boolean isActive) {
        mc.renderEngine.bindTexture(firstSwordCenter);
        firstSwordModel.renderPart("centerplate_center");
        mc.renderEngine.bindTexture(firstSwordGrip);
        firstSwordModel.renderPart("grip_Cylinder");
        if (isActive) {
            mc.renderEngine.bindTexture(firstSwordEdge);
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.5f, 0, 0);
            firstSwordModel.renderPart("edge01");
            GL11.glTranslatef(1.0f, 0, 0);
            firstSwordModel.renderPart("edge02");
            GL11.glPopMatrix();
            mc.renderEngine.bindTexture(firstSwordCase);
            firstSwordModel.renderPart("boxC");
        } else {
            mc.renderEngine.bindTexture(firstSwordEdge);
            firstSwordModel.renderPart("edge01");
            firstSwordModel.renderPart("edge02");
            mc.renderEngine.bindTexture(firstSwordCase);
            firstSwordModel.renderPart("boxO");
        }
    }

    private void renderUnionSwordModel(ItemStack item, float size) {
        GL11.glScalef(size, size, size);
        renderUnionSwordCore(false);
        renderOrganix(false);
        renderButterflyEdge("01", false);
        renderButterflyEdge("02", false);
        renderRuneBlade("01", false);
        renderRuneBlade("02", false);
    }

    private void renderOrganix(boolean limitBreak) {
        mc.renderEngine.bindTexture(organixEdge);
        unionSwordModel.renderPart("Organix_Org");
        mc.renderEngine.bindTexture(organixGrip);
        unionSwordModel.renderPart("OGrip01_OG01");
        unionSwordModel.renderPart("OGrip02_OG02");
    }

    private void renderButterflyEdge(String str, boolean limitBreak) {
        String edge = String.format("ButterflyEdge%s_BE%s", str, str);
        String grip = String.format("BGrip%s_BG%s", str, str);
        mc.renderEngine.bindTexture(butterflyedgeEdge);
        unionSwordModel.renderPart(edge);
        mc.renderEngine.bindTexture(butterflyedgeGrip);
        unionSwordModel.renderPart(grip);
    }

    private void renderRuneBlade(String str, boolean limitBreak) {
        String edge = String.format("RuneBlade%s_RE%s", str, str);
        String hand = String.format("RuneGuard%s_RH%s", str, str);
        String grip = String.format("RGrip%s_RG%s", str, str);
        mc.renderEngine.bindTexture(runebladeEdge);
        unionSwordModel.renderPart(edge);
        mc.renderEngine.bindTexture(runebladeHand);
        unionSwordModel.renderPart(grip);
        mc.renderEngine.bindTexture(runebladeGrip);
        unionSwordModel.renderPart(grip);
    }
}