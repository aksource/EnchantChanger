package ak.enchantchanger.api;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
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
    default IBakedModel getPresentModel(@Nonnull ItemStack itemStack, @Nonnull List<IBakedModel> modelList) {
        return modelList.get(0);
    }
}
