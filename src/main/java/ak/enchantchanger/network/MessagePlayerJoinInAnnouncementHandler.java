package ak.enchantchanger.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * プレイヤーの同期処理Handlerクラス
 * Created by A.K. on 15/03/12.
 */
public class MessagePlayerJoinInAnnouncementHandler implements IMessageHandler<MessagePlayerJoinInAnnouncement, MessagePlayerProperties> {
    @Override
    public MessagePlayerProperties onMessage(MessagePlayerJoinInAnnouncement message, MessageContext ctx) {
        //UUIDの文字列を受け取る
        String uuidString = message.getUuidString();
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        //取得したPlayerが同一UUIDを持つか判定
        if (player.getGameProfile().getId().toString().equals(uuidString)) {
            //クライアント側にデータを送る
            return new MessagePlayerProperties(player);
        }
        //UUIDが違っていた場合、同期処理を呼ばない
        return null;
    }
}
