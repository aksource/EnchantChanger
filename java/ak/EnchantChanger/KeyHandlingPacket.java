package ak.EnchantChanger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ak.MultiToolHolders.ItemMultiToolHolder;

//クライアント側でキー入力によって変化したboolean変数をサーバー側に伝達するパケット。AbstractPacketを継承
public class KeyHandlingPacket extends AbstractPacket
{
	//保持しておくboolean型変数
	boolean doMagic;

	//引数を持つコンストラクタを追加する場合は、空のコンストラクタを用意してくれとのこと。
	public KeyHandlingPacket() {
	}

	//パケット生成を簡略化するために、boolean型変数を引数に取るコンストラクタを追加。
	public KeyHandlingPacket(boolean ver1) {
		doMagic = ver1;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//ByteBufに変数を代入。基本的にsetメソッドではなく、writeメソッドを使う。
		buffer.writeBoolean(doMagic);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
		//ByteBufから変数を取得。こちらもgetメソッドではなく、readメソッドを使う。
		doMagic = buffer.readBoolean();
	}

	@Override
	public void handleClientSide(EntityPlayer player) {
		//今回はクライアントの情報をサーバーに送るので、こちらはなにもしない。
	}

	@Override
	public void handleServerSide(EntityPlayer player) {
		if (player.inventory.getCurrentItem() == null)
			return;
		if (player.inventory.getCurrentItem().getItem() instanceof EcItemSword)
		{
			EcItemSword sword = (EcItemSword) player.getCurrentEquippedItem().getItem();
			//代入したいクラスにメソッドを用意してboolean型変数を投げている。別にここで代入しても変わらない。ただの趣味。
			sword.readPacketToggleData(this.doMagic);
		}
		else if (EnchantChanger.loadMTH && player.inventory.getCurrentItem().getItem() instanceof ItemMultiToolHolder)
		{
			//ツールホルダーとの連携処理。
			ItemMultiToolHolder mth = (ItemMultiToolHolder) player.inventory.getCurrentItem().getItem();
			if (mth.tools.getStackInSlot(mth.SlotNum) != null
					&& mth.tools.getStackInSlot(mth.SlotNum).getItem() instanceof EcItemSword)
			{
				EcItemSword sword = (EcItemSword) mth.tools.getStackInSlot(mth.SlotNum).getItem();
				sword.readPacketToggleData(this.doMagic);
			}
		}
	}

}