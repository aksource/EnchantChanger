package ak.enchantchanger.item;

import ak.enchantchanger.api.MasterMateriaType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;

public class EcItemMasterMateria extends EcItem {

    public EcItemMasterMateria(String name) {
        super(name);
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(@Nonnull ItemStack itemStack) {
        int itemDmg = itemStack.getItemDamage();
        return "ItemMasterMateria." + itemDmg;
    }

    @Override
    public void getSubItems(@Nonnull Item itemIn, @Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        for (MasterMateriaType type: MasterMateriaType.values()) {
            subItems.add(new ItemStack(this, 1, type.getMeta()));
        }
    }
}