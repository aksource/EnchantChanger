package ak.enchantchanger.eventhandler;

import ak.enchantchanger.item.EcItemEnchantmentTable;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ContainerEventHandler {
    @SubscribeEvent
    @SuppressWarnings("unused")
    public void openEnchantmentContainerEvent(PlayerContainerEvent.Open event) {
        Container container = event.getEntityPlayer().openContainer;
        ItemStack itemStack = event.getEntityPlayer().getHeldItemMainhand();
        if (container instanceof ContainerEnchantment && itemStack != null && itemStack.getItem() instanceof EcItemEnchantmentTable) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}
