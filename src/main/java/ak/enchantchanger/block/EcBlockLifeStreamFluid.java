package ak.enchantchanger.block;

import ak.enchantchanger.EnchantChanger;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;


/**
 * ライフストリームの液体ブロッククラス
 * Created by A.K. on 14/03/06.
 */
public class EcBlockLifeStreamFluid extends BlockFluidClassic {

    public EcBlockLifeStreamFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    @Override
    public boolean canDisplace(IBlockAccess world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getMaterial().isLiquid() && super.canDisplace(world, blockPos);
    }

    @Override
    public boolean displaceIfPossible(World world, BlockPos blockPos) {
        return !world.getBlockState(blockPos).getMaterial().isLiquid() && super.displaceIfPossible(world, blockPos);
    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos blockPos, @Nonnull IBlockState state, @Nonnull Random rand) {
        super.updateTick(world, blockPos, state, rand);
        world.scheduleUpdate(blockPos, this, tickRate);
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(blockPos));
        for (EntityLivingBase entity : list) {
            if (entity instanceof EntityPlayer) {
                entity.addPotionEffect(new PotionEffect(EnchantChanger.potionMako, 20 * 60, 0));
                entity.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 20 * 5, 0));
            } else if (entity instanceof EntityMob) {
                entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 20 * 10, 1));
            }
        }
    }
}
