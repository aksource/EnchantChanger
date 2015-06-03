package ak.EnchantChanger.block;

import ak.EnchantChanger.EnchantChanger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;

import java.util.List;
import java.util.Random;


/**
 * Created by A.K. on 14/03/06.
 */
public class EcBlockLifeStreamFluid extends BlockFluidFinite {

    public EcBlockLifeStreamFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getBlock().getMaterial().isLiquid() && super.displaceIfPossible(world, blockPos);
    }

    @Override
    public void updateTick(World world, BlockPos blockPos, IBlockState state, Random rand) {
        super.updateTick(world, blockPos, state, rand);
        world.scheduleUpdate(blockPos, this, tickRate);
        List list = world.getEntitiesWithinAABB(EntityLivingBase.class, AxisAlignedBB.fromBounds(blockPos.getX() + this.minX, blockPos.getY() + this.minY, blockPos.getZ() + this.minZ, blockPos.getX() + this.maxX, blockPos.getY() + this.maxY, blockPos.getZ() + this.maxZ));
        for (Object object : list) {
            EntityLivingBase entity = (EntityLivingBase) object;
            if (entity instanceof EntityPlayer) {
                entity.addPotionEffect(new PotionEffect(EnchantChanger.potionMako.getId(), 20 * 60, 0));
                entity.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 5, 0));
            } else if (entity instanceof EntityMob) {
                entity.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 20 * 10, 1));
            }
        }
    }
}
