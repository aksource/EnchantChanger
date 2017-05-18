package ak.enchantchanger.utils;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import static ak.enchantchanger.api.Constants.MOD_ID;
import static ak.enchantchanger.api.Constants.OBJ_ITEM_DOMAIN;

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

    public static String makeObjMaterialKeyName(String materilaName) {
        return "#" + materilaName;
    }

    /**
     * ドメイン付きブロック固有名からBlockを取得するメソッド
     * Blockが取得できなかった場合は、Airブロックを返す。
     * @param stringBlockName ドメイン付きブロック固有名
     * @return 固有名から取得したBlock
     */
    public static Block getBlockFromString(String stringBlockName) {
        return Block.REGISTRY.getObject(new ResourceLocation(stringBlockName));
    }
}
