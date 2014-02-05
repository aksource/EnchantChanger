package SpawnChange;

import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class scBlockObsidian extends BlockObsidian
{
	private static boolean portalSpawn;
	public scBlockObsidian()
	{
		super();
		portalSpawn = SpawnChange.portalSpawn;
	}
	
	public boolean canCreatureSpawn(EnumCreatureType type, World world, int x, int y, int z) 
	{
		if (portalSpawn && world.getBlock(x, y + 1, z) == Blocks.portal)
			return false;
		return true;
	}

}
