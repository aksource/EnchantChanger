package ak.enchantchanger.client.renderer;

import ak.enchantchanger.api.ICustomModelItem;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;

public class RenderingObjModelEvent {
    private Minecraft mc = Minecraft.getMinecraft();
//    @SubscribeEvent
    public void renderWorlLast(RenderWorldLastEvent event) {
        EntityPlayer player = mc.player;
        ItemStack heldMain = player.getHeldItemMainhand();
        if (!heldMain.isEmpty() && heldMain.getItem() instanceof ICustomModelItem) {
            try {
                EcRenderSwordModel.INSTANCE.renderSwordModel(heldMain, true);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
    }
}
