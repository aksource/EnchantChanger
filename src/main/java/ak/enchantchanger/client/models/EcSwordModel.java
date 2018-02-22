package ak.enchantchanger.client.models;

import ak.enchantchanger.api.ICustomModelItem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.ArrayList;
import java.util.List;

/**
 * 追加武器のモデルクラス
 * Created by A.K. on 15/07/25.
 */
public class EcSwordModel implements IBakedModel {
    /**
     * インベントリアイコン用モデル
     */
    private final IBakedModel guiModel;
    private final ItemOverrideList itemOverrideList;

    public EcSwordModel(IBakedModel model1, List<IModel> reTexturableModelList, float sizeFPV, float sizeTPV, ImmutableMap<String, String> textureMap) {
        this.guiModel = model1;
        List<IBakedModel> list = new ArrayList<>();
        for (IModel model : reTexturableModelList) {
            IBakedModel reTexturedModel;
            reTexturedModel = model.retexture(textureMap).bake(model.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
            PerspectiveModel perspectiveModel = new PerspectiveModel(model1, reTexturedModel, sizeFPV, sizeTPV);
            list.add(perspectiveModel);
        }
        this.itemOverrideList = new ItemOverrideListSword(guiModel, list);
    }

    @Override
    @Nonnull
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return Lists.newArrayList();
    }

    @Override
    @Nonnull
    public ItemOverrideList getOverrides() {
        return itemOverrideList;
    }

    @Override
    public boolean isGui3d() {
        return this.guiModel.isGui3d();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.guiModel.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.guiModel.isBuiltInRenderer();
    }

    @Override
    @Nonnull
    public TextureAtlasSprite getParticleTexture() {
        return this.guiModel.getParticleTexture();
    }

    @Override
    @Nonnull
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    private static class ItemOverrideListSword extends ItemOverrideList {
        /**
         * インベントリアイコン用モデル
         */
        private final IBakedModel guiModel;
        /**
         * モデルリスト
         */
        private final List<IBakedModel> modelList;

        ItemOverrideListSword(IBakedModel guiModel, List<IBakedModel> modelList) {
            super(Lists.newArrayList());
            this.guiModel = guiModel;
            this.modelList = modelList;
        }

        @Override
        @Nonnull
        public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack,
                                           @Nullable World world, @Nullable EntityLivingBase entity) {
            if (!stack.isEmpty() && stack.getItem() instanceof ICustomModelItem) {
                ICustomModelItem modelItem = (ICustomModelItem) stack.getItem();
                return modelItem.getPresentModel(stack, this.modelList);
            }
            return this.guiModel;
        }
    }

    public static class PerspectiveModel implements IBakedModel {

        /**
         * 一人称視点時のサイズ補正
         */
        private Vector3f scaleFirstPerson;
        /**
         * 三人称視点時のサイズ補正
         */
        private Vector3f scaleThirdPerson;
        /**
         * 一人称視点時の平行移動ベクトル
         */
        private Vector3f translationFirstPerson = new Vector3f(0.5F, 0.0F, 0.1F);
        /**
         * 三人称視点時の平行移動ベクトル
         */
//        private Vector3f translationThirdPerson = new Vector3f(-0.03F, 0.05F, -0.13F);
        private Vector3f translationThirdPerson = new Vector3f(0.0F, 0.05F, 0.0F);
        /**
         * 三人称視点時の平行移動ベクトル（左手装備時用）
         */
        private Vector3f translationThirdPersonLeft = new Vector3f(-0.03F, 0.05F, 0.0F);
        /**
         * 一人称視点時の回転ベクトル
         */
        private Quat4f rotateFirstPerson = TRSRTransformation.quatFromXYZDegrees(new Vector3f(10, 0, 0));
        /**
         * 一人称視点時の回転ベクトル（左手装備時用）
         */
        private Quat4f rotateFirstPersonLeft = TRSRTransformation.quatFromXYZDegrees(new Vector3f(10, 0, 0));
        /**
         * 三人称視点時の回転ベクトル
         */
        private Quat4f rotateThirdPerson = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 90, 0));
        /**
         * 三人称視点時の回転ベクトル（左手装備時用）
         */
        private Quat4f rotateThirdPersonLeft = TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, -90, 0));
        /**
         * インベントリアイコン用モデル
         */
        private IBakedModel guiModel;
        /**
         * 一人称・三人称視点用モデル
         */
        private IBakedModel handHeldModel;

        public PerspectiveModel(IBakedModel guiModel, IBakedModel handHeldModel, float sizeFPV, float sizeTPV) {
            this.guiModel = guiModel;
            this.handHeldModel = handHeldModel;
            this.scaleFirstPerson = new Vector3f(sizeFPV, sizeFPV, sizeFPV);
            this.scaleThirdPerson = new Vector3f(sizeTPV, sizeTPV, sizeTPV);
            rotateFirstPerson.mul(TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 90, 0)));
            rotateFirstPersonLeft.mul(TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, -90, 0)));
        }

        @Override
        @Nonnull
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(@Nonnull ItemCameraTransforms.TransformType cameraTransformType) {

            IBakedModel model;
            Matrix4f matrix4f = new Matrix4f();
            matrix4f.setIdentity();
//            GlStateManager.disableLighting();
            switch (cameraTransformType) {
                case GUI:
                    model = this.guiModel;
                    break;
                case FIRST_PERSON_RIGHT_HAND:
                    matrix4f = TRSRTransformation.mul(translationFirstPerson, rotateFirstPerson, scaleFirstPerson, null);
                    model = this.handHeldModel;
                    break;
                case FIRST_PERSON_LEFT_HAND:
                    matrix4f = TRSRTransformation.mul(translationFirstPerson, rotateFirstPersonLeft, scaleFirstPerson, null);
                    model = this.handHeldModel;
                    break;
                case HEAD:
                    matrix4f = TRSRTransformation.mul(null, null, scaleThirdPerson, null);
                    model = this.handHeldModel;
                    break;
                case THIRD_PERSON_RIGHT_HAND:
                    matrix4f = TRSRTransformation.mul(translationThirdPerson, rotateThirdPerson, scaleThirdPerson, null);
                    model = this.handHeldModel;
                    break;
                case THIRD_PERSON_LEFT_HAND:
                    matrix4f = TRSRTransformation.mul(translationThirdPersonLeft, rotateThirdPersonLeft, scaleThirdPerson, null);
                    model = this.handHeldModel;
                    break;
                case GROUND:
                    matrix4f = null;
                    model = this.guiModel;
                    break;
                default:
                    model = this.guiModel;
            }
            return Pair.of(model, matrix4f);
        }

        @Override
        @Nonnull
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return this.guiModel.getQuads(state, side, rand);
        }

        @Override
        public boolean isGui3d() {
            return this.guiModel.isGui3d();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return this.guiModel.isAmbientOcclusion();
        }

        @Override
        public boolean isBuiltInRenderer() {
            return this.guiModel.isBuiltInRenderer();
        }

        @Override
        @Nonnull
        public TextureAtlasSprite getParticleTexture() {
            return this.guiModel.getParticleTexture();
        }

        @Override
        @Nonnull
        public ItemCameraTransforms getItemCameraTransforms() {
            return this.guiModel.getItemCameraTransforms();
        }

        @Override
        @Nonnull
        public ItemOverrideList getOverrides() {
            return this.guiModel.getOverrides();
        }
    }
}
