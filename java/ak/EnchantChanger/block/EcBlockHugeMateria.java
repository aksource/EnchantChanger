package ak.EnchantChanger.block;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
import ak.EnchantChanger.tileentity.EcTileEntityHugeMateria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.b3d.B3DLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.io.IOException;
import java.util.Random;

public class EcBlockHugeMateria extends BlockContainer {
    public static final IProperty propertyParts = PropertyInteger.create("part", 0, 2);
    private ExtendedBlockState extendedState = new ExtendedBlockState(this, new IProperty[]{propertyParts}, new IUnlistedProperty[]{B3DLoader.B3DFrameProperty.instance});

    public EcBlockHugeMateria() {
        super(Material.rock);
    }

    @Override
    public boolean isFullCube() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        try {
            IModel model = ModelLoaderRegistry.getModel(new ResourceLocation(Constants.EcAssetsDomain, "/block/hugemateria.b3d"));
            B3DLoader.B3DState defaultState = ((B3DLoader.Wrapper) model).getDefaultState();
            return ((IExtendedBlockState) this.extendedState.getBaseState()).withProperty(B3DLoader.B3DFrameProperty.instance, defaultState);
        } catch (IOException e) {
            e.printStackTrace();
            return this.extendedState.getBaseState();
        }
    }

    @Override
    public boolean onBlockActivated(World par1World, BlockPos blockPos, IBlockState state1, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9) {
        if (par1World.isRemote) {
            return true;
        } else if (par1World.getBlockState(blockPos.down()).getBlock() == this && (Integer) state1.getValue(propertyParts) != 0) {
            return this.onBlockActivated(par1World, blockPos.down(), par1World.getBlockState(blockPos.down()), par5EntityPlayer, par6, par7, par8, par9);
        } else {
            if (par1World.getTileEntity(blockPos) != null)
                par5EntityPlayer.openGui(EnchantChanger.instance, Constants.GUI_ID_HUGE_MATERIA, par1World, blockPos.getX(), blockPos.getY(), blockPos.getZ());
            return true;
        }

    }

    @Override
    public void onNeighborBlockChange(World par1World, BlockPos blockPos, IBlockState state1, Block par5) {
        int var6 = (int) state1.getValue(propertyParts);

        if (var6 != 0) {
            if (var6 == 1) {
                if (par1World.getBlockState(blockPos.down()).getBlock() != this || par1World.getBlockState(blockPos.up()).getBlock() != this) {
                    par1World.setBlockToAir(blockPos);
                }
                if (par5 != this) {
                    this.onNeighborBlockChange(par1World, blockPos.down(), par1World.getBlockState(blockPos.down()), par5);
                    this.onNeighborBlockChange(par1World, blockPos.up(), par1World.getBlockState(blockPos.up()), par5);
                }
            } else {//var6 ==2
                if (par1World.getBlockState(blockPos.down()).getBlock() != this) {
                    par1World.setBlockToAir(blockPos);
                }
            }
        } else {
            boolean var7 = false;

            if (par1World.getBlockState(blockPos.up()).getBlock() != this) {
                par1World.setBlockToAir(blockPos);
                var7 = true;
            }

            if (var7) {
                if (!par1World.isRemote) {
                    this.dropBlockAsItem(par1World, blockPos, state1, 0);
                }
            }
        }
    }

    @Override
    public void breakBlock(World par1World, BlockPos blockPos, IBlockState state1) {
        if (par1World.getTileEntity(blockPos) != null) {
            EcTileEntityHugeMateria t = (EcTileEntityHugeMateria) par1World.getTileEntity(blockPos);
            for (int l = 0; l < t.getSizeInventory(); l++) {
                ItemStack ist = t.getStackInSlot(l);
                if (ist == null || ist.stackSize <= 0) {
                    continue;
                }
                EntityItem eit = new EntityItem(par1World, (double) blockPos.getX() + 0.5D, (double) blockPos.getY() + 0.5D, (double) blockPos.getZ() + 0.5D, ist);
                par1World.spawnEntityInWorld(eit);
            }
        }
        super.breakBlock(par1World, blockPos, state1);
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, BlockPos blockPos) {
        int var1 = (int) par1IBlockAccess.getBlockState(blockPos).getValue(propertyParts);
        switch (var1) {
            case 0:
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F);
                return;
            case 1:
                this.setBlockBounds(0.0F, -1.0F, 0.0F, 1.0F, 2.0F, 1.0F);
                return;
            case 2:
                this.setBlockBounds(0.0F, -2.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Override
    public Item getItemDropped(IBlockState par1, Random par2Random, int par3) {
        return (int) par1.getValue(propertyParts) != 0 ? null : EnchantChanger.itemHugeMateria;
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
    public int getMetaFromState(IBlockState state) {
        return (Integer)state.getValue(propertyParts);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, propertyParts);
    }
}