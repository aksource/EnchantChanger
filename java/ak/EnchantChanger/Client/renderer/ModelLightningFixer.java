package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.item.EcItemMasterMateria;
import ak.EnchantChanger.item.EcItemMateria;
import ak.EnchantChanger.item.EcItemSword;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * 3Dモデルの光源処理修正クラス
 * Created by Akira on 2015/11/09.
 */
public class ModelLightningFixer {

    @SubscribeEvent
    public void lightningDisableProcess(RenderPlayerEvent.Pre event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack heldItem = player.getCurrentEquippedItem();
        if (heldItem != null && isAdditionalModelItem(heldItem)) {
            GlStateManager.disableLighting();
        }
    }

    @SubscribeEvent
    public void lightningEnableProcess(RenderPlayerEvent.Post event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack heldItem = player.getCurrentEquippedItem();
        if (heldItem != null && isAdditionalModelItem(heldItem)) {
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
