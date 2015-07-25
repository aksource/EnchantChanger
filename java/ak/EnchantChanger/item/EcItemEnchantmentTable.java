package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EcItemEnchantmentTable extends EcItem {

    public EcItemEnchantmentTable(String name) {
        super(name);
        maxStackSize = 1;
        setMaxDamage(0);
//        this.setTextureName(Constants.EcTextureDomain + "PortableEnchantmentTable");
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        par3EntityPlayer.displayGui(new IInteractionObject() {
            @Override
            public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
                return new ContainerEnchantment(playerInventory, playerIn.worldObj, playerIn.getPosition());
            }

            @Override
            public String getGuiID() {
                return "minecraft:enchanting_table";
            }

            @Override
            public String getName() {
                return "container.enchantment";
            }

            @Override
            public boolean hasCustomName() {
                return false;
            }

            @Override
            public IChatComponent getDisplayName() {
                return new ChatComponentTranslation(this.getName());
            }
        });
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