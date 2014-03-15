package ak.EnchantChanger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import java.util.List;
import java.util.Random;


/**
 * Created by A.K. on 14/03/06.
 */
public class EcBlockLifeStreamFluid extends BlockFluidClassic{
    @SideOnly(Side.CLIENT)
    protected IIcon stillIcon;
    @SideOnly(Side.CLIENT)
    protected IIcon flowingIcon;
    public EcBlockLifeStreamFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        if (world.getBlock(x,  y,  z).getMaterial().isLiquid()) return false;
        return super.displaceIfPossible(world, x, y, z);
    }
    @Override
    public void registerBlockIcons(IIconRegister register) {
        stillIcon = register.registerIcon(EnchantChanger.EcTextureDomain + "lifestream_still");
        flowingIcon = register.registerIcon(EnchantChanger.EcTextureDomain + "lifestream_flow");
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        if (world.getTotalWorldTime() % 20L == 0L) return;
        List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getSelectedBoundingBoxFromPool(world, x, y, z));
        for (EntityLivingBase entity : list) {
            if (entity instanceof EntityPlayer) {
                entity.addPotionEffect(new PotionEffect(EnchantChanger.potionMako.getId(), 20 * 5, 0));
                entity.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 5, 0));
            } else if (entity instanceof EntityMob) {
                entity.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 20 * 10, 1));
            }
        }
    }
}
