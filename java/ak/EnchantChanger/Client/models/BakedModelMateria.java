package ak.EnchantChanger.Client.models;

import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.utils.EnchantmentUtils;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ak.EnchantChanger.Client.ClientProxy.resourceLocationsMateria;

/**
 * Created by A.K. on 2015/07/26.
 */
public class BakedModelMateria implements ISmartItemModel{

    public static Map<Integer, ResourceLocation> magicMateriaMap = new ConcurrentHashMap<>();
    public static Map<Integer, ResourceLocation> materiaMap = new ConcurrentHashMap<>();
    private static Map<ResourceLocation, IBakedModel> retexturedModelMap = new ConcurrentHashMap<>();
//    private ImmutableMap<String, String> retextureMap = new ImmutableMap.Builder<String, String>().put("materia10", "enchantchanger:gui/materia10").build();
    private Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
        public TextureAtlasSprite apply(ResourceLocation location) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        }
    };

    private IRetexturableModel b3dModel;
    public BakedModelMateria(IRetexturableModel model) {
        this.b3dModel = model;
    }

    public static void registerExtraMateria(int enchantmentId, int texId) {
        if (texId > 0 && texId < 16 && texId != 2)
            materiaMap.put(enchantmentId, resourceLocationsMateria[texId]);
    }

    private ImmutableMap<String, String> getTextureMap(ItemStack itemStack) {
        return new ImmutableMap.Builder<String, String>().put("materia10", getTextureFromItemStack(itemStack).toString()).build();
    }

    private ResourceLocation getTextureFromItemStack(ItemStack item) {
        if (item.getItem() instanceof EcItemMateria)
            return getTextureFromEnchantedItem(item);
        else
            return resourceLocationsMateria[10];
    }

    public ResourceLocation getTextureFromEnchantedItem(ItemStack item) {
        if (item.getItemDamage() > 0) {
            if (magicMateriaMap.containsKey(item.getItemDamage() - 1)) {
                return magicMateriaMap.get(item.getItemDamage() - 1);
            } else {
                return resourceLocationsMateria[10];
            }
        } else if (!item.isItemEnchanted())
            return resourceLocationsMateria[0];
        else if (materiaMap.containsKey(EnchantmentUtils.getEnchantmentId(item))) {
            return materiaMap.get(EnchantmentUtils.getEnchantmentId(item));
        } else
            return resourceLocationsMateria[10];
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        ResourceLocation resourceLocation = getTextureFromItemStack(stack);
        if (!retexturedModelMap.containsKey(resourceLocation)) {
            IModel retextureModel = this.b3dModel.retexture(getTextureMap(stack));
            IBakedModel bakedModel = retextureModel.bake(b3dModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
            retexturedModelMap.put(resourceLocation, new ReTexturedModel(bakedModel));
        }
        return retexturedModelMap.get(resourceLocation);
    }

    @Override
    public List getFaceQuads(EnumFacing enumFacing) {
        return null/*this.b3dModel.getFaceQuads(enumFacing)*/;
    }

    @Override
    public List getGeneralQuads() {
        return null/*this.b3dModel.getGeneralQuads()*/;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false/*this.b3dModel.isAmbientOcclusion()*/;
    }

    @Override
    public boolean isGui3d() {
        return false/*this.b3dModel.isGui3d()*/;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false/*this.b3dModel.isBuiltInRenderer()*/;
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return null/*this.b3dModel.getTexture()*/;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null/*this.b3dModel.getItemCameraTransforms()*/;
    }

    private static class ReTexturedModel implements IPerspectiveAwareModel {
        IBakedModel b3dModel;
        private float handheldSize = 0.3F;
        private float guiSize = 2.0F;
        private Vector3f vectorTransGui = new Vector3f(1.0F, 0.95F, 0.0F);
        private Vector3f vectorTransHand = new Vector3f(0.1F, 0.15F, -0.01F);
        public ReTexturedModel(IBakedModel bakedModel) {
            this.b3dModel = bakedModel;
        }

        @Override
        public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            /* TRSRTransformation��mul���\�b�h�ō���Ă邪�AMatrix4f�N���X�ɒP�Ƃ̐ݒ胁�\�b�h�����݂���
             * TRSR�̈Ӗ�
              * T=Transformation ���s�ړ��@��1����
              * R=Rotate ��]�i�O�j�@��2����
              * S=Scale ���T�C�Y�@��3����
              * R=Rotate ��]�i��j�@��4����
              * �ݒ肵����������Vector3f�N���X,Quat4f�N���X�̃C���X�^���X���w��B
              * �S��null���Ƃ����炭Identity Matrix�i�P�ʍs��j���Ԃ�*/
            Matrix4f matrix4fGui = TRSRTransformation.mul(vectorTransGui, null, new Vector3f(guiSize, guiSize, guiSize), null);
            Matrix4f matrix4fHandHeld = TRSRTransformation.mul(vectorTransHand, null, new Vector3f(handheldSize, handheldSize, handheldSize), null);
            GlStateManager.disableLighting();
            switch (cameraTransformType) {
                case GUI:
                    RenderItem.applyVanillaTransform(this.b3dModel.getItemCameraTransforms().gui);
                    return Pair.of(this.b3dModel, matrix4fGui);
                case FIRST_PERSON:
                    RenderItem.applyVanillaTransform(this.b3dModel.getItemCameraTransforms().firstPerson);
                    break;
                case THIRD_PERSON:
                    RenderItem.applyVanillaTransform(this.b3dModel.getItemCameraTransforms().thirdPerson);
                    break;
            }
            return Pair.of(this.b3dModel, matrix4fHandHeld);
        }

        @Override
        public List getFaceQuads(EnumFacing enumFacing) {
            return this.b3dModel.getFaceQuads(enumFacing);
        }

        @Override
        public List getGeneralQuads() {
            return this.b3dModel.getGeneralQuads();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return this.b3dModel.isAmbientOcclusion();
        }

        @Override
        public boolean isGui3d() {
            return this.b3dModel.isGui3d();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return this.b3dModel.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getTexture() {
            return this.b3dModel.getTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return this.b3dModel.getItemCameraTransforms();
        }
    }
    static {
        magicMateriaMap.put(0, resourceLocationsMateria[0]);
        magicMateriaMap.put(1, resourceLocationsMateria[15]);
        magicMateriaMap.put(2, resourceLocationsMateria[5]);
        magicMateriaMap.put(3, resourceLocationsMateria[9]);
        magicMateriaMap.put(4, resourceLocationsMateria[1]);
        magicMateriaMap.put(5, resourceLocationsMateria[7]);
        magicMateriaMap.put(6, resourceLocationsMateria[6]);
        magicMateriaMap.put(7, resourceLocationsMateria[10]);
        materiaMap.put(Enchantment.protection.effectId, resourceLocationsMateria[8]);
        materiaMap.put(Enchantment.fireProtection.effectId, resourceLocationsMateria[8]);
        materiaMap.put(Enchantment.featherFalling.effectId, resourceLocationsMateria[8]);
        materiaMap.put(Enchantment.blastProtection.effectId, resourceLocationsMateria[8]);
        materiaMap.put(Enchantment.projectileProtection.effectId, resourceLocationsMateria[8]);
        materiaMap.put(Enchantment.respiration.effectId, resourceLocationsMateria[4]);
        materiaMap.put(Enchantment.aquaAffinity.effectId, resourceLocationsMateria[4]);
        materiaMap.put(Enchantment.thorns.effectId, resourceLocationsMateria[8]);
        materiaMap.put(Enchantment.depthStrider.effectId, resourceLocationsMateria[4]);
        materiaMap.put(Enchantment.sharpness.effectId, resourceLocationsMateria[13]);
        materiaMap.put(Enchantment.smite.effectId, resourceLocationsMateria[13]);
        materiaMap.put(Enchantment.baneOfArthropods.effectId, resourceLocationsMateria[13]);
        materiaMap.put(Enchantment.knockback.effectId, resourceLocationsMateria[14]);
        materiaMap.put(Enchantment.fireAspect.effectId, resourceLocationsMateria[1]);
        materiaMap.put(Enchantment.looting.effectId, resourceLocationsMateria[9]);
        materiaMap.put(Enchantment.efficiency.effectId, resourceLocationsMateria[5]);
        materiaMap.put(Enchantment.silkTouch.effectId, resourceLocationsMateria[7]);
        materiaMap.put(Enchantment.unbreaking.effectId, resourceLocationsMateria[3]);
        materiaMap.put(Enchantment.fortune.effectId, resourceLocationsMateria[9]);
        materiaMap.put(Enchantment.power.effectId, resourceLocationsMateria[13]);
        materiaMap.put(Enchantment.punch.effectId, resourceLocationsMateria[14]);
        materiaMap.put(Enchantment.flame.effectId, resourceLocationsMateria[1]);
        materiaMap.put(Enchantment.infinity.effectId, resourceLocationsMateria[12]);
        materiaMap.put(Enchantment.luckOfTheSea.effectId, resourceLocationsMateria[4]);
        materiaMap.put(Enchantment.lure.effectId, resourceLocationsMateria[4]);
    }
}