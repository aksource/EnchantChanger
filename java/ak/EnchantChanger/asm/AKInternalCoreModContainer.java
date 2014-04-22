package ak.EnchantChanger.asm;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Arrays;

/**
 * Created by A.K. on 14/03/13.
 */
public class AKInternalCoreModContainer extends DummyModContainer {
    public AKInternalCoreModContainer() {
        super(new ModMetadata());

        ModMetadata meta = getMetadata();
        meta.modId = "AKInternalCore";
        meta.name = "AKInternalCore";
        meta.version = "1.0.0";
        meta.authorList = Arrays.asList("A.K.");
        meta.description = "Change some constant number in vanilla";
        meta.url = "";
        meta.credits = "";
        this.setEnabledState(true);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
