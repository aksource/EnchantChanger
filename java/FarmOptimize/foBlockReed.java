package FarmOptimize;

import java.util.Random;

import net.minecraft.block.BlockReed;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.BonemealEvent;

public class foBlockReed extends BlockReed
{
    public foBlockReed()
    {
        super();
    }
    @OVerride
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (isGrow(par1World, par2, par3, par4))
        {
            int var6;

            for (var6 = 1; par1World.getBlockId(par2, par3 - var6, par4) == this.blockID; ++var6)
            {
                ;
            }

            if (var6 < FarmOptimize.SugarcaneLimit)
            {
                int var7 = par1World.getBlockMetadata(par2, par3, par4);

                if (var7 >= FarmOptimize.SugarcaneSpeed)
                {
                    par1World.setBlock(par2, par3 + 1, par4, this.blockID,0,3);
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, 0,3);
                }
                else
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var7 + 1,3);
                }
            }
        }
    }

	private boolean isGrow(World par1World, int par2, int par3, int par4)
	{
		return par1World.isAirBlock(par2, par3 + 1, par4) || FarmOptimize.SugarcaneGrowWater 
			&& par1World.getBlockMaterial(par2, par3 + 1, par4).equals(Material.water);
	}

	@SubscribeEvent
	public void useBonemeal(BonemealEvent event)
	{
		if(!event.world.isRemote && event.ID == blockID && canBlockStay(event.world, event.X, event.Y, event.Z))
		{
            int size;
            for (size = event.Y; canBlockStay(event.world, event.X, size, event.Z); --size)
            {
                ;
            }
            int min = size + 1;
			int top = 0;
            for (size = min; (size - min + 1) < FarmOptimize.SugarcaneUsedBonemealLimit; size++)
            {
                if(isGrow(event.world, event.X, size, event.Z))
				{
                    event.world.setBlock(event.X, size + 1, event.Z, this.blockID,0,3);
					top++;
				}
            }
			if(top > 0)
			{
				event.setResult(Result.ALLOW);
			}
		}
	}
}
