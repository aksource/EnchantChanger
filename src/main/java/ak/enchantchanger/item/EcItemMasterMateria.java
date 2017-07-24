package ak.enchantchanger.item;

import ak.enchantchanger.api.MasterMateriaType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

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
    public void getSubItems(@Nonnull Item itemIn, @Nullable CreativeTabs tab, @Nonnull List<ItemStack> subItems) {
        for (MasterMateriaType type: MasterMateriaType.values()) {
            subItems.add(new ItemStack(this, 1, type.getMeta()));
        }
    }
}