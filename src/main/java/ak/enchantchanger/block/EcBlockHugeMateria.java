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
import javax.annotation.Nullable;
import java.util.Random;

public class EcBlockHugeMateria extends BlockContainer {
    public static final IProperty<Integer> PART = PropertyInteger.create("part", 0, 2);

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
                                    @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @
                                            Nullable ItemStack heldItem, @Nonnull EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return true;
        } else if (worldIn.getBlockState(pos.down()).getBlock() == this
                && state.getValue(PART) != 0) {
            return this.onBlockActivated(worldIn, pos.down(),
                    worldIn.getBlockState(pos.down()), playerIn, hand, heldItem, side, hitX, hitY, hitZ);
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
        int var6 = state.getValue(PART);

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
                if (stack == null) {
                    continue;
                }
                EntityItem eit = new EntityItem(worldIn,
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D, stack);
                worldIn.spawnEntityInWorld(eit);
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
    public Item getItemDropped(@Nonnull IBlockState state, @Nonnull Random rand, int fortune) {
        return state.getValue(PART) != 0 ? null : Items.itemHugeMateria;
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return meta == 0 ? new EcTileEntityHugeMateria() : null;
    }

    @Override
    @Nonnull
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