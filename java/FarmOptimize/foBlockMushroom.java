package FarmOptimize;

import java.util.Random;

import net.minecraft.block.BlockMushroom;
import net.minecraft.world.World;

public class foBlockMushroom extends BlockMushroom
{
    public foBlockMushroom()
    {
        super();
    }
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (FarmOptimize.MushroomSpeed == 0 
			|| par5Random.nextInt(FarmOptimize.MushroomSpeed) == 0)
        {
            byte var6 = FarmOptimize.MushroomArea;
            int var7 = FarmOptimize.MushroomLimit;
            int var8;
            int var9;
            int var10;

            for (var8 = par2 - var6; var8 <= par2 + var6; ++var8)
            {
                for (var9 = par4 - var6; var9 <= par4 + var6; ++var9)
                {
                    for (var10 = par3 - 1; var10 <= par3 + 1; ++var10)
                    {
                        if (par1World.getBlockId(var8, var10, var9) == this.blockID)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                return;
                            }
                        }
                    }
                }
            }

            var8 = par2 + par5Random.nextInt(3) - 1;
            var9 = par3 + par5Random.nextInt(2) - par5Random.nextInt(2);
            var10 = par4 + par5Random.nextInt(3) - 1;

            for (int var11 = 0; var11 < 4; ++var11)
            {
                if (par1World.isAirBlock(var8, var9, var10) && this.canBlockStay(par1World, var8, var9, var10))
                {
                    par2 = var8;
                    par3 = var9;
                    par4 = var10;
                }

                var8 = par2 + par5Random.nextInt(3) - 1;
                var9 = par3 + par5Random.nextInt(2) - par5Random.nextInt(2);
                var10 = par4 + par5Random.nextInt(3) - 1;
            }

            if (par1World.isAirBlock(var8, var9, var10) && this.canBlockStay(par1World, var8, var9, var10))
            {
                par1World.setBlock(var8, var9, var10, this.blockID,0,3);
            }
        }
    }

}
