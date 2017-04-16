package ak.EnchantChanger.Client.models;

import ak.EnchantChanger.api.Constants;
import com.google.common.base.Function;
import com.google.common.collect.Sets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelState;

import java.util.Collection;
import java.util.Collections;

/**
 * 魔晄炉ブロック用IModelクラス
 * Created by A.K. on 2015/09/04.
 */
public class MakoReactorModel implements IModel {

    /**
     * 関連モデルを返す。null
     * @return 関連モデルのResourceLocationコレクション
     */
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    /**
     * 新規テクスチャのResourceLocationコレクションを返す
     * @return 新規テクスチャのResourceLocationコレクション
     */
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Sets.newHashSet(Constants.MAKO_REACTOR_FRONT_RL, Constants.MAKO_REACTOR_SIDE_RL);
    }

    /**
     * IFlexibleBakedModelを返すメソッド。
     * @param state モデルの状態
     * @param format 頂点
     * @param bakedTextureGetter ResourceLocationからTextureAtlasSpriteを出力するFunctionクラス
     * @return 生成されたIFlexibleBakedModel
     */
    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new MakoReactorWrapperBakedModel(bakedTextureGetter);
    }

    /**
     * 標準のモデルステートを返す
     * @return 標準のモデルステート
     */
    @Override
    public IModelState getDefaultState() {
        return ModelRotation.X0_Y0;
    }
}
