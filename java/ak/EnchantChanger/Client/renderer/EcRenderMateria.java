package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.utils.EnchantmentUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

@SideOnly(Side.CLIENT)
public class EcRenderMateria implements IItemRenderer {
    private static double vert[];
    private static int face[];
    public static HashMap<Integer, ResourceLocation> masterMateriaMap = new HashMap<>();
    public static HashMap<Integer, ResourceLocation> materiaMap = new HashMap<>();
    private static final ResourceLocation materia0 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia0.png");
    private static final ResourceLocation materia1 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia1.png");
    private static final ResourceLocation materia3 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia3.png");
    private static final ResourceLocation materia4 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia4.png");
    private static final ResourceLocation materia5 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia5.png");
    private static final ResourceLocation materia6 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia6.png");
    private static final ResourceLocation materia7 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia7.png");
    private static final ResourceLocation materia8 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia8.png");
    private static final ResourceLocation materia9 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia9.png");
    private static final ResourceLocation materia10 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia10.png");
    private static final ResourceLocation materia11 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia11.png");
    private static final ResourceLocation materia12 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia12.png");
    private static final ResourceLocation materia13 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia13.png");
    private static final ResourceLocation materia14 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia14.png");
    private static final ResourceLocation materia15 = new ResourceLocation(Constants.EcAssetsDomain,
            "textures/gui/materia15.png");
    private static final ResourceLocation[] materiaRes = new ResourceLocation[]{materia0, materia1, null, materia3,
            materia4, materia5, materia6, materia7, materia8, materia9, materia10, materia11, materia12, materia13,
            materia14, materia15};
    private static final ResourceLocation materiaObj = new ResourceLocation(Constants.EcAssetsDomain, "models/spherelight.obj");
    private final IModelCustom sphereModel;

    public EcRenderMateria() {
        sphereModel = AdvancedModelLoader.loadModel(materiaObj);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        float f0 = 0.2F;
        float f1 = 8F;
        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            this.renderMateriaModel(item, 0.5f, 0, 0, f0, type);
        } else if (type == ItemRenderType.INVENTORY) {
            this.renderMateriaModel(item, 8f, 8f, 0f, f1, type);
        } else if (type == ItemRenderType.ENTITY) {
            this.renderMateriaModel(item, 0, 0, 0, f0, type);
        }
    }

    //Obj仕様renderメソッド
    private void renderMateriaModel(ItemStack item, float x, float y, float z, float size, ItemRenderType type) {
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) GL11.glTranslatef(0.3f, 0.2f, 0);
        mc.renderEngine.bindTexture(this.getTextureFromItemStack(item));
        GL11.glScalef(size, size, size);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        sphereModel.renderAll();
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    @Deprecated
    public void renderMateria(ItemStack item, float x, float y, float z, float size, ItemRenderType type) {
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, z);
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON)
            GL11.glTranslatef(0.3f, 0.2f, 0);
        mc.renderEngine.bindTexture(this.getTextureFromItemStack(item));
        //		GL11.glEnable(GL_BLEND);
        //		GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        GL11.glScalef(size, size, size);
        tessellator.startDrawing(4);

        for (int v : face) {
            double d = Math.atan2(vert[v * 3], vert[v * 3 + 2]) / Math.PI;
            if (d > 0) {
                tessellator.addVertexWithUV(
                        0 + (vert[v * 3] + 1) / 2,
                        0 + (vert[v * 3 + 1] + 1) / 2,
                        0 + (vert[v * 3 + 2] + 1) / 2,
                        d / 16D,
                        (vert[v * 3 + 1] + 1) / 32D);
            } else {
                tessellator.addVertexWithUV(
                        0 + (vert[v * 3] + 1) / 2,
                        0 + (vert[v * 3 + 1] + 1) / 2,
                        0 + (vert[v * 3 + 2] + 1) / 2,
                        -d / 16D,
                        (vert[v * 3 + 1] + 1) / 32D);
            }

        }

        tessellator.draw();
        GL11.glTranslatef(-x, -y, -z);
        GL11.glPopMatrix();
    }

    private ResourceLocation getTextureFromItemStack(ItemStack item) {
        if (item.getItem() instanceof EcItemMateria)
            return getTextuerfromEnch(item);
        else
            return materia10;
    }

    public ResourceLocation getTextuerfromEnch(ItemStack item) {
        if (item.getItemDamage() > 0) {
            if (masterMateriaMap.containsKey(item.getItemDamage() - 1)) {
                return masterMateriaMap.get(item.getItemDamage() - 1);
            } else {
                return materia10;
            }
        } else if (!item.isItemEnchanted())
            return materia0;
        else if (materiaMap.containsKey(EnchantmentUtils.getMateriaEnchKind(item))) {
            return materiaMap.get(EnchantmentUtils.getMateriaEnchKind(item));
        } else
            return materia10;
    }

    public static void registerExtraMateria(int enchantmentId, int texId) {
        if (texId > 0 && texId < 16 && texId != 2)
            materiaMap.put(enchantmentId, materiaRes[texId]);
    }

    static {
        masterMateriaMap.put(0, materia0);
        masterMateriaMap.put(1, materia15);
        masterMateriaMap.put(2, materia5);
        masterMateriaMap.put(3, materia9);
        masterMateriaMap.put(4, materia11);
        masterMateriaMap.put(5, materia7);
        masterMateriaMap.put(6, materia6);
        masterMateriaMap.put(7, materia10);
        materiaMap.put(0, materia8);
        materiaMap.put(1, materia8);
        materiaMap.put(2, materia8);
        materiaMap.put(3, materia8);
        materiaMap.put(4, materia8);
        materiaMap.put(5, materia4);
        materiaMap.put(6, materia4);
        materiaMap.put(7, materia8);
        materiaMap.put(16, materia13);
        materiaMap.put(17, materia13);
        materiaMap.put(18, materia13);
        materiaMap.put(19, materia14);
        materiaMap.put(20, materia1);
        materiaMap.put(21, materia9);
        materiaMap.put(32, materia5);
        materiaMap.put(33, materia7);
        materiaMap.put(34, materia3);
        materiaMap.put(35, materia9);
        materiaMap.put(48, materia13);
        materiaMap.put(49, materia14);
        materiaMap.put(50, materia1);
        materiaMap.put(51, materia12);
        materiaMap.put(61, materia4);
        materiaMap.put(62, materia4);
        vert = new double[]{
                0.000000, 1.000000, 0.000000,
                0.000000, -1.000000, 0.000000,
                0.873422, 0.486965, 0.000000,
                -0.873422, -0.486965, 0.000000,
                0.269902, 0.486965, -0.830673,
                -0.269902, -0.486965, 0.830673,
                -0.706613, 0.486965, -0.513384,
                0.706613, -0.486965, 0.513384,
                -0.706613, 0.486965, 0.513385,
                0.706613, -0.486965, -0.513385,
                0.269902, 0.486965, 0.830673,
                -0.269902, -0.486965, -0.830673,
                0.506476, 0.862254, 0.000000,
                0.666149, 0.567453, -0.483986,
                0.156510, 0.862254, -0.481687,
                -0.254446, 0.567453, -0.783106,
                -0.409748, 0.862254, -0.297699,
                -0.823406, 0.567453, 0.000000,
                -0.409748, 0.862254, 0.297699,
                -0.254446, 0.567453, 0.783106,
                0.156510, 0.862254, 0.481687,
                0.666149, 0.567453, 0.483986,
                -0.666149, -0.567453, 0.483986,
                -0.506476, -0.862254, 0.000000,
                -0.156510, -0.862254, 0.481687,
                0.254446, -0.567453, 0.783106,
                0.409748, -0.862254, 0.297699,
                0.823406, -0.567453, -0.000000,
                0.409748, -0.862254, -0.297699,
                0.254446, -0.567453, -0.783106,
                -0.156510, -0.862254, -0.481687,
                -0.666149, -0.567453, -0.483986,
                0.951056, 0.000000, -0.309017,
                0.587785, 0.000000, -0.809017,
                -0.000000, 0.000000, -1.000000,
                -0.587785, 0.000000, -0.809017,
                -0.951056, 0.000000, -0.309017,
                -0.951056, 0.000000, 0.309017,
                -0.587785, 0.000000, 0.809017,
                0.000000, 0.000000, 1.000000,
                0.587785, 0.000000, 0.809017,
                0.951056, 0.000000, 0.309017,
                0.220950, 0.699114, -0.680015,
                0.430402, 0.747999, -0.505224,
                0.485560, 0.546961, -0.681957,
                0.262436, 0.964949, 0.000000,
                0.347236, 0.903206, -0.252282,
                0.081097, 0.964949, -0.249592,
                0.798626, 0.546961, -0.251059,
                0.613498, 0.747999, -0.253213,
                0.715010, 0.699114, 0.000000,
                -0.578456, 0.699114, -0.420272,
                -0.347495, 0.747999, -0.565459,
                -0.498533, 0.546961, -0.672532,
                -0.132633, 0.903206, -0.408201,
                -0.212316, 0.964949, -0.154256,
                0.008017, 0.546961, -0.837120,
                -0.051239, 0.747999, -0.661719,
                -0.578456, 0.699114, 0.420273,
                -0.645166, 0.747999, 0.155751,
                -0.793671, 0.546961, 0.266309,
                -0.429208, 0.903206, 0.000000,
                -0.212316, 0.964949, 0.154256,
                -0.793671, 0.546961, -0.266309,
                -0.645166, 0.747999, -0.155751,
                0.220950, 0.699114, 0.680015,
                -0.051239, 0.747999, 0.661719,
                0.008017, 0.546961, 0.837120,
                -0.132632, 0.903206, 0.408201,
                0.081097, 0.964949, 0.249592,
                -0.498533, 0.546961, 0.672532,
                -0.347495, 0.747999, 0.565459,
                0.613498, 0.747999, 0.253213,
                0.798626, 0.546961, 0.251059,
                0.347237, 0.903206, 0.252282,
                0.485561, 0.546961, 0.681957,
                0.430402, 0.747999, 0.505224,
                -0.081097, -0.964949, 0.249592,
                -0.347236, -0.903206, 0.252282,
                -0.262436, -0.964949, 0.000000,
                -0.485560, -0.546961, 0.681957,
                -0.430402, -0.747999, 0.505224,
                -0.220950, -0.699114, 0.680015,
                -0.715010, -0.699114, 0.000000,
                -0.613498, -0.747999, 0.253213,
                -0.798626, -0.546961, 0.251059,
                0.212316, -0.964949, 0.154256,
                0.132633, -0.903206, 0.408201,
                0.498533, -0.546961, 0.672532,
                0.347495, -0.747999, 0.565459,
                0.578456, -0.699114, 0.420272,
                0.051239, -0.747999, 0.661719,
                -0.008017, -0.546961, 0.837120,
                0.212316, -0.964949, -0.154256,
                0.429208, -0.903206, -0.000000,
                0.793671, -0.546961, -0.266309,
                0.645166, -0.747999, -0.155751,
                0.578456, -0.699114, -0.420273,
                0.645166, -0.747999, 0.155751,
                0.793671, -0.546961, 0.266309,
                -0.081097, -0.964949, -0.249592,
                0.132632, -0.903206, -0.408201,
                -0.008017, -0.546961, -0.837120,
                0.051239, -0.747999, -0.661719,
                -0.220950, -0.699114, -0.680015,
                0.347495, -0.747999, -0.565459,
                0.498533, -0.546961, -0.672532,
                -0.347237, -0.903206, -0.252282,
                -0.798626, -0.546961, -0.251059,
                -0.613498, -0.747999, -0.253213,
                -0.430402, -0.747999, -0.505224,
                -0.485561, -0.546961, -0.681957,
                0.676468, -0.254494, -0.691103,
                0.809017, 0.000000, -0.587785,
                0.866318, -0.254494, -0.429797,
                0.664005, 0.300487, -0.684693,
                0.448238, 0.254494, -0.856922,
                0.953495, 0.254494, -0.161496,
                0.856371, 0.300487, -0.419925,
                -0.448238, -0.254494, -0.856922,
                -0.309017, 0.000000, -0.951056,
                -0.141054, -0.254494, -0.956732,
                -0.445993, 0.300487, -0.843088,
                -0.676468, 0.254494, -0.691103,
                0.141054, 0.254494, -0.956732,
                -0.134739, 0.300487, -0.944221,
                -0.953495, -0.254494, 0.161496,
                -1.000000, 0.000000, 0.000000,
                -0.953495, -0.254494, -0.161496,
                -0.939644, 0.300487, 0.163636,
                -0.866318, 0.254494, 0.429797,
                -0.866318, 0.254494, -0.429797,
                -0.939644, 0.300487, -0.163636,
                -0.141054, -0.254494, 0.956732,
                -0.309017, 0.000000, 0.951057,
                -0.448238, -0.254494, 0.856922,
                -0.134739, 0.300487, 0.944221,
                0.141054, 0.254494, 0.956732,
                -0.676468, 0.254494, 0.691103,
                -0.445993, 0.300487, 0.843088,
                0.866318, -0.254494, 0.429797,
                0.809017, 0.000000, 0.587785,
                0.676468, -0.254494, 0.691103,
                0.856371, 0.300487, 0.419925,
                0.953495, 0.254494, 0.161496,
                0.448238, 0.254494, 0.856922,
                0.664005, 0.300487, 0.684693,
                -0.664005, -0.300487, 0.684693,
                -0.809017, 0.000000, 0.587785,
                -0.856371, -0.300487, 0.419925,
                0.445993, -0.300487, 0.843088,
                0.309017, 0.000000, 0.951056,
                0.134739, -0.300487, 0.944221,
                0.939644, -0.300487, -0.163636,
                1.000000, 0.000000, -0.000000,
                0.939644, -0.300487, 0.163636,
                0.134739, -0.300487, -0.944221,
                0.309017, 0.000000, -0.951057,
                0.445993, -0.300487, -0.843088,
                -0.856371, -0.300487, -0.419925,
                -0.809017, 0.000000, -0.587785,
                -0.664005, -0.300487, -0.684693
        };
        face = new int[]{
                13, 44, 43,
                4, 42, 44,
                14, 43, 42,
                42, 43, 44,
                14, 47, 46,
                0, 45, 47,
                12, 46, 45,
                45, 46, 47,
                12, 50, 49,
                2, 48, 50,
                13, 49, 48,
                48, 49, 50,
                14, 46, 43,
                12, 49, 46,
                13, 43, 49,
                49, 43, 46,
                15, 53, 52,
                6, 51, 53,
                16, 52, 51,
                51, 52, 53,
                16, 55, 54,
                0, 47, 55,
                14, 54, 47,
                47, 54, 55,
                14, 42, 57,
                4, 56, 42,
                15, 57, 56,
                56, 57, 42,
                16, 54, 52,
                14, 57, 54,
                15, 52, 57,
                57, 52, 54,
                17, 60, 59,
                8, 58, 60,
                18, 59, 58,
                58, 59, 60,
                18, 62, 61,
                0, 55, 62,
                16, 61, 55,
                55, 61, 62,
                16, 51, 64,
                6, 63, 51,
                17, 64, 63,
                63, 64, 51,
                18, 61, 59,
                16, 64, 61,
                17, 59, 64,
                64, 59, 61,
                19, 67, 66,
                10, 65, 67,
                20, 66, 65,
                65, 66, 67,
                20, 69, 68,
                0, 62, 69,
                18, 68, 62,
                62, 68, 69,
                18, 58, 71,
                8, 70, 58,
                19, 71, 70,
                70, 71, 58,
                20, 68, 66,
                18, 71, 68,
                19, 66, 71,
                71, 66, 68,
                21, 73, 72,
                2, 50, 73,
                12, 72, 50,
                50, 72, 73,
                12, 45, 74,
                0, 69, 45,
                20, 74, 69,
                69, 74, 45,
                20, 65, 76,
                10, 75, 65,
                21, 76, 75,
                75, 76, 65,
                12, 74, 72,
                20, 76, 74,
                21, 72, 76,
                76, 72, 74,
                23, 79, 78,
                1, 77, 79,
                24, 78, 77,
                77, 78, 79,
                24, 82, 81,
                5, 80, 82,
                22, 81, 80,
                80, 81, 82,
                22, 85, 84,
                3, 83, 85,
                23, 84, 83,
                83, 84, 85,
                24, 81, 78,
                22, 84, 81,
                23, 78, 84,
                84, 78, 81,
                24, 77, 87,
                1, 86, 77,
                26, 87, 86,
                86, 87, 77,
                26, 90, 89,
                7, 88, 90,
                25, 89, 88,
                88, 89, 90,
                25, 92, 91,
                5, 82, 92,
                24, 91, 82,
                82, 91, 92,
                26, 89, 87,
                25, 91, 89,
                24, 87, 91,
                91, 87, 89,
                26, 86, 94,
                1, 93, 86,
                28, 94, 93,
                93, 94, 86,
                28, 97, 96,
                9, 95, 97,
                27, 96, 95,
                95, 96, 97,
                27, 99, 98,
                7, 90, 99,
                26, 98, 90,
                90, 98, 99,
                28, 96, 94,
                27, 98, 96,
                26, 94, 98,
                98, 94, 96,
                28, 93, 101,
                1, 100, 93,
                30, 101, 100,
                100, 101, 93,
                30, 104, 103,
                11, 102, 104,
                29, 103, 102,
                102, 103, 104,
                29, 106, 105,
                9, 97, 106,
                28, 105, 97,
                97, 105, 106,
                30, 103, 101,
                29, 105, 103,
                28, 101, 105,
                105, 101, 103,
                30, 100, 107,
                1, 79, 100,
                23, 107, 79,
                79, 107, 100,
                23, 83, 109,
                3, 108, 83,
                31, 109, 108,
                108, 109, 83,
                31, 111, 110,
                11, 104, 111,
                30, 110, 104,
                104, 110, 111,
                23, 109, 107,
                31, 110, 109,
                30, 107, 110,
                110, 107, 109,
                32, 114, 113,
                9, 112, 114,
                33, 113, 112,
                112, 113, 114,
                33, 116, 115,
                4, 44, 116,
                13, 115, 44,
                44, 115, 116,
                13, 48, 118,
                2, 117, 48,
                32, 118, 117,
                117, 118, 48,
                33, 115, 113,
                13, 118, 115,
                32, 113, 118,
                118, 113, 115,
                34, 121, 120,
                11, 119, 121,
                35, 120, 119,
                119, 120, 121,
                35, 123, 122,
                6, 53, 123,
                15, 122, 53,
                53, 122, 123,
                15, 56, 125,
                4, 124, 56,
                34, 125, 124,
                124, 125, 56,
                35, 122, 120,
                15, 125, 122,
                34, 120, 125,
                125, 120, 122,
                36, 128, 127,
                3, 126, 128,
                37, 127, 126,
                126, 127, 128,
                37, 130, 129,
                8, 60, 130,
                17, 129, 60,
                60, 129, 130,
                17, 63, 132,
                6, 131, 63,
                36, 132, 131,
                131, 132, 63,
                37, 129, 127,
                17, 132, 129,
                36, 127, 132,
                132, 127, 129,
                38, 135, 134,
                5, 133, 135,
                39, 134, 133,
                133, 134, 135,
                39, 137, 136,
                10, 67, 137,
                19, 136, 67,
                67, 136, 137,
                19, 70, 139,
                8, 138, 70,
                38, 139, 138,
                138, 139, 70,
                39, 136, 134,
                19, 139, 136,
                38, 134, 139,
                139, 134, 136,
                40, 142, 141,
                7, 140, 142,
                41, 141, 140,
                140, 141, 142,
                41, 144, 143,
                2, 73, 144,
                21, 143, 73,
                73, 143, 144,
                21, 75, 146,
                10, 145, 75,
                40, 146, 145,
                145, 146, 75,
                41, 143, 141,
                21, 146, 143,
                40, 141, 146,
                146, 141, 143,
                22, 80, 147,
                5, 135, 80,
                38, 147, 135,
                135, 147, 80,
                38, 138, 148,
                8, 130, 138,
                37, 148, 130,
                130, 148, 138,
                37, 126, 149,
                3, 85, 126,
                22, 149, 85,
                85, 149, 126,
                38, 148, 147,
                37, 149, 148,
                22, 147, 149,
                149, 147, 148,
                25, 88, 150,
                7, 142, 88,
                40, 150, 142,
                142, 150, 88,
                40, 145, 151,
                10, 137, 145,
                39, 151, 137,
                137, 151, 145,
                39, 133, 152,
                5, 92, 133,
                25, 152, 92,
                92, 152, 133,
                40, 151, 150,
                39, 152, 151,
                25, 150, 152,
                152, 150, 151,
                27, 95, 153,
                9, 114, 95,
                32, 153, 114,
                114, 153, 95,
                32, 117, 154,
                2, 144, 117,
                41, 154, 144,
                144, 154, 117,
                41, 140, 155,
                7, 99, 140,
                27, 155, 99,
                99, 155, 140,
                32, 154, 153,
                41, 155, 154,
                27, 153, 155,
                155, 153, 154,
                29, 102, 156,
                11, 121, 102,
                34, 156, 121,
                121, 156, 102,
                34, 124, 157,
                4, 116, 124,
                33, 157, 116,
                116, 157, 124,
                33, 112, 158,
                9, 106, 112,
                29, 158, 106,
                106, 158, 112,
                34, 157, 156,
                33, 158, 157,
                29, 156, 158,
                158, 156, 157,
                31, 108, 159,
                3, 128, 108,
                36, 159, 128,
                128, 159, 108,
                36, 131, 160,
                6, 123, 131,
                35, 160, 123,
                123, 160, 131,
                35, 119, 161,
                11, 111, 119,
                31, 161, 111,
                111, 161, 119,
                36, 160, 159,
                35, 161, 160,
                31, 159, 161,
                161, 159, 160
        };
    }
}