package ak.EnchantChanger.api;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * �}�X�^�[�}�e���A�֘A�̃��[�e�B���e�B�N���X
 * Created by A.K. on 2015/07/25.
 */
public class MasterMateriaUtils {
    /**
     * �q���[�W�}�e���A�p�f�ރ}�b�v
     * Key:�}�X�^�[�}�e���A��damage�l�BValue:�������Ƒf�ނ̃y�A�N���X�̃Z�b�g�B
     */
    private static final Map<Integer, Set<MaterialResultPair>> MATERIAL_MAP = new HashMap<>();

    /**
     * �q���[�W�}�e���A�ւ̓o�^���\�b�h
     * @param master �}�X�^�[�}�e���A��damage�l�B0:���ɁA1:�h��A2:���A3:�U���A4:�̌@�A5:�|�A6:�ǉ�
     * @param material �f��
     * @param result �������B�}�e���A����Ȃ��Ă��ǂ��B
     */
    public static void registerHugeMateria(int master, ItemStack material, ItemStack result) {
        Set<MaterialResultPair> set;
        if (!MATERIAL_MAP.containsKey(master)) {
            set = new HashSet<>();
            set.add(MaterialResultPair.getMaterialResultPair(material, result));
            MATERIAL_MAP.put(master, set);
        }
        set = MATERIAL_MAP.get(master);
        for (MaterialResultPair mrp : set) {
            if (mrp.getMaterial().getContainItemStack().isItemEqual(material)) {
                return;
            }
        }
        set.add(MaterialResultPair.getMaterialResultPair(material, result));
    }

    /**
     * �f�ނ��Ή�����}�X�^�[�}�e���A�ɓo�^����Ă邩�ǂ����B
     * @param master �}�X�^�[�}�e���A��damage�l
     * @param material �f��
     * @return �o�^����Ă�����true
     */
    public static boolean isMaterialValid(int master, ItemStack material) {
        if (!MATERIAL_MAP.containsKey(master)) return false;
        Set<MaterialResultPair> set = MATERIAL_MAP.get(master);
        for (MaterialResultPair mrp : set) {
            ItemStack containItemStack = mrp.getMaterial().getContainItemStack();
            if (areItemsEqualsWildCard(containItemStack, material)) {
                return true;
            }
        }
        return false;
    }

    /**
     * �}�X�^�[�}�e���A�ʂ̑f�ނɑΉ����Ă��鐶������Ԃ�
     * @param master �}�X�^�[�}�e���A��damage�l
     * @param material �f��
     * @return ������
     */
    public static ItemStack getResult(int master, ItemStack material) {
        Set<MaterialResultPair> set = MATERIAL_MAP.get(master);
        for (MaterialResultPair mrp : set) {
            ItemStack containItemStack = mrp.getMaterial().getContainItemStack();
            if (areItemsEqualsWildCard(containItemStack, material)) {
                return mrp.getResultCopy();
            }
        }
        return new ItemStack(Blocks.air);
    }

    private static boolean areItemsEqualsWildCard(ItemStack master, ItemStack checkStack) {
        return (master.getItemDamage() == OreDictionary.WILDCARD_VALUE && master.getItem() == checkStack.getItem())
                || master.isItemEqual(checkStack);
    }
}
