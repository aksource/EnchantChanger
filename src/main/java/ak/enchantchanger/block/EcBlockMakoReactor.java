package ak.enchantchanger.block;

import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.api.Constants;
import ak.enchantchanger.tileentity.EcTileEntityMakoReactor;
import ak.enchantchanger.tileentity.EcTileMultiPass;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * 魔晄炉のブロッククラス
 * Created by A.K. on 14/03/08.
 */
public class EcBlockMakoReactor extends EcBlockMultiPass {
    public static final PropertyEnum<EnumMRBaseType> PROP_BASE_TYPE = PropertyEnum.create("base", EnumMRBaseType.class);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public EcBlockMakoReactor() {
        super(Material.IRON);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PROP_BASE_TYPE, EnumMRBaseType.IRON));
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(PROP_BASE_TYPE).ordinal();
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.blockState.getBaseState().withProperty(PROP_BASE_TYPE, EnumMRBaseType.getByIndex(meta));
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) worldIn.getTileEntity(pos);
        byte face = 0;
        if (tile != null) {
            face = tile.face;
        }
        return state.withProperty(FACING, EnumFacing.getFront(face));
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing,
                                            float hitX, float hitY, float hitZ, int meta,
                                            @Nonnull EntityLivingBase placer, @Nonnull EnumHand hand) {
        return this.getDefaultState().withProperty(PROP_BASE_TYPE, EnumMRBaseType.getByIndex(meta)).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public void onBlockPlacedBy(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState state,
                                @Nonnull EntityLivingBase setter, @Nonnull ItemStack item) {
        super.onBlockPlacedBy(world, blockPos, state, setter, item);
        int l = MathHelper.floor((double) (setter.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EcTileMultiPass tile = (EcTileMultiPass) world.getTileEntity(blockPos);
        if (tile instanceof EcTileEntityMakoReactor) {
            ((EcTileEntityMakoReactor) tile).setFace((byte) setter.getHorizontalFacing().getOpposite().getIndex());
        }
    }

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nonnull EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) worldIn.getTileEntity(pos);
        if (tile != null && tile.isActivated()) {
            playerIn.openGui(EnchantChanger.instance, Constants.GUI_ID_MAKO_REACTOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new EcTileEntityMakoReactor();
    }

    @Override
    public void getSubBlocks(@Nonnull Item itemIn, @Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        for (EnumMRBaseType type : EnumMRBaseType.values()) {
            ItemStack makoReactorController = new ItemStack(this, 1, type.ordinal());
            list.add(makoReactorController);
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState state) {
        EcTileEntityMakoReactor tile = (EcTileEntityMakoReactor) world.getTileEntity(blockPos);
        if (tile != null) {
            for (int i1 = 0; i1 < tile.getSizeInventory(); ++i1) {
                ItemStack itemstack = tile.getStackInSlot(i1);

                if (!itemstack.isEmpty()) {
                    float f = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = world.rand.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.getCount() > 0) {
                        int j1 = world.rand.nextInt(21) + 10;

                        if (j1 > itemstack.getCount()) {
                            j1 = itemstack.getAnimationsToGo();
                        }

                        itemstack.shrink(j1);
                        EntityItem entityitem = new EntityItem(world,
                                blockPos.getX() + f,
                                blockPos.getY() + f1,
                                blockPos.getZ() + f2,
                                new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

                        if (itemstack.hasTagCompound()) {
                            entityitem.getItem().setTagCompound(itemstack.getTagCompound().copy());
                        }

                        float f3 = 0.05F;
                        entityitem.motionX = (double) ((float) world.rand.nextGaussian() * f3);
                        entityitem.motionY = (double) ((float) world.rand.nextGaussian() * f3 + 0.2F);
                        entityitem.motionZ = (double) ((float) world.rand.nextGaussian() * f3);
                        world.spawnEntity(entityitem);
                    }
                }
            }
        }
        super.breakBlock(world, blockPos, state);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, PROP_BASE_TYPE, FACING);
    }
}
