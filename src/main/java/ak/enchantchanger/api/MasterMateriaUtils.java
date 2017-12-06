package ak.enchantchanger.api;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * マスターマテリア関連のユーティリティクラス
 * Created by A.K. on 2015/07/25.
 */
public class MasterMateriaUtils {
    /**
     * ヒュージマテリア用素材マップ
     * Key:マスターマテリアのdamage値。Value:生成物と素材のペアクラスのセット。
     */
    private static final Map<Integer, Set<Pair<ItemStack, ItemStack>>> MATERIAL_MAP = new HashMap<>();

    /**
     * ヒュージマテリアへの登録メソッド
     *
     * @param master   マスターマテリアのdamage値。0:究極、1:防御、2:水、3:攻撃、4:採掘、5:弓、6:追加
     * @param material 素材
     * @param result   生成物。マテリアじゃなくても良い。
     */
    public static void registerHugeMateria(int master, ItemStack material, ItemStack result) {
        Set<Pair<ItemStack, ItemStack>> set;
        if (!MATERIAL_MAP.containsKey(master)) {
            set = new HashSet<>();
            set.add(Pair.of(material, result));
            MATERIAL_MAP.put(master, set);
        }
        set = MATERIAL_MAP.get(master);
        for (Pair<ItemStack, ItemStack> mrp : set) {
            if (mrp.getLeft().isItemEqual(material)) {
                return;
            }
        }
        set.add(Pair.of(material, result));
    }

    /**
     * 素材が対応するマスターマテリアに登録されてるかどうか。
     *
     * @param master   マスターマテリアのdamage値
     * @param material 素材
     * @return 登録されていたらtrue
     */
    public static boolean isMaterialValid(int master, ItemStack material) {
        if (!MATERIAL_MAP.containsKey(master)) return false;
        Set<Pair<ItemStack, ItemStack>> set = MATERIAL_MAP.get(master);
        for (Pair<ItemStack, ItemStack> mrp : set) {
            ItemStack containItemStack = mrp.getLeft();
            if (areItemsEqualsWildCard(containItemStack, material)) {
                return true;
            }
        }
        return false;
    }

    /**
     * マスターマテリア別の素材に対応している生成物を返す
     *
     * @param master   マスターマテリアのdamage値
     * @param material 素材
     * @return 生成物
     */
    public static ItemStack getResult(int master, ItemStack material) {
        Set<Pair<ItemStack, ItemStack>> set = MATERIAL_MAP.get(master);
        for (Pair<ItemStack, ItemStack> mrp : set) {
            ItemStack containItemStack = mrp.getLeft();
            if (areItemsEqualsWildCard(containItemStack, material)) {
                return mrp.getRight().copy();
            }
        }
        return new ItemStack(Blocks.AIR);
    }

    private static boolean areItemsEqualsWildCard(ItemStack master, ItemStack checkStack) {
        return (master.getItemDamage() == OreDictionary.WILDCARD_VALUE && master.getItem() == checkStack.getItem())
                || master.isItemEqual(checkStack);
    }
}
