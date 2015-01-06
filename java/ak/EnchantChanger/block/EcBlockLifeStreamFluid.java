package ak.EnchantChanger.block;

import ak.EnchantChanger.EnchantChanger;
import ak.EnchantChanger.api.Constants;
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
//    private int waitTime = 0;
    public EcBlockLifeStreamFluid(Fluid fluid, Material material) {
        super(fluid, material);
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 0 || side == 1)? stillIcon : flowingIcon;
    }

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {
        return !world.getBlock(x,  y,  z).getMaterial().isLiquid() && super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {
        return !world.getBlock(x,  y,  z).getMaterial().isLiquid() && super.displaceIfPossible(world, x, y, z);
    }
    @Override
    public void registerBlockIcons(IIconRegister register) {
        stillIcon = register.registerIcon(Constants.EcTextureDomain + "lifestream_still");
        flowingIcon = register.registerIcon(Constants.EcTextureDomain + "lifestream_flow");
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        super.updateTick(world, x, y, z, rand);
        world.scheduleBlockUpdate(x, y, z, this, tickRate);
        List list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getCollisionBoundingBoxFromPool(world, x, y, z));
        for (Object object : list) {
            EntityLivingBase entity = (EntityLivingBase)object;
            if (entity instanceof EntityPlayer) {
                entity.addPotionEffect(new PotionEffect(EnchantChanger.potionMako.getId(), 20 * 60, 0));
                entity.addPotionEffect(new PotionEffect(Potion.confusion.getId(), 20 * 5, 0));
            } else if (entity instanceof EntityMob) {
                entity.addPotionEffect(new PotionEffect(Potion.damageBoost.getId(), 20 * 10, 1));
            }
        }
    }
}
