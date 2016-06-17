package ak.EnchantChanger.Client.models;

import ak.EnchantChanger.item.EcItemSword;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * 追加武器のモデルクラス
 * Created by A.K. on 15/07/25.
 */
public class EcSwordModel implements ISmartItemModel{
    /** インベントリアイコン用モデル */
    private IBakedModel guiModel;
    /** モデルリスト */
    private List<IPerspectiveAwareModel> modelList;
    private Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
        public TextureAtlasSprite apply(ResourceLocation location) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        }
    };


    public EcSwordModel(IBakedModel model1, List<IRetexturableModel> retexturableModelList, float sizeFPV, float sizeTPV, ImmutableMap<String, String> textureMap) {
       this.guiModel = model1;
        List<IPerspectiveAwareModel> list = new ArrayList<>();
        for (IRetexturableModel model : retexturableModelList) {
            IFlexibleBakedModel retexturedModel;
            retexturedModel = model.retexture(textureMap).bake(model.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
            PerspectiveModel perspectiveModel = new PerspectiveModel(
                    (model1 instanceof IFlexibleBakedModel) ? (IFlexibleBakedModel) model1 : new IFlexibleBakedModel.Wrapper(model1, null),
                    retexturedModel, sizeFPV, sizeTPV);
            list.add(perspectiveModel);
        }
        this.modelList = list;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof EcItemSword) {
            EcItemSword ecItemSword = (EcItemSword) stack.getItem();
            IPerspectiveAwareModel model = ecItemSword.getPresentModel(stack, this.modelList);
            return model;
        }
        return this.guiModel;
    }



    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return null;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return null;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return null;
    }

    public static class PerspectiveModel implements IPerspectiveAwareModel {

        /** 一人称視点時のサイズ補正 */
        private Vector3f scaleFirstPerson;
        /** 三人称視点時のサイズ補正 */
        private Vector3f scaleThirdPerson;
        /** 一人称視点時の平行移動ベクトル */
        private Vector3f translationFirstPerson = new Vector3f(0.0F, 0.0F, 0.1F);
        /** 三人称視点時の平行移動ベクトル */
//        private Vector3f translationThirdPerson = new Vector3f(0.03F, 0.05F, -0.13F);
        private Vector3f translationThirdPerson = new Vector3f(-0.03F, 0.05F, -0.13F);
        /** 一人称視点時の回転ベクトル */
        private Quat4f rotateFirstPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, 0, 0));
        /** 三人称視点時の回転ベクトル */
//        private Quat4f rotateThirdPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(-90, 0, 0));
        private Quat4f rotateThirdPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(-90, 0, 0));
        /** インベントリアイコン用モデル */
        private IFlexibleBakedModel guiModel;
        /** 一人称・三人称視点用モデル */
        private IFlexibleBakedModel handHeldModel;
        public PerspectiveModel (IFlexibleBakedModel guiModel, IFlexibleBakedModel handHeldModel, float sizeFPV, float sizeTPV) {
            this.guiModel = guiModel;
            this.handHeldModel = handHeldModel;
            this.scaleFirstPerson = new Vector3f(sizeFPV, sizeFPV, sizeFPV);
            this.scaleThirdPerson = new Vector3f(sizeTPV, sizeTPV, sizeTPV);
            this.rotateThirdPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, -90, 0)));
            rotateFirstPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 120, 0)));
//            rotateFirstPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 0, 90)));
        }

        @Override
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {

            IFlexibleBakedModel model = null;
            Matrix4f matrix4f = null;
            switch (cameraTransformType) {
                case GUI:
                    model = this.guiModel;
                    matrix4f = null;
                    break;
                case FIRST_PERSON:
                    matrix4f = TRSRTransformation.mul(translationFirstPerson, rotateFirstPerson, scaleFirstPerson, null);
                    model = this.handHeldModel;
                    break;
                case HEAD:
                    model = this.handHeldModel;
                    break;
                case THIRD_PERSON:
                    matrix4f = TRSRTransformation.mul(translationThirdPerson, rotateThirdPerson, scaleThirdPerson, null);
                    model = this.handHeldModel;
                    break;
                default:
                    model = this.guiModel;
            }
            return Pair.of(model, matrix4f);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing facing) {
            return this.guiModel.getFaceQuads(facing);
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            GlStateManager.depthMask(false);
            return this.guiModel.getGeneralQuads();
        }

        @Override
        public boolean isGui3d() {
            return false;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return this.guiModel.isBuiltInRenderer();
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return this.guiModel.getParticleTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return this.guiModel.getItemCameraTransforms();
        }

        @Override
        public VertexFormat getFormat() {
            return null;
        }
    }
}
