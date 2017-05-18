package ak.enchantchanger.api;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 手持ち等で描画を変えるアイテム用インターフェース
 * Created by A.K. on 2017/05/01.
 */
public interface ICustomModelItem {

    /**
     * アイテムの状態別にモデルを返すメソッド。
     * 必要に応じてオーバーライドする。
     * @param itemStack モデルを取得したいアイテム
     * @param modelList アイテムに登録されたモデルのリスト
     * @return 描画させたいモデル
     */
    @SideOnly(Side.CLIENT)
    @Nonnull
    default IPerspectiveAwareModel getPresentModel(@Nonnull ItemStack itemStack, @Nonnull List<IPerspectiveAwareModel> modelList) {
        return modelList.get(0);
    }
}
