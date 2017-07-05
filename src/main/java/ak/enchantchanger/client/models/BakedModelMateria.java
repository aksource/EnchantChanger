package ak.enchantchanger.client.models;

import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.EnchantmentUtils;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ak.enchantchanger.client.ClientModelUtils.resourceLocationsMateria;

/**
 * マテリアのB3Dモデル描画用クラス
 * Created by A.K. on 2015/07/26.
 * @since 2015/07/26
 */
public class BakedModelMateria implements IBakedModel{

    public static Map<Integer, ResourceLocation> magicMateriaMap = new ConcurrentHashMap<>();
    public static Map<ResourceLocation, ResourceLocation> materiaMap = new ConcurrentHashMap<>();
    private static Map<ResourceLocation, IBakedModel> reTexturedModelMap = new ConcurrentHashMap<>();

    private final ItemOverrideList itemOverrideList;

    public BakedModelMateria(IRetexturableModel model) {
        itemOverrideList = new ItemOverrideListMateria(model);
    }

    public static void registerExtraMateria(ResourceLocation registerName, int texId) {
        if (texId > 0 && texId < 16 && texId != 2)
            materiaMap.put(registerName, resourceLocationsMateria[texId]);
    }

    private static ImmutableMap<String, String> getTextureMap(ItemStack itemStack) {
        return new ImmutableMap.Builder<String, String>().put("#materia10", getTextureFromItemStack(itemStack).toString()).build();
    }

    private static ResourceLocation getTextureFromItemStack(ItemStack item) {
        if (item.getItem() instanceof EcItemMateria)
            return getTextureFromEnchantedItem(item);
        else
            return resourceLocationsMateria[10];
    }

    private static ResourceLocation getTextureFromEnchantedItem(ItemStack item) {
        if (item.getItemDamage() > 0) {
            if (magicMateriaMap.containsKey(item.getItemDamage() - 1)) {
                return magicMateriaMap.get(item.getItemDamage() - 1);
            } else {
                return resourceLocationsMateria[10];
            }
        } else if (!item.isItemEnchanted())
            return resourceLocationsMateria[0];
        else if (materiaMap.containsKey(EnchantmentUtils.getEnchantmentRegisterName(item))) {
            return materiaMap.get(EnchantmentUtils.getEnchantmentRegisterName(item));
        } else
            return resourceLocationsMateria[10];
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false/*this.objModel.isAmbientOcclusion()*/;
    }

    @Override
    public boolean isGui3d() {
        return false/*this.objModel.isGui3d()*/;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false/*this.objModel.isBuiltInRenderer()*/;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null/*this.objModel.getTexture()*/;
    }

    @Override
    @Nonnull
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
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

    private static class ItemOverrideListMateria extends ItemOverrideList {
        private final IRetexturableModel objModel;
        public ItemOverrideListMateria(IRetexturableModel objModel) {
            super(Lists.newArrayList());
            this.objModel = objModel;
        }

        @Override
        @Nonnull
        public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, @Nonnull ItemStack stack,
                                           @Nullable World world, @Nullable EntityLivingBase entity) {
            ResourceLocation resourceLocation = getTextureFromItemStack(stack);
            if (!reTexturedModelMap.containsKey(resourceLocation)) {
                IModel reTextureModel = this.objModel.retexture(getTextureMap(stack));
                IBakedModel bakedModel = reTextureModel.bake(objModel.getDefaultState(), Attributes.DEFAULT_BAKED_FORMAT, ModelLoader.defaultTextureGetter());
                reTexturedModelMap.put(resourceLocation, new ReTexturedModel(bakedModel));
            }
            return reTexturedModelMap.get(resourceLocation);
        }
    }

    private static class ReTexturedModel implements IPerspectiveAwareModel {
        IBakedModel b3dModel;
        private float handheldSize = 0.3F;
        private float guiSize = 0.48F;
//        private Vector3f vectorTransGui = new Vector3f(1.0F, 0.95F, 0.0F);
        private Vector3f vectorTransGui = new Vector3f(0.28F, 0.28F, 0.0F);
        private Vector3f vectorTransHand = new Vector3f(0.1F, 0.15F, -0.01F);
        public ReTexturedModel(IBakedModel bakedModel) {
            this.b3dModel = bakedModel;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
            /* TRSRTransformationのmulメソッドで作ってるが、Matrix4fクラスに単独の設定メソッドが存在する
             * TRSRの意味
              * T=Transformation 並行移動　第1引数
              * R=Rotate 回転（前）　第2引数
              * S=Scale リサイズ　第3引数
              * R=Rotate 回転（後）　第4引数
              * 設定したい引数にVector3fクラス,Quat4fクラスのインスタンスを指定。
              * 全部nullだとおそらくIdentity Matrix（単位行列）が返る*/
            Matrix4f matrix4fGui = TRSRTransformation.mul(vectorTransGui, null, new Vector3f(guiSize, guiSize, guiSize), null);
            Matrix4f matrix4fHandHeld = TRSRTransformation.mul(vectorTransHand, null, new Vector3f(handheldSize, handheldSize, handheldSize), null);
//            GlStateManager.disableLighting();
            switch (cameraTransformType) {
                case GUI:
                    return Pair.of(this.b3dModel, matrix4fGui);
                case FIRST_PERSON_RIGHT_HAND:
                    break;
                case THIRD_PERSON_RIGHT_HAND:
                    break;
            }
            return Pair.of(this.b3dModel, matrix4fHandHeld);
        }

        @Override
        @Nonnull
        public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
            return this.b3dModel.getQuads(state, side, rand);
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
        @Nonnull
        public TextureAtlasSprite getParticleTexture() {
            return this.b3dModel.getParticleTexture();
        }

        @Override
        @Nonnull
        public ItemCameraTransforms getItemCameraTransforms() {
            return this.b3dModel.getItemCameraTransforms();
        }

        @Override
        @Nonnull
        public ItemOverrideList getOverrides() {
            return this.b3dModel.getOverrides();
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
        materiaMap.put(new ResourceLocation("protection"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("fire_protection"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("feather_falling"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("blast_protection"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("projectile_protection"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("respiration"), resourceLocationsMateria[4]);
        materiaMap.put(new ResourceLocation("aqua_affinity"), resourceLocationsMateria[4]);
        materiaMap.put(new ResourceLocation("thorns"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("depth_strider"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("frost_walker"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("binding_curse"), resourceLocationsMateria[8]);
        materiaMap.put(new ResourceLocation("sharpness"), resourceLocationsMateria[13]);
        materiaMap.put(new ResourceLocation("smite"), resourceLocationsMateria[13]);
        materiaMap.put(new ResourceLocation("bane_of_arthropods"), resourceLocationsMateria[13]);
        materiaMap.put(new ResourceLocation("knockback"), resourceLocationsMateria[14]);
        materiaMap.put(new ResourceLocation("fire_aspect"), resourceLocationsMateria[1]);
        materiaMap.put(new ResourceLocation("looting"), resourceLocationsMateria[9]);
        materiaMap.put(new ResourceLocation("sweeping"), resourceLocationsMateria[14]);
        materiaMap.put(new ResourceLocation("efficiency"), resourceLocationsMateria[5]);
        materiaMap.put(new ResourceLocation("silk_touch"), resourceLocationsMateria[7]);
        materiaMap.put(new ResourceLocation("unbreaking"), resourceLocationsMateria[3]);
        materiaMap.put(new ResourceLocation("fortune"), resourceLocationsMateria[9]);
        materiaMap.put(new ResourceLocation("power"), resourceLocationsMateria[13]);
        materiaMap.put(new ResourceLocation("punch"), resourceLocationsMateria[14]);
        materiaMap.put(new ResourceLocation("flame"), resourceLocationsMateria[1]);
        materiaMap.put(new ResourceLocation("infinity"), resourceLocationsMateria[12]);
        materiaMap.put(new ResourceLocation("luck_of_the_sea"), resourceLocationsMateria[4]);
        materiaMap.put(new ResourceLocation("lure"), resourceLocationsMateria[4]);
        materiaMap.put(new ResourceLocation("mending"), resourceLocationsMateria[10]);
        materiaMap.put(new ResourceLocation("vanishing_curse"), resourceLocationsMateria[10]);
    }
}
