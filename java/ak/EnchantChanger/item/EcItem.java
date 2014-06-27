package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import net.minecraft.item.Item;

/**
 * Created by A.K. on 14/06/27.
 */
public class EcItem extends Item {
    public EcItem(String name) {
        super();
        String s = String.format("%s%s", EnchantChanger.EcTextureDomain, name);
        this.setUnlocalizedName(s);
        this.setTextureName(s);
        this.setCreativeTab(EnchantChanger.tabsEChanger);
    }
}
