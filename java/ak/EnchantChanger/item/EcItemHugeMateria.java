package ak.EnchantChanger.item;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.block.EcBlockHugeMateria;
import net.minecraft.block.Block;
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
            settingPos = settingPos.offsetDown(3);
        } else {
            settingPos = settingPos.offset(par7);
        }
        BlockPos settingPos2 = settingPos.offsetUp();
        BlockPos settingPos3 = settingPos.offsetUp(2);
//		switch(par7)
//		{
//		case 0:par5-=3;break;
//		case 1:par5++;break;
//		case 2:par6--;break;
//		case 3:par6++;break;
//		case 4:par4--;break;
//		case 5:par4++;break;
//		}
        Block hugeMateria = EnchantChanger.blockHugeMateria;
        if (par2EntityPlayer.func_175151_a/*canPlayerEdit*/(settingPos, par7, par1ItemStack)
                && par2EntityPlayer.func_175151_a/*canPlayerEdit*/(settingPos2, par7, par1ItemStack)
                && par2EntityPlayer.func_175151_a/*canPlayerEdit*/(settingPos3, par7, par1ItemStack)) {
            if (!hugeMateria.canPlaceBlockAt(par3World, settingPos)
                    || !hugeMateria.canPlaceBlockAt(par3World, settingPos2)
                    || !hugeMateria.canPlaceBlockAt(par3World, settingPos3)) {
                return false;
            } else {
                par3World.setBlockState(settingPos, hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.propertyParts, 0));
                par3World.setBlockState(settingPos2, hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.propertyParts, 1));
                par3World.setBlockState(settingPos3, hugeMateria.getDefaultState().withProperty(EcBlockHugeMateria.propertyParts, 2));
//				par3World.setBlock(par4, par5, par6, hugeMateria, 0, 1);
//				par3World.setBlock(par4, par5 + 1, par6, hugeMateria, 1, 1);
//				par3World.setBlock(par4, par5 + 2, par6, hugeMateria, 2, 1);
//				par3World.notifyBlocksOfNeighborChange(par4, par5, par6, hugeMateria);
//				par3World.notifyBlocksOfNeighborChange(par4, par5 + 1, par6, hugeMateria);
//				par3World.notifyBlocksOfNeighborChange(par4, par5 + 2, par6, hugeMateria);
                --par1ItemStack.stackSize;
                return true;
            }
        } else {
            return false;
        }
    }
}