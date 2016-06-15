package ak.EnchantChanger.Client.models;

import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.block.EcBlockMakoReactor;
import ak.EnchantChanger.item.EcItemBlockMakoReactor;
import ak.EnchantChanger.tileentity.EcTileEntityMakoReactor;
import ak.EnchantChanger.tileentity.EcTileMultiPass;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 魔晄炉ブロックのIFlexibleBakedModelクラス
 * Created by A.K. on 2015/09/05.
 */
public class MakoReactorWrapperBakedModel implements IFlexibleBakedModel, ISmartItemModel, ISmartBlockModel {

    private static Map<ResourceLocation, IBakedModel> blockModelMap = new ConcurrentHashMap<>();
    private TextureAtlasSprite frontTAS;
    private TextureAtlasSprite sideTAS;
    private IFlexibleBakedModel defaultModel;

    public MakoReactorWrapperBakedModel(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        this.frontTAS = bakedTextureGetter.apply(Constants.MAKO_REACTOR_FRONT_RL);
        this.sideTAS = bakedTextureGetter.apply(Constants.MAKO_REACTOR_SIDE_RL);
        TextureAtlasSprite defaultBlockTAS = bakedTextureGetter.apply(new ResourceLocation("blocks/iron_block"));
        this.defaultModel = new MakoReactorBakedModel(frontTAS, sideTAS, defaultBlockTAS, EnumFacing.NORTH);
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        IBakedModel bakedModel = this.defaultModel;
        Block block = state.getBlock();
        if (block instanceof EcBlockMakoReactor) {
            Integer posX = (Integer) extendedBlockState.getValue(EcBlockMakoReactor.PROPERTY_POS_X);
            Integer posY = (Integer) extendedBlockState.getValue(EcBlockMakoReactor.PROPERTY_POS_Y);
            Integer posZ = (Integer) extendedBlockState.getValue(EcBlockMakoReactor.PROPERTY_POS_Z);
            TileEntity tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(new BlockPos(posX, posY, posZ));
            if (tileEntity instanceof EcTileEntityMakoReactor) {
                ItemStack itemStack = ((EcTileMultiPass) tileEntity).getBaseBlockItemStack();
                byte side = ((EcTileEntityMakoReactor) tileEntity).face;
                bakedModel = makeMakoReactorBakedModel(itemStack, EnumFacing.getFront(side));
            }
        }
        return bakedModel;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        IBakedModel bakedModel = this.defaultModel;
        Item item = stack.getItem();
        if (item instanceof EcItemBlockMakoReactor) {
            ItemStack itemStack = ((EcItemBlockMakoReactor)item).getBaseBlockItemStack(stack);
            bakedModel = makeMakoReactorBakedModel(itemStack, EnumFacing.NORTH);
        }
        return bakedModel;
    }

    /**
     * 基底ブロックのItemStackからMakoReactorBakedModelを生成するメソッド
     * @param itemStack 基底ブロックのItemStack
     * @return MakoReactorBakedModel
     */
    private IBakedModel makeMakoReactorBakedModel(ItemStack itemStack, EnumFacing face) {
        IBakedModel itemStackModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
        TextureAtlasSprite particleTAS = itemStackModel.getTexture();
        return new MakoReactorBakedModel(frontTAS, sideTAS, particleTAS, face);
    }
    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
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

    @Override
    public VertexFormat getFormat() {
        return null;
    }

    private static class MakoReactorBakedModel implements IFlexibleBakedModel, IPerspectiveAwareModel {
        private TextureAtlasSprite frontTAS;
        private TextureAtlasSprite sideTAS;
        private TextureAtlasSprite blockTAS;
        private EnumFacing face;
        private FaceBakery faceBakery = new FaceBakery();
        private float innerStart = 0.01F;
        private float innerEnd = 15.99F;
        /** 三人称視点時の平行移動ベクトル */
        private Vector3f translationThirdPerson = new Vector3f(0.0F, 0.1F, -0.175F);
        private float scaleTirdFloat = 0.375F;
        /** 三人称視点時のサイズ補正 */
        private Vector3f scaleThirdPerson = new Vector3f(scaleTirdFloat, scaleTirdFloat, scaleTirdFloat);;
        /** 三人称視点時の回転ベクトル */
        private Quat4f rotateThirdPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, 0, 0));
        /** 一人称視点時の回転ベクトル */
        private Quat4f rotateFirstPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 90, 0));

        public MakoReactorBakedModel(TextureAtlasSprite frontTAS, TextureAtlasSprite sideTAS, TextureAtlasSprite blockTAS, EnumFacing face) {
            this.frontTAS = frontTAS;
            this.sideTAS = sideTAS;
            this.blockTAS = blockTAS;
            this.face = face;
            rotateThirdPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, -45, 0)));
            rotateThirdPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 0, 170)));
        }

        @Override
        public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            Matrix4f matrix4f = null;
            if (ItemCameraTransforms.TransformType.THIRD_PERSON == cameraTransformType) {
                matrix4f = TRSRTransformation.mul(translationThirdPerson, rotateThirdPerson, scaleThirdPerson, null);
            } else if (ItemCameraTransforms.TransformType.FIRST_PERSON == cameraTransformType) {
                matrix4f = TRSRTransformation.mul(null, rotateFirstPerson, null, null);
            }
            return Pair.of((IBakedModel) this, matrix4f);
        }

        @Override
        public List<BakedQuad> getFaceQuads(EnumFacing side) {
            Vector3f fromVec = new Vector3f(0F, 0F, 0F);
            Vector3f toVec = new Vector3f(16F, 16F, 16F);
            BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16F, 16F}, 0);
            TextureAtlasSprite sideTas = (face == side) ? frontTAS : sideTAS;
            String rlName = (face == side) ? frontTAS.getIconName() : sideTAS.getIconName();
            BlockPartFace partFace = new BlockPartFace(side, 0, rlName, uv);
            BakedQuad bakedQuad = faceBakery.makeBakedQuad(fromVec, toVec, partFace, sideTas, side, ModelRotation.X0_Y0, null, true, true);

            Vector3f fromVecBase = new Vector3f(innerStart, innerStart, innerStart);
            Vector3f toVecBase = new Vector3f(innerEnd, innerEnd, innerEnd);
            BlockFaceUV uvBase = new BlockFaceUV(new float[]{0.0F, 0.0F, 16F, 16F}, 0);
            BlockPartFace partFaceBase = new BlockPartFace(side, 0, blockTAS.getIconName(), uvBase);
            BakedQuad bakedQuadBase = faceBakery.makeBakedQuad(fromVecBase, toVecBase, partFaceBase, blockTAS, side, ModelRotation.X0_Y0, null, true, true);

            return Lists.newArrayList(bakedQuad, bakedQuadBase);
        }

        @Override
        public List<BakedQuad> getGeneralQuads() {
            return Collections.emptyList();
        }

        @Override
        public VertexFormat getFormat() {
            return Attributes.DEFAULT_BAKED_FORMAT;
        }

        @Override
        public boolean isAmbientOcclusion() {
            return true;
        }

        @Override
        public boolean isGui3d() {
            return true;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getTexture() {
            return blockTAS;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }
    }

}
