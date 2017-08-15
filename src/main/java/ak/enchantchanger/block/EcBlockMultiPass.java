package ak.enchantchanger.block;

import ak.enchantchanger.tileentity.EcTileMultiPass;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 複数レイヤーの描画を持つブロッククラス
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMultiPass extends BlockContainer {
    private static final Map<BlockPos, EcTileMultiPass> removingTileMap = new HashMap<>();

    public EcBlockMultiPass(Material material) {
        super(material);
    }

    @Override
    public boolean isNormalCube(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(@Nonnull IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState state,
                                @Nonnull EntityLivingBase setter, @Nonnull ItemStack item) {
        super.onBlockPlacedBy(world, blockPos, state, setter, item);
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(blockPos);
        if (item.hasTagCompound() && item.getTagCompound().hasKey("enchantchanger|baseBlock")) {
            tile.baseBlock = item.getTagCompound().getString("enchantchanger|baseBlock");
            tile.baseMeta = item.getTagCompound().getInteger("enchantchanger|baseMeta");
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState state) {
        removingTileMap.put(blockPos, (EcTileMultiPass) world.getTileEntity(blockPos));
        super.breakBlock(world, blockPos, state);
    }

    @Override
    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos blockPos,
                                    @Nonnull IBlockState state, int fortune) {
        EcTileMultiPass tileMultiPass = removingTileMap.remove(blockPos);
        List<ItemStack> list = super.getDrops(world, blockPos, state, fortune);
        if (tileMultiPass != null) {
            list.clear();
            ItemStack dropItemBlock = new ItemStack(this, 1, this.damageDropped(state));
            dropItemBlock.setTagCompound(new NBTTagCompound());
            dropItemBlock.getTagCompound().setString("enchantchanger|baseBlock", tileMultiPass.baseBlock);
            dropItemBlock.getTagCompound().setInteger("enchantchanger|baseMeta", tileMultiPass.baseMeta);
            list.add(dropItemBlock);
            return list;
        }
        return list;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, @Nonnull RayTraceResult target, @Nonnull World world,
                                  @Nonnull BlockPos pos, @Nonnull EntityPlayer player) {
        ItemStack pickItem = super.getPickBlock(state, target, world, pos, player);
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(pos);
        pickItem.setTagCompound(new NBTTagCompound());
        pickItem.getTagCompound().setString("enchantchanger|baseBlock", tile.baseBlock);
        pickItem.getTagCompound().setInteger("enchantchanger|baseMeta", tile.baseMeta);
        return pickItem;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new EcTileMultiPass();
    }
}
