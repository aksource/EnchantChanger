package ak.enchantchanger.client.models;

import ak.enchantchanger.api.Constants;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.io.IOException;

/**
 * 魔晄炉ブロック用カスタムモデルローダークラス
 * Created by A.K. on 2015/08/22.
 */
@Deprecated
public class MakoReactorModelLoader implements ICustomModelLoader {
    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        boolean ret;
        ret = Constants.MOD_ID.toLowerCase().equals(modelLocation.getResourceDomain());
        ret &= "models/block/blockmakoreactor".equals(modelLocation.getResourcePath())
        || "models/item/blockmakoreactor".equals(modelLocation.getResourcePath());
        return ret;
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException {
        return null/*new MakoReactorModel()*/;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }
}
