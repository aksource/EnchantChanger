package ak.EnchantChanger;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class MateriaTeleporter extends Teleporter {

    public MateriaTeleporter(WorldServer par1WorldServer) {
        super(par1WorldServer);
        par1WorldServer.customTeleporters.add(this);
    }

    //placeInPortal
    @Override
    public void func_180266_a(Entity par1Entity, float par8) {
        par1Entity.motionX = par1Entity.motionY = par1Entity.motionZ = 0.0D;

        int x = (int) par1Entity.posX;
        int z = (int) par1Entity.posZ;
        BlockPos blockPos = par1Entity.worldObj.func_175672_r(new BlockPos(par1Entity.posX, par1Entity.posY, par1Entity.posZ));
        par1Entity.moveToBlockPosAndAngles(blockPos, 0, 0);
        par1Entity.fallDistance = 0;
    }
}
