package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.block.EcBlockHugeMateria;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class EcItemHugeMateria extends EcItem {
    public EcItemHugeMateria(String name) {
        super(name);
    }

    @Override
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos blockPos, EnumFacing par7, float par8, float par9, float par10) {
        BlockPos settingPos = blockPos;
        if (par7 == EnumFacing.DOWN) {
            settingPos = settingPos.down(3);
        } else {
            settingPos = settingPos.offset(par7);
        }
        BlockPos settingPos2 = settingPos.up();
        BlockPos settingPos3 = settingPos.up(2);

        Block hugeMateria = EnchantChanger.blockHugeMateria;
        if (par2EntityPlayer.canPlayerEdit(settingPos, par7, par1ItemStack)
                && par2EntityPlayer.canPlayerEdit(settingPos2, par7, par1ItemStack)
                && par2EntityPlayer.canPlayerEdit(settingPos3, par7, par1ItemStack)
                && par3World.canBlockBePlaced(hugeMateria, settingPos, false, par7, (Entity)null, par1ItemStack)
                && par3World.canBlockBePlaced(hugeMateria, settingPos2, false, par7, (Entity)null, par1ItemStack)
                && par3World.canBlockBePlaced(hugeMateria, settingPos3, false, par7, (Entity)null, par1ItemStack)) {
            if (!hugeMateria.canPlaceBlockAt(par3World, settingPos)
                    || !hugeMateria.canPlaceBlockAt(par3World, settingPos2)
                    || !hugeMateria.canPlaceBlockAt(par3World, settingPos3)) {
                return false;
            }
            IBlockState blockState0 = hugeMateria.onBlockPlaced(par3World, settingPos, par7, par8, par9, par10, 0, par2EntityPlayer);
            IBlockState blockState1 = hugeMateria.onBlockPlaced(par3World, settingPos2, par7, par8, par9, par10, 1, par2EntityPlayer);
            IBlockState blockState2 = hugeMateria.onBlockPlaced(par3World, settingPos3, par7, par8, par9, par10, 2, par2EntityPlayer);
            boolean flag0 = par3World.setBlockState(settingPos, blockState0, 1);
            boolean flag1 = par3World.setBlockState(settingPos2, blockState1, 1);
            boolean flag2 = par3World.setBlockState(settingPos3, blockState2, 1);

            if (flag0 && flag1 && flag2) {
                par3World.playSoundEffect(
                        (double)((float)blockPos.getX() + 0.5F),
                        (double)((float)blockPos.getY() + 0.5F),
                        (double)((float)blockPos.getZ() + 0.5F),
                        hugeMateria.stepSound.getPlaceSound(),
                        (hugeMateria.stepSound.getVolume() + 1.0F) / 2.0F,
                        hugeMateria.stepSound.getFrequency() * 0.8F);

                par3World.notifyBlockOfStateChange(settingPos, hugeMateria);
                par3World.notifyBlockOfStateChange(settingPos2, hugeMateria);
                par3World.notifyBlockOfStateChange(settingPos3, hugeMateria);
                --par1ItemStack.stackSize;
            }

            return true;
        } else {
            return false;
        }
    }
}