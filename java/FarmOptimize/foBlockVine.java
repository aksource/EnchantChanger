package FarmOptimize;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

public class foBlockVine extends BlockVine
{
    public foBlockVine()
    {
        super();
    }

    private boolean canBePlacedOn(int par1)
    {
        if (par1 == 0)
        {
            return false;
        }
        else
        {
            Block var2 = Block.blocksList[par1];
            return var2.renderAsNormalBlock() && var2.blockMaterial.blocksMovement();
        }
    }
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
		if(FarmOptimize.growSpeedVine == -1) return;
		
        if (!par1World.isRemote && (FarmOptimize.growSpeedVine == 0 
			|| par1World.rand.nextInt(FarmOptimize.growSpeedVine) == 0))
        {
            byte var6 = 4;
            int var7 = 5;
            boolean var8 = false;
            int var9;
            int var10;
            int var11;
            label138:

            for (var9 = par2 - var6; var9 <= par2 + var6; ++var9)
            {
                for (var10 = par4 - var6; var10 <= par4 + var6; ++var10)
                {
                    for (var11 = par3 - 1; var11 <= par3 + 1; ++var11)
                    {
                        if (par1World.getBlockId(var9, var11, var10) == this.blockID)
                        {
                            --var7;

                            if (var7 <= 0)
                            {
                                var8 = true;
                                break label138;
                            }
                        }
                    }
                }
            }

            var9 = par1World.getBlockMetadata(par2, par3, par4);
            var10 = par1World.rand.nextInt(6);
            var11 = Direction.facingToDirection[var10];
            int var12;
            int var13;

            if (var10 == 1 && par3 < 255 && par1World.isAirBlock(par2, par3 + 1, par4))
            {
                if (var8)
                {
                    return;
                }

                var12 = par1World.rand.nextInt(16) & var9;

                if (var12 > 0)
                {
                    for (var13 = 0; var13 <= 3; ++var13)
                    {
                        if (!this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var13], par3 + 1, par4 + Direction.offsetZ[var13])))
                        {
                            var12 &= ~(1 << var13);
                        }
                    }

                    if (var12 > 0)
                    {
                        par1World.setBlock(par2, par3 + 1, par4, this.blockID, var12,3);
                    }
                }
            }
            else
            {
                int var14;

                if (var10 >= 2 && var10 <= 5 && (var9 & 1 << var11) == 0)
                {
                    if (var8)
                    {
                        return;
                    }

                    var12 = par1World.getBlockId(par2 + Direction.offsetX[var11], par3, par4 + Direction.offsetZ[var11]);

                    if (var12 != 0 && Block.blocksList[var12] != null)
                    {
                        if (Block.blocksList[var12].blockMaterial.isOpaque() && Block.blocksList[var12].renderAsNormalBlock())
                        {
                            par1World.setBlockMetadataWithNotify(par2, par3, par4, var9 | 1 << var11,3);
                        }
                    }
                    else
                    {
                        var13 = var11 + 1 & 3;
                        var14 = var11 + 3 & 3;

                        if ((var9 & 1 << var13) != 0 && this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var11] + Direction.offsetX[var13], par3, par4 + Direction.offsetZ[var11] + Direction.offsetZ[var13])))
                        {
                            par1World.setBlock(par2 + Direction.offsetX[var11], par3, par4 + Direction.offsetZ[var11], this.blockID, 1 << var13,3);
                        }
                        else if ((var9 & 1 << var14) != 0 && this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var11] + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var11] + Direction.offsetZ[var14])))
                        {
                            par1World.setBlock(par2 + Direction.offsetX[var11], par3, par4 + Direction.offsetZ[var11], this.blockID, 1 << var14,3);
                        }
                        else if ((var9 & 1 << var13) != 0 && par1World.isAirBlock(par2 + Direction.offsetX[var11] + Direction.offsetX[var13], par3, par4 + Direction.offsetZ[var11] + Direction.offsetZ[var13]) && this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var13], par3, par4 + Direction.offsetZ[var13])))
                        {
                            par1World.setBlock(par2 + Direction.offsetX[var11] + Direction.offsetX[var13], par3, par4 + Direction.offsetZ[var11] + Direction.offsetZ[var13], this.blockID, 1 << (var11 + 2 & 3),3);
                        }
                        else if ((var9 & 1 << var14) != 0 && par1World.isAirBlock(par2 + Direction.offsetX[var11] + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var11] + Direction.offsetZ[var14]) && this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var14])))
                        {
                            par1World.setBlock(par2 + Direction.offsetX[var11] + Direction.offsetX[var14], par3, par4 + Direction.offsetZ[var11] + Direction.offsetZ[var14], this.blockID, 1 << (var11 + 2 & 3),3);
                        }
                        else if (this.canBePlacedOn(par1World.getBlockId(par2 + Direction.offsetX[var11], par3 + 1, par4 + Direction.offsetZ[var11])))
                        {
                            par1World.setBlock(par2 + Direction.offsetX[var11], par3, par4 + Direction.offsetZ[var11], this.blockID, 0,3);
                        }
                    }
                }
                else if (par3 > 1)
                {
                    var12 = par1World.getBlockId(par2, par3 - 1, par4);

                    if (var12 == 0)
                    {
                        var13 = par1World.rand.nextInt(16) & var9;

                        if (var13 > 0)
                        {
                            par1World.setBlock(par2, par3 - 1, par4, this.blockID, var13,3);
                        }
                    }
                    else if (var12 == this.blockID)
                    {
                        var13 = par1World.rand.nextInt(16) & var9;
                        var14 = par1World.getBlockMetadata(par2, par3 - 1, par4);

                        if (var14 != (var14 | var13))
                        {
                            par1World.setBlockMetadataWithNotify(par2, par3 - 1, par4, var14 | var13,3);
                        }
                    }
                }
            }
        }
    }

}
