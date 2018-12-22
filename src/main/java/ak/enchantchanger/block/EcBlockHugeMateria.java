package ak.enchantchanger.block;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.api.Constants;
import ak.enchantchanger.tileentity.EcTileEntityHugeMateria;
import ak.enchantchanger.utils.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Random;

import static net.minecraft.init.Items.AIR;

public class EcBlockHugeMateria extends BlockContainer {
    public static final IProperty<Integer> PART = PropertyInteger.create("part", 0, 2);
    //不要？
//    private ExtendedBlockState extendedState = new ExtendedBlockState(this, new IProperty[]{PART}, new IUnlistedProperty[]{B3DLoader.B3DFrameProperty.instance});

    public EcBlockHugeMateria() {
        super(Material.ROCK);
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(@Nonnull IBlockState state) {
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
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else if (worldIn.getBlockState(pos.down()).getBlock() == this
                && state.getValue(PART) != 0) {
            return this.onBlockActivated(worldIn, pos.down(),
                    worldIn.getBlockState(pos.down()), playerIn, hand, facing, hitX, hitY, hitZ);
        } else {
            if (worldIn.getTileEntity(pos) != null)
                playerIn.openGui(EnchantChanger.instance, Constants.GUI_ID_HUGE_MATERIA,
                        worldIn, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }

    }

    @Override
    public void onNeighborChange(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull BlockPos neighbor) {
        if (!(world instanceof World)) {
            return;
        }
        IBlockState state = world.getBlockState(pos);
        IBlockState neighborState = world.getBlockState(neighbor);
        Block neighborBlock = neighborState.getBlock();
        Collection<IProperty<?>> propertyKeys = state.getPropertyKeys();
        if (propertyKeys.contains(PART)) {
            int value = state.getValue(PART);

            if (value != 0) {
                if (value == 1) {
                    if (world.getBlockState(pos.down()).getBlock() != this || world.getBlockState(pos.up()).getBlock() != this) {
                        ((World) world).setBlockToAir(pos);
                    }
                    if (neighborBlock != this) {
                        this.onNeighborChange(world, pos.down(), pos);
                        this.onNeighborChange(world, pos.up(), pos);
                    }
                } else {//value ==2
                    if (world.getBlockState(pos.down()).getBlock() != this) {
                        ((World) world).setBlockToAir(pos);
                    }
                }
            } else {
                boolean broken = false;

                if (world.getBlockState(pos.up()).getBlock() != this) {
                    ((World) world).setBlockToAir(pos);
                    broken = true;
                }

                if (broken) {
                    if (!((World) world).isRemote) {
                        this.dropBlockAsItem((World) world, pos, state, 0);
                    }
                }
            }
        }
    }

    @Override
    public void breakBlock(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        EcTileEntityHugeMateria t = (EcTileEntityHugeMateria) worldIn.getTileEntity(pos);
        if (t != null) {
            for (int l = 0; l < t.getSizeInventory(); l++) {
                ItemStack stack = t.getStackInSlot(l);
                if (stack.isEmpty()) {
                    continue;
                }
                EntityItem eit = new EntityItem(worldIn,
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D, stack);
                worldIn.spawnEntity(eit);
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        Integer var1 = state.getValue(PART);
        AxisAlignedBB aabb;
        switch (var1) {
            case 0:
                aabb = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F);
                break;
            case 1:
                aabb = new AxisAlignedBB(0.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F);
                break;
            case 2:
                aabb = new AxisAlignedBB(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                break;
            default:
                aabb = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        return aabb;
    }

    @Override
    @Nonnull
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) {
        return state.getValue(PART) != 0 ? AIR : Items.itemHugeMateria;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return meta == 0 ? new EcTileEntityHugeMateria() : null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState blockState = super.getStateFromMeta(meta);
        return blockState.withProperty(PART, meta);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(PART);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PART);
    }
}