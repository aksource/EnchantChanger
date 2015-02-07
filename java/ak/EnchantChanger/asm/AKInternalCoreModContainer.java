package ak.EnchantChanger.asm;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

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
        meta.version = "1.0.1";
        meta.authorList = Arrays.asList("A.K.");
        meta.description = "Change some constant number in vanilla";
        meta.url = "http://forum.minecraftuser.jp/viewtopic.php?f=13&t=6672";
        meta.credits = "Special thanks to chicken_bones";
        this.setEnabledState(true);
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}
