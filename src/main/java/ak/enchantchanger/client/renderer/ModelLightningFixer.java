package ak.enchantchanger.client.renderer;

import ak.enchantchanger.item.EcItemMasterMateria;
import ak.enchantchanger.item.EcItemMateria;
import ak.enchantchanger.item.EcItemSword;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * 3Dモデルの光源処理修正クラス
 * Created by A.K. on 2015/11/09.
 */
public class ModelLightningFixer {

//    @SubscribeEvent
    @SuppressWarnings("unused")
    public void lightningDisableProcess(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && isAdditionalModelItem(heldItem)) {
            GlStateManager.disableLighting();
        }
    }

//    @SubscribeEvent
    @SuppressWarnings("unused")
    public void lightningEnableProcess(RenderPlayerEvent.Post event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && isAdditionalModelItem(heldItem)) {
            GlStateManager.enableLighting();
        }
    }

    private boolean isAdditionalModelItem(ItemStack itemStack) {
        Item item = itemStack.getItem();
        boolean check;
        check = item instanceof EcItemMateria;
        check |= item instanceof EcItemMasterMateria;
        check |= item instanceof EcItemSword;
        return check;
    }
}
