package StoneCutter;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class ItemStoneCutter extends Item
{
    private boolean repair = false;
    public ItemStoneCutter()
    {
        super();
        this.setMaxStackSize(1);
        this.setMaxDamage(16); //効果がわかりやすいように数値を低く設定
    }

    //アイテムがクラフト後に戻らないようにする
    @Override
    public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack)
    {
        return false;
    }

    //修理以外ならクラフト後にgetContainerItemStackを呼び出す
    @Override
    public boolean hasContainerItem()
    {
        return !repair;
    }
	//修理かどうかを判定する
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event)
	{
		//IDが無くなったので、アイテムインスタンスで比較。
		repair = StoneCutter.cutter == event.crafting.getItem();
	}
    //クラフト後のアイテムを、ダメージを与えて返す
    @Override
    public ItemStack getContainerItem(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() == this)
        {
            itemStack.setItemDamage(itemStack.getItemDamage() + 1);
        }
        return itemStack;
    }

    //既存のハサミと見分けるため、テクスチャを赤で乗算
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
        return 0xFF0000;
    }
}
