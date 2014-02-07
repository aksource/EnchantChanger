package ak.ChainDestruction;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
//クライアント側でキー入力によって変化したboolean変数をサーバー側に伝達するパケット。AbstractPacketを継承
public class KeyHandlingPacket extends AbstractPacket
{
	//保持しておくboolean型変数
	boolean toggle;
	boolean digUnderToggle;
	//引数を持つコンストラクタを追加する場合は、空のコンストラクタを用意してくれとのこと。
	public KeyHandlingPacket() {
	}
	//パケット生成を簡略化するために、boolean型変数を引数に取るコンストラクタを追加。
	public KeyHandlingPacket(boolean ver1, boolean ver2) {
		toggle = ver1;
		digUnderToggle = ver2;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//ByteBufに変数を代入。基本的にsetメソッドではなく、writeメソッドを使う。
		buffer.writeBoolean(toggle);
		buffer.writeBoolean(digUnderToggle);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//ByteBufから変数を取得。こちらもgetメソッドではなく、readメソッドを使う。
		toggle = buffer.readBoolean();
		digUnderToggle = buffer.readBoolean();

	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//今回はクライアントの情報をサーバーに送るので、こちらはなにもしない。
		//NO OP

	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		//代入したいクラスの変数に代入。Worldインスタンスはplayerから取得できる。
		ChainDestruction.interactblockhook.toggle = toggle;
		ChainDestruction.interactblockhook.digUnderToggle = digUnderToggle;

	}

}