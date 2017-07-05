package ak.enchantchanger.item;

import ak.enchantchanger.entity.EcEntityExExpBottle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class EcItemExExpBottle extends EcItem {
    public EcItemExExpBottle(String name) {
        super(name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack itemStack) {
        return true;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, @Nonnull EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (!playerIn.capabilities.isCreativeMode) {
            heldItem.shrink(1);
        }
        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT,
                SoundCategory.PLAYERS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EcEntityExExpBottle exExpBottle = new EcEntityExExpBottle(worldIn, playerIn);
            exExpBottle.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, -20.0F, 0.7F, 1.0F);
            worldIn.spawnEntity(exExpBottle);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, heldItem);
    }
}