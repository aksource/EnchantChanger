package ak.enchantchanger.block;


import ak.enchantchanger.EnchantChanger;
import ak.enchantchanger.capability.CapabilityPlayerStatusHandler;
import ak.enchantchanger.tileentity.EcTileEntityMaterializer;
import ak.enchantchanger.utils.ConfigurationUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EcBlockMaterializer extends BlockContainer {
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);

    public EcBlockMaterializer() {
        super(Material.ROCK);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess source, @Nonnull BlockPos pos) {
        return AABB;
    }

    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(@Nonnull IBlockState state) {
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

    @Override
    public boolean onBlockActivated(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state,
                                    @Nonnull EntityPlayer playerIn, @Nonnull EnumHand hand, @Nullable ItemStack heldItem,
                                    @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (ConfigurationUtils.difficulty < 2 || checkCost(playerIn)) {
            playerIn.openGui(EnchantChanger.instance, 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        CapabilityPlayerStatusHandler.getPlayerStatusHandler(playerIn).setLimitGaugeValue(0);//test code
        return true;
    }

    private boolean checkCost(EntityPlayer player) {
        int expLv = player.experienceLevel;
        if (expLv >= ConfigurationUtils.enchantChangerCost) {
            player.addExperienceLevel(-ConfigurationUtils.enchantChangerCost);
            return true;
        }
        player.addChatMessage(new TextComponentString(String.format("Need %dLevel to open enchantchanger", ConfigurationUtils.enchantChangerCost)));
        return false;
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState state) {
        super.breakBlock(world, blockPos, state);
        world.removeTileEntity(blockPos);
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
        return new EcTileEntityMaterializer();
    }

}