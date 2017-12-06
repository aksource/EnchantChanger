package ak.enchantchanger.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerEnchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.List;

public class EcItemEnchantmentTable extends EcItem {

    private static final String REGISTERED_POS_X = "registered_pos_x";
    private static final String REGISTERED_POS_Y = "registered_pos_y";
    private static final String REGISTERED_POS_Z = "registered_pos_z";

    public EcItemEnchantmentTable(String name) {
        super(name);
        maxStackSize = 1;
        setMaxDamage(0);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        BlockPos pos = playerIn.getPosition();
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (heldItem.hasTagCompound()
                && heldItem.getTagCompound().hasKey(REGISTERED_POS_X, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
            NBTTagCompound nbt = heldItem.getTagCompound();
            pos = new BlockPos(nbt.getInteger(REGISTERED_POS_X), nbt.getInteger(REGISTERED_POS_Y), nbt.getInteger(REGISTERED_POS_Z));
        }
        playerIn.displayGui(new CustomInteractionObj(pos));

        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos,
                                      @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            ItemStack stack = player.getHeldItem(hand);
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound nbtTagCompound = stack.getTagCompound();
            nbtTagCompound.setInteger(REGISTERED_POS_X, pos.getX());
            nbtTagCompound.setInteger(REGISTERED_POS_Y, pos.up().getY());
            nbtTagCompound.setInteger(REGISTERED_POS_Z, pos.getZ());
            if (worldIn.isRemote) {
                player.sendMessage(
                        new TextComponentString(
                                String.format("registered position x:%d, y:%d, z:%d!!", pos.getX(), pos.up().getY(), pos.getZ())));
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nonnull EntityPlayer playerIn, @Nonnull List<String> tooltip, boolean advanced) {
        super.addInformation(stack, playerIn, tooltip, advanced);
        if (stack.hasTagCompound()) {
            NBTTagCompound nbt = stack.getTagCompound();
            if (nbt.hasKey(REGISTERED_POS_X, net.minecraftforge.common.util.Constants.NBT.TAG_INT)) {
                tooltip.add(String.format("Position x:%d, y:%d, z:%d", nbt.getInteger(REGISTERED_POS_X), nbt.getInteger(REGISTERED_POS_Y), nbt.getInteger(REGISTERED_POS_Z)));
            }
        }
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void openEnchantmentContainerEvent(PlayerContainerEvent.Open event) {
        Container container = event.getEntityPlayer().openContainer;
        ItemStack itemStack = event.getEntityPlayer().getHeldItemMainhand();
        if (container instanceof ContainerEnchantment && !itemStack.isEmpty() && itemStack.getItem() instanceof EcItemEnchantmentTable) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    private static class CustomInteractionObj implements IInteractionObject {
        BlockPos pos;

        public CustomInteractionObj(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        @Nonnull
        public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer playerIn) {
            return new ContainerEnchantment(playerInventory, playerIn.world, pos);
        }

        @Override
        @Nonnull
        public String getGuiID() {
            return "minecraft:enchanting_table";
        }

        @Override
        @Nonnull
        public String getName() {
            return "container.enchantment";
        }

        @Override
        public boolean hasCustomName() {
            return false;
        }

        @Override
        @Nonnull
        public ITextComponent getDisplayName() {
            return new TextComponentString(this.getName());
        }
    }
}