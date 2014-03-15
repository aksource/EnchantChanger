package ak.EnchantChanger.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.io.File;
import java.util.Map;

/**
 * Created by A.K. on 14/03/13.
 */
@IFMLLoadingPlugin.MCVersion("1.7.2")
public class ExtendArrayCorePlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"ak.EnchantChanger.asm.PotionArrayTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "ak.EnchantChanger.asm.ExtendArrayModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
