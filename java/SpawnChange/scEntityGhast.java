package SpawnChange;

import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class scEntityGhast extends EntityGhast
{
	private static int LightValue;

    public scEntityGhast(World par1World)
    {
        super(par1World);
		LightValue = SpawnChange.netherSpawnLightValue + 1;
    }

	protected boolean isValidLightLevel()
	{
		int var1 = MathHelper.floor_double(this.posX);
		int var2 = MathHelper.floor_double(this.boundingBox.minY);
		int var3 = MathHelper.floor_double(this.posZ);
		int var4 = this.worldObj.getBlockLightValue(var1, var2, var3);
		return var4 <= this.rand.nextInt(LightValue);
	}

	public boolean getCanSpawnHere()
	{
		return this.isValidLightLevel() && super.getCanSpawnHere();
	}

}
