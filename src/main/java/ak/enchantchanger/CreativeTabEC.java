package ak.enchantchanger;

import ak.enchantchanger.utils.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class CreativeTabEC extends CreativeTabs {
    public CreativeTabEC(String var1) {
        super(var1);
    }

    @Override
    @Nonnull
    public String getTranslatedTabLabel() {
        return "E.Changer";
    }

    @Override
    @Nonnull
    public Item getTabIconItem() {
        return Items.itemZackSword;
    }
}