package ak.enchantchanger.recipe;

import ak.enchantchanger.api.Constants;
import ak.enchantchanger.api.MasterMateriaType;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.utils.Items;
import com.google.common.collect.Sets;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EcRecipeMasterMateria implements IRecipe {
    private static final Map<MasterMateriaType, Set<Enchantment>> RECIPE_MAP = new HashMap<>();

    static {
        RECIPE_MAP.put(MasterMateriaType.PROTECTION,
                Sets.newHashSet(
                        Enchantments.PROTECTION,
                        Enchantments.FIRE_PROTECTION,
                        Enchantments.FEATHER_FALLING,
                        Enchantments.BLAST_PROTECTION,
                        Enchantments.PROJECTILE_PROTECTION,
                        Enchantments.THORNS));
        RECIPE_MAP.put(MasterMateriaType.WATER,
                Sets.newHashSet(
                        Enchantments.RESPIRATION,
                        Enchantments.AQUA_AFFINITY,
                        Enchantments.DEPTH_STRIDER,
                        Enchantments.FROST_WALKER,
                        Enchantments.LUCK_OF_THE_SEA,
                        Enchantments.LURE
                ));
        RECIPE_MAP.put(MasterMateriaType.ATTACK,
                Sets.newHashSet(
                        Enchantments.SHARPNESS,
                        Enchantments.SMITE,
                        Enchantments.BANE_OF_ARTHROPODS,
                        Enchantments.KNOCKBACK,
                        Enchantments.FIRE_ASPECT,
                        Enchantments.LOOTING
                ));
        RECIPE_MAP.put(MasterMateriaType.DIGGING,
                Sets.newHashSet(
                        Enchantments.EFFICIENCY,
                        Enchantments.SILK_TOUCH,
                        Enchantments.UNBREAKING,
                        Enchantments.FORTUNE
                ));
        RECIPE_MAP.put(MasterMateriaType.BOW,
                Sets.newHashSet(
                        Enchantments.POWER,
                        Enchantments.PUNCH,
                        Enchantments.FLAME,
                        Enchantments.INFINITY
                ));
    }

    private ItemStack output = null;

    @Override
    public boolean matches(@Nonnull InventoryCrafting inv, @Nonnull World worldIn) {
        Set<Enchantment> enchantmentSet = Sets.newHashSet();
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack craftItem = inv.getStackInSlot(i);
            if (craftItem == null) continue;
            if (craftItem.getItem() instanceof EcItemMateria && craftItem.isItemEnchanted()) {
                NBTTagCompound nbtTagCompound = (NBTTagCompound) craftItem.getEnchantmentTagList().get(0);
                Enchantment enchantment = Enchantment.getEnchantmentByID(nbtTagCompound.getShort(Constants.NBT_KEY_ENCHANT_ID));
                int level = nbtTagCompound.getShort(Constants.NBT_KEY_ENCHANT_LEVEL);
                if (enchantment == null || enchantmentSet.contains(enchantment) || level < enchantment.getMaxLevel()) {
                    return false;
                }
                enchantmentSet.add(enchantment);
            }
        }
        return searchMateria(enchantmentSet);
    }

    @Override
    public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
        return this.output.copy();
    }

    @Override
    public int getRecipeSize() {
        return 3;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    private boolean searchMateria(Set<Enchantment> enchantments) {
        for (MasterMateriaType type : MasterMateriaType.values()) {
            Set<Enchantment> set = RECIPE_MAP.get(type);
            if (set != null && set.equals(enchantments)) {
                this.output = new ItemStack(Items.itemMasterMateria, 1, type.getMeta());
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public ItemStack[] getRemainingItems(@Nonnull InventoryCrafting inventoryCrafting) {
        ItemStack[] nonNullList = new ItemStack[inventoryCrafting.getSizeInventory()];

        for (int i = 0; i < nonNullList.length; ++i) {
            ItemStack itemstack = inventoryCrafting.getStackInSlot(i);
            nonNullList[i] = net.minecraftforge.common.ForgeHooks.getContainerItem(itemstack);
        }

        return nonNullList;
    }
}