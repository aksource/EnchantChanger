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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

import java.util.List;

public class EcItemEnchantmentTable extends EcItem {

    private static final String REGISTERED_POS_X = "registered_pos_x";
    private static final String REGISTERED_POS_Y = "registered_pos_y";
    private static final String REGISTERED_POS_Z = "registered_pos_z";

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
        if (par1ItemStack.hasTagCompound() && par1ItemStack.getTagCompound().hasKey(REGISTERED_POS_X, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
            NBTTagCompound nbt = par1ItemStack.getTagCompound();
            par3EntityPlayer.displayGUIEnchantment(nbt.getInteger(REGISTERED_POS_X), nbt.getInteger(REGISTERED_POS_Y), nbt.getInteger(REGISTERED_POS_Z), "container.enchantment");
        } else {
            par3EntityPlayer.displayGUIEnchantment(MathHelper.floor(par3EntityPlayer.posX), MathHelper.floor(par3EntityPlayer.posY), MathHelper.floor(par3EntityPlayer.posZ), "container.enchantment");
        }
        return par1ItemStack;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            if (!itemStack.hasTagCompound()) {
                itemStack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound nbtTagCompound = itemStack.getTagCompound();
            nbtTagCompound.setInteger(REGISTERED_POS_X, x);
            nbtTagCompound.setInteger(REGISTERED_POS_Y, y + 1);
            nbtTagCompound.setInteger(REGISTERED_POS_Z, z);
            if (world.isRemote) {
                player.addChatMessage(new ChatComponentText(String.format("registered position x:%d, y:%d, z:%d!!", x, y + 1, z)));
            }
        }
        return super.onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b) {
        super.addInformation(itemStack, player, list, b);
        if (itemStack.hasTagCompound()) {
            NBTTagCompound nbt = itemStack.getTagCompound();
            if (nbt.hasKey(REGISTERED_POS_X, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
                list.add(String.format("Position x:%d, y:%d, z:%d", nbt.getInteger(REGISTERED_POS_X), nbt.getInteger(REGISTERED_POS_Y), nbt.getInteger(REGISTERED_POS_Z)));
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
}