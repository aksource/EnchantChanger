package ak.enchantchanger.item;

import ak.enchantchanger.inventory.EcInventoryCloudSword;
import ak.enchantchanger.network.MessageCloudSword;
import ak.enchantchanger.network.PacketHandler;
import ak.enchantchanger.utils.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import javax.annotation.Nonnull;

public class EcItemCloudSword extends EcItemSword {

    public EcItemCloudSword(String name) {
        super(ToolMaterial.DIAMOND, name);
    }

    public static int getSlotNumFromItemStack(ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        return itemStack.getTagCompound().getInteger("enchantchanger|slot");
    }

    public static void setSlotNumToItemStack(ItemStack itemStack, int slotNum) {
        if (!itemStack.hasTagCompound()) itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setInteger("enchantchanger|slot", slotNum);
    }

    public static IInventory getInventoryFromItemStack(ItemStack itemStack) {
        return new EcInventoryCloudSword(itemStack);
    }

    @Override
    public boolean onLeftClickEntity(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull Entity entity) {
        int slot = getSlotNumFromItemStack(stack);
        IInventory swordData = getInventoryFromItemStack(stack);
        if (slot == 5/* || swordData == null*/)
            return super.onLeftClickEntity(stack, player, entity);
        if (swordData.getStackInSlot(slot) != null) {
            this.attackTargetEntityWithTheItem(entity, player, swordData.getStackInSlot(slot), false);
            swordData.markDirty();
            return true;
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull ItemStack itemStack, @Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (playerIn.isSneaking()) {
            ItemStack heldItem = playerIn.getHeldItem(handIn);
            this.doCastOffSwords(heldItem, worldIn, playerIn);
            return ActionResult.newResult(EnumActionResult.SUCCESS, this.makeCloudSwordCore(heldItem));
        } else {
            return super.onItemRightClick(itemStack, worldIn, playerIn, handIn);
        }
    }

    @Override
    public void doCtrlKeyAction(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        increaseSlotNum(itemStack);
        int slot = getSlotNumFromItemStack(itemStack);
        PacketHandler.INSTANCE.sendTo(new MessageCloudSword((byte) slot), (EntityPlayerMP) entityPlayer);
    }

    public ItemStack makeCloudSwordCore(ItemStack stack) {
        ItemStack ChangeSwordCore = new ItemStack(Items.itemCloudSwordCore, 1, stack.getItemDamage());
        ChangeSwordCore.setTagCompound(stack.getTagCompound());
        return ChangeSwordCore;
    }

    public void doCastOffSwords(ItemStack ItemStack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            IInventory swordData = getInventoryFromItemStack(ItemStack);
            for (int i = 0; i < 5; i++) {
                int j;
                for (j = 0; j < 9; j++) {
                    if (player.inventory.getStackInSlot(j) == null) {
                        player.inventory.setInventorySlotContents(j, swordData.getStackInSlot(i));
                        break;
                    }
                }
                if (j == 9)
                    player.dropItem(swordData.getStackInSlot(i), false);
                swordData.setInventorySlotContents(i, null);
            }
        }
    }

    public void destroyTheItem(EntityPlayer player, ItemStack orig, EnumHand hand) {
        IInventory swordData = getInventoryFromItemStack(orig);
        swordData.setInventorySlotContents(getSlotNumFromItemStack(orig), null);
        MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(player, orig, hand));
        this.doCastOffSwords(orig, player.getEntityWorld(), player);
        player.inventory.setInventorySlotContents(player.inventory.currentItem,
                this.makeCloudSwordCore(player.getHeldItem(hand)));
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack itemStack, @Nonnull EntityLivingBase target, @Nonnull EntityLivingBase attacker) {
        target.hurtResistantTime = 0;
        return super.hitEntity(itemStack, target, attacker);
    }

    private void increaseSlotNum(ItemStack item) {
        int newSlot = (getSlotNumFromItemStack(item) + 1) % 6;
        setSlotNumToItemStack(item, newSlot);
    }
}