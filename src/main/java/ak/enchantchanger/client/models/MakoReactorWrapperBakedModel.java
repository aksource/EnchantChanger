package ak.enchantchanger.client.models;

/**
 * 魔晄炉ブロックのIFlexibleBakedModelクラス
 * Created by A.K. on 2015/09/05.
 */
@Deprecated
public class MakoReactorWrapperBakedModel/* implements IBakedModel*/ {
//
//    private static Map<ResourceLocation, IBakedModel> blockModelMap = new ConcurrentHashMap<>();
//    private final TextureAtlasSprite frontTAS;
//    private final TextureAtlasSprite sideTAS;
//    private final IBakedModel defaultModel;
//    private final FaceBakery faceBakery = new FaceBakery();
//    private static final float innerStart = 0.01F;
//    private static final float innerEnd = 15.99F;
//
//    public MakoReactorWrapperBakedModel(Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
//        this.frontTAS = bakedTextureGetter.apply(Constants.MAKO_REACTOR_FRONT_RL);
//        this.sideTAS = bakedTextureGetter.apply(Constants.MAKO_REACTOR_SIDE_RL);
//        TextureAtlasSprite defaultBlockTAS = bakedTextureGetter.apply(new ResourceLocation("blocks/iron_block"));
////        this.defaultModel = new MakoReactorBakedModel(frontTAS, sideTAS, defaultBlockTAS, EnumFacing.NORTH);
//    }
//
////    @Override
//    public IBakedModel handleItemState(ItemStack stack) {
//        IBakedModel bakedModel = this.defaultModel;
//        Item item = stack.getItem();
//        if (item instanceof EcItemBlockMakoReactor) {
//            ItemStack itemStack = ((EcItemBlockMakoReactor)item).getBaseBlockItemStack(stack);
//            bakedModel = makeMakoReactorBakedModel(itemStack, EnumFacing.NORTH);
//        }
//        return bakedModel;
//    }
//
//    /**
//     * 基底ブロックのItemStackからMakoReactorBakedModelを生成するメソッド
//     * @param itemStack 基底ブロックのItemStack
//     * @return MakoReactorBakedModel
//     */
//    private IBakedModel makeMakoReactorBakedModel(ItemStack itemStack, EnumFacing face) {
//        IBakedModel itemStackModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(itemStack);
//        TextureAtlasSprite particleTAS = itemStackModel.getParticleTexture();
//        return new MakoReactorBakedModel(frontTAS, sideTAS, particleTAS, face);
//    }
//
//    @Override
//    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
//        if (side == null) side = EnumFacing.NORTH;
//        if (state != null) {
//
//            IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
//            Integer posX = (Integer) extendedBlockState.getValue(EcBlockMakoReactor.PROPERTY_POS_X);
//            Integer posY = (Integer) extendedBlockState.getValue(EcBlockMakoReactor.PROPERTY_POS_Y);
//            Integer posZ = (Integer) extendedBlockState.getValue(EcBlockMakoReactor.PROPERTY_POS_Z);
//            TileEntity tileEntity = Minecraft.getMinecraft().world.getTileEntity(new BlockPos(posX, posY, posZ));
//            if (tileEntity != null) {
//                byte byteSide = ((EcTileEntityMakoReactor) tileEntity).face;
//                EnumFacing face = EnumFacing.getFront(byteSide);
//                org.lwjgl.util.vector.Vector3f fromVec = new org.lwjgl.util.vector.Vector3f(0F, 0F, 0F);
//                org.lwjgl.util.vector.Vector3f toVec = new org.lwjgl.util.vector.Vector3f(16F, 16F, 16F);
//                BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16F, 16F}, 0);
//                TextureAtlasSprite sideTas = (face == side) ? frontTAS : sideTAS;
//                String rlName = (face == side) ? frontTAS.getIconName() : sideTAS.getIconName();
//                BlockPartFace partFace = new BlockPartFace(side, 0, rlName, uv);
//                BakedQuad bakedQuad = faceBakery.makeBakedQuad(fromVec, toVec, partFace, sideTas, side, ModelRotation.X0_Y0, null, true, true);
//
//                org.lwjgl.util.vector.Vector3f fromVecBase = new org.lwjgl.util.vector.Vector3f(innerStart, innerStart, innerStart);
//                org.lwjgl.util.vector.Vector3f toVecBase = new org.lwjgl.util.vector.Vector3f(innerEnd, innerEnd, innerEnd);
//                BlockFaceUV uvBase = new BlockFaceUV(new float[]{0.0F, 0.0F, 16F, 16F}, 0);
//                BlockPartFace partFaceBase = new BlockPartFace(side, 0, getParticleTexture().getIconName(), uvBase);
//                BakedQuad bakedQuadBase = faceBakery.makeBakedQuad(fromVecBase, toVecBase, partFaceBase, getParticleTexture(), side, ModelRotation.X0_Y0, null, true, true);
//
//                return Lists.newArrayList(bakedQuad, bakedQuadBase);
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public boolean isAmbientOcclusion() {
//        return this.defaultModel.isAmbientOcclusion();
//    }
//
//    @Override
//    public boolean isGui3d() {
//        return this.defaultModel.isGui3d();
//    }
//
//    @Override
//    public boolean isBuiltInRenderer() {
//        return this.defaultModel.isBuiltInRenderer();
//    }
//
//    @Override
//    @Nonnull
//    public TextureAtlasSprite getParticleTexture() {
//        return this.defaultModel.getParticleTexture();
//    }
//
//    @Override
//    @Nonnull
//    public ItemCameraTransforms getItemCameraTransforms() {
//        return ItemCameraTransforms.DEFAULT;
//    }
//
//    @Override
//    @Nonnull
//    public ItemOverrideList getOverrides() {
//        return this.defaultModel.getOverrides();
//    }
//
//    private static class MakoReactorBakedModel implements IPerspectiveAwareModel {
//        private TextureAtlasSprite frontTAS;
//        private TextureAtlasSprite sideTAS;
//        private TextureAtlasSprite blockTAS;
//        private EnumFacing face;
//        private FaceBakery faceBakery = new FaceBakery();
//        private float innerStart = 0.01F;
//        private float innerEnd = 15.99F;
//        /** 三人称視点時の平行移動ベクトル */
//        private Vector3f translationThirdPerson = new Vector3f(0.0F, 0.1F, -0.175F);
//        private float scaleTirdFloat = 0.375F;
//        /** 三人称視点時のサイズ補正 */
//        private Vector3f scaleThirdPerson = new Vector3f(scaleTirdFloat, scaleTirdFloat, scaleTirdFloat);;
//        /** 三人称視点時の回転ベクトル */
//        private Quat4f rotateThirdPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, 0, 0));
//        /** 一人称視点時の回転ベクトル */
//        private Quat4f rotateFirstPerson = TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 90, 0));
//
//        public MakoReactorBakedModel(TextureAtlasSprite frontTAS, TextureAtlasSprite sideTAS, TextureAtlasSprite blockTAS, EnumFacing face) {
//            this.frontTAS = frontTAS;
//            this.sideTAS = sideTAS;
//            this.blockTAS = blockTAS;
//            this.face = face;
//            rotateThirdPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, -45, 0)));
//            rotateThirdPerson.mul(TRSRTransformation.quatFromYXZDegrees(new Vector3f(0, 0, 170)));
//        }
//
//        @Override
//        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//            Matrix4f matrix4f = null;
//            if (ItemCameraTransforms.TransformType.THIRD_PERSON == cameraTransformType) {
//                matrix4f = TRSRTransformation.mul(translationThirdPerson, rotateThirdPerson, scaleThirdPerson, null);
//            } else if (ItemCameraTransforms.TransformType.FIRST_PERSON == cameraTransformType) {
//                matrix4f = TRSRTransformation.mul(null, rotateFirstPerson, null, null);
//            }
//            return Pair.of(this, matrix4f);
//        }
//
//        @Override
//        public List<BakedQuad> getFaceQuads(EnumFacing side) {
//            org.lwjgl.util.vector.Vector3f fromVec = new org.lwjgl.util.vector.Vector3f(0F, 0F, 0F);
//            org.lwjgl.util.vector.Vector3f toVec = new org.lwjgl.util.vector.Vector3f(16F, 16F, 16F);
//            BlockFaceUV uv = new BlockFaceUV(new float[]{0.0F, 0.0F, 16F, 16F}, 0);
//            TextureAtlasSprite sideTas = (face == side) ? frontTAS : sideTAS;
//            String rlName = (face == side) ? frontTAS.getIconName() : sideTAS.getIconName();
//            BlockPartFace partFace = new BlockPartFace(side, 0, rlName, uv);
//            BakedQuad bakedQuad = faceBakery.makeBakedQuad(fromVec, toVec, partFace, sideTas, side, ModelRotation.X0_Y0, null, true, true);
//
//            org.lwjgl.util.vector.Vector3f fromVecBase = new org.lwjgl.util.vector.Vector3f(innerStart, innerStart, innerStart);
//            org.lwjgl.util.vector.Vector3f toVecBase = new org.lwjgl.util.vector.Vector3f(innerEnd, innerEnd, innerEnd);
//            BlockFaceUV uvBase = new BlockFaceUV(new float[]{0.0F, 0.0F, 16F, 16F}, 0);
//            BlockPartFace partFaceBase = new BlockPartFace(side, 0, blockTAS.getIconName(), uvBase);
//            BakedQuad bakedQuadBase = faceBakery.makeBakedQuad(fromVecBase, toVecBase, partFaceBase, blockTAS, side, ModelRotation.X0_Y0, null, true, true);
//
//            return Lists.newArrayList(bakedQuad, bakedQuadBase);
//        }
//
//        @Override
//        public boolean isAmbientOcclusion() {
//            return true;
//        }
//
//        @Override
//        public boolean isGui3d() {
//            return true;
//        }
//
//        @Override
//        public boolean isBuiltInRenderer() {
//            return false;
//        }
//
//        @Override
//        @Nonnull
//        public TextureAtlasSprite getParticleTexture() {
//            return blockTAS;
//        }
//
//        @Override
//        @Nonnull
//        public ItemCameraTransforms getItemCameraTransforms() {
//            return ItemCameraTransforms.DEFAULT;
//        }
//    }

}
