package FarmOptimize;

import java.util.Random;

import net.minecraft.block.BlockCarrot;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class foBlockCarrot extends BlockCarrot
{
    public foBlockCarrot()
    {
        super();
    }
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        super.checkFlowerChange(par1World, par2, par3, par4);

        if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9)
        {
            int var6 = par1World.getBlockMetadata(par2, par3, par4);

            if (var6 < 7)
            {
                float var7 = this.getGrowthRate(par1World, par2, par3, par4);
				int speed;
		        switch (blockID)
		        {
		            case 59:
		                 speed = FarmOptimize.growSpeedCrops; break;
		            case 141:
		                 speed = FarmOptimize.growSpeedCarrot; break;
		            case 142:
		                 speed = FarmOptimize.growSpeedPotato; break;
		            default:
		                 speed = 100;
		        }
                if (speed == 0 || par5Random.nextInt((int)(25.0F / var7) + 1) == 0)
                {
                    ++var6;
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, var6,3);
                }
            }
        }
    }
    private float getGrowthRate(World par1World, int par2, int par3, int par4)
    {
        float var5 = 1.0F;
        int var6 = par1World.getBlockId(par2, par3, par4 - 1);
        int var7 = par1World.getBlockId(par2, par3, par4 + 1);
        int var8 = par1World.getBlockId(par2 - 1, par3, par4);
        int var9 = par1World.getBlockId(par2 + 1, par3, par4);
        int var10 = par1World.getBlockId(par2 - 1, par3, par4 - 1);
        int var11 = par1World.getBlockId(par2 + 1, par3, par4 - 1);
        int var12 = par1World.getBlockId(par2 + 1, par3, par4 + 1);
        int var13 = par1World.getBlockId(par2 - 1, par3, par4 + 1);
        boolean var14 = var8 == this.blockID || var9 == this.blockID;
        boolean var15 = var6 == this.blockID || var7 == this.blockID;
        boolean var16 = var10 == this.blockID || var11 == this.blockID || var12 == this.blockID || var13 == this.blockID;

        for (int var17 = par2 - 1; var17 <= par2 + 1; ++var17)
        {
            for (int var18 = par4 - 1; var18 <= par4 + 1; ++var18)
            {
                int var19 = par1World.getBlockId(var17, par3 - 1, var18);
                float var20 = 0.0F;

                if (blocksList[var19] != null && blocksList[var19].canSustainPlant(par1World, var17, par3 - 1, var18, ForgeDirection.UP, this))
                {
                    var20 = 1.0F;

                    if (blocksList[var19].isFertile(par1World, var17, par3 - 1, var18))
                    {
                        var20 = 3.0F;
                    }
                }

                if (var17 != par2 || var18 != par4)
                {
                    var20 /= 4.0F;
                }

                var5 += var20;
            }
        }

        if (var16 || var14 && var15)
        {
            var5 /= 2.0F;
        }

        return var5;
    }
}
