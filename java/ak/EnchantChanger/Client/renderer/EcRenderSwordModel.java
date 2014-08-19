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

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

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

    private Minecraft mc;

    public EcRenderSwordModel() {
        mc = Minecraft.getMinecraft();
        zackSwordModel = AdvancedModelLoader.loadModel(zackSwordObj);
        ultimateWeaponModel = AdvancedModelLoader.loadModel(ultimateWeaponObj);
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
		if (item.getItem() instanceof EcItemZackSword) {
//            ZModel.renderItem(item, (EntityLivingBase) data[1]);
            renderSwordModel(item, (EntityLivingBase) data[1], type);
        }
		if (item.getItem() instanceof EcItemCloudSword) {
            CModel.renderItem(item, (EntityLivingBase) data[1]);
        }
		if (item.getItem() instanceof EcItemCloudSwordCore) {
            CCModel.renderItem(item, (EntityLivingBase) data[1],
                    ((EcItemCloudSwordCore) item.getItem()).isActive(item));
        }
		if (item.getItem() instanceof EcItemSephirothSword
				|| item.getItem() instanceof EcItemSephirothSwordImit) {
			SModel.renderItem(item, (EntityLivingBase) data[1]);
        }
		if (item.getItem() instanceof EcItemUltimateWeapon) {
//            UModel.renderItem(item, (EntityLivingBase) data[1]);]
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
//        if (item.getItem() instanceof EcItemCloudSword)
//            CModel.renderItem(item, (EntityLivingBase) data[1]);
//        if (item.getItem() instanceof EcItemCloudSwordCore)
//            CCModel.renderItem(item, (EntityLivingBase) data[1],
//                    ((EcItemCloudSwordCore) item.getItem()).isActive(item));
//        if (item.getItem() instanceof EcItemSephirothSword
//                || item.getItem() instanceof EcItemSephirothSwordImit)
//            SModel.renderItem(item, (EntityLivingBase) data[1]);
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
}