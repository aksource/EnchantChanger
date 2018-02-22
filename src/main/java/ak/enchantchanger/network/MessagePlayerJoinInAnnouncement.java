package ak.enchantchanger.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * プレイヤーの同期処理Messageクラス
 * Created by A.K. on 15/03/12.
 */
@Deprecated
public class MessagePlayerJoinInAnnouncement implements IMessage {
    private String uuidString;

    public MessagePlayerJoinInAnnouncement() {
    }

    public MessagePlayerJoinInAnnouncement(EntityPlayer player) {
        this.uuidString = player.getGameProfile().getId().toString();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.uuidString = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.uuidString);
    }

    public String getUuidString() {
        return uuidString;
    }
}
