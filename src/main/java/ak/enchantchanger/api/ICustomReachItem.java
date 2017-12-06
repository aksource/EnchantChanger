package ak.enchantchanger.api;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * 拡張リーチアイテムのインターフェース
 * Created by A.K. on 14/09/09.
 */
public interface ICustomReachItem {
    /**
     * @param itemStack ItemStack
     * @return 攻撃到達距離
     */
    double getReach(@Nonnull ItemStack itemStack);
}
