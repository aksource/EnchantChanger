package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import cofh.lib.util.helpers.MathHelper;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class EcItemEnchantmentTable extends EcItem {

    public EcItemEnchantmentTable(String name) {
        super(name);
        maxStackSize = 1;
        setMaxDamage(0);
        this.setTextureName(Constants.EcTextureDomain + "PortableEnchantmentTable");
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par3EntityPlayer.displayGUIEnchantment(MathHelper.floor(par3EntityPlayer.posX),MathHelper.floor(par3EntityPlayer.posY),MathHelper.floor(par3EntityPlayer.posZ), "container.enchantment");
//        par3EntityPlayer.openGui(EnchantChanger.instance, Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE, par2World, 0, 0, 0);

        return par1ItemStack;
    }

    @SubscribeEvent
    public void openEnchantmentContainerEvent(PlayerOpenContainerEvent event) {
        Container container = event.entityPlayer.openContainer;
        ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
        if (container instanceof ContainerEnchantment && itemStack != null && itemStack.getItem() instanceof EcItemEnchantmentTable) {
            event.setResult(Event.Result.ALLOW);
        }
    }
}