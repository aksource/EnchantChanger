package ak.enchantchanger.item;

import ak.enchantchanger.utils.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EcItemHugeMateria extends EcItem {
    public EcItemHugeMateria(String name) {
        super(name);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos,
                                      @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos settingPos = pos;
        if (facing == EnumFacing.DOWN) {
            settingPos = settingPos.down(3);
        } else {
            settingPos = settingPos.offset(facing);
        }
        BlockPos settingPos2 = settingPos.up();
        BlockPos settingPos3 = settingPos.up(2);
        Block hugeMateria = Blocks.blockHugeMateria;
        ItemStack heldItem = player.getHeldItem(hand);
        if (player.canPlayerEdit(settingPos, facing, heldItem)
                && player.canPlayerEdit(settingPos2, facing, heldItem)
                && player.canPlayerEdit(settingPos3, facing, heldItem)
                && worldIn.mayPlace(hugeMateria, settingPos, false, facing, null)
                && worldIn.mayPlace(hugeMateria, settingPos2, false, facing, null)
                && worldIn.mayPlace(hugeMateria, settingPos3, false, facing, null)
                ) {
            if (!hugeMateria.canPlaceBlockAt(worldIn, settingPos)
                    || !hugeMateria.canPlaceBlockAt(worldIn, settingPos2)
                    || !hugeMateria.canPlaceBlockAt(worldIn, settingPos3)) {
                return EnumActionResult.FAIL;
            }
            IBlockState blockState0 = hugeMateria.getStateForPlacement(worldIn, settingPos, facing, hitX, hitY, hitZ, 0, player, hand);
            IBlockState blockState1 = hugeMateria.getStateForPlacement(worldIn, settingPos, facing, hitX, hitY, hitZ, 1, player, hand);
            IBlockState blockState2 = hugeMateria.getStateForPlacement(worldIn, settingPos, facing, hitX, hitY, hitZ, 2, player, hand);
            boolean flag0 = worldIn.setBlockState(settingPos, blockState0, 1);
            boolean flag1 = worldIn.setBlockState(settingPos2, blockState1, 1);
            boolean flag2 = worldIn.setBlockState(settingPos3, blockState2, 1);
            if (flag0 && flag1 && flag2) {
                SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
                worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                heldItem.shrink(1);

                worldIn.notifyNeighborsOfStateChange(settingPos, hugeMateria, false);
                worldIn.notifyNeighborsOfStateChange(settingPos2, hugeMateria, false);
                worldIn.notifyNeighborsOfStateChange(settingPos3, hugeMateria, false);
                return EnumActionResult.SUCCESS;
            }
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}