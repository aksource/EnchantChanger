package SpawnChange;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.world.World;

public class scEntitySlime extends EntitySlime
{
	private static double SpawnHeight;
	
	public scEntitySlime(World par1World)
	{
		super(par1World);
		SpawnHeight = SpawnChange.SlimeSpawnHeight;
	}
	@Override
	public boolean getCanSpawnHere()
	{
		return this.posY < SpawnHeight ? super.getCanSpawnHere() : false;
	}
}
