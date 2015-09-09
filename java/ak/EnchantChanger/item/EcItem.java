package ak.EnchantChanger.item;

import ak.EnchantChanger.api.Constants;
import net.minecraft.item.Item;

/**
 * ���@�\�A�C�e���p�N���X
 * Created by A.K. on 14/06/27.
 */
public class EcItem extends Item {
    public EcItem(String name) {
        super();
        String s = String.format("%s%s", Constants.EcTextureDomain, name);
        this.setUnlocalizedName(s);
        this.setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
    }
}
