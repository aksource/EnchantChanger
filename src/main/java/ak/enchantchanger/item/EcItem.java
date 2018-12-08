package ak.enchantchanger.item;

import ak.enchantchanger.api.Constants;
import net.minecraft.item.Item;

/**
 * 無機能アイテム用クラス
 * Created by A.K. on 14/06/27.
 */
public class EcItem extends Item {
    public EcItem(String name) {
        super();
        String s = String.format("%s%s", Constants.EcTextureDomain, name);
        this.setTranslationKey(s);
        this.setCreativeTab(Constants.TAB_ENCHANT_CHANGER);
    }
}
