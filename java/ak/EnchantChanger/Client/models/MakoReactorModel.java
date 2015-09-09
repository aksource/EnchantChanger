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
 * ����F�u���b�N�pIModel�N���X
 * Created by A.K. on 2015/09/04.
 */
public class MakoReactorModel implements IModel {

    /**
     * �֘A���f����Ԃ��Bnull
     * @return �֘A���f����ResourceLocation�R���N�V����
     */
    @Override
    public Collection<ResourceLocation> getDependencies() {
        return Collections.emptyList();
    }

    /**
     * �V�K�e�N�X�`����ResourceLocation�R���N�V������Ԃ�
     * @return �V�K�e�N�X�`����ResourceLocation�R���N�V����
     */
    @Override
    public Collection<ResourceLocation> getTextures() {
        return Sets.newHashSet(Constants.MAKO_REACTOR_FRONT_RL, Constants.MAKO_REACTOR_SIDE_RL);
    }

    /**
     * IFlexibleBakedModel��Ԃ����\�b�h�B
     * @param state ���f���̏��
     * @param format ���_
     * @param bakedTextureGetter ResourceLocation����TextureAtlasSprite���o�͂���Function�N���X
     * @return �������ꂽIFlexibleBakedModel
     */
    @Override
    public IFlexibleBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return new MakoReactorWrapperBakedModel(bakedTextureGetter);
    }

    /**
     * �W���̃��f���X�e�[�g��Ԃ�
     * @return �W���̃��f���X�e�[�g
     */
    @Override
    public IModelState getDefaultState() {
        return ModelRotation.X0_Y0;
    }
}
