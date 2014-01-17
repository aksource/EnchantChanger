package FarmOptimize;

import java.util.Random;

import net.minecraft.world.World;

public class foBlockNetherStalk extends BlockNetherStalk
{
    public foBlockNetherStalk()
    {
        super();
    }
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int var6 = par1World.getBlockMetadata(par2, par3, par4);

        if (var6 < 3 && (FarmOptimize.growSpeedNetherWart == 0 
			|| par5Random.nextInt(FarmOptimize.growSpeedNetherWart) == 0))
        {
            ++var6;
            par1World.setBlockMetadataWithNotify(par2, par3, par4, var6,3);
        }

        super.updateTick(par1World, par2, par3, par4, par5Random);
    }

}
