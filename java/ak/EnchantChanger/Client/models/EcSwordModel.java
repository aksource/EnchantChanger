package ak.EnchantChanger.Client.models;

import ak.EnchantChanger.item.EcItemSword;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
       this.guiModel = guiModel;
        List<IPerspectiveAwareModel> list = new ArrayList<>();
        for (IRetexturableModel model : retexturableModelList) {
            IBakedModel retexturedModel;
            retexturedModel = model.retexture(textureMap).bake(model.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
            PerspectiveModel perspectiveModel = new PerspectiveModel(model1, retexturedModel, sizeFPV, sizeTPV);
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
    public List getFaceQuads(EnumFacing facing) {
        return null;
    }

    @Override
    public List getGeneralQuads() {
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
    public TextureAtlasSprite getTexture() {
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
        private Vector3f translationThirdPerson = new Vector3f(0.03F, 0.05F, -0.13F);
        /** 一人称視点時の回転ベクトル */
        private Quat4f rotateFirstPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, 0, 0));
        /** 三人称視点時の回転ベクトル */
        private Quat4f rotateThirdPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(-90, 0, 0));
        /** インベントリアイコン用モデル */
        private IBakedModel guiModel;
        /** 一人称・三人称視点用モデル */
        private IBakedModel handHeldModel;
        public PerspectiveModel (IBakedModel guiModel, IBakedModel handHeldModel, float sizeFPV, float sizeTPV) {
            this.guiModel = guiModel;
            this.handHeldModel = handHeldModel;
            this.scaleFirstPerson = new Vector3f(sizeFPV, sizeFPV, sizeFPV);
            this.scaleThirdPerson = new Vector3f(sizeTPV, sizeTPV, sizeTPV);
//        this.rotateThirdPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 30, 0)));
            rotateFirstPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 120, 0)));
//        rotateFirstPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 0, 25)));
        }

        @Override
        public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {

            IBakedModel model = null;
            Matrix4f matrix4f = null;
            switch (cameraTransformType) {
                case GUI:
                    RenderItem.applyVanillaTransform(this.guiModel.getItemCameraTransforms().gui);
                    model = this.guiModel;
                    matrix4f = null;
                    break;
                case FIRST_PERSON:
//                RenderItem.applyVanillaTransform(this.handHeldModel.getItemCameraTransforms().firstPerson);
                    matrix4f = TRSRTransformation.mul(translationFirstPerson, rotateFirstPerson, scaleFirstPerson, null);
                    model = this.handHeldModel;
                    break;
                case HEAD:
//                RenderItem.applyVanillaTransform(this.handHeldModel.getItemCameraTransforms().head);
                    model = this.handHeldModel;
                    break;
                case THIRD_PERSON:
//                RenderItem.applyVanillaTransform(vanillaToolTPTransformation.toItemTransform());
                    matrix4f = TRSRTransformation.mul(translationThirdPerson, rotateThirdPerson, scaleThirdPerson, null);
                    model = this.handHeldModel;
                    break;
            }
            return Pair.of(model, matrix4f);
        }

        @Override
        public List getFaceQuads(EnumFacing facing) {
            return this.guiModel.getFaceQuads(facing);
        }

        @Override
        public List getGeneralQuads() {
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
        public TextureAtlasSprite getTexture() {
            return this.guiModel.getTexture();
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return this.guiModel.getItemCameraTransforms();
        }
    }
}
