package ak.enchantchanger;

import ak.enchantchanger.utils.Items;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CreativeTabEC extends CreativeTabs {
    public CreativeTabEC(String var1) {
        super(var1);
    }

    @Override
    @Nonnull
    public String getTranslationKey() {
        return "E.Changer";
    }

    @Override
    @Nonnull
    public ItemStack createIcon() {
        return new ItemStack(Items.itemZackSword);
    }
}