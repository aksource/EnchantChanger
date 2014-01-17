package FarmOptimize;

import java.util.Random;

import net.minecraft.block.BlockSapling;
import net.minecraft.world.World;

public class foBlockSapling extends BlockSapling
{
    public foBlockSapling()
    {
        super();
    }
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.isRemote)
        {
            super.updateTick(par1World, par2, par3, par4, par5Random);

            if (FarmOptimize.growSpeedSapling == 0 
				|| par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9 
				&& par5Random.nextInt(FarmOptimize.growSpeedSapling) == 0)
            {
                int var6 = par1World.getBlockMetadata(par2, par3, par4);

                if ((var6 & 8) == 0)
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var6 | 8,3);
                }
                else
                {
                    this.growTree(par1World, par2, par3, par4, par5Random);
                }
            }
        }
    }

}
