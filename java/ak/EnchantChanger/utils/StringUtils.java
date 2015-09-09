package ak.EnchantChanger.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameData;

import static ak.EnchantChanger.api.Constants.MOD_ID;
import static ak.EnchantChanger.api.Constants.OBJ_ITEM_DOMAIN;

/**
 * ��������������[�e�B���e�B�N���X
 * Created by A.K. on 2015/08/11.
 */
public class StringUtils {

    /**
     * �����I�ȃp�X�������⊮���郁�\�b�h
     * @param path �����p�X������
     * @return �⊮���ꂽ�p�X������
     */
    public static String makeObjTexturePath(String path) {
        return MOD_ID.toLowerCase() + OBJ_ITEM_DOMAIN + path;
    }

    /**
     * �h���C���t���u���b�N�ŗL������Block���擾���郁�\�b�h
     * Block���擾�ł��Ȃ������ꍇ�́AAir�u���b�N��Ԃ��B
     * @param stringBlockName �h���C���t���u���b�N�ŗL��
     * @return �ŗL������擾����Block
     */
    public static Block getBlockFromString(String stringBlockName) {
        Block block = GameData.getBlockRegistry().getObject(stringBlockName);
//        String[] strings = stringBlockName.split(":");
//        if (strings.length > 1) {
//            block = GameRegistry.findBlock(strings[0], strings[1]);
//        } else {
//            block = GameRegistry.findBlock("minecraft", strings[0]);
//        }
        if (block == null) {
            block = Blocks.air;
        }
        return block;
    }
}
