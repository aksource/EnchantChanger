package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EcItemEnchantmentTable extends EcItem {

    private static final String REGISTERED_POS_X = "registered_pos_x";
    private static final String REGISTERED_POS_Y = "registered_pos_y";
    private static final String REGISTERED_POS_Z = "registered_pos_z";

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
        BlockPos pos = par3EntityPlayer.getPosition();
        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey(REGISTERED_POS_X, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
            NBTTagCompound nbt = par1ItemStack.getTagCompound();
            pos = new BlockPos(nbt.getInteger(REGISTERED_POS_X), nbt.getInteger(REGISTERED_POS_Y), nbt.getInteger(REGISTERED_POS_Z));
        }
        par3EntityPlayer.displayGui(new CustomInteractionObj(pos));
//        par3EntityPlayer.openGui(EnchantChanger.instance, Constants.GUI_ID_PORTABLE_ENCHANTMENT_TABLE, par2World, 0, 0, 0);

        return par1ItemStack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking()) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            nbtTagCompound.setInteger(REGISTERED_POS_X, pos.getX());
            nbtTagCompound.setInteger(REGISTERED_POS_Y, pos.up().getY());
            nbtTagCompound.setInteger(REGISTERED_POS_Z, pos.getZ());
            if (worldIn.isRemote) {
                playerIn.addChatMessage(new ChatComponentText(String.format("registered position x:%d, y:%d, z:%d!!", pos.getX(), pos.up().getY(), pos.getZ())));
            }
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey(REGISTERED_POS_X, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
                tooltip.add(String.format("Position x:%d, y:%d, z:%d", nbt.getInteger(REGISTERED_POS_X), nbt.getInteger(REGISTERED_POS_Y), nbt.getInteger(REGISTERED_POS_Z)));
            }
        }
    }

    @SubscribeEvent
    public void openEnchantmentContainerEvent(PlayerOpenContainerEvent event) {
        Container container = event.entityPlayer.openContainer;
        ItemStack itemStack = event.entityPlayer.getCurrentEquippedItem();
        if (container instanceof ContainerEnchantment && itemStack != null && itemStack.getItem() instanceof EcItemEnchantmentTable) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    private static class CustomInteractionObj implements IInteractionObject {
        BlockPos pos;

        public CustomInteractionObj(BlockPos pos) {
            this.pos = pos;
        }
        @Override
        public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
            return new ContainerEnchantment(playerInventory, playerIn.worldObj, pos);
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
    }
}