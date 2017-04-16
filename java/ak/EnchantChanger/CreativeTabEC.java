package ak.EnchantChanger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class CreativeTabEC extends CreativeTabs {
    public CreativeTabEC(String var1) {
        super(var1);
    }

    public String getTranslatedTabLabel() {
        return "E.Changer";
    }

    @Override
    public Item getTabIconItem() {
        return EnchantChanger.itemZackSword;
    }
}