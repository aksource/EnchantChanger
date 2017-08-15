package ak.enchantchanger.api;

import ak.enchantchanger.utils.Blocks;
import ak.enchantchanger.utils.Items;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by A.K. on 14/10/12.
 */
public class MakoUtils {
    public static final Set<Pair<Predicate<ItemStack>, Function<ItemStack, Integer>>> set = Sets.newHashSet();

    public static void init() {
        Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> materiaPair = Pair.of(
                e -> e.getItem() == Items.itemMateria && e.getItemDamage() == 0,
                itemStack -> {
                    int coefficient = 5;
                    if (itemStack.isItemEnchanted()) {
                        NBTTagList enchantmentList = itemStack.getEnchantmentTagList();
                        for (int i = 0; i < enchantmentList.tagCount(); i++) {
                            if (enchantmentList.getCompoundTagAt(i).getShort("lvl") > 0) {
                                coefficient *= enchantmentList.getCompoundTagAt(i).getShort("lvl");
                                break;
                            }
                        }
                    }
                    return coefficient;
                });
        set.add(materiaPair);

        Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> magicMateriaPair = Pair.of(
                e -> e.getItem() == Items.itemMateria && e.getItemDamage() > 0,
                itemStack -> 100
        );
        set.add(magicMateriaPair);

        Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> masterMateriaPair = Pair.of(
                e -> e.getItem() == Items.itemMasterMateria,
                itemStack -> {
                    for (MasterMateriaType type : MasterMateriaType.values()) {
                        if (itemStack.getItemDamage() == type.getMeta()) {
                            return type.getMakoAmount();
                        }
                    }
                    return 0;
                }
        );
        set.add(masterMateriaPair);

        Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> lifeStreamPair = Pair.of(
                MakoUtils::isLifeStreamContainer,
                MakoUtils::getAmount
        );
        set.add(lifeStreamPair);

        Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> chalcedonyPair = Pair.of(
                e -> {
                    int blockChalcedonyId = OreDictionary.getOreID("blockChalcedony");
                    return Sets.newHashSet(OreDictionary.getOreIDs(e)).contains(blockChalcedonyId);
                },
                itemStack -> 1
        );
    }

    public static boolean isMako(ItemStack itemStack) {
        for (Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> pair : set) {
            if (pair.getLeft().test(itemStack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLifeStreamContainer(ItemStack itemStack) {
        IFluidHandler fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        if (fluidHandler != null) {
            IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
            for (IFluidTankProperties properties : tankProperties) {
                FluidStack fs = properties.getContents();
                if (fs == null || fs.getFluid() != Blocks.fluidLifeStream) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static int getAmount(ItemStack itemStack) {
        int total = 0;
        IFluidHandler fluidHandler = itemStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
        if (fluidHandler != null) {
            IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
            for (IFluidTankProperties properties : tankProperties) {
                FluidStack fs = properties.getContents();
                if (fs != null && fs.getFluid() == Blocks.fluidLifeStream) {
                    total += fs.amount;
                }
            }
        }
        return total;
    }

    public static int getMakoFromItem(ItemStack itemStack) {
        for (Pair<Predicate<ItemStack>, Function<ItemStack, Integer>> pair : set) {
            if (pair.getLeft().test(itemStack)) {
                return pair.getRight().apply(itemStack);
            }
        }
        return 0;
    }
}
