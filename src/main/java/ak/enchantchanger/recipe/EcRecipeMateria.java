package ak.enchantchanger.recipe;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.EnchantmentUtils;
import ak.enchantchanger.utils.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EcRecipeMateria implements IRecipe {
    private ItemStack output = null;

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        ItemStack materia1 = ItemStack.EMPTY;
        ItemStack materia2 = ItemStack.EMPTY;
        ItemStack expBottle = ItemStack.EMPTY;
        ItemStack exExpBottle = ItemStack.EMPTY;
        boolean flag = false;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack craftItem = inv.getStackInSlot(i);
            if (craftItem.isEmpty())
                continue;
            if (craftItem.getItem() instanceof EcItemMateria) {
                if (materia1.isEmpty())
                    materia1 = craftItem;
                else if (materia2.isEmpty())
                    materia2 = craftItem;
                else
                    return false;
            }
/*			else if(craftItem.getItem() == Items.experience_bottle && expBottle == null && exExpBottle == null && materia2 == null)
                expBottle = craftItem;
			else if(craftItem.getItem() == enchantchanger.itemExExpBottle && expBottle == null && exExpBottle == null && materia2 == null)
				exExpBottle = craftItem;*/
            else
                return false;
        }
        if (materia2 != null && materia1.isItemEnchanted() && materia2.isItemEnchanted()) {
            if (ItemStack.areItemStackTagsEqual(materia1, materia2)) {
                this.output = materia1.copy();
                this.output.setCount(1);
                output.getTagCompound().removeTag(Constants.NBT_ENCHANTMENT);
                EnchantmentUtils.addEnchantmentToItem(output, EnchantmentUtils.getEnchantmentFromItemStack(materia1), EnchantmentUtils.getEnchantmentLv(materia1) + 1);
                flag = true;
            }
        } else if (materia1 != null) {
            if (materia1.getItemDamage() == 0) {
                if (!expBottle.isEmpty() && EnchantmentUtils.getEnchantmentLv(materia1) < 5 && materia1.isItemEnchanted()) {
                    this.output = materia1.copy();
                    this.output.setCount(1);
                    output.getTagCompound().removeTag(Constants.NBT_ENCHANTMENT);
                    EnchantmentUtils.addEnchantmentToItem(output, EnchantmentUtils.getEnchantmentFromItemStack(materia1), EnchantmentUtils.getEnchantmentLv(materia1) + 1);
                    flag = true;
                } else if (exExpBottle != null && EnchantmentUtils.getEnchantmentLv(materia1) >= 5 && materia1.isItemEnchanted()) {
                    this.output = materia1.copy();
                    this.output.setCount(1);
                    output.getTagCompound().removeTag(Constants.NBT_ENCHANTMENT);
                    EnchantmentUtils.addEnchantmentToItem(output, EnchantmentUtils.getEnchantmentFromItemStack(materia1), EnchantmentUtils.getEnchantmentLv(materia1) + 1);
                    flag = true;
                } else if (!expBottle.isEmpty() && !exExpBottle.isEmpty()) {
                    this.output = new ItemStack(Items.itemMateria, 1, 0);
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    @Nonnull
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        return this.output.copy();
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    @Nonnull
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    @Nonnull
    public NonNullList<ItemStack> getRemainingItems(@Nonnull InventoryCrafting inventoryCrafting) {
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

        for (int i = 0; i < nonNullList.size(); ++i) {
            ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
            nonNullList.set(i, net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack));
        }

        return nonNullList;
    }
}