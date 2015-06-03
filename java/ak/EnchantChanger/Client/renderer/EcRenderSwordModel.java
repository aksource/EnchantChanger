package ak.EnchantChanger.Client.renderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EcRenderSwordModel {
/*	private static final EcModelUltimateWeapon UModel = new EcModelUltimateWeapon();
    private static final EcModelCloudSwordCore2 CCModel = new EcModelCloudSwordCore2();
	private static final EcModelCloudSword2 CModel = new EcModelCloudSword2();
	private static final EcModelSephirothSword SModel = new EcModelSephirothSword();
	private static final EcModelZackSword ZModel = new EcModelZackSword();

    private static final ResourceLocation zackSwordObj = new ResourceLocation(Constants.EcAssetsDomain, "models/bustersword.obj");
//    private final IModelCustom zackSwordModel;
    private static final ResourceLocation zackSwordEdge = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/bustersword256-edge.png");
    private static final ResourceLocation zackSwordBox = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/bustersword256-box.png");
    private static final ResourceLocation zackSwordCylinder = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/bustersword256-cylinder.png");

    private static final ResourceLocation ultimateWeaponObj = new ResourceLocation(Constants.EcAssetsDomain, "models/ultimateweapon.obj");
//    private final IModelCustom ultimateWeaponModel;
    private static final ResourceLocation ultimateWeaponSword = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/ultimateweapon256-sword.png");
    private static final ResourceLocation ultimateWeaponEmblem = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/ultimateweapon256-emblem.png");
    private static final ResourceLocation ultimateWeaponHandguard = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/ultimateweapon256-handguard.png");
    private static final ResourceLocation ultimateWeaponGrip = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/ultimateweapon256-grip.png");
    private static final ResourceLocation ultimateWeaponPipe01 = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/ultimateweapon256-pipe01.png");
    private static final ResourceLocation ultimateWeaponPipe02 = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/ultimateweapon256-pipe02.png");

    private static final ResourceLocation masamuneObj = new ResourceLocation(Constants.EcAssetsDomain, "models/masamune.obj");
//    private final IModelCustom masamuneModel;
    private static final ResourceLocation masamuneSword = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/masamune256-sword.png");
    private static final ResourceLocation masamuneGrip = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/masamune256-grip.png");

    private static final ResourceLocation firstSwordObj = new ResourceLocation(Constants.EcAssetsDomain, "models/firstsword.obj");
//    private final IModelCustom firstSwordModel;
    private static final ResourceLocation firstSwordEdge = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/firstsword256-edge.png");
    private static final ResourceLocation firstSwordCenter = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/firstsword256-center.png");
    private static final ResourceLocation firstSwordCase = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/firstsword256-case.png");
    private static final ResourceLocation firstSwordGrip = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/firstsword256-grip.png");

    private static final ResourceLocation unionSwordObj = new ResourceLocation(Constants.EcAssetsDomain, "models/unionsword.obj");
//    private final IModelCustom unionSwordModel;
    private static final ResourceLocation organixEdge = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/organix256-edge.png");
    private static final ResourceLocation organixGrip = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/organix256-grip.png");
    private static final ResourceLocation butterflyedgeEdge = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/butterflyedge256-edge.png");
    private static final ResourceLocation butterflyedgeGrip = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/butterflyedge256-grip.png");
    private static final ResourceLocation runebladeEdge = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/runeblade256-edge.png");
    private static final ResourceLocation runebladeHand = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/runeblade256-hand.png");
    private static final ResourceLocation runebladeGrip = new ResourceLocation(Constants.EcAssetsDomain, "textures/item/runeblade256-grip.png");

    private Minecraft mc;

    public EcRenderSwordModel() {
        mc = Minecraft.getMinecraft();
//        zackSwordModel = AdvancedModelLoader.loadModel(zackSwordObj);
//        ultimateWeaponModel = AdvancedModelLoader.loadModel(ultimateWeaponObj);
//        masamuneModel = AdvancedModelLoader.loadModel(masamuneObj);
//        firstSwordModel = AdvancedModelLoader.loadModel(firstSwordObj);
//        unionSwordModel = AdvancedModelLoader.loadModel(unionSwordObj);
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
            EntityLivingBase entityLivingBase = (EntityLivingBase)data[1];
            boolean isHanded = ItemStack.areItemStacksEqual(item, entityLivingBase.getHeldItem());
            renderSwordModel(item, entityLivingBase, type, isHanded);
        }
	}

    //Obj仕様renderメソッド
    private void renderSwordModel(ItemStack item, EntityLivingBase entityLivingBase, ItemRenderType type, boolean isHanded) {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.7F, 0.38F, 0.0F);
        GL11.glRotatef(40.0F, 0.0F, 0.0F, 1.0F);
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

    private void renderMasamuneModel(ItemStack itme, float size, boolean isHanded) {
        GL11.glScalef(size, size, size);
        mc.renderEngine.bindTexture(masamuneSword);
        masamuneModel.renderPart("sword");
        mc.renderEngine.bindTexture(masamuneGrip);
        masamuneModel.renderPart("grip");
        if (!isHanded) {
            //黒テクスチャなので流用
            mc.renderEngine.bindTexture(butterflyedgeGrip);
            masamuneModel.renderPart("sheath");
        }
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

    private void renderUnionSwordModel(ItemStack item, float size, boolean isHanded) {
        boolean isLimitBreak = mc.thePlayer.isSneaking() && isHanded;
        GL11.glPushMatrix();
        GL11.glScalef(size, size, size);
        renderUnionSwordCore(isLimitBreak);
        renderOrganix(isLimitBreak);
        renderButterflyEdge("01", isLimitBreak);
        renderButterflyEdge("02", isLimitBreak);
        renderRuneBlade("01", isLimitBreak, size);
        renderRuneBlade("02", isLimitBreak, size);
        GL11.glPopMatrix();
    }

    private void renderOrganix(boolean limitBreak) {
        if (limitBreak) {
            GL11.glPushMatrix();
            GL11.glRotatef(-15F, 0F, 0F, 1.0F);
            GL11.glTranslatef(0F, 2.0F, 0F);
        }
        mc.renderEngine.bindTexture(organixEdge);
        unionSwordModel.renderPart("Organix_Org");
        mc.renderEngine.bindTexture(organixGrip);
        unionSwordModel.renderPart("OGrip01_OG01");
        unionSwordModel.renderPart("OGrip02_OG02");
        if (limitBreak) {
            GL11.glPopMatrix();
        }
    }

    private void renderButterflyEdge(String str, boolean limitBreak) {
        String edge = String.format("ButterflyEdge%s_BE%s", str, str);
        String grip = String.format("BGrip%s_BG%s", str, str);
        if (limitBreak) {
            GL11.glPushMatrix();
            GL11.glRotatef(15F, 0F, 0F, 1.0F);
            GL11.glTranslatef(0F, 2.0F, 0F);
        }
        mc.renderEngine.bindTexture(butterflyedgeEdge);
        unionSwordModel.renderPart(edge);
        mc.renderEngine.bindTexture(butterflyedgeGrip);
        unionSwordModel.renderPart(grip);
        if (limitBreak) {
            GL11.glPopMatrix();
        }
    }

    private void renderRuneBlade(String str, boolean limitBreak, float size) {
        String edge = String.format("RuneBlade%s_RE%s", str, str);
        String hand = String.format("RuneGuard%s_RH%s", str, str);
        String grip = String.format("RGrip%s_RG%s", str, str);
        if (limitBreak) {
            GL11.glPushMatrix();
            GL11.glTranslatef(0F, 5.0F, 0F);
        }
        mc.renderEngine.bindTexture(runebladeEdge);
        unionSwordModel.renderPart(edge);

        if (limitBreak) {
            GL11.glPushMatrix();
            GL11.glTranslated(-0.46925D, 0.68262D, 0D);
            GL11.glRotatef(-45F, 0F, 0F, 1.0F);
            GL11.glTranslated(0.46925D, -0.68262D, 0D);

        }
        mc.renderEngine.bindTexture(runebladeHand);
        unionSwordModel.renderPart(hand);

        if (limitBreak) {
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glTranslated(-0.36411D, 0.68262D, 0D);
            GL11.glRotatef(180F, 0F, 0F, 1.0F);
            GL11.glTranslated(0.36411D, -0.68262D, 0D);

        }
        mc.renderEngine.bindTexture(runebladeGrip);
        unionSwordModel.renderPart(grip);
        if (limitBreak) {
            GL11.glPopMatrix();
            GL11.glPopMatrix();
        }
    }*/
}