package ak.EnchantChanger.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by A.K. on 14/03/13.
 */
@IFMLLoadingPlugin.MCVersion("1.7.2")
public class AKInternalCorePlugin implements IFMLLoadingPlugin {
    public static Logger logger = Logger.getLogger("AKInternalCore");
    public static int maxDamageModifier;
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"ak.EnchantChanger.asm.PotionArrayTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "ak.EnchantChanger.asm.AKInternalCoreModContainer";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if (data.containsKey("mcLocation"))
        {
            File mcLocation = (File) data.get("mcLocation");
            File configLocation = new File(mcLocation, "config");
            File configFile = new File(configLocation, "FarmOptimizeCore.cfg");
            initConfig(configFile);
        }
    }

    private void initConfig(File configFile) {
        Configuration config = new Configuration(configFile);
        config.load();
        maxDamageModifier = config.get(Configuration.CATEGORY_GENERAL, "maxDamageModifier", 25, "set damagemodifier max value. default:25").getInt();
        config.save();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
