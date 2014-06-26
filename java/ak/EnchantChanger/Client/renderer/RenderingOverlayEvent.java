package ak.EnchantChanger.Client.renderer;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.inventory.EcInventoryCloudSword;
import ak.EnchantChanger.item.EcItemCloudSword;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderHorse;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

/**
 * Created by A.K. on 14/03/11.
 */
@SideOnly(Side.CLIENT)
public class RenderingOverlayEvent {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final RenderItem itemRenderer = new RenderItem();

    @SubscribeEvent
    public void overlayEvent(RenderGameOverlayEvent.Text event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack holdItem = player.getCurrentEquippedItem();
        if (holdItem != null && holdItem.getItem() instanceof EcItemCloudSword) {
            renderCloudSwordInventory(holdItem, event.partialTicks);
        }
    }

    private void renderCloudSwordInventory(ItemStack holdItem, float partialTicks) {
        EcInventoryCloudSword swordData = EcItemCloudSword.getInventoryFromItemStack(holdItem);
        int slot = EcItemCloudSword.getSlotNumFromItemStack(holdItem);

        for (int i = 0; i < 5; i++) {
            int xShift = (i == slot) ? 16 : 0;
            renderInventorySlot(swordData.getStackInSlot(i), EnchantChanger.cloudInvXCoord + xShift, EnchantChanger.cloudInvYCoord + i * 16);
        }
    }

    private void renderInventorySlot(ItemStack itemstack, int par2, int par3)
    {
        if (itemstack != null){
            RenderHelper.enableGUIStandardItemLighting();
            itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, par2, par3);
            itemRenderer.renderItemOverlayIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), itemstack, par2, par3);
            RenderHelper.disableStandardItemLighting();
            String s = itemstack.getDisplayName();
            this.mc.fontRenderer.drawStringWithShadow(s, par2 + 16, par3 + 4, 0xFFFFFF);
        }
    }
}
