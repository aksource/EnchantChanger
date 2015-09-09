package ak.EnchantChanger.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.GameData;

import static ak.EnchantChanger.api.Constants.MOD_ID;
import static ak.EnchantChanger.api.Constants.OBJ_ITEM_DOMAIN;

/**
 * 文字列を扱うユーティリティクラス
 * Created by A.K. on 2015/08/11.
 */
public class StringUtils {

    /**
     * 部分的なパス文字列を補完するメソッド
     * @param path 部分パス文字列
     * @return 補完されたパス文字列
     */
    public static String makeObjTexturePath(String path) {
        return MOD_ID.toLowerCase() + OBJ_ITEM_DOMAIN + path;
    }

    /**
     * ドメイン付きブロック固有名からBlockを取得するメソッド
     * Blockが取得できなかった場合は、Airブロックを返す。
     * @param stringBlockName ドメイン付きブロック固有名
     * @return 固有名から取得したBlock
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
