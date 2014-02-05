package ak.EnchantChanger;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class MateriaTeleporter extends Teleporter {

	public MateriaTeleporter(WorldServer par1WorldServer) {
		super(par1WorldServer);
		par1WorldServer.customTeleporters.add(this);
	}

	@Override
	public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {
		par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;

		int x = (int)par1Entity.posX;
		int z = (int)par1Entity.posZ;
		int y = par1Entity.worldObj.getTopSolidOrLiquidBlock(x, z);
		par1Entity.setLocationAndAngles(x, y, z, 0, 0);
		par1Entity.fallDistance = 0;
	}
}
