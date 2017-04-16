package ak.EnchantChanger.block;

import ak.EnchantChanger.tileentity.EcTileMultiPass;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMultiPass extends BlockContainer {
    private static final Map<BlockPos, EcTileMultiPass> removingTileMap = new HashMap<>();

    public EcBlockMultiPass(Material material) {
        super(material);
    }

    @Override
    public boolean isNormalCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer getBlockLayer()
    {
        return EnumWorldBlockLayer.CUTOUT;
    }

//    @Override
//    public boolean canRenderInPass(int pass) {
//        ClientProxy.customRenderPass = pass;
//        return true;
//    }

//    @Override
//    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
//        EcTileMultiPass tileMultiPass = (EcTileMultiPass)world.getTileEntity(x, y, z);
//        if (tileMultiPass != null && ClientProxy.customRenderPass == 0) {
//            Block block = tileMultiPass.getBaseBlock();
//            return block.getIcon(side, tileMultiPass.getBaseMeta());
//        }
//        return this.blockIcon;
//    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos blockPos, IBlockState state, EntityLivingBase setter, ItemStack item) {
        super.onBlockPlacedBy(world, blockPos, state, setter, item);
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(blockPos);
        if (item.hasTagCompound() && item.getTagCompound().hasKey("EnchantChanger|baseBlock")) {
            tile.baseBlock = item.getTagCompound().getString("EnchantChanger|baseBlock");
            tile.baseMeta = item.getTagCompound().getInteger("EnchantChanger|baseMeta");
        }
    }

    @Override
    public void breakBlock(World world, BlockPos blockPos, IBlockState state) {
        removingTileMap.put(blockPos, (EcTileMultiPass) world.getTileEntity(blockPos));
        super.breakBlock(world, blockPos, state);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState state, int fortune) {
        EcTileMultiPass tileMultiPass = removingTileMap.remove(blockPos);
        List<ItemStack> list = super.getDrops(world, blockPos, state, fortune);
        if (tileMultiPass != null) {
            list.clear();
            ItemStack dropItemBlock = new ItemStack(this, 1, this.damageDropped(state));
            dropItemBlock.setTagCompound(new NBTTagCompound());
            dropItemBlock.getTagCompound().setString("EnchantChanger|baseBlock", tileMultiPass.baseBlock);
            dropItemBlock.getTagCompound().setInteger("EnchantChanger|baseMeta", tileMultiPass.baseMeta);
            list.add(dropItemBlock);
            return list;
        }
        return list;
    }

    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, BlockPos blockPos) {
        ItemStack pickItem = super.getPickBlock(target, world, blockPos);
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(blockPos);
        pickItem.setTagCompound(new NBTTagCompound());
        pickItem.getTagCompound().setString("EnchantChanger|baseBlock", tile.baseBlock);
        pickItem.getTagCompound().setInteger("EnchantChanger|baseMeta", tile.baseMeta);
        return pickItem;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return new EcTileMultiPass();
    }
}
