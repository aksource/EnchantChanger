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
import java.util.Random;

public class EcBlockHugeMateria extends BlockContainer {
    public static final IProperty propertyParts = PropertyInteger.create("part", 0, 2);
    //不要？
//    private ExtendedBlockState extendedState = new ExtendedBlockState(this, new IProperty[]{propertyParts}, new IUnlistedProperty[]{B3DLoader.B3DFrameProperty.instance});

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

    //なくても描画される？
/*    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(Constants.EcAssetsDomain, "/block/hugemateria.b3d"));
            B3DLoader.B3DState defaultState = ((B3DLoader.Wrapper) model).getDefaultState();
            return ((IExtendedBlockState) this.extendedState.getBaseState()).withProperty(B3DLoader.B3DFrameProperty.instance, defaultState);
        } catch (IOException e) {
            e.printStackTrace();
            return this.extendedState.getBaseState();
        }
    }*/

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else if (worldIn.getBlockState(pos.down()).getBlock() == this
                && (Integer) state.getValue(propertyParts) != 0) {
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
        int var6 = (int) state.getValue(propertyParts);

        if (var6 != 0) {
            if (var6 == 1) {
                if (world.getBlockState(pos.down()).getBlock() != this || world.getBlockState(pos.up()).getBlock() != this) {
                    ((World) world).setBlockToAir(pos);
                }
                if (neighborBlock != this) {
                    this.onNeighborChange(world, pos.down(), pos);
                    this.onNeighborChange(world, pos.up(), pos);
                }
            } else {//var6 ==2
                if (world.getBlockState(pos.down()).getBlock() != this) {
                    ((World) world).setBlockToAir(pos);
                }
            }
        } else {
            boolean var7 = false;

            if (world.getBlockState(pos.up()).getBlock() != this) {
                ((World) world).setBlockToAir(pos);
                var7 = true;
            }

            if (var7) {
                if (!((World) world).isRemote) {
                    this.dropBlockAsItem((World) world, pos, state, 0);
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
        Integer var1 = (Integer) source.getBlockState(pos).getValue(propertyParts);
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
        return (int) state.getValue(propertyParts) != 0 ? null : Items.itemHugeMateria;
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2) {
        return var2 == 0 ? new EcTileEntityHugeMateria() : null;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState blockState = super.getStateFromMeta(meta);
        return blockState.withProperty(propertyParts, meta);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return (Integer) state.getValue(propertyParts);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, propertyParts);
    }
}